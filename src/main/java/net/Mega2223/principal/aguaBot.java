package net.Mega2223.principal;


import com.sun.javaws.exceptions.InvalidArgumentException;
import lavaplayer.PlayerManager;
import net.Mega2223.utils.Janela;
import net.Mega2223.utils.PingPongMatch;
import net.Mega2223.utils.TextFileModifier;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.util.List;
import java.util.*;



@SuppressWarnings("ALL")

@Deprecated
public class aguaBot {


    public static final String projectPath = System.getProperty("user.dir");
    public static final String LOG_PATH = projectPath + "\\log.txt";
    public static final String PROPERTIES_PATH = projectPath + "\\configs.properties";
    public static final String Mega2223ID = "301424656051732491";
    protected static final String fileCreatorPath = projectPath + "\\src\\main\\java\\net\\Mega2223\\arquivosConfidenciais";
    protected static final String ImperioID = "606274842722959384";
    public static List<String> BOTBANNED;
    public static List<String> TRUSTED;
    public static JDABuilder builder;
    public static JDA jda;
    public static TextChannel canalDoBot;
    public static Guild Imperio;
    public static ListenerAdapter Perigosos;
    public static PlayerManager fer;
    public static ListenerAdapter kik;
    public static String log;
    public static Properties properties;
    public static String JDAKey;
    public static String RedditKey;
    public static List<notifier> Notifiers;
    public static List<PingPongMatch> universalMatches;
    public static Janela currentJanela;
    public static List<User> censoredUsers;
    protected static String TCBotID;


