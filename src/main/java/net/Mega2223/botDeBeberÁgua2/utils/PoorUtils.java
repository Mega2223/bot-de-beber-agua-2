package net.Mega2223.botDeBeberÁgua2.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PoorUtils {

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

    public static BufferedImage apobrezar(BufferedImage image) {
        String[] pobres = {
                "Você é pobre",
                "Você possui baixo capital acumulado kk",
                "Pobre kk",
                "Alá o pobre kkjk",
                "Olha lá aquele pobre do caralho kkjkkk",
                "Você possui baixo capital acumulado",
                "VÁ ACUMULAR CAPITAL IMEDIATAMENTE"};//todo config

        List<String> foda = Arrays.asList(pobres);
        String choosed = (String) getRandomFromList(foda);
        Random gen = new Random();
        Graphics2D graphics2D = image.createGraphics();
        int w = image.getWidth();
        int h = image.getHeight();

        int x = gen.nextInt(w - (w / 4));
        int y = gen.nextInt(h - (h / 4));

        graphics2D.setFont(Font.getFont("Impact Regular"));

        System.out.println("Hello world");
        //muito gay esse bricks

        int adj = w / 300 + 1;
        graphics2D.scale(adj, adj);
        //graphics2D


        System.out.println(w / 150);
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
