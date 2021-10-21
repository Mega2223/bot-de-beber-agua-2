package net.Mega2223.botDeBeberÁgua2.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class aguaUtils {
    public static boolean isBotBanned(User user, List<String> BOTBANNED) {
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

    public static boolean isTrusted(User user, List<String> TRUSTED) {
        boolean is = false;
        for (String s : TRUSTED) {
            if (user.getId().equalsIgnoreCase(s)) {
                is = true;
            }
        }
        return is;
    }

    public static boolean isTrusted(Member user, List<String> TRUSTED) {
        return isTrusted(user.getUser(), TRUSTED);
    }
}
