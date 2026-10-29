# eBank Microservices App

A small banking platform built with **Spring Boot 4 / Spring Cloud** microservices. It has an **Angular** frontend and an **AI assistant**: a Gemini-powered agent that manages customers and accounts through **MCP tools**, which you can use over REST or as a **Discord bot**.

## Architecture

```
                         ┌──────────────────────┐
                         │  discovery-service   │  Eureka registry :8761
                         └──────────▲───────────┘
                                    │ register / lookup
 ┌──────────────┐  HTTP  ┌──────────┴───────────┐        ┌──────────────────┐
 │   Angular    │───────▶│   gateway-service    │───────▶│  ebank-service   │ :8057
 │  :4200       │        │   :9999 (CORS)       │        │  accounts (H2)   │
 └──────────────┘        └──────────────────────┘        └───────┬──────────┘
                                                                 │ OpenFeign
                                                         ┌───────▼──────────┐
                                                         │ customer-service │ :8088
                                                         │  customers (H2)  │
                                                         └───────▲──────────┘
 ┌──────────────┐        ┌──────────────────────┐   MCP (streamable HTTP)
 │   Discord    │───────▶│      ebank-bot       │──────────┴──▶ both services
 │  / REST /chat│        │  :8058  Gemini agent │
 └──────────────┘        └──────────────────────┘
```

| Module | Port | Role |
|---|---|---|
| `discovery-service` | 8761 | Eureka server. All services register here. |
| `gateway-service` | 9999 | Spring Cloud Gateway with discovery-based routes and CORS for the Angular dev server |
| `customer-service` | 8088 | Customers CRUD (JPA + H2). Exposes MCP tools. |
| `ebank-service` | 8057 | Bank accounts (JPA + H2). Fetches each account's customer via OpenFeign. Exposes MCP tools. |
| `ebank-bot` | 8058 | Spring AI agent (Google Gemini) using both services' MCP tools, served over REST and Discord |
| `Angular-FrontEnd` | 4200 | Angular 21 + Bootstrap 5 UI that lists accounts through the gateway |

## Tech stack

- **Backend:** Java 21, Spring Boot 4.1, Spring Cloud 2025.1 (Eureka, Gateway WebFlux, OpenFeign), Spring Data JPA, H2, Lombok, springdoc-openapi
- **AI:** Spring AI 2.0 (Google GenAI / Gemini, MCP server and client, chat memory)
- **Bot:** JDA + `spring-boot-starter-discord`
- **Frontend:** Angular 21, Bootstrap 5, Bootstrap Icons

## Prerequisites

- **JDK 21+**
- **Node.js** and npm (for the frontend)
- A **Google Gemini API key** (for `ebank-bot`)
- A **Discord bot token** (optional, only for the Discord integration)

No Maven install is needed: every module ships with the Maven wrapper (`mvnw`).

## Configuration

`ebank-bot` reads its secrets from environment variables. Never commit real values.

| Variable | Required | Description |
|---|---|---|
| `GOOGLE_GENAI_API_KEY` | yes | Gemini API key |
| `DISCORD_TOKEN` | yes | Discord bot token |
| `MODEL` | no | Gemini model (default `gemini-2.5-flash`) |

Set them in your IDE run configuration, or in an `ebank-bot/.env` file (loaded by `spring-dotenv`; `.env` is git-ignored):

```env
GOOGLE_GENAI_API_KEY=your-key
DISCORD_TOKEN=your-token
```

### Discord setup

1. In the [Discord Developer Portal](https://discord.com/developers/applications), open your app → **Bot** → turn on **Message Content Intent**.
2. Under **OAuth2 → URL Generator**, tick the `bot` scope plus the `View Channels`, `Send Messages` and `Read Message History` permissions. Then open the generated URL to add the bot to your server.

## Running locally

Start the services **in this order**, each in its own terminal (or from IntelliJ):

```bash
cd discovery-service && ./mvnw spring-boot:run
```

```bash
cd customer-service && ./mvnw spring-boot:run
```

`ebank-service` depends on the `customer-service` artifact, so install it once first:

```bash
cd customer-service && ./mvnw install -DskipTests
```

```bash
cd ebank-service && ./mvnw spring-boot:run
```

```bash
cd gateway-service && ./mvnw spring-boot:run
```

```bash
cd ebank-bot && ./mvnw spring-boot:run
```

Then start the frontend:

```bash
cd Angular-FrontEnd && npm install && npm start
```

Open <http://localhost:4200/account>.

The Eureka dashboard is at <http://localhost:8761>. Wait until `EBANK-SERVICE` and `CUSTOMER-SERVICE` show as registered before you call them through the gateway.

## API

### Through the gateway (`http://localhost:9999`)

Routes are generated from Eureka service IDs: `/{SERVICE-ID}/**`.

| Method | Path | Description |
|---|---|---|
| GET | `/CUSTOMER-SERVICE/customers` | List customers |
| GET | `/CUSTOMER-SERVICE/customers/{id}` | Get a customer |
| POST | `/CUSTOMER-SERVICE/customers` | Create a customer |
| GET | `/EBANK-SERVICE/accounts` | List accounts (with their customer) |
| GET | `/EBANK-SERVICE/accounts/{id}` | Get an account |
| GET | `/EBANK-SERVICE/accounts/customer/{customerId}` | Get a customer's account |
| POST | `/EBANK-SERVICE/accounts` | Create an account |

Each service also has Swagger UI at `/swagger-ui.html` and an H2 console at `/h2-console`.

### AI assistant (`http://localhost:8058`)

```bash
curl "http://localhost:8058/chat?query=list%20all%20customers"
```

In Discord, mention the bot or DM it, for example: `@YourBot show me all bank accounts`.

### MCP tools

The agent calls these tools over MCP (streamable HTTP at `/mcp` on each service):

| Service | Tools |
|---|---|
| customer-service | `get_all_customers`, `find_customer_by_id`, `save_customer` |
| ebank-service | `get_all_customer_accounts`, `find_bank_account_by_id`, `save_bank_account` |

## Project structure

```
microServices-ebank-app/
├── discovery-service/   # Eureka server
├── gateway-service/     # Spring Cloud Gateway
├── customer-service/    # Customers + MCP server
├── ebank-service/       # Accounts + Feign client + MCP server
├── ebank-bot/           # Gemini agent, REST /chat, Discord bot
└── Angular-FrontEnd/    # Angular UI
```

## Notes

- Both services use **in-memory H2**, so their data resets on every restart. Sample data is seeded at startup by a `CommandLineRunner`.
- `ebank-bot` fails to start if `customer-service` or `ebank-service` is down, because its MCP clients connect at startup.
