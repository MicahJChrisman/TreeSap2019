package com.example.treesapv2new;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.io.IOException;

public class ConnectionCheck {
    public static boolean offlineMessageShown = false;
    public static boolean offlineCuratorMessageShown = false;
//    public static boolean offlineAddTreeMessageShown = false;
    public static boolean offlineAccountMessageShown = false;

    public static boolean isConnectedToFirebase(Context context){
        String status = null;
        ConnectivityManager cm = (ConnectivityManager)           context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                return true;
            }
        } else {
            status = "No internet is available";
            return false;
        }
        return false;

//        public static boolean isConnectedToFirebase(){
        // Note: Pinging does NOT work on emulators unless you change their settings
//        try {
//            final String command = "ping -c 1 google.com";
//            return Runtime.getRuntime().exec(command).waitFor() == 0;
//        }
//        catch (IOException e)          { e.printStackTrace(); }
//        catch (InterruptedException e) { e.printStackTrace(); }

//        return false;

    }

    public static void showOfflineMessage(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("While disconnected, you cannot load new trees from the all users database, but you can access locally stored data." +
                "\n\nIf you are connected to the internet, then there is a server issue, and it's probably not our fault. :)").setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("No database connection, please check your internet connection");
        a_builder.show();
    }

    public static void showOfflineCuratorMessage(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage(("No database connection, please check your internet connection\nYou will not be able to load new pending trees." +
                "\nAccepting or rejecting locally stored pending trees may slow down the app." +
                        "\nPlease keep the app open after accepting or rejecting pending trees until connection is restored.")).setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("Attention curator:");
        a_builder.show();
    }

    public static void showOfflineAddTreeMessage1(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage(("Submitting new trees while offline may slow down the app. \nPlease keep the app open after submitting until connection is restored. \nPress okay to continue.")).setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("No database connection, please check your internet connection");
        a_builder.show();
    }

    public static void showOfflineAddTreeMessage2(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage(("Please keep the app open after submitting until reconnected, or the tree may not submit.\nYou may still submit other trees.")).setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("Reminder:");
        a_builder.show();
    }

    public static void showOfflineNotificationsMessage(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage(("You cannot access notifications while offline.")).setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("No database connection, please check your internet connection");
        a_builder.show();
    }

    public static void showOfflineAccountMessage(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage(("Changing your account information while offline may slow down the app." +
                "\nIf you change your account information, please do not close the app until connection has been restored, so TreeSap can update its database.")).setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("No database connection, please check your internet connection");
        a_builder.show();
    }
}
