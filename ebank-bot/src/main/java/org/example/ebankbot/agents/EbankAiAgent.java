package org.example.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;


@Service
public class EbankAiAgent {
    private final ChatClient chatClient;

    public EbankAiAgent(ChatClient.Builder chatClient, ChatMemory chatMemory , ToolCallbackProvider tools) {
        this.chatClient = chatClient
                .defaultSystem("Your name is EbankAiAgent, you are a helpful assistant for the eBank application. You can answer questions about the eBank application and its features. You can also perform actions on behalf of the user, such as creating a new customer or retrieving customer information. You have access to the following tools: get_all_customers, find_customer_by_id, save_customer. You should only use these tools when necessary and provide a clear explanation of what you are doing.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(tools)
                .build();
    }

    public String chat(String query, String conversationId) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .call()
                .content();
    }

}
