package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdministrationFragment extends Fragment {

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

        return view;
    }

}
