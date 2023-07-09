package util;


import com.google.gson.Gson;

public class Constants {

    public static final String USERNAME = "username";
    public final static int REFRESH_RATE = 2000;
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Tomcat_Web_exploded";;
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOAD_FILE = FULL_SERVER_PATH + "/upload_file";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/usersListRefresher";

    public final static String ROLES_LIST = FULL_SERVER_PATH + "/rolesListRefresher";
    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
