package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class PerfilFragment extends Fragment {

    private EmployeeService employeeService = new EmployeeService();
    private Empleado empleado = employeeService.getEmpleadoFromSharedPreferences();
    private UtilService utilService = new UtilService();

    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    TextView tv_nombre, tv_user, tv_direccion, tv_telefono, tv_fecha, tv_empleado;
    Button bt_change_pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        getActivity().setTitle("Mi Perfil");

        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_user = view.findViewById(R.id.tv_user);
        tv_direccion = view.findViewById(R.id.tv_direccion);
        tv_telefono = view.findViewById(R.id.tv_telefono);
        tv_fecha = view.findViewById(R.id.tv_fecha);
        tv_empleado = view.findViewById(R.id.tv_empleado);
        bt_change_pass = view.findViewById(R.id.bt_change_pass);

        tv_nombre.setText(empleado.Nombre +" " +empleado.Apellidos);
        tv_user.setText(empleado.User);
        tv_direccion.setText(empleado.Direccion);
        tv_telefono.setText(empleado.Telefono);
        tv_fecha.setText(empleado.Fecha_contratacion);

        bt_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });

        return view;
    }

    public void changePass(){
        queue.add(employeeService.changePassword(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                    }
                }
                , empleado.Id, "123456")
        );
    }

}
