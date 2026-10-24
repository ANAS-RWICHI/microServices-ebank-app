package org.example.ebankbot.Discord;

import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.ebankbot.agents.EbankAiAgent;

@DiscordController
public class DiscordBot {
    private final EbankAiAgent ebankAiAgent;

    public DiscordBot(EbankAiAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }
    @DiscordMapping
    public void perform(MessageReceivedEvent event){
        System.out.println(event);
        if(!event.getAuthor().isBot()){
            String query=event.getMessage().getContentRaw();
            String response=ebankAiAgent.chat(query,event.getAuthor().getId());
            event.getChannel().sendMessage(response).queue();
        }
    }
}
