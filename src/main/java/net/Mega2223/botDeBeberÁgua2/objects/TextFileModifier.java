package net.Mega2223.botDeBeberÁgua2.objects;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileModifier {

    public static String readFile(String path) throws IOException {
        String retorno = "";
        File ret = new File(path);
        BufferedReader iStream = new BufferedReader(new InputStreamReader(new FileInputStream(ret)));
        String e = "";
        while (e != null) {
            e = iStream.readLine();
            retorno = retorno + e + "\n";
        }
        iStream.close();
        return retorno;
    }

    public static List<File> listFilesForFolder(final String path) {
        File folder = new File(path);
        List<File> fileList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry.getPath());
            } else {
                fileList.add(fileEntry);
            }
        }

        return fileList;
    }


    public static void writeInFile(String what, String path) throws IOException {

        File filo = new File(path);
        OutputStreamWriter writer;

        if (filo.exists()) {
            writer = new OutputStreamWriter(new FileOutputStream(filo));
        } else {
            filo.createNewFile();
            writer = new OutputStreamWriter(new FileOutputStream(filo));
        }

        writer.write(what);
        writer.close();


    }
}
