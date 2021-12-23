package net.Mega2223.botDeBeberÁgua2.principal;

import net.Mega2223.botDeBeberÁgua2.objects.Janela;
import net.Mega2223.botDeBeberÁgua2.objects.Notifier;
import net.Mega2223.botDeBeberÁgua2.objects.TextFileModifier;
import net.Mega2223.botDeBeberÁgua2.utils.aguaUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static net.Mega2223.botDeBeberÁgua2.principal.aguaBot.*;
import static net.Mega2223.botDeBeberÁgua2.utils.aguaUtils.isBotBanned;

/**
 * essa classe referencia objetos estáticos, não faz mais de uma instância dela pelo amor de Deus
 */
public class aguaListener extends ListenerAdapter {

    //100x mais fácil que trocar todas as ocorrências na classe
    private static boolean isTrusted(User user) {
        return aguaUtils.isTrusted(user, TRUSTED);
    }

    private static boolean isTrusted(Member user) {
        return aguaUtils.isTrusted(user, TRUSTED);
    }


    public void onGuildVoiceJoin(final GuildVoiceJoinEvent event) {
        GuildVoiceState state = event.getVoiceState();

        if (state.inVoiceChannel() && censoredUsers.contains(event.getMember().getUser())) {
            event.getGuild().kickVoiceMember(event.getMember()).queue();
            System.out.println("removendo o " + event.getMember().getEffectiveName() + " no server " + event.getGuild().getName());
        }
    }

    //eu tinha escrito "mensagem principais" aqui
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        Member member = event.getMember();
        User user = event.getAuthor();

        final String contentRaw = message.getContentRaw();
        final String[] rawSplit = contentRaw.split(" ");

        boolean isCommand;
        boolean isBanned;

        isBanned = isBotBanned(event.getAuthor(), BOTBANNED);
        try {
            String firstC = message.getContentRaw().substring(0, 1);
            isCommand = firstC.contains("-");

        } catch (StringIndexOutOfBoundsException e) {
            isCommand = false;
        }

        BOTBANNED.clear();
        BOTBANNED = new ArrayList<>();
        String[] banRef = properties.getProperty("banned").split(",");
        for (String s : banRef) {
            BOTBANNED.add(s);
        }

        if (isCommand && isBanned) {
            event.getChannel().sendMessage("Você não pode usar comandos do bot " + user.getName() + " :(").queue();
            return;
        }
        //fixme meu deus eu to virando o YandereDev
        if (censoredUsers.contains(event.getAuthor())) {
            event.getMessage().delete().queue();
            return;
        }


