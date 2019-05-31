package aj.corp.gestioncallcenter.services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import aj.corp.gestioncallcenter.models.Empleado;

import org.json.JSONException;
import org.json.JSONObject;

public class EmployeeService {

    private final ApiService apiService = new ApiService();
    private final String serviceURL = this.apiService.API+"/empleado";

    private final UtilService utilService = new UtilService();

    public void getEmpleados(Context context, Response.Listener<JSONObject> callback){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL,callback));
    }

    public void getEmpleadoById(Context context, Response.Listener<JSONObject> callback, int id){
        Volley.newRequestQueue(context).add(this.apiService.get(this.serviceURL+"/"+id,callback));
    }

    public void post(Context context, Response.Listener<JSONObject> callback, Empleado empleado){
        Volley.newRequestQueue(context).add(this.apiService.post(this.serviceURL,callback,empleado.toJSON()));
    }

    public void put(Context context, Response.Listener<JSONObject> callback, Empleado empleado){
        Volley.newRequestQueue(context).add(this.apiService.put(this.serviceURL,callback,empleado.toJSON()));
    }

    public void delete(Context context, Response.Listener<String> callback, int idEmpleado){
        Volley.newRequestQueue(context).add(this.apiService.delete(this.serviceURL+"/"+idEmpleado, callback));
    }

    public void changePassword(Context context, Response.Listener<JSONObject> callback, int idEmpleado, String newPassword){
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id",idEmpleado);
            parameters.put("pass",newPassword);

        }catch(JSONException e){
            e.printStackTrace();
        }
        Volley.newRequestQueue(context).add(this.apiService.put(this.serviceURL+"/pass",callback,parameters));
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



    /*

     */

}
