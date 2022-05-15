package net.Mega2223.botDeBeberÁgua2.objects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BetterCallSaulListener extends ListenerAdapter {

    static final String SAUL = "https://tenor.com/view/howard-hamlin-better-call-saul-howar-hamlini-howardsus-gif-25568404";
    final User user;
    boolean active = true;
    JDA jda;

    public BetterCallSaulListener(User user) {
        this.user = user;
        jda = user.getJDA();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (!active) {
            return;
        }

        String[] msg = event.getMessage().getContentRaw().split(" ");
        if (msg[0].equalsIgnoreCase("-stopcallingsaul") && msg[1].equalsIgnoreCase(user.getAsMention())) {
            active = false;
            jda.removeEventListener(this);
            return;
        }
        if (event.getAuthor().getId().equalsIgnoreCase(user.getId())) {
            event.getMessage().reply(SAUL).complete();
        }
    }
}
