package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlanillasFragment extends Fragment {

    private View view_month_gen, view_month_pubcli, view_month_op, view_month_year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_planillas, container, false);
        getActivity().setTitle("Planillas");

        view_month_gen = view.findViewById(R.id.view_month_gen);
        view_month_pubcli = view.findViewById(R.id.view_month_pubcli);
        view_month_op = view.findViewById(R.id.view_month_op);
        view_month_year = view.findViewById(R.id.view_month_op);

        view_month_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlanillaActivity.class);
                intent.putExtra("year", "2019");
                startActivity(intent);
            }
        });

        view_month_pubcli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view_month_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

}
