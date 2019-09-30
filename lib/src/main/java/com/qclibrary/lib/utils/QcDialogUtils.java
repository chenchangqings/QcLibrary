package com.qclibrary.lib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class QcDialogUtils {

    public static void showDialog(Context context,int titleId, int messageId,int okTitleId){
        showDialog(context,context.getResources().getString(titleId),context.getResources().getString(messageId),context.getResources().getString(okTitleId));
    }

    public static void showDialog(Context context,int titleId, int messageId,int okTitleId,int cancelTitleId,DialogInterface.OnClickListener listenerOk,DialogInterface.OnClickListener listenerCancel){
        showDialog(context,context.getResources().getString(titleId),context.getResources().getString(messageId),context.getResources().getString(okTitleId),context.getResources().getString(cancelTitleId),listenerOk,listenerCancel);
    }

    public static void showDialog(Context context,String title, String message, String okTitle){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, okTitle, (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    public static void showDialog(Context context,String title, String message, String okTitle,String cancelTitle,DialogInterface.OnClickListener listenerOk,DialogInterface.OnClickListener listenerCancel){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, okTitle,listenerOk);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelTitle,listenerCancel);
        alertDialog.show();
    }
    
}
