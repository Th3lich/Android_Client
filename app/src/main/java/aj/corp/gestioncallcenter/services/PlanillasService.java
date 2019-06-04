package aj.corp.gestioncallcenter.services;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


public class PlanillasService {

    private final ApiService apiService = new ApiService();
    private final String serviceURL = this.apiService.API+"/planilla/";

    public StringRequest getPlanillaMensualGeneral(Response.Listener<String> callback, String mes){
        return this.apiService.getString(this.serviceURL+"planilla_mensual/"+mes+"/pdf", callback);
    }

    public StringRequest getPlanillaMensualPubli(Response.Listener<String> callback, String mes){
        return this.apiService.getString(this.serviceURL+"planilla_mensual_publi/"+mes+"/pdf", callback);
    }

    public StringRequest getPlanillaMensualOperador(Response.Listener<String> callback, String mes, String operadores){
        String pdfType = "pdfv";
        String[] opArr = operadores.split("-");
        if(opArr.length > 2){
            pdfType = "pdfh";
        }
        return this.apiService.getString(this.serviceURL+"planilla_mensual_operador/"+mes+"/"+operadores+"/"+pdfType, callback);
    }

    public StringRequest getPlanillaAnualGeneral(Response.Listener<String> callback, String anio){
        return this.apiService.getString(this.serviceURL+"planilla_anual/"+anio+"/pdf", callback);
    }

    public String getServiceURL(){
        return this.serviceURL;
    }

}
