package net.Mega2223.objects;

import net.Mega2223.principal.aguaBot;
import net.Mega2223.principal.notifier;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static net.Mega2223.principal.aguaBot.jda;


public class Janela extends JFrame {
    private static JLabel label1;
    private static JScrollPane container;
    private static JPanel panel;
    private final BotThread botThread;
    private JScrollBar jScrollBar;

    public Janela() throws IOException {

        setSize(800, 800);

        setVisible(true);
        setTitle("log:");
        setDefaultCloseOperation(Janela.DISPOSE_ON_CLOSE);
        setIconImage(aguaBot.getImageByURL(jda.getSelfUser().getAvatarUrl()));


        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        setLayout(layout);


        label1 = new JLabel(aguaBot.log);
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.getLayout();
        panel.setSize(this.getSize());

        container = new JScrollPane(panel);

        jScrollBar = new JScrollBar();

        container.setVerticalScrollBar(jScrollBar);
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //container.set
        container.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        container.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        container.setAutoscrolls(true);
        jScrollBar = new JScrollBar();

        container.setLayout(new ScrollPaneLayout());
        panel.add(label1);

        add(container);


        ScheduledThreadPoolExecutor executor;
        executor = new ScheduledThreadPoolExecutor(1);
        botThread = new BotThread();
        executor.scheduleAtFixedRate(botThread, 0, 1, TimeUnit.SECONDS);
        System.out.println("Scheduled");

        //frameInit();


    }


    protected class BotThread extends Thread {

        @Override
        public void run() {
            String setWhat = notifier.ConvertToHTML(aguaBot.log.split("\n"));
            //System.out.println(setWhat);
            label1.setText(setWhat);
            container.setSize(getSize());
        }
    }
}

