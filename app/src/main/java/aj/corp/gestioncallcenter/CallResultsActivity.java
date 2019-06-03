package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import aj.corp.gestioncallcenter.adapters.AdapterLlamadas;
import aj.corp.gestioncallcenter.models.Llamada;
import aj.corp.gestioncallcenter.models.Operador;
import aj.corp.gestioncallcenter.services.CallService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class CallResultsActivity extends AppCompatActivity {

    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private CallService callService = new CallService();

    private String dia, id_operador;
    private RecyclerView rv_llamadas;
    private TextView tv_sin_resultados;
    private ArrayList<Llamada> llamadas = new ArrayList<>();
    private AdapterLlamadas adapterLlamadas;

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

        rv_llamadas = findViewById(R.id.rv_llamadas);
        tv_sin_resultados = findViewById(R.id.tv_sin_resultados);

        tv_sin_resultados.setVisibility(View.VISIBLE);
        tv_sin_resultados.setText("No hay resultados para éste filtro");

        setFilter();
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
        llamadas.clear();
        queue.add(callService.getLlamadasByDia(
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        setTitle("Resultados: "+response.length());
                        if (response != null) {
                            tv_sin_resultados.setVisibility(View.GONE);
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    llamadas.add(new Llamada(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setRecycler();
                        }

                    }
                }, dia));
    }

    public void getLlamadasByOperador(){
        llamadas.clear();
        queue.add(callService.getLlamadasByOperador(
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        setTitle("Resultados: "+response.length());
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                tv_sin_resultados.setVisibility(View.GONE);
                                try {
                                    llamadas.add(new Llamada(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setRecycler();
                        }
                    }
                }, id_operador));
    }

    public void getLlamadasByDiaOperador(){
        llamadas.clear();
        queue.add(callService.getLlamadasByDiaOperador(
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        setTitle("Resultados: "+response.length());
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                tv_sin_resultados.setVisibility(View.GONE);
                                try {
                                    llamadas.add(new Llamada(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setRecycler();
                        }
                    }
                }, dia, id_operador));
    }

    public void getAllLlamadas(){
        llamadas.clear();
        queue.add(callService.getAllLlamadas(
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        setTitle("Resultados: "+response.length());
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                tv_sin_resultados.setVisibility(View.GONE);
                                try {
                                    llamadas.add(new Llamada(response.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setRecycler();
                        }
                    }
                }));
    }

    public void setRecycler(){
        adapterLlamadas = new AdapterLlamadas(getApplicationContext(), llamadas);
        adapterLlamadas.notifyDataSetChanged();
        rv_llamadas.setAdapter(adapterLlamadas);
        rv_llamadas.setLayoutManager(new LinearLayoutManager(CallResultsActivity.this, LinearLayoutManager.VERTICAL, false));
    }
}
