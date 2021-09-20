package principal;

import net.dv8tion.jda.api.entities.User;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Random;

public class notifier extends JFrame {
    private final JLabel label;
    private JLabel label2;

    public notifier(final User user, final net.dv8tion.jda.api.entities.Icon image) {
        setVisible(true);
        setSize(180, 80);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setAlwaysOnTop(true);
        setFocusable(false);

        Random r = new Random();

        setLocation(r.nextInt(800), r.nextInt(800));//todo usa as resoluções preguiçoso

        label = new JLabel(user.getName() + ":");
        BufferedImage decoded = decodeToImage(image.getEncoding());
        Image foda = decoded;
        if (decoded == null) {
            System.out.println("NULL PORRA");
        }
        label2 = new JLabel(new ImageIcon(foda));

        add(label);
        add(label2);

    }

    public notifier(final User user, final String message) {
        setVisible(true);
        setSize(180, 80);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setAlwaysOnTop(true);
        setFocusable(false);
        //setUndecorated(true);

        Random r = new Random();

        setLocation(r.nextInt(800), r.nextInt(800));//todo usa as resoluções preguiçoso


        label = new JLabel(user.getName() + " : " + message);
        add(label);

    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
