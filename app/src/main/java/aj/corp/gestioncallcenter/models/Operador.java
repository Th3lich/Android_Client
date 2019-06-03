package aj.corp.gestioncallcenter.models;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Operador {

    public String Id;
    public String Nombre;

    public Operador(String Id, String Nombre){
        this.Id = Id;
        this.Nombre = Nombre;
    }

    public Operador(){
        this.Id = "";
        this.Nombre = "";
    }

    public Operador(JSONObject json){
        try {
            this.Id = json.getString("Id");
            this.Nombre = json.getString("Nombre");
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON(){
        try{
            JSONObject json = new JSONObject();
            json.put("Id",this.Id);
            json.put("Nombre",this.Nombre);
            return json;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public String toJSONString(){
        return new Gson().toJson(this);
    }

}
