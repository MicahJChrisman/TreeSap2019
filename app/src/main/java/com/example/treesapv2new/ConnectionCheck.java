package com.example.treesapv2new;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.treesapv2new.view.CuratorMessage;

import java.io.IOException;

public class ConnectionCheck {
    public static boolean offlineMessageShown = false;
    public static boolean offlineCuratorMessageShown = false;
    public static boolean offlineAddTreeMessageShown = false;
    public static boolean offlineAccountMessageShown = false;

    public static boolean isConnectedToFirebase() throws InterruptedException, IOException {
        final String command = "ping -c 1 firebase.google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public static void showOfflineMessage(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("While disconnected, you cannot load trees from currently unselected databases, but you can access locally stored data." +
                "\n\nIf you are connected to the internet, then there is a server issue, and it's probably not our fault.").setCancelable(true)
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
        a_builder.setMessage(("You will not be able to load new pending trees." +
                "\nAccepting or rejecting locally stored pending trees may slow down the app." +
                        "\nplease keep the app open after accepting or rejecting pending trees until connection is restored.")).setCancelable(true)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("No database connection, please check your internet connection");
        a_builder.show();
    }

    public static void showOfflineAddTreeMessage(Context context){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage(("Submitting a new tree while offline may slow down the app. \nPlease keep the app open after submitting until connection is restored. \nPress okay to continue.")).setCancelable(true)
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
                "\nIf you change your account information, please do not close the app until connection has been restored, so TreeSap can update its database.")).setCancelable(true)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("No database connection, please check your internet connection");
        a_builder.show();
    }
}