    public static void main(String[] args) throws LoginException, IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(projectPath+"\\key.txt")));
        String key = inputStream.readLine();
        inputStream.close();
        JDAKey = key.split(" ")[0];
        RedditKey = key.split(" ")[1];

        System.out.println("Ativando o JDA na key " + JDAKey);
        System.out.println("Ativando o Reddit na key " + RedditKey);

        builder = JDABuilder.create(JDAKey, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_VOICE_STATES);
        EnumSet<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS);
        builder.enableIntents(intents);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableCache(CacheFlag.ACTIVITY);
        jda = builder.build();
        log = loadLog() + "\n";

        try {/*Thread.sleep(7000);*/
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadProperties();

        saveProperties(properties);

        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.getPresence().setActivity(Activity.streaming("sua mãe pelada", "https://www.youtube.com/watch?v=dQw4w9WgXcQ"));

        Imperio = jda.getGuildById(ImperioID);
        TRUSTED = new ArrayList<>();
        BOTBANNED = new ArrayList<>();
        censoredUsers = new ArrayList<>();//eu sou mt consistente nas capslocks ne pqp
        Notifiers = new ArrayList<>();
        universalMatches = new ArrayList<>();
        TCBotID = properties.getProperty("botchannel");

        initalizeLists();

        Imperio.loadMembers();
        Imperio.getMemberCache();

        canalDoBot = jda.getTextChannelById(TCBotID);

        jda.addEventListener(new seraQueEuLembroComoUsaEventListeners());
        jda.addEventListener(new eventListenerParalelo());
        jda.addEventListener(new listenersDoLog());
        jda.addEventListener(new sdds());


        fer = PlayerManager.getInstance();

        log = log + "[BOT LIGADO EM: " + Time.from(Instant.now()) + "]\n";
        writeInLog(log);

        System.out.println("tudopronto");
        Message mensagemfoda = canalDoBot.sendMessage("tudo pronto to rodando").complete();


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mensagemfoda.delete().queue();
    }

    public static boolean isBotBanned(User user) {
        boolean is = false;
        for (String s : BOTBANNED) {
            //System.out.print(user.getId() + " comparando com" + s);
            if (user.getId().equalsIgnoreCase(s)) {
                is = true;
                //System.out.print(user.getId() + " é igual " + s);
            }
        }
        return is;
    }

    public static boolean isTrusted(User user) {
        boolean is = false;
        for (String s : TRUSTED) {
            if (user.getId().equalsIgnoreCase(s)) {
                is = true;
            }
        }
        return is;
    }

    @SuppressWarnings("unused")
    public static boolean isTrusted(Member user) {
        return isTrusted(user.getUser());
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

    public static void updatePingPongMatches(GuildMessageReceivedEvent event) throws InvalidArgumentException { //lembrando que tem que ter confirmação que o user tá jogando


        if (!isPlaying(event.getAuthor())) {
            throw new InvalidArgumentException(new String[]{"o user n tá jogando o cabaço"});
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
        return retorno;
    }


    public static void writeInLog(String what) throws FileNotFoundException {
        /**
         * Escreve algo nos logs, vale lembrar que ele reescreve tudo que tava antes.
         * */

        //System.out.println("Pegando o log em " +LOG_PATH);
        log = log.replace("null\n", "");


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
        String propsss[] = {};
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
        String trustedUsers[] = properties.getProperty("trusted").split(",");
        String toCensorr[] = properties.getProperty("censored").split(",");
        String bannedUsers[] = properties.getProperty("banned").split(",");

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


    public static class sdds extends ListenerAdapter{
        public void onUserActivityStart(UserActivityStartEvent event){
           if(event.getMember().getUser().getId().equalsIgnoreCase(idDoPudim)){
                jda.getUserById(Mega2223ID).openPrivateChannel().complete().sendMessage("ELE VOLTOU!").queue();
                new notifier(jda.getSelfUser(),"ELE VOLTOU");
                jda.getPresence().setPresence(Activity.playing("ELE VOLTOU"),true);
                
           }
        };

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
            if (isTrusted(event.getAuthor())) {
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

    public static class listenersDoLog extends ListenerAdapter {

        public void onGuildMessageDelete(GuildMessageDeleteEvent ev){
            log = log + "["+ev.getGuild().getName() +" | "+ Time.from(Instant.now()) +" |  #" + ev.getChannel().getName() + " | DELETE]:" + ev.getMessageId() +"\n";
            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onGuildVoiceJoin(GuildVoiceJoinEvent ev) {
            log = log + "["  + ev.getGuild().getName() + " | " + Time.from(Instant.now()) + " | " + ev.getMember().getUser().getName() + "] <- " + ev.getChannelJoined().getName() +"\n";
            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onGuildVoiceLeave(GuildVoiceLeaveEvent ev) {
            log = log + "["  + ev.getGuild().getName() + " | " + Time.from(Instant.now()) + " | " + ev.getMember().getUser().getName() + "] <- " + ev.getChannelLeft().getName() +"\n";
            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

            List<MessageEmbed> embeds = event.getMessage().getEmbeds();

            if (!event.getMessage().getAuthor().isBot()) {
                System.out.println("Pessoa: [" + event.getAuthor().getName() +"] Guild: [" + event.getGuild().getName() +"] ID:"+event.getMessageId()+" Mensagem: " + event.getMessage().getContentRaw());
                log = log + "[" + event.getGuild().getName() + " | #" + event.getChannel().getName() + " | " + Date.from(Instant.now()) + " | " + event.getMember().getEffectiveName() + " | " + event.getMessageId() +"]: " + event.getMessage().getContentRaw() + "\n";
            }

            else if (embeds.size() >= 1) {
                //System.out.println();
                //System.out.println(event.getAuthor().getName() + ": " + embeds.get(0).getFooter().getText());
                log = log + "["
                        + event.getGuild().getName() +
                        " | #" + event.getChannel().getName() +
                        " | " + Date.from(Instant.now()) + " | " +
                        event.getAuthor().getName()+ " | " +
                        event.getMessageId() +"]: <<<" +
                        embeds.get(0).toData().toString() +

                        ">>>\n";

            }


            try {
                writeInLog(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        public void onDisconnect(DisconnectEvent event) {

            log = log + "[AVISO: DESCONECTADO EM: " + Date.from(Instant.now()) + " | " +  event.getCloseCode() + "]";


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

    public static class seraQueEuLembroComoUsaEventListeners extends ListenerAdapter { //lembro?



        public void onGuildVoiceJoin(final GuildVoiceJoinEvent event) {
            GuildVoiceState state = event.getVoiceState();

            if (state.inVoiceChannel() && censoredUsers.contains(event.getMember().getUser())) {
                event.getGuild().kickVoiceMember(event.getMember()).queue();
                System.out.println("removendo o " + event.getMember().getEffectiveName() + " no server " + event.getGuild().getName());
            }
        }

        //receivers de mensagem principais
        public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
            Message message = event.getMessage();
            Member member = event.getMember();
            User user = event.getAuthor();

            final String contentRaw = message.getContentRaw();
            final String[] rawSplit = contentRaw.split(" ");

            boolean isCommand;
            boolean isBanned;

            isBanned = isBotBanned(event.getAuthor());
            try {
                String firstC = message.getContentRaw().substring(0, 1);
                isCommand = firstC.contains("-");

            } catch (StringIndexOutOfBoundsException e) {
                isCommand = false;
            }

            BOTBANNED.clear();
            BOTBANNED = new ArrayList<>();
            String banRef[] = properties.getProperty("banned").split(",");
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
            } else if (event.getChannel().getId().equals(canalDoBot.getId()) && contentRaw.equalsIgnoreCase("-curva")) {
                fazerUmaCurvaLegal(15, event.getChannel());
            } else if (rawSplit[0].equalsIgnoreCase("oi") && message.getMentionedUsers().contains(jda.getSelfUser())) {
                event.getChannel().sendMessage("oi " + event.getAuthor().getAsMention() + " :)").queue();
            } else if (contentRaw.equalsIgnoreCase("-enablelisteners") && isTrusted(user)) {
                Perigosos = new listenersPerigosos();
                jda.addEventListener(Perigosos);
                event.getChannel().sendMessage(":thumbsup: ativados").queue();
            } else if (contentRaw.equalsIgnoreCase("-disablelisteners") && isTrusted(user)) {
                jda.removeEventListener(Perigosos);
                event.getChannel().sendMessage(":thumbsdown: desativados").queue();
            } else if (contentRaw.equalsIgnoreCase("-tiraogames")) {
                event.getChannel().sendMessage("ok vou tirar").queue();
                kik = new kicka();
                jda.addEventListener(kik);
                Imperio.kickVoiceMember(Imperio.getMember(jda.getUserById("638898751880036400"))).queue();
            } else if (contentRaw.equalsIgnoreCase("-deixaogames")) {
                event.getChannel().sendMessage("ok vou deixar").queue();
                jda.removeEventListener(kik);
            } else if (contentRaw.equalsIgnoreCase("-entra")) {
                event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
                event.getChannel().sendMessage(":thumbsup: ok").queue();
            } else if (contentRaw.equalsIgnoreCase("-vaza")) {
                event.getChannel().sendMessage(":thumbsup: tá").queue();
                event.getGuild().getAudioManager().closeAudioConnection();
            } else if (rawSplit[0].equalsIgnoreCase("-toca")) {//to;do split //eu não sei oq eu quis dizer com o cmentário aanterior mas tá bom né
                event.getChannel().sendMessage(":thumbsup: Tocarei-de-eu").queue();

                event.getGuild().getAudioManager().getSendingHandler();


                //final AudioPlayerManager manager = new DefaultAudioPlayerManager();
                //final AudioPlayer player = manager.createPlayer();

                try {
                    fer.loadAndPlay(event.getChannel(), rawSplit[1]);
                } catch (InvalidDnDOperationException e) {


                }


            } else if (rawSplit[0].equals("-skip")) {
                event.getChannel().sendMessage(":thumbsup: Podeixar patrão").queue();

                fer.getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
            } else if (rawSplit[0].equals("-volume")) {
                if (rawSplit.length == 2) {
                    if (Integer.parseInt(rawSplit[1]) > 100 && !isTrusted(user)) {
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
            } else if (rawSplit[0].equalsIgnoreCase("-status") && isTrusted(event.getAuthor())) {
                jda.getPresence().setPresence(Activity.streaming(rawSplit[1], rawSplit[2]), true);

            } else if (rawSplit[0].equalsIgnoreCase("-mega") && rawSplit.length >= 2 && message.getAttachments().size() == 0) { //método de texto
                String args = "";
                for (int g = 1; g < rawSplit.length; g++) {
                    args = args + rawSplit[g] + " ";
                }
                Notifiers.add(new notifier(event.getAuthor(), args));
                event.getChannel().sendMessage("notifiquei o puto").queue();

            } else if (rawSplit[0].equalsIgnoreCase("-mega") && message.getAttachments().size() >= 1) {
                try {
                    Notifiers.add(new notifier(event.getAuthor(), new URL(message.getAttachments().get(0).getUrl())));
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
                for (net.Mega2223.principal.notifier notifier : Notifiers) {
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
                    String manda[] = TextFileModifier.readFile(fileCreatorPath + "\\" + rawSplit[1]).split(rawSplit[2]);

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


            } else if (rawSplit[0].equalsIgnoreCase("-printlog") && isTrusted(event.getAuthor())){
                    System.out.println(log);
            }

        }

        private void refreshCensored() {
            StringBuilder toCensor = new StringBuilder();
            for (User censoredUser : censoredUsers) {
                toCensor.append(censoredUser.getId());
            }
            properties.setProperty("censored", toCensor.toString());
        }

    }

    private final static String idDoPudim = "491748519984627712";

}