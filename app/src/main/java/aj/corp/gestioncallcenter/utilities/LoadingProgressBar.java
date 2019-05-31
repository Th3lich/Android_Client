package aj.corp.gestioncallcenter.utilities;

import android.content.Context;
import android.graphics.Color;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LoadingProgressBar {

    ACProgressFlower loading;

    public LoadingProgressBar(Context application){
        this.loading = new ACProgressFlower.Builder(application)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build();
    }

    public void show(){
        loading.show();
    }

    public void cancel(){
        loading.cancel();
    }

}
