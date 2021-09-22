package principal;

import net.dv8tion.jda.api.entities.User;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class notifier extends JFrame {
    private JLabel label;
    private JLabel label2;

    public notifier(final User user, final URL url) throws IOException {

        //System.out.println("cabeid");


        ;
        //setSize(180, 80);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setAlwaysOnTop(true);

        //System.out.println("cabeir");

        Random r = new Random();

        setLocation(r.nextInt(800), r.nextInt(800));//todo usa as resoluções preguiçoso

        //System.out.println("cabei232");

        //label = new JLabel(user.getName() + ":");
        //System.out.println(url.toString());

        int length;

        //Image img = ImageIO.read(url.openStream());

        System.setProperty("http.agent", "Chrome"); //sério isso?
                                                    //isso é prova definitiva que HTTP é coisa do demônio
                                                    //mano eu to mt puto vai se fuder era so fingir que era a porra do Chrome
        //System.out.println("cabei333");
        //System.out.println("conectado");

        Image bImg = ImageIO.read(url);
        //System.out.println(bImg.getHeight()+":"+bImg.getWidth());

        if (bImg == null) {
            System.out.println("NULL PORRA");
        }
        ImageIcon imgIcon = new ImageIcon(bImg);
        //System.out.println("imgIcon: "+imgIcon.getIconHeight() + ":" + imgIcon.getIconWidth());
        //System.out.println("Icon: "+((Icon)imgIcon).getIconHeight() + ":" + ((Icon)imgIcon).getIconWidth());

        label2 = new JLabel((Icon)imgIcon);
        label2.setIcon(imgIcon);
        //add(label);
        add(label2);
        URL url2 = new URL(user.getAvatarUrl());
        Image iImg = ImageIO.read(url2);
        setIconImage(iImg);
        setName(user.getName());
        setTitle(user.getName() + ":");
        ImageIcon iC = new ImageIcon(bImg);

        setSize(iC.getIconWidth(),iC.getIconHeight() + 20); //todo pega a medida da barra
        setVisible(true);
        //System.out.println("cabei");
    }

    int x;
    int y;
    public notifier(final User user, String message) {
        setVisible(true);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setAlwaysOnTop(true);
        setFocusable(false);


        Random r = new Random();

        setLocation(r.nextInt(1360), r.nextInt(768));//to.do usa as resoluções preguiçoso

        String messag = user.getName() + ":\n" + message;
        String spt[] = messag.split("\n");
        int maxc = 0;
        for(int g = 0; g < spt.length; g++){
             // bad code lol
            if(spt[g].length() > maxc){maxc = spt[g].length();}
        }

        //System.out.println("tamanho: " +  x +" : " + y);
        x = maxc * 10 + 20;
        y = spt.length * 17 + 60;
        setSize(x+1, y+1);

        messag = ConvertToHTML(spt);
        label = new JLabel(messag);
        add(label);
        label.setText(messag);
        setSize(x, y);
    }



    private static String ConvertToHTML(String[] eee) {
        String end = "";
        for (int cocó = 0; cocó < eee.length; cocó++) {
            //pra deixar claro eu copiei isso de um projeto antigo


            // esse negócio basicamente converte as coisas em um texto HTML
            // pro swing fazer o split de linha certo e não ficar aquela
            // aberração da natureza
            // vai ficar tipo assim: <html>Primeiralinha <br/>segundalinha <br/>terceiralinha etc.
            if (cocó == 0) {
                end = "<html>" + eee[cocó];
            } else if (cocó == 1){
                end = end + "<br/>" + eee[cocó].replace("-mega","");
            }

            else {
                end = end + "<br/>" + eee[cocó];
            }

        }
        return end;
    }
}