        if (event.getChannel().getId().equals(canalDoBot.getId()) && contentRaw.equalsIgnoreCase("-curva")) {
            fazerUmaCurvaLegal(15, event.getChannel()); //acho que todos os else ifs deveriam seguir essa estrutura
        } else if (rawSplit[0].equalsIgnoreCase("oi") && message.getMentionedUsers().contains(jda.getSelfUser())) {
            event.getChannel().sendMessage("oi " + event.getAuthor().getAsMention() + " :)").queue();
        } else if (contentRaw.equalsIgnoreCase("-enablelisteners") && isTrusted(user)) {
            Perigosos = new aguaBot.listenersPerigosos();
            jda.addEventListener(Perigosos);
            event.getChannel().sendMessage(":thumbsup: ativados").queue();
        } else if (contentRaw.equalsIgnoreCase("-disablelisteners") && isTrusted(user)) {
            jda.removeEventListener(Perigosos);
            event.getChannel().sendMessage(":thumbsdown: desativados").queue();
        } else if (contentRaw.equalsIgnoreCase("-entra")) {
            event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
            event.getChannel().sendMessage(":thumbsup: ok").queue();
        } else if (contentRaw.equalsIgnoreCase("-vaza")) {
            event.getChannel().sendMessage(":thumbsup: tá").queue();
            event.getGuild().getAudioManager().closeAudioConnection();
        } else if (rawSplit[0].equalsIgnoreCase("-toca")) {//to;do split //eu não sei oq eu quis dizer com o cmentário aanterior mas tá bom né
            event.getChannel().sendMessage(":thumbsup: Tocarei-de-eu").queue();
            event.getGuild().getAudioManager().getSendingHandler();
            try {
                playerManager.loadAndPlay(event.getChannel(), rawSplit[1]);
            } catch (InvalidDnDOperationException e) { }
        } else if (rawSplit[0].equals("-skip")) {
            event.getChannel().sendMessage(":thumbsup: Podeixar patrão").queue();
            playerManager.getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
        } else if (rawSplit[0].equals("-volume")) {
            if (rawSplit.length == 2) {
                if (Integer.parseInt(rawSplit[1]) > 100 && !isTrusted(user)) {
                    event.getChannel().sendMessage(":thumbsdown: Só os membros confiados podem colocar as coisas acima de 100 :(").queue();
                } else {
                    event.getChannel().sendMessage(":thumbsup: Bele vamo pra " + rawSplit[1] + " de volume então").queue();
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(Integer.parseInt(rawSplit[1]));

                }
            }
        } else if (rawSplit[0].equals("-shuffle")) {
            playerManager.getGuildMusicManager(event.getGuild()).scheduler.shuffle();

        } else if (contentRaw.equalsIgnoreCase("-shutdown") && event.getAuthor().getId().equalsIgnoreCase(Mega2223ID)) {
            event.getChannel().sendMessage(":thumbsup: flw patrão").queue();
            jda.shutdown();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            System.exit(0);
        } else if (rawSplit[0].equalsIgnoreCase("-status") && isTrusted(event.getAuthor())) {
            jda.getPresence().setPresence(Activity.streaming(rawSplit[1], rawSplit[2]), true);

        } else if (rawSplit[0].equalsIgnoreCase("-mega") && rawSplit.length >= 2 && message.getAttachments().size() == 0) { //método de texto
            String args = "";
            for (int g = 1; g < rawSplit.length; g++) {
                args = args + rawSplit[g] + " ";
            }
            Notifiers.add(new Notifier(event.getAuthor(), args));
            event.getChannel().sendMessage("notifiquei o puto").queue();

        } else if (rawSplit[0].equalsIgnoreCase("-mega") && message.getAttachments().size() >= 1) {
            try {
                Notifiers.add(new Notifier(event.getAuthor(), new URL(message.getAttachments().get(0).getUrl())));
            } catch (IOException e) {
                e.printStackTrace();
            }


            event.getChannel().sendMessage("notifiquei o puto").queue();
        } else if (isTrusted(user) && rawSplit[0].equals("-fala")) {
            String mensagem = "";
            for (int ee = 1; ee < rawSplit.length; ee++) {
                mensagem = mensagem + rawSplit[ee] + " ";
            }
            event.getChannel().sendMessage(mensagem).queue();
            event.getMessage().delete().queue();

        } else if (isTrusted(event.getAuthor()) && rawSplit[0].equalsIgnoreCase("-cleanlogs")) {
            try {
                log = "";
                writeInLog(log);

                event.getChannel().sendMessage("deletados").queue();
            } catch (FileNotFoundException e) {
                event.getChannel().sendMessage("não achei os arquivos, chama o <!@" + Mega2223ID + "> pra ver oq rolou").queue();
                e.printStackTrace();
            }

        } else if (message.getAuthor().getId().equals(Mega2223ID)
                && event.getMessage().getMentionedMembers().size() >= 1
                && rawSplit[0].equalsIgnoreCase("-webhook")
        ) {
            List<Member> ment = event.getMessage().getMentionedMembers();
            Member mentioned = ment.get(0);

            WebhookManager webhook = event.getChannel().createWebhook(mentioned.getNickname()).complete().getManager();
            try {
                BufferedImage ee = (BufferedImage) getImageByURL(event.getAuthor().getAvatarUrl());
                byte[] bito = ((DataBufferByte) (ee.getRaster().getDataBuffer())).getData();
                //Icon icone = Icon.from(new Imageb);
                event.getChannel().sendFile(bito, rawSplit[2]).queue();
                //webhook.setAvatar(icone);
                //mano
                //mano
                //mano

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (rawSplit[0].equalsIgnoreCase("-pingpong")
                && 1 == event.getMessage().getMentionedMembers().size()) {

            refreshFinishedMatches();

            if (isPlaying(event.getAuthor())) {
                event.getChannel().sendMessage("Cê já tá jogando bocó").queue();
            }

            if (rawSplit.length >= 4) {
                runPingPongMatch(event, Integer.parseInt(rawSplit[2]), Integer.parseInt(rawSplit[3]));
            } else {
                runPingPongMatch(event);
            }
            //System.out.println("Argumentos de pingpong: " + rawSplit.length);

        } else if (rawSplit[0].equalsIgnoreCase("-calaaboca") && isTrusted(user)) {

            //String idDoBricks = "557722338033139760";
            event.getMessage().delete().queue();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            Member bricks = event.getGuild().getMember(jda.getUserById("557722338033139760"));
            MessageHistory history = event.getChannel().getHistory();
            List<Message> messages = history.retrievePast(Integer.parseInt(rawSplit[2])).complete();
            Member mentionedMember = event.getMessage().getMentionedMembers().get(0);

            for (Message value : messages) {
                if (value.getMember().getId().equals(mentionedMember.getId())) {
                    //System.out.println("deletede");
                    value.delete().complete();

                }
            }

            event.getChannel().sendMessage("cala a boca " + mentionedMember.getEffectiveName());
        } else if (isTrusted(user) && rawSplit[0].equalsIgnoreCase("-givelogs")) {

            System.out.println("Salvando em: " + LOG_PATH);

            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            System.out.println("Pegando file de " + LOG_PATH);
            File filo = new File(LOG_PATH);
            event.getChannel().sendFile(filo, "log.txt").queue();

        }  //será q vale a pena fazer os funnies?
        else if (isTrusted(user) && rawSplit[0].equalsIgnoreCase("-1984")) {
            List<Member> memberList = message.getMentionedMembers();
            for (Member value : memberList) {
                censoredUsers.add(value.getUser());
                GuildVoiceState voice = value.getVoiceState();
                if (voice.inVoiceChannel()) {
                    event.getGuild().kickVoiceMember(value).queue();
                }
            }

            refreshCensored();

            event.getChannel().sendMessage("Ok ministro da verdade :thumbsup:").queue();


        } else if (isTrusted(user) && rawSplit[0].equalsIgnoreCase("-2020")) {
            List<Member> memberList = message.getMentionedMembers();
            for (Member value : memberList) {
                censoredUsers.remove(value.getUser());
            }
            //to;do colocar em seu próprio void para uso futuro
            //acho que não precisa
            String mand = "";
            for (User censoredUser : censoredUsers) {
                String at = censoredUser.getId();
                mand = mand + at + ",";
            }
            properties.setProperty("censored", mand);
            try {
                saveProperties(properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.getChannel().sendMessage("Seus privilégios de fala foram restaurados").queue();
        } else if (rawSplit[0].equalsIgnoreCase("-liquidifica") && isTrusted(event.getAuthor())) {

            if (!isTrusted(user)) {
                event.getChannel().sendMessage(":thumbsdown: Só o <@!" + Mega2223ID + "> e usuários confiados podem fazer isso").queue();
            }

            List<VoiceChannel> channels = event.getGuild().getVoiceChannels();
            Member mentioned = event.getMessage().getMentionedMembers().get(0);
            VoiceChannel finals = mentioned.getVoiceState().getChannel();

            int quantas = 1;
            if (rawSplit.length >= 3) {
                quantas = Integer.parseInt(rawSplit[2]);
            }

            for (int f = 0; f < quantas; f++) {
                for (VoiceChannel atual : channels) {
                    event.getGuild().moveVoiceMember(mentioned, atual).queue();
                }
            }
            event.getGuild().moveVoiceMember(mentioned, finals).queue();
        } else if (contentRaw.equalsIgnoreCase("-givePM") && isTrusted(event.getAuthor())) {
            PrivateChannel privateChannel = jda.openPrivateChannelById(event.getAuthor().getId()).complete();

            List<PrivateChannel> channels = jda.getPrivateChannels();


            System.out.println("Size:" + channels.size());
            String deb = "";

            //ok já que eu não posso ter uma lista de usuários com privatechannel de mão dada eu vou fazer do pior jeito possivel

            List<Member> members = new ArrayList<>();
            List<Member> channelados = new ArrayList<>();
            jda.getUserById(Mega2223ID).openPrivateChannel().complete().getHistory().retrievePast(10).complete().size();

            for (int f = 0; f < jda.getGuilds().size(); f++) {
                Guild atual = jda.getGuilds().get(f);
                for (int n = 0; n < atual.getMembers().size(); n++) {
                    //System.out.println( jda.getGuilds().size()+" * "+atual.getMembers().size());
                    Member attm = atual.getMembers().get(n);
                    members.add(attm);

                    //behold a coisa mais idiota que eu tive que programar na minha vida

                    boolean putaMerdaIssoVaiDemorar;
                    //System.out.println("AAAAA" + jda.getUserById(Mega2223ID).openPrivateChannel().complete().getHistory().retrievePast(10).complete());

                    try {
                        PrivateChannel privateChannel1 = attm.getUser().openPrivateChannel().complete();
                        jda.awaitReady();
                        final List<Message> completed = privateChannel1.getHistory().retrievePast(10).complete();
                        putaMerdaIssoVaiDemorar = !completed.isEmpty();
                        System.out.println("Identificando membro " + attm.getEffectiveName() + " da guild " + atual.getName() + " | " + completed.size() + " | " + putaMerdaIssoVaiDemorar + "(" + n + "/" + atual.getMembers().size() + ")");
                    } catch (ErrorResponseException | InterruptedException | IllegalStateException e) {
                        putaMerdaIssoVaiDemorar = false;
                    } catch (UnsupportedOperationException e) {
                        putaMerdaIssoVaiDemorar = false;
                    }

                    if (putaMerdaIssoVaiDemorar) {
                        channelados.add(attm);
                    }


                    //getting your bot from Discord any%
                }
            }

            for (int g = 0; g < channelados.size(); g++) {
                System.out.println(g + ": tenho um canal com " + channelados.get(g).getUser());
                User atualizado = channelados.get(g).getUser();
                List<Message> chat = atualizado.openPrivateChannel().complete().getHistory().retrievePast(100).complete();
                for (Message atualllllll : chat) {
                    if (atualllllll.getAuthor().isBot()) {
                        System.out.println("bot em " + atualizado.getName());
                        continue;
                    }
                    System.out.println("C: " + atualllllll.getChannel().getName() + "| A: " + atualllllll.getAuthor().getName() + "| M:" + atualllllll.getContentRaw());
                }
            }


            if (true) {
                return;
            }

            for (PrivateChannel atual : channels) {
                deb = deb + "*" + atual.getUser() + ":*" + "\n";
                List<Message> reTrievePast = atual.getHistory().retrievePast(100).complete();
                System.out.println("AtChannelSize:" + reTrievePast.size());
                System.out.println("CANAL: " + reTrievePast.get(0).getAuthor().getName());
                for (int f = 0; f < reTrievePast.size(); f++) {
                    System.out.println("C:" + atual.getUser().getName() + "   A:" + reTrievePast.get(f).getAuthor().getName() + "  M:" + f + " : " + reTrievePast.get(f).getContentRaw());
                    Message reading = reTrievePast.get(f);
                    deb = deb + "[" + reading.getAuthor().getName() + "]: " + reading.getContentRaw() + "\n";
                }
                deb = deb + "\n";
            }

            privateChannel.sendMessage(deb).queue();
        } else if (rawSplit[0].equalsIgnoreCase("-move")) {
            VoiceChannel voice = event.getMember().getVoiceState().getChannel();
            VoiceChannel voice2 = jda.getVoiceChannelById(rawSplit[1]);
            Guild gui = event.getGuild();
            for (int f = 0; f < voice.getMembers().size(); f++) {
                Member atual = voice.getMembers().get(f);
                gui.moveVoiceMember(atual, voice2).queue();
            }
        } else if (rawSplit[0].equalsIgnoreCase("-addproperty") || rawSplit[0].equalsIgnoreCase("-setproperty")) {

            try {
                String argName = rawSplit[1];
                String argValue = rawSplit[2];
                properties.put(argName, argValue);
                saveProperties(properties);
                event.getChannel().sendMessage("ok coloquei :thumbsup:").queue();
            } catch (ArrayIndexOutOfBoundsException e) {
                event.getChannel().sendMessage("Deu algo errado, vê se os arrays tão certos").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (rawSplit[0].equalsIgnoreCase("-removeproperty")) {
            try {
                properties.remove(rawSplit[1]);
                saveProperties(properties);
                event.getChannel().sendMessage("ok apaguei :thumbsup:").queue();
            } catch (ArrayIndexOutOfBoundsException | IOException ex) {
                event.getChannel().sendMessage("identação errada seu trouxa").queue();
            }

        } else if (rawSplit[0].equalsIgnoreCase("-getproperties") && isTrusted(event.getAuthor())) {
            String fala = "";
            for (int f = 0; f < properties.size(); f++) {
                String act = properties.stringPropertyNames().stream().toArray()[f].toString();
                fala = fala + act + "=" + properties.get(act) + "\n";

            }
            event.getAuthor().openPrivateChannel().complete().sendMessage(fala).queue();

        } else if (rawSplit[0].equalsIgnoreCase("-giveservers") && isTrusted(event.getAuthor())) {
            String out = "";
            for (int g = 0; g < jda.getGuilds().size(); g++) {
                System.out.println(g + "/" + jda.getGuilds().size());
                Guild atual = jda.getGuilds().get(g);
                out = out + "**" + atual.getName() + ":** " + atual.getMembers().size() + " membros \n";
            }

            System.out.print(out);
            event.getChannel().sendMessage(out).queue();
        } else if (rawSplit[0].equalsIgnoreCase("-cleannotifiers") && event.getAuthor().getId().equals(Mega2223ID)) {
            for (Notifier notifier : Notifiers) {
                notifier.dispose();
            }
            Notifiers.clear();

        } else if (rawSplit[0].equalsIgnoreCase("-report") && isTrusted(event.getAuthor())) {

            String finalReport = "```[INFO]:\n\n";
            String[] e;

            Object[] intsss = jda.getGatewayIntents().stream().toArray();

            finalReport = finalReport + "GatewayIntents: ";
            for (int f = 0; f < intsss.length; f++) {
                GatewayIntent at = (GatewayIntent) intsss[f];
                if (f != intsss.length - 1) {
                    finalReport = finalReport + at.name() + ", ";
                } else {
                    finalReport = finalReport + at.name() + ".";
                }
            }
            finalReport = finalReport + "\n\n";


            finalReport = finalReport + "[SERVIDORES:]\n\n";
            List<Guild> servers = jda.getGuilds();


            for (Guild atual : servers) {
                finalReport = finalReport + atual.getName() + ":\n";
                finalReport = finalReport + "Membros: " + atual.getMembers().size() + "\n";
                finalReport = finalReport + "Emotes: " + atual.getEmotes().size() + "\n";
                finalReport = finalReport + "Dono: " + atual.getOwner().getUser().getName() + "#" + atual.getOwner().getUser().getDiscriminator() + "\n\n";

            }

            System.out.println(finalReport);
            event.getChannel().sendMessage(finalReport + "```").queue();
        } else if (rawSplit[0].equalsIgnoreCase("-pegaarquivo")) {
            try {
                String manda = TextFileModifier.readFile(fileCreatorPath + "\\" + rawSplit[1]);
                event.getChannel().sendMessage(manda.replace("null\n", "")).queue();
            } catch (IOException e) {
                event.getChannel().sendMessage("não deu mano, IOException :(").queue();
                e.printStackTrace();
            }
        } else if (rawSplit[0].equalsIgnoreCase("-mandaarquivo") || rawSplit[0].equalsIgnoreCase("-escrevenoarquivo")) {
            try {
                String path = rawSplit[1];
                String writeWhat = "";
                for (int g = 2; g < rawSplit.length; g++) {
                    writeWhat = writeWhat + rawSplit[g] + " ";
                }
                System.out.println(fileCreatorPath + "\\" + path);
                TextFileModifier.writeInFile(writeWhat, fileCreatorPath + "\\" + path);
                event.getChannel().sendMessage(":thumbsup: beleza mano").queue();
            } catch (ArrayIndexOutOfBoundsException | IOException ex) {
                event.getChannel().sendMessage(ex.getMessage()).queue();
                ex.printStackTrace();
            }
        } else if (rawSplit[0].equalsIgnoreCase("-listaosarquivos")) {
            List<File> files = TextFileModifier.listFilesForFolder(fileCreatorPath);
            String resposta = "";
            for (File at : files) {
                resposta = resposta + at.getName() + "\n";
            }
            event.getChannel().sendMessage(resposta).queue();
        } else if (rawSplit[0].equalsIgnoreCase("-saysplit")) {
            try {
                String[] manda = TextFileModifier.readFile(fileCreatorPath + "\\" + rawSplit[1]).split(rawSplit[2]);

                for (int u = 0; u < manda.length - 1; u++) {
                    try {
                        event.getChannel().sendMessage(manda[u]).queue();
                    } catch (IllegalArgumentException e) {
                    }
                }

            } catch (IOException | ArrayIndexOutOfBoundsException exception) {
                event.getChannel().sendMessage("sintaxe errada trouxa").queue();
            }
        } else if (rawSplit[0].equalsIgnoreCase("-windowlog") && event.getAuthor().getId().equalsIgnoreCase(Mega2223ID)) {
            try {
                currentJanela = new Janela();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (rawSplit[0].equalsIgnoreCase("-printlog") && isTrusted(event.getAuthor())) {
            System.out.println(log);
        } else if (rawSplit[0].contains("-redditpost")) {
            Message legal = event.getChannel().sendMessage("Ok perai").complete();

            String[] enterSplit = message.getContentRaw().split("\n");

            if (enterSplit.length < 3) {
                event.getChannel().sendMessage("tem que ter 3 argumentos seu otário").queue();
                return;
            }

            String content = "";
            for (int f = 2; f < enterSplit.length; f++) {
                content = content + enterSplit[f] + "\n\n";
            }

            makePost(properties.getProperty("botSub").toLowerCase(), enterSplit[1], content, event, false);
            legal.delete().queue();
        } else if (rawSplit[0].contains("-redditimage") && isTrusted(event.getAuthor())) {
            String embed;
            try {
                embed = event.getMessage().getEmbeds().get(0).getImage().getUrl();
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                embed = message.getAttachments().get(0).getUrl();
            }

            Message legal = event.getChannel().sendMessage("Ok perai").complete();

            makePost(properties.getProperty("botSub").toLowerCase(), "", embed, event, true);
            legal.delete().queue();
        } else if (message.getContentRaw().equalsIgnoreCase("<@!654020411327250433>")) {
            event.getChannel().sendMessage("Bom dia " + event.getAuthor().getAsMention() + ", eu sou o Beba Água Bot, um bot de utilidades feito pelo <@!" + Mega2223ID + "> para a República Nômade e a Cúpula da Edição ;) \nSe vc quiser ver os comandos, olha o meu README.md no GitHub:\n https://github.com/Mega2223/bot-de-beber-agua-2/blob/main/README.md ").queue();
        } else if (message.getContentRaw().equalsIgnoreCase("-runnableteste")) {

            //fixme POOL_EXECUTOR.scheduleAtFixedRate(new aguaBot.WaterReminder(),1,1, TimeUnit.SECONDS);
            event.getChannel().sendMessage("Ok foi").queue();

        } else if (message.getContentRaw().equalsIgnoreCase("-amogus") && isTrusted(event.getAuthor())) {
            List<Member> membros = event.getGuild().getMembers();
            for (Member men : membros) {
                try {
                    men.modifyNickname(men.getEffectiveName().replace("a", "ඞ")).queue();
                } catch (InsufficientPermissionException | HierarchyException ex) {
                }
            }
            event.getChannel().sendMessage("AMOGUS").queue();
        } else if (message.getContentRaw().equalsIgnoreCase("-unamogus") && isTrusted(event.getAuthor())) {
            List<Member> membros = event.getGuild().getMembers();
            for (Member men : membros) {
                try {
                    men.modifyNickname(men.getEffectiveName().replace("ඞ", "a")).queue();
                } catch (InsufficientPermissionException | HierarchyException ex) {
                }
            }
            event.getChannel().sendMessage("AMOGUS").queue();
        } else if (rawSplit[0].equalsIgnoreCase("-cantacomigo")) {

            try {
                String[] manda = TextFileModifier.readFile(fileCreatorPath + "\\" + rawSplit[1]).split(rawSplit[2]);

                for (int u = 0; u < manda.length - 1; u++) {
                    try {
                        event.getChannel().sendMessage(manda[u]).queue();
                    } catch (IllegalArgumentException e) {
                    }
                }

            } catch (IOException | ArrayIndexOutOfBoundsException exception) {
                event.getChannel().sendMessage("sintaxe errada trouxa").queue();
            }

        }

    }
}

