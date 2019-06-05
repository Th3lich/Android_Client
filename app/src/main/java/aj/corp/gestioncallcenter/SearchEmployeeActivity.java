package aj.corp.gestioncallcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import aj.corp.gestioncallcenter.adapters.ItemAdapter;
import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.models.Item;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.RecyclerItemTouchHelper;
import aj.corp.gestioncallcenter.utilities.RecyclerItemTouchHelperListener;

public class SearchEmployeeActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private ApiService apiService = new ApiService();
    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private CoordinatorLayout root_layout;

    private ItemAdapter itemAdapter;
    private ArrayList<Item> items = new ArrayList<>();
    private RecyclerView rv_empleados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Buscar Empleado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        root_layout = findViewById(R.id.root_layout);
        rv_empleados = findViewById(R.id.rv_empleados);

        itemAdapter = new ItemAdapter(SearchEmployeeActivity.this, items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchEmployeeActivity.this);
        rv_empleados.setLayoutManager(layoutManager);
        rv_empleados.setItemAnimator(new DefaultItemAnimator());
        rv_empleados.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_empleados.setAdapter(itemAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, SearchEmployeeActivity.this, true);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rv_empleados);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchEmployeeActivity.this, AddEditEmployeeActivity.class);
                intent.putExtra("edit", false);
                startActivity(intent);
            }
        });

        checkUser();
        getEmpleados();
    }

    private void checkUser(){
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

    public void getEmpleados(){
        queue.add(employeeService.getEmpleados(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println(response.toString());
                    if(response != null){
                        for(int i=0; i<response.length(); i++){
                            Empleado empleado = new Empleado(response.getJSONObject(i));
                            items.add(new Item(empleado.Id, empleado.Nombre));
                        }
                    }
                    itemAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }));
    }

    public void deleteUser(final int empleado){
        queue.add(employeeService.delete(new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                System.out.println(response.toString());
                Snackbar snackbar = Snackbar.make(root_layout, "Empleado "+empleado +" eliminado", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }, empleado));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if(viewHolder instanceof ItemAdapter.ItemViewHolder){
            final Item deletedItem = items.get(position);
            itemAdapter.removeItem(position);

            AlertDialog.Builder alert = new AlertDialog.Builder(SearchEmployeeActivity.this);
            alert.setTitle("Eliminar Usuario");
            alert.setMessage("¿Seguro que quieres eliminar el usuario?");
            alert.setCancelable(true);
            alert.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteUser(deletedItem.Id);
                    dialog.cancel();
                }
            });
            alert.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    itemAdapter.restoreItem(deletedItem, position);
                    dialog.cancel();
                }
            });
            alert.show();


        }
    }
}
