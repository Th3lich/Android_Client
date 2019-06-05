package aj.corp.gestioncallcenter.models;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Empleado {

    public Integer Id = null;
    public String User;
    public String Nombre;
    public String Apellidos;
    public String Direccion;
    public String Telefono;
    public String Fecha_contratacion;

    public Empleado(Integer id, String user, String nombre, String apellidos, String direccion, String telefono, String fecha_contratacion) {
        this.Id = id;
        this.User = user;
        this.Nombre = nombre;
        this.Apellidos = apellidos;
        this.Direccion = direccion;
        this.Telefono = telefono;
        this.Fecha_contratacion = fecha_contratacion;
    }

    public Empleado(){

    }

    public Empleado(JSONObject json){
        try {
            this.Id = json.getInt("Id");
            this.User = json.getString("User");
            this.Nombre = json.getString("Nombre");
            this.Apellidos = json.getString("Apellidos");
            this.Direccion = json.getString("Direccion");
            this.Telefono = json.getString("Telefono");
            this.Fecha_contratacion = json.getString("Fecha_contratacion");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Empleado(String JSON){

        Empleado empleado = new Gson().fromJson(JSON,Empleado.class);

        this.Id = empleado.Id;
        this.User = empleado.User;
        this.Nombre = empleado.Nombre;
        this.Apellidos = empleado.Apellidos;
        this.Direccion = empleado.Direccion;
        this.Telefono = empleado.Telefono;
        this.Fecha_contratacion = empleado.Fecha_contratacion;
    }

    public JSONObject toJSON(){
        try {
            JSONObject json = new JSONObject();
            json.put("Id",this.Id);
            json.put("User", this.User);
            json.put("Nombre",this.Nombre);
            json.put("Apellidos",this.Apellidos);
            json.put("Direccion",this.Direccion);
            json.put("Telefono",this.Telefono);
            json.put("Fecha_contratacion",this.Fecha_contratacion);
            return json;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public String toJSONString(){
        return new Gson().toJson(this);
    }


}
