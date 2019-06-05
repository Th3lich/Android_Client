package aj.corp.gestioncallcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import aj.corp.gestioncallcenter.adapters.AdapterOperadores;
import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.models.Operador;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.OperatorService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.Functions;

public class PlanillasFragment extends Fragment {

    private ApiService apiService = new ApiService();
    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private OperatorService operatorService = new OperatorService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());

    private View view_month_gen, view_month_pubcli, view_month_op, view_year;
    private ArrayList<Operador> operadores = new ArrayList<>();
    private AdapterOperadores adapterOperadores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_planillas, container, false);
        getActivity().setTitle("Planillas");

        view_month_gen = view.findViewById(R.id.view_month_gen);
        view_month_pubcli = view.findViewById(R.id.view_month_pubcli);
        view_month_op = view.findViewById(R.id.view_month_op);
        view_year = view.findViewById(R.id.view_year);

        checkUser();
        getOperadores();

        view_month_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escoge el mes");

                Calendar cal = Calendar.getInstance();

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.picker_month_year, (ViewGroup) getView(), false);

                final NumberPicker np_month = viewInflated.findViewById(R.id.np_month);
                final NumberPicker np_year = viewInflated.findViewById(R.id.np_year);
                np_month.setMinValue(1);
                np_month.setMaxValue(12);
                np_month.setValue(cal.get(Calendar.MONTH));
                np_year.setMinValue(1998);
                np_year.setMaxValue(2100);
                np_year.setValue(cal.get(Calendar.YEAR));
                builder.setView(viewInflated);

                builder.setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), PlanillaActivity.class);
                        intent.putExtra("mes", Functions.toMonthConversion(np_month.getValue(), np_year.getValue()) );
                        intent.putExtra("planilla", 1);
                        startActivity(intent);
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
        });

        view_month_pubcli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escoge el mes");

                Calendar cal = Calendar.getInstance();

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.picker_month_year, (ViewGroup) getView(), false);

                final NumberPicker np_month = viewInflated.findViewById(R.id.np_month);
                final NumberPicker np_year = viewInflated.findViewById(R.id.np_year);
                np_month.setMinValue(1);
                np_month.setMaxValue(12);
                np_month.setValue(cal.get(Calendar.MONTH));
                np_year.setMinValue(1998);
                np_year.setMaxValue(2100);
                np_year.setValue(cal.get(Calendar.YEAR));
                builder.setView(viewInflated);

                builder.setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), PlanillaActivity.class);
                        intent.putExtra("mes", Functions.toMonthConversion(np_month.getValue(), np_year.getValue()));
                        intent.putExtra("planilla", 2);
                        startActivity(intent);
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
        });

        view_month_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escoge el mes");

                Calendar cal = Calendar.getInstance();

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.picker_month_year, (ViewGroup) getView(), false);

                final NumberPicker np_month = viewInflated.findViewById(R.id.np_month);
                final NumberPicker np_year = viewInflated.findViewById(R.id.np_year);
                np_month.setMinValue(1);
                np_month.setMaxValue(12);
                np_month.setValue(cal.get(Calendar.MONTH));
                np_year.setMinValue(1998);
                np_year.setMaxValue(2100);
                np_year.setValue(cal.get(Calendar.YEAR));
                builder.setView(viewInflated);

                builder.setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showOperadores(Functions.toMonthConversion(np_month.getValue(), np_year.getValue()));
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
        });

        view_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Escoge el año");

                Calendar cal = Calendar.getInstance();

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.picker_month_year, (ViewGroup) getView(), false);

                final NumberPicker np_year = viewInflated.findViewById(R.id.np_year);
                final NumberPicker np_month = viewInflated.findViewById(R.id.np_month);
                np_month.setVisibility(View.GONE);
                np_year.setMinValue(1998);
                np_year.setMaxValue(2100);
                np_year.setValue(cal.get(Calendar.YEAR));
                builder.setView(viewInflated);

                builder.setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), PlanillaActivity.class);
                        intent.putExtra("planilla", 4);
                        intent.putExtra("year", String.valueOf(np_year.getValue()));
                        startActivity(intent);
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
        });

        return view;
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

                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    operadores.add(new Operador(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }));
    }

    public void showOperadores(String mes){
        adapterOperadores = new AdapterOperadores(getActivity(), operadores, mes, true);
        adapterOperadores.notifyDataSetChanged();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elige un operador");

        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.frame_operadores, null);

        final RecyclerView rv_operadores = viewInflated.findViewById(R.id.rv_operadores);
        builder.setView(viewInflated);

        rv_operadores.setAdapter(adapterOperadores);
        rv_operadores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        builder.show();
    }

}
