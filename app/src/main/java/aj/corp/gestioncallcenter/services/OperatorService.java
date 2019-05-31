package aj.corp.gestioncallcenter.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import aj.corp.gestioncallcenter.models.Empleado;

import org.json.JSONObject;

public class OperatorService {

    private final ApiService apiService = new ApiService();
    private final String serviceURL = this.apiService.API+"/operador";

    public void getOperadores(Context context, Response.Listener<JSONObject> callback){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL,callback));
    }

    public void getOperadorById(Context context, Response.Listener<JSONObject> callback, String idOperador){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/"+idOperador,callback));
    }

    public void post(Context context, Response.Listener<JSONObject> callback, Empleado empleado){
        Volley.newRequestQueue(context).add(this.apiService.post(this.serviceURL,callback,empleado.toJSON()));
    }

    public void put(Context context, Response.Listener<JSONObject> calback, Empleado empleado){
        Volley.newRequestQueue(context).add(this.apiService.put(this.serviceURL,calback, empleado.toJSON()));
    }

    public void delete(Context context, Response.Listener<String> callback, String idOperador){
        Volley.newRequestQueue(context).add(this.apiService.delete(this.serviceURL+"/"+idOperador,callback));
    }

}
