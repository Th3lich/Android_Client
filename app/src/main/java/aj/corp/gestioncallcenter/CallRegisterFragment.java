package aj.corp.gestioncallcenter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

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
import aj.corp.gestioncallcenter.utilities.Dialogs;

public class CallRegisterFragment extends Fragment {

    private ApiService apiService = new ApiService();
    private final CallService callService = new CallService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private OperatorService operatorService = new OperatorService();
    private UtilService utilService = new UtilService();
    private EmployeeService employeeService = new EmployeeService();
    private Empleado empleado = employeeService.getEmpleadoFromSharedPreferences();

    private Operador operador;
    private String tipo_telefono = "movil";
    private int tipo_cliente = 1;
    private boolean repite = false;

    private NumberPicker picker_minutos;
    private EditText et_fecha;
    private Button bt_call_register, bt_select_day, bt_edit_calls;
    private RadioButton rb_movil, rb_fijo, rb_cliente, rb_publicidad;
    private Spinner sp_operadores;
    private Switch sw_repite;
    private ArrayList<Operador> operadores = new ArrayList<>();
    private ArrayList<String> nombre_operadores = new ArrayList<>();
    private ArrayAdapter<String> adapterOperadores;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_call_register, container, false);
        getActivity().setTitle("Registrar Llamada");
        apiService.context = getActivity();

        picker_minutos = view.findViewById(R.id.picker_minutos);
        et_fecha = view.findViewById(R.id.et_fecha);
        bt_call_register = view.findViewById(R.id.bt_call_register);
        bt_select_day = view.findViewById(R.id.bt_select_day);
        bt_edit_calls = view.findViewById(R.id.bt_edit_calls);
        rb_movil = view.findViewById(R.id.rb_movil);
        rb_fijo = view.findViewById(R.id.rb_fijo);
        rb_cliente = view.findViewById(R.id.rb_cliente);
        rb_publicidad = view.findViewById(R.id.rb_publicidad);
        sp_operadores = view.findViewById(R.id.sp_operadores);
        sw_repite = view.findViewById(R.id.sw_repite);

        picker_minutos.setMinValue(0);
        picker_minutos.setMaxValue(1000);
        picker_minutos.setValue(1);

        rb_movil.setChecked(true);
        rb_cliente.setChecked(true);

        bt_select_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et_fecha.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }
                        ,dia,mes,ano);
                datePickerDialog.show();
            }
        });

        bt_edit_calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallsFilterActivity.class);
                startActivity(intent);
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

        bt_call_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLlamada();
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

        checkUser();
        getOperadores();

        adapterOperadores = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nombre_operadores);
        sp_operadores.setAdapter(adapterOperadores);

        sp_operadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operador = operadores.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void checkUser(){
        queue.add(apiService.checkUser(getActivity(), new Response.Listener<JSONObject>() {
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

    private void postLlamada(){
        int minutos = picker_minutos.getValue();
        System.out.println("MINUTOS "+minutos);
        String dia = et_fecha.getText().toString();
        if(dia.isEmpty()){
            Dialogs.ErrorAlertDialog(getActivity(), "", "No puede haber campos vacíos", "aceptar");
        }else {

            queue.add(callService.post(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            Dialogs.ErrorAlertDialog(getActivity(), "Llamada Registrada", "La llamada se registró correctamente en el sistema", "aceptar");
                        }
                    }
                    , new Llamada(null, operador.Id, dia, minutos,
                            tipo_telefono, tipo_cliente, repite, empleado.Id)));
        }
    }

}
