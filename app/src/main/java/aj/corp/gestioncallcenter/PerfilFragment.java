package aj.corp.gestioncallcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import aj.corp.gestioncallcenter.utilities.Functions;

public class PerfilFragment extends Fragment {

    private EmployeeService employeeService = new EmployeeService();
    private ApiService apiService = new ApiService();
    private Empleado empleado = employeeService.getEmpleadoFromSharedPreferences();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    TextView tv_nombre, tv_user, tv_direccion, tv_telefono, tv_fecha, tv_empleado;
    Button bt_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        getActivity().setTitle("Mi Perfil");
        setHasOptionsMenu(true);

        apiService.context = getActivity();

        checkUser();

        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_user = view.findViewById(R.id.tv_user);
        tv_direccion = view.findViewById(R.id.tv_direccion);
        tv_telefono = view.findViewById(R.id.tv_telefono);
        tv_fecha = view.findViewById(R.id.tv_fecha);
        tv_empleado = view.findViewById(R.id.tv_empleado);
        bt_logout = view.findViewById(R.id.bt_logout);

        tv_nombre.setText(empleado.Nombre +" " +empleado.Apellidos);
        tv_user.setText(empleado.User);
        tv_fecha.setText(empleado.Fecha_contratacion);

        if(empleado.Direccion.toString().compareTo("") != 0){
            tv_direccion.setText(empleado.Direccion);
        }else{
            tv_direccion.setText("sin dirección");
        }
        if(empleado.Telefono.toString().compareTo("") != 0){
            tv_telefono.setText(empleado.Telefono);
        }else{
            tv_telefono.setText("sin teléfono");
        }

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    public void changePass(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cambiar Contraseña");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.frame_text_input_layout, (ViewGroup) getView(), false);

        final EditText et_pass = viewInflated.findViewById(R.id.et_pass);
        final EditText et_repass = viewInflated.findViewById(R.id.et_repass);
        builder.setView(viewInflated);

        builder.setPositiveButton("Cambiar Contraseña", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(et_pass.getText().toString().compareTo(et_repass.getText().toString()) != 0){
                    Functions.ErrorAlertDialog(getActivity(), "", "Las contraseñas no coinciden, vuelve a intentarlo", "aceptar");
                }else{
                    queue.add(employeeService.changePassword(
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println(response.toString());
                                    logout();
                                }
                            }
                            , empleado.Id, et_pass.getText().toString())
                    );
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

                    empleado = employeeService.getEmpleadoFromSharedPreferences();

                    if(response.getInt("permisos") > 5){
                        System.out.println("ADMINISTRADOR");
                        tv_empleado.setText("Administrador");
                    }else {
                        System.out.println("USUARIO");
                        tv_empleado.setText("Empleado");
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }));
    }

    public void logout(){
        utilService.clearSharedPreferences();
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                changePass();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
