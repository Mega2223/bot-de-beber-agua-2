package net.Mega2223.botDeBeberÁgua2.principal;

import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import static net.Mega2223.botDeBeberÁgua2.principal.aguaBot.getImageByURL;

public class notifier extends JFrame {
    int x;
    int y;
    private JLabel label;
    private JLabel label2;
    public notifier(final User user, final URL url) throws IOException {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setAlwaysOnTop(true);

        Random r = new Random();

        setLocation(r.nextInt(1360), r.nextInt(768));

        Image bImg = getImageByURL(url);

        if (bImg == null) {
            System.out.println("NULL PORRA");
        }
        ImageIcon imgIcon = new ImageIcon(bImg);

        label2 = new JLabel(imgIcon);
        label2.setIcon(imgIcon);

        add(label2);
        URL url2 = new URL(user.getAvatarUrl());
        Image iImg = ImageIO.read(url2);
        setIconImage(iImg);
        setName(user.getName());
        setTitle(user.getName() + ":");
        ImageIcon iC = new ImageIcon(bImg);

        setSize(iC.getIconWidth(), iC.getIconHeight() + 20); //todo pega a medida da barra
        setVisible(true);
        //System.out.println("cabei");
    }

    public notifier(final User user, String message) {
        setVisible(true);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setAlwaysOnTop(true);
        setFocusable(false);


        Random r = new Random();

        setLocation(r.nextInt(1360), r.nextInt(768));//to.do usa as resoluções preguiçoso

        String messag = user.getName() + ":\n" + message;
        String[] spt = messag.split("\n");
        int maxc = 0;
        for (String s : spt) {
            // bad code lol
            if (s.length() > maxc) {
                maxc = s.length();
            }
        }

        //System.out.println("tamanho: " +  x +" : " + y);
        x = maxc * 10 + 20;
        y = spt.length * 17 + 60;
        setSize(x + 1, y + 1);

        messag = ConvertToHTML(spt);
        label = new JLabel(messag);
        add(label);
        label.setText(messag);
        setSize(x, y);
    }


    public static String ConvertToHTML(String[] eee) {
        String end = "";
        for (int ever = 0; ever < eee.length; ever++) {
            //pra deixar claro eu copiei isso de um projeto antigo

            if (ever == 0) {
                end = "<html>" + eee[ever];
            } else if (ever == 1) {
                end = end + "<br/>" + eee[ever].replace("-mega", "");
            } else {
                end = end + "<br/>" + eee[ever];
            }

        }
        return end;
    }
}
