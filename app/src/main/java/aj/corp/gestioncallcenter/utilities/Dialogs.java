package aj.corp.gestioncallcenter.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import aj.corp.gestioncallcenter.CallResultsActivity;

public class Dialogs {

    public static void ErrorAlertDialog(Context context, String titulo, String message, String aceptar){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton(aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setTitle(titulo);
        alert.show();
    }

    public static void SearchAlertDialog(final Context context, String message, String aceptar, String cancelar, final String day){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton(aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, CallResultsActivity.class);
                intent.putExtra("search", 1);
                intent.putExtra("date", day);
                context.startActivity(intent);
            }
        });
        alert.setNegativeButton(cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public static void VolverAlertDialog(final Context context, String titulo, String message, String aceptar, String cancelar){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton(aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setNegativeButton(cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finish();
            }
        });
        alert.setTitle(titulo);
        alert.show();
    }

}
