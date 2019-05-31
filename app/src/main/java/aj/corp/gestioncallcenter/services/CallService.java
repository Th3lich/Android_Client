package aj.corp.gestioncallcenter.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import aj.corp.gestioncallcenter.models.Llamada;

import org.json.JSONObject;

public class CallService {

    private final ApiService apiService = new ApiService();
    private final String serviceURL = this.apiService.API+"/llamada";

    public static final int CLIENT_TYPE_CLIENTE = 1;
    public static final int CLIENT_TYPE_PUBLICIDAD = 3;

    public static final String PHONE_TYPE_MOVIL = "movil";
    public static final String PHONE_TYPE_FIJO = "fijo";

    public static final int[] CLIENT_TYPES = {CLIENT_TYPE_CLIENTE, CLIENT_TYPE_PUBLICIDAD};
    public static final String[] PHONE_TYPES = {PHONE_TYPE_FIJO, PHONE_TYPE_MOVIL};

    public void getUltimaLlamada(Context context, Response.Listener<JSONObject> callback){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/ultima",callback));
    }

    public void getAllLlamadas(Context context, Response.Listener<JSONObject> callback){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL,callback));
    }

    public void getLlamadasByOperador(Context context, Response.Listener<JSONObject> callback, String idOperador){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/operador/"+idOperador,callback));
    }

    public void getLlamadasByDia(Context context, Response.Listener<JSONObject> callback, String dia){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/dia/"+dia,callback));
    }

    public void getLlamadasByDiaOperador(Context context, Response.Listener<JSONObject> callback, String dia, String idOperador){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/dia/"+dia+"/operador/"+idOperador, callback));
    }

    public void getDailyReport(Context context, Response.Listener<JSONObject> callback, String dia){
        Volley.newRequestQueue(context).add(this.apiService.get(this.apiService+"/resumen/diario/"+dia,callback));
    }

    public void getDailyReportOperator(Context context, Response.Listener<JSONObject> callback, String dia, String idOperador){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/resumen/diario/"+dia+"/operador/"+idOperador,callback));
    }

    public void getDailyReportEmpleado(Context context, Response.Listener<JSONObject> callback, String dia, String idEmpleado){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/resumen/diario/"+dia+"/empleado/"+idEmpleado, callback));
    }
    public void post(Context context, Response.Listener<JSONObject> callback, Llamada llamada){
        Volley.newRequestQueue(context).add(this.apiService.post(this.serviceURL,callback,llamada.toJSON()));
    }

    public void put(Context context, Response.Listener<JSONObject> callback, Llamada llamada){
        Volley.newRequestQueue(context).add(this.apiService.put(this.serviceURL,callback,llamada.toJSON()));
    }

    public void delete(Context context, Response.Listener<String> callback, Llamada llamada){
        Volley.newRequestQueue(context).add(this.apiService.delete(this.serviceURL+"/"+llamada.Id,callback));
    }

    public void delete(Context context, Response.Listener<String> callback, int llamada){
        Volley.newRequestQueue(context).add(this.apiService.delete(this.serviceURL+"/"+llamada,callback));
    }

    /*
    public func getDailyReportEmpleado(day: String, employeeId: String) -> DataRequest{
        return self.api.get(url: "\(self.api.URL)/llamada/resumen/diario/\(day)/empleado/\(employeeId)")
}

     */
}
