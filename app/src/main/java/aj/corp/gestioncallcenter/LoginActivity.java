package aj.corp.gestioncallcenter;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.OperatorService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class LoginActivity extends AppCompatActivity {

    private final EmployeeService employeeService = new EmployeeService();
    private final ApiService apiService = new ApiService();
    private final UtilService utilService = new UtilService();
    private final OperatorService operatorService = new OperatorService();
    private final RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    TextInputLayout til_user, til_pass;
    EditText edt_user, edt_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        til_user = findViewById(R.id.til_user);
        til_pass = findViewById(R.id.til_pass);
        edt_user = findViewById(R.id.edt_user);
        edt_pass = findViewById(R.id.edt_pass);

        til_user.setError(null);
        til_pass.setError(null);
    }

    public void login(View view){

        til_user.setError(null);
        til_pass.setError(null);

        if(edt_user.getText().toString().isEmpty()) {
            til_user.setError("Debe introducir el nombre de usuario");
        }else if(edt_pass.getText().toString().isEmpty()) {
            til_pass.setError("Debe introducir la contraseña");
        }else{
            til_user.setError(null);
            til_pass.setError(null);

            login();
        }
    }

    private void login(){
        apiService.login(ApplicationContext.getAppContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());
                            utilService.saveTokenToSharedPreferences(response.getString("token"));
                            utilService.saveRefreshTokenToSharedPreferences(response.getString("refresh"));
                            employeeService.saveEmpleadoToSharedPreferences(new Gson().fromJson(response.getString("empleado"), Empleado.class));
                            if(response.getInt("permisos") > 5){
                                System.out.println("ADMINISTRADOR");
                            }else {
                                System.out.println("USUARIO");
                            }
                            segunda();

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                , "alejandro", "123456");

    }

    private void segunda(){
        operatorService.getOperadores(ApplicationContext.getAppContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // SE EJECUTA LA PETICION PERO NO LLEGA AQUI
                        System.out.println(response.toString());
                        utilService.saveStringToSharedPreferences("operadores",response.toString());
                    }
                });
    }
}