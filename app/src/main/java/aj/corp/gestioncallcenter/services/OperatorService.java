package aj.corp.gestioncallcenter.services;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import aj.corp.gestioncallcenter.models.Empleado;

import org.json.JSONArray;
import org.json.JSONObject;

public class OperatorService {

    private final ApiService apiService = new ApiService();
    private final String serviceURL = this.apiService.API+"/operador";

    public JsonArrayRequest getOperadores(Response.Listener<JSONArray> callback){
        return this.apiService.getArray(this.serviceURL,callback);
    }

    public JsonObjectRequest getOperadorById(Response.Listener<JSONObject> callback, String idOperador){
        return this.apiService.get(this.serviceURL+"/"+idOperador,callback);
    }

    public JsonObjectRequest post(Response.Listener<JSONObject> callback, Empleado empleado){
        return this.apiService.post(this.serviceURL,callback,empleado.toJSON());
    }

    public JsonObjectRequest put(Response.Listener<JSONObject> calback, Empleado empleado){
        return this.apiService.put(this.serviceURL,calback, empleado.toJSON());
    }

    public StringRequest delete(Response.Listener<String> callback, String idOperador){
        return this.apiService.delete(this.serviceURL+"/"+idOperador,callback);
    }

}
