package aj.corp.gestioncallcenter.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class Functions {

    public static void ErrorAlertDialog(Context context, String message, String aceptar){
        Log.v("AJCORP", "ENTRA EN EL ALERTDIALOG");
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton(aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setTitle("Error");
        alert.show();
    }

}
