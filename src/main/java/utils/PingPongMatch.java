package utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PingPongMatch {

    private int whosturn;
    public Member U1;
    public Member U2;
    public boolean rodando = true;
    private Random random;
    public int interval = 5;
    public int maxseconds = 6;
    private JDA currentJDA;
    private TextChannel textChannel;

    public static final int USER1 = 1;
    public static final int USER2 = 2; //0 e 1 talvez?

    public PingPongMatch(GuildMessageReceivedEvent event,  int intervaloEntreMinEMax, int intervaloEntreJogadas){
        initializeEverything(event,intervaloEntreMinEMax,intervaloEntreJogadas);
    }

    public PingPongMatch(GuildMessageReceivedEvent event){
        initializeEverything(event,5,4);
    }

    private void initializeEverything(GuildMessageReceivedEvent event, int intervaloEntreMinEMax, int intervaloEntreJogadas){

        U1 = event.getMessage().getMember();// System.out.println("U1 init");
        U2 = event.getMessage().getMentionedMembers().get(0); //System.out.println("U2 init");
        whosturn = 1; //System.out.println("whosturn init");
        currentJDA = event.getJDA(); //System.out.println("jda init");
        random = new Random(); //System.out.println("random init");
        interval = intervaloEntreMinEMax;
        maxseconds = intervaloEntreJogadas;
        textChannel = event.getChannel();

        setTimers(); //System.out.println("timers init");
        event.getChannel().sendMessage(":ping_pong: Nova partida de ping pong, o " + getCurrentUser().getAsMention() + " têm de " + (int)minTimer + " a " + (int)maxTimer + " segundos pra responder").queue();
        initThread(); //System.out.println("thread init");

        threadInitialized = true;
        thread.start(); //System.out.println("thread start");

    }

    private void end(){
        //System.out.println("fodase essa classe todos os meus casas odeiam essa classe");
        try {

            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void setTimers(){
        //System.out.println("setandotimers");

        timer = 0;
        int I1 = new Random().nextInt(maxseconds);
        System.out.println("random = " + I1);
        if(I1 < 1){I1 = I1 + 1;}
        minTimer = I1;
        maxTimer = I1 + interval;
    }

    private User getCurrentUser(){
        switch (this.whosturn){
            case USER1:
                return U1.getUser();
            case USER2:
                return U2.getUser();
            default:
                throw new IllegalStateException("Unexpected value: " + this.whosturn);
        }
    }

    private double minTimer = 0;
    private double maxTimer = 0;
    private double timer = 0;

    private boolean threadInitialized = false;
    private void initThread(){
        thread = new Thread(){
            @Override
            public void run(){}

            @Override
            public void start(){
                //thread
                if (threadInitialized) {
                        //Thread.sleep(1000);
                        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);

                        poolExecutor.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                if(!rodando){

                                    Thread.currentThread().interrupt(); return;}
                                if (timer > maxTimer + 2){
                                    end();
                                    return;
                                }
                                else if (!threadInitialized){
                                    end();
                                    return;

                                }
                                timer = timer + 1;
                                //System.out.println(timer);
                            }
                        },1,1, TimeUnit.SECONDS);



                }

            }
        };

    }

    private Thread thread;

    public void refute(GuildMessageReceivedEvent ev){
        //System.out.println("refute called");
        runTurn(ev);
    }

    @Override
    public void finalize(){
        this.rodando = false;
    }

    private boolean checkIfItsValid(){
        return timer >= minTimer && timer <= maxTimer;

    }

    private void runTurn(GuildMessageReceivedEvent ev){

        final String id = ev.getMember().getId();
        final String currentUserId = getCurrentUser().getId();

        //System.out.println("DEBUG: MEMBERSAYING=" + ev.getMember().getId() + " MEMBER1=" + U1.getId() + " MEMBER2= " + U2.getId());

        if(id.equals(U1.getId()) && currentUserId.equals(U2.getId())){return;}
        else
        if(id.equals(U2.getId()) && currentUserId.equals(U1.getId())){return;}


        if (!checkIfItsValid()){
            ev.getChannel().sendMessage(":ping_pong: tu demorou " + (int)timer + " segundos pra responder, era entre " + (int)minTimer + " e " +  (int)maxTimer + " já era, acabou").queue();

            this.rodando = false;
            //this.finalize();
            return;}


        switch (this.whosturn){

            case USER1:
                //System.out.println("Turno do USER2");
                whosturn = USER2;
                break;
            case USER2:
                //System.out.println("Turno do USER1");
                whosturn = USER1;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.whosturn);
        }
        setTimers();
        ev.getChannel().sendMessage(":ping_pong: o " + getCurrentUser().getAsMention() + " têm de " + (int)minTimer + " pra " + (int)maxTimer + " segundos pra responder").complete();


    }

}
