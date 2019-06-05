package aj.corp.gestioncallcenter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.Dialogs;

public class AddEditEmployeeActivity extends AppCompatActivity {

    private ApiService apiService = new ApiService();
    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private CoordinatorLayout root_layout;

    private int empleado_id;
    private boolean permisos;
    private Empleado empleado;
    private EditText et_nombre, et_apellidos, et_direccion, et_telefono, et_fecha, et_usuario;
    private Button bt_fecha, bt_add, bt_reset_pass, bt_save_changes, bt_delete;
    private Switch sw_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_employee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Empleado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        root_layout = findViewById(R.id.root_layout);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellidos = findViewById(R.id.et_apellidos);
        et_direccion = findViewById(R.id.et_direccion);
        et_telefono = findViewById(R.id.et_telefono);
        et_fecha = findViewById(R.id.et_fecha);
        et_usuario = findViewById(R.id.et_usuario);
        bt_fecha = findViewById(R.id.bt_fecha);
        bt_add = findViewById(R.id.bt_add);
        bt_reset_pass = findViewById(R.id.bt_reset_pass);
        bt_save_changes = findViewById(R.id.bt_save_changes);
        bt_delete = findViewById(R.id.bt_delete);
        sw_admin = findViewById(R.id.sw_admin);

        checkUser();
        setFilter();

        bt_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmpleado();
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(empleado.Id);
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarUser();
            }
        });

        bt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                int dia=c.get(Calendar.DAY_OF_MONTH);
                int mes=c.get(Calendar.MONTH);
                int ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEditEmployeeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        et_fecha.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }
                        ,dia,mes,ano);
                datePickerDialog.show();
            }
        });
    }

    private void setFilter(){
        Intent intent = getIntent();
        if(intent.getBooleanExtra("edit", false)){
            empleado_id = intent.getIntExtra("empleado", -1);
            bt_add.setVisibility(View.GONE);
            getEmpleadoById();
            isAdmin();
        }else{
            bt_save_changes.setVisibility(View.GONE);
            bt_delete.setVisibility(View.GONE);
            bt_reset_pass.setVisibility(View.GONE);
        }
    }

    private void checkUser(){
        queue.add(apiService.checkUser(AddEditEmployeeActivity.this, new Response.Listener<JSONObject>() {
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

    private void getEmpleadoById(){
        queue.add(employeeService.getEmpleadoById(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                empleado = new Empleado(response);

                et_nombre.setText(empleado.Nombre);
                et_apellidos.setText(empleado.Apellidos);
                et_direccion.setText(empleado.Direccion);
                et_telefono.setText(empleado.Telefono);
                et_fecha.setText(empleado.Fecha_contratacion);
                et_usuario.setText(empleado.User);
            }
        }, empleado_id));
    }

    private void isAdmin(){
        queue.add(employeeService.isAdmin(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);

                try {
                    if(response.getInt("permisos") > 5){
                        sw_admin.setChecked(true);
                        permisos = true;
                    }else{
                        sw_admin.setChecked(false);
                        permisos = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, empleado_id));
    }

    private void saveEmpleado(){

        if(sw_admin.isChecked() != permisos){
            changePermission();
        }

        Empleado empleado_edit = new Empleado(empleado.Id, et_usuario.getText().toString(), et_nombre.getText().toString(), et_apellidos.getText().toString(),
                et_direccion.getText().toString(), et_telefono.getText().toString(), et_fecha.getText().toString());
        queue.add(employeeService.put(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                Dialogs.VolverAlertDialog(AddEditEmployeeActivity.this, "Llamada Editada", "La llamada se editó correctamente", "continuar", "volver");
            }
        }, empleado_edit));
    }

    private void changePermission(){
        final int permision;
        if(sw_admin.isChecked()){
            permision = 8;
        }else{
            permision = 3;
        }

        queue.add(employeeService.changePermission(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                if(permision == 8){
                    permisos = true;
                    Snackbar snackbar = Snackbar.make(root_layout, "El empleado "+empleado.Id +" ahora es administrador", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    permisos = false;
                    Snackbar snackbar = Snackbar.make(root_layout, "El administrador "+empleado.Id +" ahora es empleado", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        }, empleado_id, permision));
    }

    public void deleteUser(final int empleado){
        queue.add(employeeService.delete(new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                System.out.println(response);
                finish();
                Snackbar snackbar = Snackbar.make(root_layout, "Empleado "+empleado +" eliminado", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }, empleado));
    }

    public void agregarUser(){
        Empleado new_empleado = new Empleado(null, et_usuario.getText().toString(), et_nombre.getText().toString(), et_apellidos.getText().toString(),
                et_direccion.getText().toString(), et_telefono.getText().toString(), et_fecha.getText().toString());
        queue.add(employeeService.post(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                Dialogs.VolverAlertDialog(AddEditEmployeeActivity.this, "Usuario Registrado", "El usuario se registró correctamente en el sistema", "aceptar", "volver");
            }
        }, new_empleado));
    }

}
