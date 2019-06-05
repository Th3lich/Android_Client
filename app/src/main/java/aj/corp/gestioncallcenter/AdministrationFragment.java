package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AdministrationFragment extends Fragment {

    private EmployeeService employeeService = new EmployeeService();
    private ApiService apiService = new ApiService();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    CardView cv_empleado, cv_operador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administration, container, false);
        getActivity().setTitle("Administración");

        cv_empleado = view.findViewById(R.id.cv_empleado);
        cv_operador = view.findViewById(R.id.cv_operador);

        cv_empleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchEmployeeActivity.class);
                startActivity(intent);
            }
        });

        cv_operador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchOperatorActivity.class);
                startActivity(intent);
            }
        });

        checkUser();

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

}
