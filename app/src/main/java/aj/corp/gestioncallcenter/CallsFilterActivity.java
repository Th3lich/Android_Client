package aj.corp.gestioncallcenter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import aj.corp.gestioncallcenter.adapters.AdapterOperadores;
import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.models.Llamada;
import aj.corp.gestioncallcenter.models.Operador;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.CallService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.OperatorService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.Dialogs;
import aj.corp.gestioncallcenter.utilities.Functions;

public class CallsFilterActivity extends AppCompatActivity {

    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private ApiService apiService = new ApiService();
    private CallService callService = new CallService();
    private OperatorService operatorService = new OperatorService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    public String operador;
    public String day;
    private ArrayList<Operador> operadores = new ArrayList<>();
    private AdapterOperadores adapterOperadores;

    private TextView tv_last_call;
    private Llamada llamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_filter);
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
        getOperadores();
        getUltimaLlamada();
    }

    private void checkUser(){
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
                        if(llamada.Id == 0){
                            tv_last_call.setText("Aún no tienes llamadas");
                        }else{
                            tv_last_call.setText(Functions.DateSimpleConversion(llamada.Dia) +" - " +llamada.Operador +" - " +llamada.Minutos +" min");
                        }
                    }
        }));
    }

    private void getOperadores(){
        queue.add(operatorService.getOperadores(
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());

                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    operadores.add(new Operador(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }));
    }

    public void editLastCall(View view){
        if(llamada.Id == 0){

        }else{
            Intent intent = new Intent(CallsFilterActivity.this, EditCallActivity.class);
            intent.putExtra("llamada", llamada.Id);
            startActivity(intent);
        }
    }

    public void searchByDay(View view){
        final Calendar c= Calendar.getInstance();
        int dia=c.get(Calendar.DAY_OF_MONTH);
        int mes=c.get(Calendar.MONTH);
        int ano=c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CallsFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                day = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                Dialogs.SearchAlertDialog(CallsFilterActivity.this,"Buscar llamadas del día: "+day, "buscar", "cancelar", day);
            }
        }
                ,dia,mes,ano);
        datePickerDialog.show();
    }

    public void searchByOperador(View view){
        adapterOperadores = new AdapterOperadores(getApplicationContext(), operadores);
        adapterOperadores.notifyDataSetChanged();
        showOperadores();
    }

    public void searchByDayOperador(View view){
        adapterOperadores = new AdapterOperadores(getApplicationContext(), operadores, day);
        adapterOperadores.notifyDataSetChanged();

        final Calendar c= Calendar.getInstance();
        int dia=c.get(Calendar.DAY_OF_MONTH);
        int mes=c.get(Calendar.MONTH);
        int ano=c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CallsFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                day = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                showOperadores();
            }
        }
                ,dia,mes,ano);
        datePickerDialog.show();
    }

    public void searchAll(View view){
        Intent intent = new Intent(getApplicationContext(), CallResultsActivity.class);
        intent.putExtra("search", 4);
        startActivity(intent);
    }

    public void showOperadores(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CallsFilterActivity.this);
        builder.setTitle("Elige un operador");

        View viewInflated = LayoutInflater.from(CallsFilterActivity.this).inflate(R.layout.dialog_operadores, null);

        final RecyclerView rv_operadores = viewInflated.findViewById(R.id.rv_operadores);
        builder.setView(viewInflated);

        rv_operadores.setAdapter(adapterOperadores);
        rv_operadores.setLayoutManager(new LinearLayoutManager(CallsFilterActivity.this, LinearLayoutManager.VERTICAL, false));

        builder.show();
    }

}
