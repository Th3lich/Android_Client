package aj.corp.gestioncallcenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;

public class PerfilFragment extends Fragment {

    UtilService utilService = new UtilService();
    EmployeeService employeeService = new EmployeeService();
    Empleado empleado = employeeService.getEmpleadoFromSharedPreferences();

    TextView tv_nombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        getActivity().setTitle("Perfil");

        tv_nombre = view.findViewById(R.id.tv_nombre);

        tv_nombre.setText(empleado.Nombre +" " +empleado.Apellidos);

        return view;
    }

}
