package aj.corp.gestioncallcenter.models;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Llamada {

    public Integer Id = null;
    public String Operador;
    public String Dia;
    public int Minutos;
    public String Tipo;
    public int Tipocli;
    public boolean Repite;
    public int Empleado;

    public Llamada(int Id, String Operador, String Dia, int Minutos, String Tipo, int Tipocli, boolean Repite, int Empleado) {
        this.Id = Id;
        this.Operador = Operador;
        this.Dia = Dia;
        this.Minutos = Minutos;
        this.Tipo = Tipo;
        this.Tipocli = Tipocli;
        this.Repite = Repite;
        this.Empleado = Empleado;
    }

    public Llamada(){
        this.Id = 0;
        this.Operador = "";
        this.Dia = "";
        this.Minutos = 0;
        this.Tipo = "";
        this.Tipocli = 0;
        this.Repite = false;
        this.Empleado = 0;
    }

    public Llamada(JSONObject json){
        try{
            this.Id = json.getInt("Id");
            this.Operador = json.getString("Operador");
            this.Dia = json.getString("Dia");
            this.Minutos = json.getInt("Minutos");
            this.Tipo = json.getString("Tipo");
            this.Tipocli = json.getInt("Tipocli");
            this.Repite = json.getBoolean("Repite");
            this.Empleado = json.getInt("Empleado");
        }catch (Exception e){

        }
    }

    public JSONObject toJSON() {
        try {
            JSONObject json = new JSONObject();
            json.put("Id",this.Id);
            json.put("Operador",this.Operador);
            json.put("Dia",this.Dia);
            json.put("Minutos",this.Minutos);
            json.put("Tipo",this.Tipo);
            json.put("Tipocli",this.Tipocli);
            json.put("Repite",this.Repite);
            json.put("Empleado",this.Empleado);
            return json;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public String toJSONString(){
        return new Gson().toJson(this);
    }}
