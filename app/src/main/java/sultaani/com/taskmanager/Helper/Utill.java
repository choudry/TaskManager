package sultaani.com.taskmanager.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.widget.Toast;

import sultaani.com.taskmanager.Model.User;

public class Utill {


    public static final String URL_STORAGE_REFERENCE = "gs://sharedtasklist-50302.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";

    public static String MyPrefrences = "MyPrefs";
    static SharedPreferences sharedPreferences;


    public static String reciver="";
    public static String CHAT_REFERENCE="";
    public static String Chat_Header="";
    public static  String Final_Chat_Name="";
    public static  boolean check=false;
    public static User current1= new User();

    public static void showToast(Context c, String message){
        Toast.makeText(c,message, Toast.LENGTH_SHORT).show();
    }


    public static boolean verifyConection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }else
            return false;

    }

    public static void addDataSP(String key,String value,Context context){
        sharedPreferences = context.getSharedPreferences(MyPrefrences, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static String getDataSP(String key,Context context){
        if (context!=null){
            sharedPreferences = context.getSharedPreferences(MyPrefrences, 0);
            return  sharedPreferences.getString(key,null);
        }else {
            return null;
        }

    }

    public static void removeDataSP(String key,Context context){
        if (context!=null){
            sharedPreferences = context.getSharedPreferences(MyPrefrences, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }






}
