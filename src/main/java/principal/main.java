package principal;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import sun.misc.Launcher;
import utils.ElectionPoll;
import utils.PingPongMatch;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class main {

    public static final String Mega2223ID = "301424656051732491";
    public static final String PudimAtomicoID = "491748519984627712";
    public static final String[] TRUSTED = {Mega2223ID,PudimAtomicoID};
    public static JDABuilder builder;
    public static JDA jda;
    public static TextChannel canalDoBot;
    public static Guild Imperio;
    public static ListenerAdapter Perigosos;
    public static PlayerManager fer;
    public static ListenerAdapter kik;
    public static String log;
    public static ElectionPoll poll; //todo remove
    public static List<ElectionPoll.Candidate> candidates;//todo remove
    public static PingPongMatch universalMatch;//todo remove
    public static List<User> censoredUsers;

    public static void main(String[] args) throws LoginException, IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(new File("C:\\Users\\Imperiums\\Desktop\\key.txt")));
        String key = inputStream.readLine();
        System.out.println("Ativando na key " + key);
        builder = JDABuilder.create(key, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_VOICE_STATES);
        EnumSet<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS);
        builder.enableIntents(intents);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ACTIVITY);
        jda = builder.build();


        try {/*Thread.sleep(7000);*/
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.streaming("sua mãe pelada", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"));

        Imperio = jda.getGuildById("606274842722959384");

        Imperio.loadMembers();
        Imperio.getMemberCache();

        List<TextChannel> textChannels = Imperio.getTextChannels();
        canalDoBot = jda.getTextChannelById("886685128762482800");

        jda.addEventListener(new seraQueEuLembroComoUsaEventListeners());
        jda.addEventListener(new eventListenerPararelo());
        loadCensored();

        System.out.println("tudopronto");
        Message mensagemfoda = canalDoBot.sendMessage("tudo pronto to rodando").complete();
        fer = PlayerManager.getInstance();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mensagemfoda.delete().queue();
    }

    public static boolean isTrusted(User user){
        boolean is = false;
        for(int q = 0; q < TRUSTED.length; q++){
            if (user.getId().equalsIgnoreCase(TRUSTED[q])){is = true;}
        }
        return is;
    }

    public static boolean isTrusted(Member user){
        return isTrusted(user.getUser());
    }

    public static void loadCensored(){
        censoredUsers = new ArrayList<User>();
        //todo
    }



    public static void runPingPongMatch(GuildMessageReceivedEvent event) {
        universalMatch = new PingPongMatch(event);
    }

    public static void runPingPongMatch(GuildMessageReceivedEvent event, int intervaloNumerico, int jogadas) {
        universalMatch = new PingPongMatch(event, intervaloNumerico, jogadas);
    }

    public static void updatePingPongMatch(GuildMessageReceivedEvent event) {
        universalMatch.refute(event);
    }

    public static BufferedImage getUserAvatar(User user) throws IOException {
        URLConnection connection = new URL(user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl()).openConnection();
        connection.setRequestProperty("User-Agent", "bot emily-bot");
        BufferedImage profileImg;
        try {
            profileImg = ImageIO.read(connection.getInputStream());
        } catch (Exception ignored) {
            profileImg = ImageIO.read(Objects.requireNonNull(Launcher.class.getClassLoader().getResource("default_profile.jpg")));
        }
        return profileImg;
    }

    public static void fazerUmaCurvaLegal(int numberOfSpaces, TextChannel canalDoBot) {
        String nextLane = "";
        for (double g = 0; g < 20; g = g + 0.1) {
            double f = Math.cos(g) * 2;
            //System.out.println("Estamos na fase " + g + ", o cosseno de g é " + f + ", que se arrendoda pra " + (int)f + ", que corrigido dá, " + fA);
            //System.out.println((numberOfSpaces/2 * f) + 0.1);
            for (int ga = 0; ga < (numberOfSpaces / 2 * f) + (numberOfSpaces - 1); ga++) {
                nextLane = nextLane + "⠀";
            }

            nextLane = nextLane + "0";
            System.out.println(nextLane);
            canalDoBot.sendMessage(nextLane).queue();
            nextLane = "";
        }
    }

    public static class listenersPerigosos extends ListenerAdapter {
        public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
            //System.out.println(event.getGuild().getId() + " != " +  Imperio.getId() + " / " + event.getMember().getUser().getName() + " / " + event.getGuild().getName());
            if (!event.getGuild().getId().equals(Imperio.getId())) {
                return;
            }
            String legal = "User " + event.getUser().getName() + " atualizou seu status para " + event.getNewValue().name() + "";
            canalDoBot.sendMessage(legal).queue();
            System.out.println(legal);
        }

        public void onUserUpdateActivities(UserUpdateActivitiesEvent event) {
            if (!event.getGuild().getId().equals(Imperio.getId())) {
                return;
            }
            String legal = "User " + event.getUser().getName() + " atualizou sua atividade para " + event.getNewValue().get(0) + "";
            canalDoBot.sendMessage(legal).queue();
            System.out.println(legal);
        }
    }

    public static class kicka extends ListenerAdapter {
        public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
            if (event.getMember().getUser().getId().equals("638898751880036400")) {
                event.getGuild().kickVoiceMember(event.getMember()).queue();
            }
        }
    }

    public static class eventListenerPararelo extends ListenerAdapter {

        public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

            String[] rawSplit = event.getMessage().getContentRaw().split(" ");

            if (rawSplit[0].equalsIgnoreCase("ping") || rawSplit[0].equalsIgnoreCase("pong")) {
                System.out.println("==========================");
                System.out.println("RESPOSTA PING IDENTIFICADA");
                System.out.println(event.getAuthor().getId() + "?=" + universalMatch.U1.getId() + " " + universalMatch.U2.getId());

                if (event.getMember().getId().equals(universalMatch.U1.getId()) ||
                        event.getMember().getId().equals(universalMatch.U2.getId())) {
                    System.out.println("um user válido fez isso, legal");
                    System.out.println("==========================");
                    updatePingPongMatch(event);
                } else {
                    event.getChannel().sendMessage("cê não tá jogando bocó").queue();
                }

            }
        }


    }

    public static class seraQueEuLembroComoUsaEventListeners extends ListenerAdapter { //lembro?

        public void onGuildVoiceJoin(final GuildVoiceJoinEvent event){
            GuildVoiceState state = event.getVoiceState();
            System.out.println("removendo o " + event.getMember().getEffectiveName() + " no server " + event.getGuild().getName());
            if (state.inVoiceChannel() && censoredUsers.contains(event.getMember().getUser())){event.getGuild().kickVoiceMember(event.getMember()).queue();}
        }

        //receivers de mensagem principais
        public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
            Message message = event.getMessage();
            Member member = event.getMember();
            User user = event.getAuthor();

            if (!event.getMessage().getAuthor().isBot()) {
                System.out.println("Pessoa: " + event.getAuthor().getName() + " Mensagem: " + message.getContentRaw());
                log = log + "[" + event.getGuild().getName() + " | " + Date.from(Instant.now()) + " | " + event.getMember().getEffectiveName() + "]: " + event.getMessage().getContentRaw() + "\n";
            }
            if(censoredUsers.contains(event.getAuthor())){
                event.getMessage().delete().queue();
                return;
            }


            canalDoBot = jda.getTextChannelById("886685128762482800");
            System.out.println("EMB: " + message.getAttachments().size());

            if (event.getChannel().getId().equals(canalDoBot.getId()) && message.getContentRaw().equalsIgnoreCase("-curva")) {
                fazerUmaCurvaLegal(15, event.getChannel());
            } else if (message.getContentRaw().split(" ")[0].equalsIgnoreCase("oi") && message.getMentionedUsers().contains(jda.getSelfUser())) {
                event.getChannel().sendMessage("oi " + event.getAuthor().getAsMention() + " :)").queue();
            } else if (message.getContentRaw().equalsIgnoreCase("-enablelisteners") && isTrusted(user)) {
                Perigosos = new listenersPerigosos();
                jda.addEventListener(Perigosos);
                event.getChannel().sendMessage(":thumbsup: ativados").queue();
            } else if (message.getContentRaw().equalsIgnoreCase("-disablelisteners") && isTrusted(user)) {
                jda.removeEventListener(Perigosos);
                event.getChannel().sendMessage(":thumbsdown: desativados").queue();
            } else if (message.getContentRaw().equalsIgnoreCase("-tiraogames")) {
                event.getChannel().sendMessage("ok vou tirar").queue();
                kik = new kicka();
                jda.addEventListener(kik);
                Imperio.kickVoiceMember(Imperio.getMember(jda.getUserById("638898751880036400"))).queue();
            } else if (message.getContentRaw().equalsIgnoreCase("-deixaogames")) {
                event.getChannel().sendMessage("ok vou deixar").queue();
                jda.removeEventListener(kik);
            } else if (message.getContentRaw().equalsIgnoreCase("-entra")) {
                event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
                event.getChannel().sendMessage(":thumbsup: ok").queue();
            } else if (message.getContentRaw().equalsIgnoreCase("-vaza")) {
                event.getChannel().sendMessage(":thumbsup: tá").queue();
                event.getGuild().getAudioManager().closeAudioConnection();

            } else if (message.getContentRaw().split(" ")[0].equalsIgnoreCase("-toca")) {//todo split
                event.getChannel().sendMessage(":thumbsup: tocarei-de-eu").queue();

                event.getGuild().getAudioManager().getSendingHandler();


                //final AudioPlayerManager manager = new DefaultAudioPlayerManager();
                //final AudioPlayer player = manager.createPlayer();


                fer.loadAndPlay(event.getChannel(), message.getContentRaw().split(" ")[1]);


            } else if (message.getContentRaw().split(" ")[0].equals("-skip")) {
                event.getChannel().sendMessage(":thumbsup: Podeixar patrão").queue();

                fer.getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
            } else {
                String contentRaw = message.getContentRaw();
                String[] rawSplit = contentRaw.split(" ");

                if (rawSplit[0].equals("-volume")) {
                    if (rawSplit.length == 2) {
                        if (Integer.parseInt(rawSplit[1]) >= 100 && !isTrusted(user)) {
                            event.getChannel().sendMessage(":thumbsdown: Só os membros confiados podem colocar as coisas acima de 100 :(").queue();
                        } else {
                            event.getChannel().sendMessage(":thumbsup: Bele vamo pra " + rawSplit[1] + " de volume então").queue();
                            fer.getGuildMusicManager(event.getGuild()).player.setVolume(Integer.parseInt(rawSplit[1]));

                        }
                    }
                } else if (contentRaw.equalsIgnoreCase("-shutdown") && event.getAuthor().getId().equalsIgnoreCase(Mega2223ID)) {
                    event.getChannel().sendMessage(":thumbsup: flw patrão").queue();
                    jda.shutdown();

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                    }

                    System.exit(0);
                } else if (rawSplit[0].equalsIgnoreCase("-status")) {
                    //jda.getPresence().set
                    //todo cavalo
                } else if (rawSplit[0].equalsIgnoreCase("-mega") && rawSplit.length >= 2 && message.getAttachments().size() == 0) { //método de texto
                    String args = "";
                    for (int g = 1; g < rawSplit.length; g++) {
                        args = args + rawSplit[g] + " ";
                    }
                    new notifier(event.getAuthor(), args);
                    event.getChannel().sendMessage("notifiquei o puto").queue();

                } else if (rawSplit[0].equalsIgnoreCase("-mega") && message.getAttachments().size() >= 1) { //todo concertaisso
                    try {
                        new notifier(event.getAuthor(), new URL(message.getAttachments().get(0).getUrl()));
                    } catch (IOException e) { e.printStackTrace();}


                    event.getChannel().sendMessage("notifiquei o puto").queue();
                } else if (isTrusted(user) && rawSplit[0].equals("-fala")) {
                    String mensagem = "";
                    for (int ee = 1; ee < rawSplit.length; ee++) {
                        mensagem = mensagem + rawSplit[ee] + " ";
                    }
                    event.getChannel().sendMessage(mensagem).queue();
                    event.getMessage().delete().queue();

                } else if (message.getAuthor().getId().equals(Mega2223ID)
                        && event.getMessage().getMentionedMembers().size() > 1
                        && rawSplit[0].equals("-webhook")
                ) {
                    List<Member> ment = event.getMessage().getMentionedMembers();
                    Member mentioned = ment.get(0);
                    WebhookManager webhook = (WebhookManager) event.getChannel().createWebhook(mentioned.getNickname()).complete();
                    //try {
                    //webhook.setAvatar(getUserAvatar(event.getAuthor())); todo termina isso
                    //} catch (IOException e) {
                    //e.printStackTrace();
                    //}
                } else if (rawSplit[0].equalsIgnoreCase("-pingpong")
                        && 1 == event.getMessage().getMentionedMembers().size()) {
                    if (rawSplit.length >= 4) {
                        runPingPongMatch(event, Integer.parseInt(rawSplit[2]), Integer.parseInt(rawSplit[3]));
                    } else {
                        runPingPongMatch(event);
                    }
                    System.out.println("Argumentos de pingpong: " + rawSplit.length);

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

                    for (int g = 0; g < messages.size(); g++) {
                        if (messages.get(g).getMember().getId().equals(mentionedMember.getId())) {
                            System.out.println("deletede");
                            messages.get(g).delete().complete();

                        }
                    }

                    event.getChannel().sendMessage("cala a boca " + mentionedMember.getEffectiveName());
                } else if (isTrusted(user) && rawSplit[0].equalsIgnoreCase("-givelog")) {
                    File logFile = new File(System.getProperty("user.dir") + "\\log.txt");
                    System.out.println("Salvando em: " + System.getProperty("user.dir") + "\\log.txt");
                    FileOutputStream inputStream;
                    try {
                        inputStream = new FileOutputStream(logFile);
                    } catch (FileNotFoundException e) {
                        event.getChannel().sendMessage("excedi").queue();
                        return;
                    }

                    OutputStreamWriter writer = new OutputStreamWriter(inputStream);

                    try {
                        writer.write(log);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    event.getChannel().sendFile(logFile, "log.txt").queue();
                } else if (rawSplit[0].equalsIgnoreCase("-startPoll")) {
                    if (event.getAuthor().getId().equalsIgnoreCase(Mega2223ID)) {
                        event.getChannel().sendMessage(":thumbsup:").queue();
                        Role cargo = event.getMessage().getMentionedRoles().get(0);
                        candidates = new ArrayList<ElectionPoll.Candidate>();
                        for (int g = 0; g < event.getMessage().getMentionedMembers().size(); g++) {
                            Member atual = event.getMessage().getMentionedMembers().get(g);
                            candidates.add(new ElectionPoll.Candidate(event.getMember(), event.getMember().getEffectiveName()));
                        }
                        poll = new ElectionPoll(candidates, cargo);

                    }
                } else if (rawSplit[0].equalsIgnoreCase("-endPoll")){
                    ElectionPoll.Candidate winner;
                    List<ElectionPoll.Candidate> runPol = poll.runPoll();
                    if(runPol.size() == 1){
                        winner = runPol.get(0);
                    } else
                    {
                        //poll = new ElectionPoll(new ArrayList<ElectionPoll.Candidate>());
                    }
                }
                else if (rawSplit[0].equalsIgnoreCase("-addfunny")){
                    String name = rawSplit[1];
                    //todo Image funny = (Image) message.getEmbeds().get(0).getImage();
                }
                else if (rawSplit[0].equalsIgnoreCase("-getfunny")){}
                else if (isTrusted(user) && rawSplit[0].equalsIgnoreCase("-1984")){
                    List<Member> memberList = message.getMentionedMembers();
                    for (int g = 0; g < memberList.size(); g++){
                        censoredUsers.add(memberList.get(g).getUser());
                        GuildVoiceState voice = memberList.get(g).getVoiceState();
                        if(voice.inVoiceChannel()){event.getGuild().kickVoiceMember(memberList.get(g)).queue();}
                    }
                    event.getChannel().sendMessage("Ok ministro da verdade :thumbsup:").queue();


                }

                else if (isTrusted(user) && rawSplit[0].equalsIgnoreCase("-2020")){
                    List<Member> memberList = message.getMentionedMembers();
                    for (int g = 0; g < memberList.size(); g++){
                        censoredUsers.remove(memberList.get(g).getUser());
                    }
                    event.getChannel().sendMessage("Seus privilégios de fala foram restaurados").queue();
                }

                else if(rawSplit[0].equalsIgnoreCase("-liquidifica")){

                    if(!isTrusted(user)){event.getChannel().sendMessage(":thumbsdown: Só o <@!" + Mega2223ID + "> e usuários confiados podem fazer isso").queue();}

                    List<VoiceChannel> channels = event.getGuild().getVoiceChannels(); //todo varias vezes
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    VoiceChannel finals = mentioned.getVoiceState().getChannel();
                    for (int g = 0; g<channels.size(); g++){
                        VoiceChannel atual = channels.get(g);
                        event.getGuild().moveVoiceMember(member, channels.get(g)).queue();
                    }

                    event.getGuild().moveVoiceMember(member, finals).queue();
                }

            }

        }

    }


}
