package aj.corp.gestioncallcenter.services;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiService {


//    public static final String API = "http://192.168.1.34:1313";
    public static final String API = "http://88.22.139.97:1313";

    private UtilService utilService = new UtilService();

    public JsonObjectRequest get(String url, Response.Listener<JSONObject> successResponse){

        return new JsonObjectRequest(Request.Method.GET, url, null, successResponse, this.errorRequest()) {
            @Override
            public Map getHeaders(){
                return getDefaultHeaders();
            }
        };
    }

    public JsonObjectRequest get(String url, Response.Listener<JSONObject> successResponse, final Map customHeaders){
        return new JsonObjectRequest(Request.Method.GET, url, null, successResponse, this.errorRequest()) {
            @Override
            public Map getHeaders(){
                return customHeaders;
            }
        };
    }

    public JsonObjectRequest post(String url, Response.Listener<JSONObject> successResponse, final JSONObject params){
        return new JsonObjectRequest(Request.Method.POST, url, params, successResponse, this.errorRequest()) {
            @Override
            public Map getHeaders(){
                return getDefaultHeaders();
            }
        };
    }

    public JsonObjectRequest put(String url, Response.Listener<JSONObject> successResponse, final JSONObject params){
        return new JsonObjectRequest(Request.Method.PUT, url, params, successResponse, this.errorRequest()) {
            @Override
            public Map getHeaders(){
                return getDefaultHeaders();
            }
        };
    }

    public StringRequest delete(String url, Response.Listener<String> successResponse){
        return new StringRequest(Request.Method.DELETE, url, successResponse, this.errorRequest()) {
            @Override
            public Map getHeaders(){
                return getDefaultHeaders();
            }
        };
    }

    public JsonRequest login(Response.Listener<JSONObject> callback, String login, String pass){
        System.out.println("LOGIN");
        return this.get(this.API+"/login",callback, this.getBasicLoginHeaders(login,pass));
    }

    public void renovarToken(){
        Volley.newRequestQueue(ApplicationContext.getAppContext()).add(
                this.get(this.API + "/login", new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    utilService.saveTokenToSharedPreferences(response.getString("token"));
                                    utilService.saveRefreshTokenToSharedPreferences(response.getString("refresh"));
                                }catch (JSONException e){
                                    e.printStackTrace();
                                    logout();
                                }

                            }
                        },this.getDefaultHeaders(this.utilService.getRefreshTokenFromoSharedPreferences())
                )
        );
    }

    public void logout(){
        System.out.println("FALTA PROGRAMAR");
    }

    public Map getDefaultHeaders(){
        HashMap headers = new HashMap();
        headers.put("Authorization","Bearer "+this.utilService.getTokenFromSharedPreferences());
        return headers;
    }

    public Map getDefaultHeaders(String customToken){
        HashMap headers = new HashMap();
        headers.put("Authorization","Bearer "+customToken);
        return headers;
    }

    public Map getBasicLoginHeaders(String user, String pass){
        // !! CAMBIAR !! ESTA DE PRUEBA, AQUI COGER TOKEN SI HAY TOKEN Y MOTNAR BEARER, SI NO COGER REFRESH, SI NO LOGOUT
        HashMap headers = new HashMap();
        headers.put("Authorization",this.generateBasicAuth(user,pass));
        return headers;
    }

    private String generateBasicAuth(String user, String pass){
        String basic= user+":"+pass;
        return "Basic " + this.utilService.encodeBase64(basic);
    }

    // MUESTRA DE COMO PASAR PARAMETROS POR BODY
    public JSONObject pruebaParametros(){
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("city", "london");
            postparams.put("timestamp", 1500134255);
            postparams.put("objeto", new Object());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postparams;

    }

    private Response.ErrorListener errorRequest(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //AQUI HACER LOGOUT
                try {
                    Log.e("BAD REQUEST", "STATUS: " + error.networkResponse.statusCode);
                    System.out.println(error.networkResponse.allHeaders);
                }catch (Exception e){

                }
            }
        };
    }
}
