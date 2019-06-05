package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class AddEditEmployeeActivity extends AppCompatActivity {

    private ApiService apiService = new ApiService();
    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    private int empleado_id;
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
    }

    private void setFilter(){
        Intent intent = getIntent();
        if(intent.getBooleanExtra("edit", false)){
            empleado_id = intent.getIntExtra("empleado", -1);
            bt_add.setVisibility(View.GONE);
            getEmpleadoById();
        }else{
            bt_save_changes.setVisibility(View.GONE);
            bt_delete.setVisibility(View.GONE);
            bt_reset_pass.setVisibility(View.GONE);
        }
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

}
