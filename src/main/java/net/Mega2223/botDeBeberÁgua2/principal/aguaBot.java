package net.Mega2223.botDeBeberÁgua2.principal;


import ga.dryco.redditjerk.implementation.RedditApi;
import ga.dryco.redditjerk.wrappers.Link;
import ga.dryco.redditjerk.wrappers.Subreddit;
import lavaplayer.PlayerManager;
import net.Mega2223.botDeBeberÁgua2.objects.Janela;
import net.Mega2223.botDeBeberÁgua2.objects.Notifier;
import net.Mega2223.botDeBeberÁgua2.objects.PingPongMatch;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static net.Mega2223.botDeBeberÁgua2.utils.aguaUtils.isTrusted;

@SuppressWarnings({"Typo", "unused", "String concatenation"})
public class aguaBot {


    protected static final ScheduledThreadPoolExecutor POOL_EXECUTOR = new ScheduledThreadPoolExecutor(1); //eu não entendo absolutamente nada de programação e o fato de que eu achei que isso ia dar errado prova o meu ponto
    static final String projectPath = System.getProperty("user.dir");
    static final String LOG_PATH = projectPath + "\\log.txt";
    static final String PROPERTIES_PATH = projectPath + "\\configs.properties";
    protected static final String fileCreatorPath = projectPath + "\\src\\main\\java\\net\\Mega2223\\arquivosConfidenciais";
    protected static final String ImperioID = "606274842722959384";
    static final String Mega2223ID = "301424656051732491";
    public static List<String> BOTBANNED;
    public static List<String> TRUSTED;
    public static JDABuilder builder;
    public static JDA jda;
    public static TextChannel canalDoBot;
    public static Guild Imperio;
    public static Subreddit subDoBot;
    public static ListenerAdapter Perigosos;
    public static PlayerManager playerManager;
    public static ListenerAdapter kik;
    public static String log;
    public static Properties properties;
    public static String JDAKey;
    public static String[] RedditKey;
    public static List<Notifier> Notifiers;
    public static List<PingPongMatch> universalMatches;
    public static Janela currentJanela;
    public static List<User> censoredUsers;
    public static List<Member> lowResDimUsers;
    public static RedditApi reddit;
    public static ga.dryco.redditjerk.wrappers.User redditUser;
    protected static String TCBotID;
    public static List<String> waterUrls;

