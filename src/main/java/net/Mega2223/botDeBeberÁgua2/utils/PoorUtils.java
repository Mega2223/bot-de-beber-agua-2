package net.Mega2223.botDeBeberÁgua2.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PoorUtils {
    /**
     * Classe de objetos estáticos que me ajudam a colocar frases depreciativas sobre pessoas de baixa classe sobre fotos de youtubers em contextos duvidosos.
     */

    public static BufferedImage getImg(Path directory) throws IOException {
        //fixme
        List<File> filo = getFilesFromFolder(directory.toFile());
        File random = (File) getRandomFromList(filo);
        BufferedImage image = ImageIO.read(new FileInputStream(random));

        return image;
    }

    public static Object getRandomFromList(List list) {
        Random random = new Random();

        return list.get(random.nextInt(list.size()));

    }

    public static BufferedImage apobrezar(BufferedImage image) throws IOException {
        File phr = new File("C:\\Users\\Imperiums\\Desktop\\Pastas\\Espaço de programar e tals\\botPuto\\src\\main\\java\\net\\Mega2223\\arquivosConfidenciais\\frasesDePobre.txt");
        BufferedReader reader = new BufferedReader(new FileReader(phr));
        String read = reader.readLine();
        List<String> pobr = new ArrayList<String>();
        while (read != null) {
            pobr.add(read);
            read = reader.readLine();
        }


        String choosed = (String) getRandomFromList(pobr);
        Random gen = new Random();
        Graphics2D graphics2D = image.createGraphics();
        int w = image.getWidth();
        int h = image.getHeight();
        int adj = w / 300 + 1;

        int xbound = w - choosed.length() * 6 * adj;
        if (xbound <= 0) {
            xbound = 1;
        }
        int x = gen.nextInt(xbound);
        int ybound = h - (h / 5);
        if (ybound <= 0) {
            ybound = 1;
        }
        int y = gen.nextInt(ybound);

        graphics2D.setFont(Font.getFont("Impact Regular"));

        //System.out.println("Hello world");
        //muito gay esse bricks


        graphics2D.scale(adj, adj);
        //graphics2D


        graphics2D.drawString(choosed, x / adj, y / adj);

        graphics2D.setColor(Color.black);
        

        return image;
    }

    public static List<File> getFilesFromFolder(final File folder) {
        List<File> ret = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                ret.addAll(getFilesFromFolder(fileEntry));
            } else {
                ret.add(fileEntry);
            }
        }
        return ret;
    }
}
