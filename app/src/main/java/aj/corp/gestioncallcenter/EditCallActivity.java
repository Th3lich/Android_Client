package aj.corp.gestioncallcenter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
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

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.models.Llamada;
import aj.corp.gestioncallcenter.models.Operador;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.CallService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.OperatorService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class EditCallActivity extends AppCompatActivity {

    private ApiService apiService = new ApiService();
    private final CallService callService = new CallService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private UtilService utilService = new UtilService();
    private EmployeeService employeeService = new EmployeeService();
    private Empleado empleado = employeeService.getEmpleadoFromSharedPreferences();
    private OperatorService operatorService = new OperatorService();

    private Llamada llamada;
    private Operador operador;
    private String tipo_telefono, operador_id;
    private int tipo_cliente;
    private boolean repite = false;

    private EditText et_minutos, et_fecha;
    private Button bt_save_changes, bt_delete_call, bt_select_day;
    private RadioButton rb_movil, rb_fijo, rb_cliente, rb_publicidad;
    private Spinner sp_operadores;
    private Switch sw_repite;
    private ArrayList<Operador> operadores = new ArrayList<>();
    private ArrayList<String> nombre_operadores = new ArrayList<>();
    private ArrayAdapter<String> adapterOperadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_call);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Editar Llamada");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);

        checkUser();

        et_minutos = findViewById(R.id.et_minutos);
        et_fecha = findViewById(R.id.et_fecha);
        bt_save_changes = findViewById(R.id.bt_save_changes);
        bt_delete_call = findViewById(R.id.bt_delete_call);
        bt_select_day = findViewById(R.id.bt_select_day);
        rb_movil = findViewById(R.id.rb_movil);
        rb_fijo = findViewById(R.id.rb_fijo);
        rb_cliente = findViewById(R.id.rb_cliente);
        rb_publicidad = findViewById(R.id.rb_publicidad);
        sp_operadores = findViewById(R.id.sp_operadores);
        sw_repite = findViewById(R.id.sw_repite);

        bt_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putLlamada();
            }
        });

        bt_delete_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLlamada();
            }
        });

        bt_select_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditCallActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et_fecha.setText(year+"-"+dayOfMonth+"-"+(monthOfYear+1));
                    }
                }
                        ,dia,mes,ano);
                datePickerDialog.show();
            }
        });

        sw_repite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    repite = true;
                }else{
                    repite = false;
                }
            }
        });

        rb_movil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_telefono = "movil";
            }
        });

        rb_fijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_telefono = "fijo";
            }
        });

        rb_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_cliente = 1;
            }
        });

        rb_publicidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_cliente = 3;
            }
        });

        getLlamada();
        getOperadores();

        adapterOperadores = new ArrayAdapter<>(EditCallActivity.this, android.R.layout.simple_spinner_item, nombre_operadores);
        sp_operadores.setAdapter(adapterOperadores);

        sp_operadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operador = operadores.get(position);
                operador_id = operador.Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

    private void getOperadores(){
        queue.add(operatorService.getOperadores(
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        utilService.saveStringToSharedPreferences("operadores",response.toString());

                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    operadores.add(new Operador(response.getJSONObject(i)));
                                    nombre_operadores.add(operadores.get(i).Nombre);
                                    adapterOperadores.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }));
    }

    public void getLlamada(){
        int id = getIntent().getIntExtra("llamada", -1);
        queue.add(callService.getLlamada(
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        llamada = new Llamada(response);
                        et_fecha.setText(llamada.Dia);
//                        et_minutos.setText(getString(llamada.Minutos), TextView.BufferType.EDITABLE);
                        if(llamada.Repite){
                            sw_repite.setChecked(true);
                        }else{
                            sw_repite.setChecked(false);
                        }
                        if(llamada.Tipo.compareTo("movil") == 0){
                            rb_movil.setChecked(true);
                        }
                        if(llamada.Tipo.compareTo("fijo") == 0){
                            rb_fijo.setChecked(true);
                        }
                        if(llamada.Tipocli == 1){
                            rb_cliente.setChecked(true);
                        }
                        if(llamada.Tipocli == 3){
                            rb_publicidad.setChecked(true);
                        }
                        operador_id = llamada.Operador;
                    }
                }, id));
    }

    public void putLlamada(){

        Llamada llamada_edit = new Llamada(llamada.Id, operador.Id, et_fecha.getText().toString(), Integer.parseInt(et_minutos.getText().toString()), tipo_telefono, tipo_cliente, repite, empleado.Id);

        queue.add(callService.put(
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                    }
                }, llamada_edit));
    }

    public void deleteLlamada(){
        queue.add(callService.delete(
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, llamada));
    }

}
