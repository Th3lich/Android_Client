package aj.corp.gestioncallcenter.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import aj.corp.gestioncallcenter.models.Llamada;

import org.json.JSONArray;
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

    public JsonRequest getLlamada(Response.Listener<JSONObject> callback, Integer idLlamada){
        return this.apiService.get(this.serviceURL+"/"+idLlamada, callback);
    }

    public JsonRequest getUltimaLlamada(Response.Listener<JSONObject> callback){
        return this.apiService.get(this.serviceURL+"/ultima",callback);
    }

    public JsonRequest getAllLlamadas(Response.Listener<JSONArray> callback){
        return this.apiService.getArray(this.serviceURL,callback);
    }

    public JsonRequest getLlamadasByOperador(Response.Listener<JSONArray> callback, String idOperador){
        return this.apiService.getArray(this.serviceURL+"/operador/"+idOperador,callback);
    }

    public JsonRequest getLlamadasByDia(Response.Listener<JSONArray> callback, String dia){
        return this.apiService.getArray(this.serviceURL+"/dia/"+dia,callback);
    }

    public JsonRequest getLlamadasByDiaOperador(Response.Listener<JSONArray> callback, String dia, String idOperador){
        return this.apiService.getArray(this.serviceURL+"/dia/"+dia+"/operador/"+idOperador, callback);
    }

    public JsonRequest getDailyReport(Response.Listener<JSONObject> callback, String dia){
        return this.apiService.get(this.apiService+"/resumen/diario/"+dia,callback);
    }

    public JsonRequest getDailyReportOperator(Response.Listener<JSONObject> callback, String dia, String idOperador){
        return this.apiService.get(this.serviceURL+"/resumen/diario/"+dia+"/operador/"+idOperador,callback);
    }

    public JsonRequest getDailyReportEmpleado(Response.Listener<JSONObject> callback, String dia, String idEmpleado){
        return this.apiService.get(this.serviceURL+"/resumen/diario/"+dia+"/empleado/"+idEmpleado, callback);
    }
    public JsonRequest post(Response.Listener<JSONObject> callback, Llamada llamada){
        return this.apiService.post(this.serviceURL,callback,llamada.toJSON());
    }

    public JsonRequest put(Response.Listener<JSONObject> callback, Llamada llamada){
        return this.apiService.put(this.serviceURL,callback,llamada.toJSON());
    }

    public StringRequest delete(Response.Listener<String> callback, Llamada llamada){
        return this.apiService.delete(this.serviceURL+"/"+llamada.Id,callback);
    }

    public StringRequest delete(Response.Listener<String> callback, int llamada){
        return this.apiService.delete(this.serviceURL+"/"+llamada,callback);
    }

    /*
    public func getDailyReportEmpleado(day: String, employeeId: String) -> DataRequest{
        return self.api.get(url: "\(self.api.URL)/llamada/resumen/diario/\(day)/empleado/\(employeeId)")
}

     */
}
