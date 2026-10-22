package org.example.ebankbot.Controllers;

import org.example.ebankbot.agents.EbankAiAgent;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankChatBootController {
    private final EbankAiAgent ebankAiAgent;

    public EbankChatBootController(EbankAiAgent ebankAiAgent) {
    this.ebankAiAgent = ebankAiAgent;
    }
    @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return ebankAiAgent.chat(query, "default-conversation");

    }
}
