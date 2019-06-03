package aj.corp.gestioncallcenter;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.models.Llamada;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.CallService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.Functions;

public class EditCallsActivity extends AppCompatActivity {

    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private ApiService apiService = new ApiService();
    private CallService callService = new CallService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    public String operador;

    private TextView tv_last_call;
    private Llamada llamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_calls);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_last_call = findViewById(R.id.tv_last_call);

        checkUser();
        getUltimaLlamada();
    }

    public void checkUser(){
        queue.add(apiService.checkUser(ApplicationContext.getAppContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    utilService.saveTokenToSharedPreferences(response.getString("token"));
                    utilService.saveRefreshTokenToSharedPreferences(response.getString("refresh"));
                    employeeService.saveEmpleadoToSharedPreferences(new Gson().fromJson(response.getString("empleado"), Empleado.class));

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }));
    }

    public void getUltimaLlamada(){
        queue.add(callService.getUltimaLlamada(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        llamada = new Llamada(response);
                        tv_last_call.setText(llamada.Dia +" - " +llamada.Operador +" - " +llamada.Minutos +" min");
                    }
        }));
    }

    public void searchByDay(View view){
        final Calendar c= Calendar.getInstance();
        int dia=c.get(Calendar.DAY_OF_MONTH);
        int mes=c.get(Calendar.MONTH);
        int ano=c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditCallsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Functions.SearchAlertDialog(EditCallsActivity.this,"Buscar llamadas del día: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year, "buscar", "cancelar");
            }
        }
                ,dia,mes,ano);
        datePickerDialog.show();
    }

    public void searchByOperador(View view){

    }

    public void searchByDayOperador(View view){

    }

    public void searchAll(View view){

    }

}