    public static void main(String[] args) throws LoginException, IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(projectPath + "\\key.txt")));
        String key = inputStream.readLine();
        inputStream.close();
        JDAKey = key.split(" ")[0];
        RedditKey = key.split(" ")[1].split(";");

        System.out.println("Ativando o JDA na key " + JDAKey);
        System.out.println("Ativando o Reddit nas keys " + key.split(" ")[1]);

        builder = JDABuilder.create(JDAKey, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_VOICE_STATES);
        EnumSet<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS);
        builder.enableIntents(intents);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ACTIVITY);

        //C:\Users\Imperiums\AppData\Local\Temp\idea_classpath1721244032 net.Mega2223.principal.aguaBot
        //Ativando

        jda = builder.build();
        log = loadLog() + "\n";

        reddit = RedditApi.getRedditInstance(RedditKey[0] + "\\" + new Random().nextInt());

        redditUser = reddit.login(RedditKey[0], RedditKey[1], RedditKey[2], RedditKey[3]);//user password clientID clientSecret

        try {/*Thread.sleep(7000);*/
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadProperties();

        saveProperties(properties);

        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.streaming("sua mãe pelada", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"));

        initStaticObjects();

        initalizeLists();

        Imperio.loadMembers();
        Imperio.getMemberCache();


        jda.addEventListener(new aguaListener());
        jda.addEventListener(new eventListenerParalelo());
        jda.addEventListener(new listenersDoLog());
        jda.addEventListener(new listenersDaLowRes());

        log = log + "[BOT LIGADO EM: " + Time.from(Instant.now()) + "]\n";
        writeInLog(log);

        //createConsoleListener();

        System.out.println("tudopronto");
        Message mensagemfoda = canalDoBot.sendMessage("tudo pronto to rodando").complete();


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mensagemfoda.delete().queue();

        //List<Member> eba = Imperio.getMembers();s
        //402804167317520385

        Guild cupula = jda.getGuildById("324759074056962048");

        /*for(TextChannel act : cupula.getTextChannels()){
            List<Message> msga = act.getHistory().retrievePast(20).complete();
            for(Message mAct : msga){
                if(mAct.getAuthor().getId().equalsIgnoreCase("409875053308674060")){mAct.delete().queue();}
            }
        }*/

    }

    private static void createConsoleListener() {/*fixme
        System.out.println("scheduled");
        Console console = System.console();
        System.out.println("scheduled");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("scheduled");
        final List<String>[] ant = new List[]{reader.lines().collect(Collectors.toList())};
        final int[] size = {ant[0].size()};
        System.out.println("scheduled");
        //muy chicke eu sei
        ScheduledThreadPoolExecutor ex = new ScheduledThreadPoolExecutor(1);
        System.out.println("scheduled");
        Thread thread = new Thread(){
            @Override
            public void run(){
                System.out.println("rodando");
                ant[0] = reader.lines().collect(Collectors.toList());
                if (size[0] != ant[0].size()){
                    size[0] = ant[0].size();
                    String cmd = ant[0].get(0);
                    System.out.println("CMD:" + cmd);
                    GuildMessageReceivedEvent event = new GuildMessageReceivedEvent(jda, 0, new FakeMessage(cmd));
                }

            }
        };
        System.out.println("scheduled");
        ex.scheduleAtFixedRate(thread,0,1, TimeUnit.SECONDS);
        System.out.println("scheduled");
    */
    }

    private static void initStaticObjects() {
        Imperio = jda.getGuildById(ImperioID);
        TRUSTED = new ArrayList<>();
        BOTBANNED = new ArrayList<>();
        censoredUsers = new ArrayList<>();//eu sou mt consistente nas capslocks ne pqp
        lowResDimUsers = new ArrayList<>();
        Notifiers = new ArrayList<>();
        waterUrls = new ArrayList<>();
        universalMatches = new ArrayList<>();
        TCBotID = properties.getProperty("botchannel");
        playerManager = PlayerManager.getInstance();
        canalDoBot = jda.getTextChannelById(TCBotID);
        subDoBot = reddit.getSubreddit(properties.getProperty("botSub"));

    }



    @Deprecated
    public static boolean addToCensoredFile(User userrr) throws IOException {
        FileOutputStream inputStreamm;
        File logFile = new File(projectPath + "\\banList.txt");
        System.out.println("Adicionando um user para a lista em: " + projectPath + "\\banList.txt");
        try {
            inputStreamm = new FileOutputStream(logFile);
        } catch (FileNotFoundException e) {
            return false;
        }

        OutputStreamWriter writerr = new OutputStreamWriter(inputStreamm);
        writerr.write(" " + userrr.getId());
        return true;
    }


    public static void runPingPongMatch(GuildMessageReceivedEvent event) {
        universalMatches.add(new PingPongMatch(event));
    }

    public static void runPingPongMatch(GuildMessageReceivedEvent event, int intervaloNumerico, int jogadas) {
        universalMatches.add(new PingPongMatch(event, intervaloNumerico, jogadas));
    }

    public static void updatePingPongMatches(GuildMessageReceivedEvent event) { //lembrando que tem que ter confirmação que o user tá jogando


        if (!isPlaying(event.getAuthor())) {
            throw new IllegalArgumentException("O cara não tá jogando");
        }

        int isPl = getPlaying(event);
        universalMatches.get(isPl).refute(event);

    }

    public static int getPlaying(GuildMessageReceivedEvent event) {
        if (!isPlaying(event.getAuthor())) {
            throw new IllegalArgumentException("ele não tá jogando cabaço");
        }
        int isPl = 0;
        String EID = event.getAuthor().getId();
        while (isPl < universalMatches.size()) {
            PingPongMatch AT = universalMatches.get(isPl);
            String IDES = AT.U1.getId() + " " + AT.U2.getId();
            if (IDES.contains(EID)) {
                break;
            } else {
                isPl++;
            }
        }

        return isPl;
    }

    public static void fazerUmaCurvaLegal(int numberOfSpaces, TextChannel canalPraMandar) {
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
            canalPraMandar.sendMessage(nextLane).queue();
            nextLane = "";
        }
    }

    public static String loadLog() throws IOException {
        String retorno = "";
        File ret = new File(LOG_PATH);
        BufferedReader iStream = new BufferedReader(new InputStreamReader(new FileInputStream(ret)));
        String e = "";
        while (e != null) {
            e = iStream.readLine();
            retorno = retorno + e + "\n";
        }
        iStream.close();
        //System.out.println(retorno);
        //System.out.println(retorno + "g");
        return retorno;
    }

    /**
     * escreve o que estiver na String log no log
     */
    public static void writeInLog() throws FileNotFoundException {
        writeInLog(log);
    }

    /**
     * Escreve algo nos logs, vale lembrar que ele reescreve tudo que tava antes.
     */
    public static void writeInLog(String what) throws FileNotFoundException {


        log = log.replace("null\n", ""); //fixme isso não deveria estar aqui, tipo, o LOG nem é referenciado aqui


        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(LOG_PATH)));

        try {
            writer.write(what);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties loadProperties() throws IOException {

        File props;

        FileInputStream inputStream2;
        FileOutputStream outputStream2;
        InputStreamReader streamReader2;
        BufferedReader bufferedReader;
        boolean deuInput = false;
        props = new File(PROPERTIES_PATH);
        try {

            inputStream2 = new FileInputStream(props);
            deuInput = true;
        } catch (FileNotFoundException ex) {
            //outputStream2 = o
            try {
                outputStream2 = new FileOutputStream(new File(PROPERTIES_PATH));
                inputStream2 = new FileInputStream(props);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        streamReader2 = new InputStreamReader(inputStream2);
        bufferedReader = new BufferedReader(streamReader2);

        String currentLine = bufferedReader.readLine();
        String[] propsss = {};
        Properties ret = new Properties();

        while (currentLine != null) {
            //System.out.println("cuurr: " + currentLine);
            propsss = currentLine.split("=");
            if (propsss.length > 1) {
                ret.put(propsss[0], propsss[1]);
                System.out.println("Registrando propriedade " + propsss[0] + ":" + propsss[1]);
            }

            currentLine = bufferedReader.readLine();
        }

        properties = ret;

        System.out.println(properties.size() + " propriedades carregadas");

        return properties;
    }

    public static void saveProperties(Properties toWhat) throws IOException {

        File props;

        FileInputStream inputStream2;
        FileOutputStream outputStream2;
        props = new File(PROPERTIES_PATH);
        try {
            outputStream2 = new FileOutputStream(new File(PROPERTIES_PATH));
            inputStream2 = new FileInputStream(props);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return;
        }

        toWhat.store(outputStream2, null);
        properties = toWhat;

    }


    public static boolean isPlaying(User user) {
        boolean is = false;
        String UID = user.getId();
        for (PingPongMatch atual : universalMatches) {
            if (UID.equalsIgnoreCase(atual.U1.getId()) || UID.equalsIgnoreCase(atual.U2.getId())) {
                is = true;
            }
        }

        return is;
    }

    public static void refreshFinishedMatches() {
        for (int g = 0; g < universalMatches.size(); g++) {
            if (!universalMatches.get(g).rodando) {
                universalMatches.remove(g);
            }
        }
    }

    public static Image getImageByURL(String url) throws IOException {
        return getImageByURL(new URL(url));
    }

    public static Image getImageByURL(URL url) throws IOException {
        System.setProperty("http.agent", "Chrome");
        //sério isso?
        // isso é prova definitiva que HTTP é coisa do demônio
        //mano eu to mt puto vai se fuder era so fingir que era a porra do Chrome
        return ImageIO.read(url);
    }

    public static void initalizeLists() {
        String[] trustedUsers = properties.getProperty("trusted").split(",");
        String[] toCensorr = properties.getProperty("censored").split(",");
        String[] bannedUsers = properties.getProperty("banned").split(",");

        /*referencia among us*/
        for (String trustedAct : trustedUsers) {
            if (trustedAct == null) {
                break;
            }
            TRUSTED.add(trustedAct);
            System.out.println("User confiado carregado: " + jda.getUserById(trustedAct).getName());
        }

        /*referencia among us*/
        for (String bannedAct : bannedUsers) {
            if (bannedAct == null) {
                break;
            }
            BOTBANNED.add(bannedAct);
            System.out.println("User banido carregado: " + jda.getUserById(bannedAct).getName());
        }

        /*referencia among us*/
        for (String cenAct : toCensorr) {
            if (cenAct == null) {
                break;
            }
            censoredUsers.add(jda.getUserById(cenAct));
            System.out.println("User censurado carregado: " + jda.getUserById(cenAct).getName());
        }
    }

    public static void makePost(String subredditS, String postTString, String postContent, GuildMessageReceivedEvent event, boolean isLink) {
        String postType = "self";
        //reddit.getSubreddit(properties.getProperty("botSub")).getNew(1).get(0).reply("gay");
        if (isLink) {
            postType = "link";
        }

        //literalmente a coisa mais burra que eu precisei fazer
        String AN = event.getAuthor().getName();
        reddit.submit(subredditS, AN + ":" + postTString /*.replace(" ","-")*/, postContent/*.replace(" ", "-")*/, postType);

        System.out.println("sleep");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        System.out.println("slept");
        Link post = reddit.getSubreddit(subredditS).getNew(1).get(0);

        if (post.getAuthor().equals(redditUser.getName())) {

            event.getChannel().sendMessage("https://www.reddit.com" + post.getPermalink()).queue();
        } else {
            System.out.println(redditUser.getName() + ": " + post.getAuthor());
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
            //System.out.println(legal);
        }

        public void onUserUpdateActivities(UserUpdateActivitiesEvent event) {
            if (!event.getGuild().getId().equals(Imperio.getId())) {
                return;
            }
            try {
                String legal = "User " + event.getUser().getName() + " atualizou sua atividade para " + event.getNewValue().get(0) + "";
                canalDoBot.sendMessage(legal).queue();
                //System.out.println(legal);

            } catch (IndexOutOfBoundsException ex) {
            }

        }
    }

    public static class kicka extends ListenerAdapter {
        public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
            if (event.getMember().getUser().getId().equals("638898751880036400")) {
                event.getGuild().kickVoiceMember(event.getMember()).queue();
            }
        }
    }

    static void refreshCensored() { //será que vale a pena deixar ele estático? sla eu nem instancio essa classe mesmo
        StringBuilder toCensor = new StringBuilder();
        for (User censoredUser : censoredUsers) {
            toCensor.append(censoredUser.getId());
        }
        properties.setProperty("censored", toCensor.toString());
    }

    public static class listenersDoLog extends ListenerAdapter {

        public void onGuildMessageDelete(GuildMessageDeleteEvent ev) {
            log = log + "[" + ev.getGuild().getName() + " | " + Time.from(Instant.now()) + " |  #" + ev.getChannel().getName() + " | DELETE]:" + ev.getMessageId() + "\n";
            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onGuildVoiceJoin(GuildVoiceJoinEvent ev) {
            log = log + "[" + ev.getGuild().getName() + " | " + Time.from(Instant.now()) + " | " + ev.getMember().getUser().getName() + "] <- " + ev.getChannelJoined().getName() + "\n";
            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onGuildVoiceLeave(GuildVoiceLeaveEvent ev) {
            log = log + "[" + ev.getGuild().getName() + " | " + Time.from(Instant.now()) + " | " + ev.getMember().getUser().getName() + "] <- " + ev.getChannelLeft().getName() + "\n";
            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

            List<MessageEmbed> embeds = event.getMessage().getEmbeds();

            if (!event.getMessage().getAuthor().isBot()) {
                System.out.println("Pessoa: [" + event.getAuthor().getName() + "] Guild: [" + event.getGuild().getName() + "] ID:" + event.getMessageId() + " Mensagem: " + event.getMessage().getContentRaw());
                log = log + "[" + event.getGuild().getName() + " | #" + event.getChannel().getName() + " | " + Date.from(Instant.now()) + " | " + event.getMember().getEffectiveName() + " | " + event.getMessageId() + "]: " + event.getMessage().getContentRaw() + "\n";
            } else if (embeds.size() >= 1) {
                //System.out.println();
                //System.out.println(event.getAuthor().getName() + ": " + embeds.get(0).getFooter().getText());
                log = log + "["
                        + event.getGuild().getName() +
                        " | #" + event.getChannel().getName() +
                        " | " + Date.from(Instant.now()) + " | " +
                        event.getAuthor().getName() + " | " +
                        event.getMessageId() + "]: <<<" +
                        embeds.get(0).toData() +

                        ">>>\n";

            }


            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        public void onDisconnect(DisconnectEvent event) {

            log = log + "[AVISO: DESCONECTADO EM: " + Date.from(Instant.now()) + " | " + event.getCloseCode() + "]";


            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onReconnected(ReconnectedEvent event) {

            log = log + "[AVISO: RECONECTADO EM: " + Date.from(Instant.now()) + "]";


            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    static void trollTheUser(Member member) {
        if (lowResDimUsers.contains(member)) {
            member.getGuild().moveVoiceMember(member, jda.getVoiceChannelById(properties.getProperty("lowResChannel"))).queue();
        }
    }

    public static class listenersDaLowRes extends ListenerAdapter {

        public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
            trollTheUser(event.getMember());
        }

        //public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){trollTheUser(event.getMember());}
        //isso nem faz sentido kkjkkjkak
        public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
            trollTheUser(event.getMember());
        }


    }

    public static class eventListenerParalelo extends ListenerAdapter {

        public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
            System.out.println(event.getMember().getEffectiveName() + " entrou no canal " + event.getChannelJoined().getName());
        }

        public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
            System.out.println(event.getMember().getEffectiveName() + " saiu do canal " + event.getChannelLeft().getName());
        }

        public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
            if (event.getAuthor().isBot()) {
                return;
            }
            if (isTrusted(event.getAuthor(), TRUSTED)) {
                String content = event.getMessage().getContentRaw();
                String[] contentSplit = content.split(" ");

                if (contentSplit[0].equalsIgnoreCase("-manda")) {
                    String personID = contentSplit[1];
                    String argsss = "";
                    for (int f = 2; f < contentSplit.length; f++) {
                        argsss = argsss + contentSplit[f] + " ";
                    }
                    jda.getUserById(personID).openPrivateChannel().complete().sendMessage(argsss).queue();
                    event.getAuthor().openPrivateChannel().complete().sendMessage("mandei pro(a) " + jda.getUserById(personID).getName()).queue();
                }

            }
            jda.getUserById(Mega2223ID).openPrivateChannel().complete().sendMessage("O user " + event.getAuthor().getName() + " mandou a seguinte mensagem:\n" + event.getMessage().getContentRaw()).queue();
        }

        public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

            String[] rawSplit = event.getMessage().getContentRaw().split(" ");

            if (rawSplit[0].equalsIgnoreCase("ping") || rawSplit[0].equalsIgnoreCase("pong")) {
                //System.out.println("==========================");
                //System.out.println("RESPOSTA PING IDENTIFICADA");
                //System.out.println(event.getAuthor().getId() + "?=" + universalMatch.U1.getId() + " " + universalMatch.U2.getId());

                refreshFinishedMatches();
                if (isPlaying(event.getMember().getUser())) {
                    System.out.println("um user válido fez isso, legal");
                    System.out.println("==========================");

                    int play = getPlaying(event);
                    //será que seria melhor fazer ele retornar a partida em si?
                    universalMatches.get(play).refute(event);
                } else {
                    event.getChannel().sendMessage("cê não tá jogando bocó").queue();
                }

            }
        }


    }

    protected static class WaterReminder extends Thread { //talvez eu coloque isso em outra classe

        private final Member member;
        private final Guild guild;
        private final int time;
        private final TextChannel channel;
        private boolean isGoing = true;

        public WaterReminder(Member toRemember, int timeInMinutes, TextChannel channelTo) {  //todo FINAL
            member = toRemember;
            guild = member.getGuild();
            time = timeInMinutes;
            channel = channelTo;
        }

        @Override
        public void run() {
            GuildVoiceState state = member.getVoiceState();
            if (!state.inVoiceChannel() && isGoing) {
                channel.sendMessage(member.getAsMention() + " você não está mais em um canal de voz, vou parar com os lembretes :thumbsdown:").queue();
                isGoing = false;
            }

            if (!isGoing) {
                return;
            }

            state.getGuild().getAudioManager().openAudioConnection(state.getChannel());


        }

    }

}