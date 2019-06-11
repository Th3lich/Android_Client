package aj.corp.gestioncallcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import aj.corp.gestioncallcenter.adapters.AdapterLlamadas;
import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.models.Llamada;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.CallService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.RecyclerItemTouchHelper;
import aj.corp.gestioncallcenter.utilities.RecyclerItemTouchHelperListener;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class CallResultsActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private EmployeeService employeeService = new EmployeeService();
    private ApiService apiService = new ApiService();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private CallService callService = new CallService();

    private String dia, id_operador;
    private RecyclerView rv_llamadas;
    private TextView tv_sin_resultados;
    private ArrayList<Llamada> llamadas = new ArrayList<>();
    private AdapterLlamadas adapterLlamadas;
    private CoordinatorLayout root_layout;
    private ACProgressFlower loadingbar;

    final Handler puente = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 100){
                loadingbar.dismiss();
                setRecycler();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Resultados: ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingbar = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Cargando")
                .fadeColor(Color.DKGRAY).build();

        root_layout = findViewById(R.id.root_layout);
        rv_llamadas = findViewById(R.id.rv_llamadas);
        tv_sin_resultados = findViewById(R.id.tv_sin_resultados);

        tv_sin_resultados.setVisibility(View.VISIBLE);
        tv_sin_resultados.setText("No hay resultados para éste filtro");

        checkUser();
        setFilter();
    }

    private void checkUser(){
        queue.add(apiService.checkUser(CallResultsActivity.this, new Response.Listener<JSONObject>() {
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

    public void setFilter(){
        Intent intent = getIntent();
        int search = intent.getIntExtra("search", 4);

        switch (search){
            case 1:
                dia = intent.getStringExtra("date");
                System.out.println("FILTRO DIA: "+dia);
                getLlamadasByDia();
                break;
            case 2:
                id_operador = intent.getStringExtra("operador");
                System.out.println("FILTRO OPERADOR: "+id_operador);
                getLlamadasByOperador();
                break;
            case 3:
                dia = intent.getStringExtra("date");
                id_operador = intent.getStringExtra("operador");
                System.out.println("FILTRO COMBINADO: "+dia +" " +id_operador);
                getLlamadasByDiaOperador();
                break;
            case 4:
                System.out.println("SIN FILTRO");
                tv_sin_resultados.setText("Aún no tienes llamadas para mostrar");
                getAllLlamadas();
                break;
        }
    }

    public void getLlamadasByDia(){
        loadingbar.show();
        llamadas.clear();
        new Thread() {
            @Override
            public void run() {
                queue.add(callService.getLlamadasByDia(
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                System.out.println(response.toString());
                                setTitle("Resultados: " + response.length());
                                if (response != null) {
                                    tv_sin_resultados.setVisibility(View.GONE);
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            llamadas.add(new Llamada(response.getJSONObject(i)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
//                            setRecycler();
                                }
                                Message msg = new Message();
                                msg.what = 100;
                                puente.sendMessage(msg);
                            }
                        }, dia));
            }
        }.start();
    }

    public void getLlamadasByOperador(){
        loadingbar.show();
        llamadas.clear();
        new Thread() {
            @Override
            public void run() {
                queue.add(callService.getLlamadasByOperador(
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                System.out.println(response.toString());
                                setTitle("Resultados: " + response.length());
                                if (response != null) {
                                    for (int i = 0; i < response.length(); i++) {
                                        tv_sin_resultados.setVisibility(View.GONE);
                                        try {
                                            llamadas.add(new Llamada(response.getJSONObject(i)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    //                            setRecycler();
                                }
                                Message msg = new Message();
                                msg.what = 100;
                                puente.sendMessage(msg);
                            }
                        }, id_operador));
            }
        }.start();
    }

    public void getLlamadasByDiaOperador(){
        llamadas.clear();
        loadingbar.show();
        new Thread() {
            @Override
            public void run() {
                queue.add(callService.getLlamadasByDiaOperador(
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                System.out.println(response.toString());
                                setTitle("Resultados: " + response.length());
                                if (response != null) {
                                    for (int i = 0; i < response.length(); i++) {
                                        tv_sin_resultados.setVisibility(View.GONE);
                                        try {
                                            llamadas.add(new Llamada(response.getJSONObject(i)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
//                                    setRecycler();
                                }
                                Message msg = new Message();
                                msg.what = 100;
                                puente.sendMessage(msg);
                            }
                        }, dia, id_operador));
            }
        }.start();
    }

    public void getAllLlamadas(){
        loadingbar.show();
        llamadas.clear();
        new Thread() {
            @Override
            public void run() {
                queue.add(callService.getAllLlamadas(
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                System.out.println(response.toString());
                                setTitle("Resultados: " + response.length());
                                if (response != null) {
                                    for (int i = 0; i < response.length(); i++) {
                                        tv_sin_resultados.setVisibility(View.GONE);
                                        try {
                                            llamadas.add(new Llamada(response.getJSONObject(i)));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
//                                    setRecycler();
                                }
                                Message msg = new Message();
                                msg.what = 100;
                                puente.sendMessage(msg);
                            }
                        }));
            }
        }.start();
    }

    public void deleteLlamada(final Llamada llamada){
        queue.add(callService.delete(
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Snackbar snackbar = Snackbar.make(root_layout, "Llamada "+llamada.Id +" eliminada", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }, llamada));
    }

    public void setRecycler(){
        adapterLlamadas = new AdapterLlamadas(CallResultsActivity.this, llamadas);
        adapterLlamadas.notifyDataSetChanged();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CallResultsActivity.this);
        rv_llamadas.setLayoutManager(layoutManager);
        rv_llamadas.setItemAnimator(new DefaultItemAnimator());
        rv_llamadas.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_llamadas.setAdapter(adapterLlamadas);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, CallResultsActivity.this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rv_llamadas);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if(viewHolder instanceof AdapterLlamadas.LlamadasViewHolder){
            final Llamada deletedItem = llamadas.get(position);
            adapterLlamadas.removeItem(position);

            AlertDialog.Builder alert = new AlertDialog.Builder(CallResultsActivity.this);
            alert.setTitle("Eliminar Llamada");
            alert.setMessage("¿Seguro que quieres eliminar la llamada?");
            alert.setCancelable(true);
            alert.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteLlamada(deletedItem);
                    dialog.cancel();
                }
            });
            alert.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapterLlamadas.restoreItem(deletedItem, position);
                    dialog.cancel();
                }
            });
            alert.show();
        }
    }
}
