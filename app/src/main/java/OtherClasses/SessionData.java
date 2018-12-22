package OtherClasses;

import Models.ClassRequest;

public class SessionData {
    static String domain = "arabslankaholidays.com";
//    static String domain = "192.168.1.250";

    public static String serverUrl = "http://" + domain + "/quikdel/";
    public static String userId = "21", userName = "Selena", designation = "adm";
    public static final String PREFS_LOGIN = "LoginCheck";
    public static String currentFragment = "request";
    public static ClassRequest classRequest;
    public static int selectedUserId = 0;


    public static String blockUrl = "https://quikdel.000webhostapp.com/dush_allow/";

}
