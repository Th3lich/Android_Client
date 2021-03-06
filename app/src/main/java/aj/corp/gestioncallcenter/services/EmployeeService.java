package aj.corp.gestioncallcenter.services;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import aj.corp.gestioncallcenter.models.Empleado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeService {

    private final ApiService apiService = new ApiService();
    private final String serviceURL = this.apiService.API+"/empleado";

    private final UtilService utilService = new UtilService();

    public JsonRequest getEmpleados(Response.Listener<JSONArray> callback){
        return this.apiService.getArray(this.serviceURL,callback);
    }

    public JsonRequest getEmpleadoById(Response.Listener<JSONObject> callback, int id){
        return this.apiService.get(this.serviceURL+"/"+id,callback);
    }

    public JsonRequest post(Response.Listener<JSONObject> callback, Empleado empleado){
        return this.apiService.post(this.serviceURL,callback,empleado.toJSON());
    }

    public JsonRequest put(Response.Listener<JSONObject> callback, Empleado empleado){
        return this.apiService.put(this.serviceURL,callback,empleado.toJSON());
    }

    public StringRequest delete(Response.Listener<String> callback, int idEmpleado){
        return this.apiService.delete(this.serviceURL+"/"+idEmpleado, callback);
    }

    public JsonRequest changePassword(Response.Listener<JSONObject> callback, int idEmpleado, String newPassword){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id",idEmpleado);
            parameters.put("pass",newPassword);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return this.apiService.put(this.serviceURL+"/pass",callback,parameters);
    }

    public JsonRequest isAdmin(Response.Listener<JSONObject> callback, int idEmpleado){
        return this.apiService.get(this.serviceURL+"/permisos/"+idEmpleado, callback);
    }

    public JsonRequest changePermission(Response.Listener<JSONObject> callback, int idEmpleado, int permission){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id",idEmpleado);
            parameters.put("permisos",permission);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return this.apiService.put(this.serviceURL+"/permisos", callback, parameters);
    }

    public void saveEmpleadoToSharedPreferences(Empleado empleado){
        this.utilService.saveStringToSharedPreferences("empleado",empleado.toJSONString());
    }

    public Empleado getEmpleadoFromSharedPreferences(){
        return new Empleado(this.utilService.getStringFromSharedPreferences("empleado"));
    }

    public void deleteEmpleadoFromSharedPreferences(){
        this.utilService.deleteKeyFromSharedPreferences("empleado");
    }
}
