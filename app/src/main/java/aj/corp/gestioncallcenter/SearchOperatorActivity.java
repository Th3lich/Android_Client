package aj.corp.gestioncallcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import aj.corp.gestioncallcenter.models.Operador;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.OperatorService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;
import aj.corp.gestioncallcenter.utilities.Dialogs;
import aj.corp.gestioncallcenter.utilities.RecyclerItemTouchHelper;
import aj.corp.gestioncallcenter.utilities.RecyclerItemTouchHelperListener;

public class SearchOperatorActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private ApiService apiService = new ApiService();
    private EmployeeService employeeService = new EmployeeService();
    private UtilService utilService = new UtilService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private OperatorService operatorService = new OperatorService();

    private CoordinatorLayout root_layout;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> items = new ArrayList<>();
    private RecyclerView rv_operadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_operator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Buscar Operador");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OperatorDialogAdd();
            }
        });

        rv_operadores = findViewById(R.id.rv_operadores);
        root_layout = findViewById(R.id.root_layout);

        itemAdapter = new ItemAdapter(SearchOperatorActivity.this, items, this, true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchOperatorActivity.this);
        rv_operadores.setLayoutManager(layoutManager);
        rv_operadores.setItemAnimator(new DefaultItemAnimator());
        rv_operadores.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_operadores.setAdapter(itemAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, SearchOperatorActivity.this, true);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rv_operadores);

        checkUser();
        getOperadores();
    }

    private void checkUser(){
        queue.add(apiService.checkUser(SearchOperatorActivity.this, new Response.Listener<JSONObject>() {
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
        queue.add(operatorService.getOperadores(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                utilService.saveStringToSharedPreferences("operadores",response.toString());

                try {
                    System.out.println(response.toString());
                    if(response != null){
                        items.clear();
                        for(int i=0; i<response.length(); i++){
                            Operador operador = new Operador(response.getJSONObject(i));
                            items.add(new Item(operador.Id, operador.Nombre));
                        }
                    }
                    itemAdapter.notifyDataSetChanged();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }));
    }

    public void OperatorDialogEdit(final int position){
        final Item editedItem = items.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(SearchOperatorActivity.this);
        builder.setTitle("Cambiar nombre operador");

        View viewInflated = LayoutInflater.from(SearchOperatorActivity.this).inflate(R.layout.dialog_edit_text, null);

        final EditText et_dialog = viewInflated.findViewById(R.id.et_dialog);
        et_dialog.setHint("nombre");
        builder.setView(viewInflated);

        builder.setPositiveButton("cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(et_dialog.getText().toString().compareTo("") != 0) {
                    final Operador editedOperator = new Operador(editedItem.Id, et_dialog.getText().toString());
                    itemAdapter.removeItem(position);
                    itemAdapter.restoreItem(new Item(editedItem.Id, et_dialog.getText().toString()), position);
                    editOperador(editedOperator);
                }else{
                    Dialogs.ErrorAlertDialog(SearchOperatorActivity.this, "", "El campo de texto no puede estar vacío", "aceptar");
                }
                dialog.cancel();
            }
        });

        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void OperatorDialogAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchOperatorActivity.this);
        builder.setTitle("Añadir Operador");
        builder.setMessage("Escribe el nombre del nuevo operador");

        View viewInflated = LayoutInflater.from(SearchOperatorActivity.this).inflate(R.layout.dialog_edit_text, null);

        final EditText et_dialog = viewInflated.findViewById(R.id.et_dialog);
        et_dialog.setHint("nombre");
        builder.setView(viewInflated);

        builder.setPositiveButton("añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id_operador = et_dialog.getText().toString().toLowerCase();
                postOperador(new Operador(id_operador, et_dialog.getText().toString()));
                dialog.cancel();
            }
        });

        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void editOperador(final Operador operador){
        queue.add(operatorService.put(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                itemAdapter.notifyDataSetChanged();
                Dialogs.ErrorAlertDialog(SearchOperatorActivity.this, "Operador editado", "El operador se modificó correctamente", "aceptar");
            }
        }, operador));
    }

    public void postOperador(final Operador operador){
        queue.add(operatorService.post(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                itemAdapter.notifyDataSetChanged();
                Dialogs.ErrorAlertDialog(SearchOperatorActivity.this, "Operador registrado", "El operador se añadió correctamente al sistema", "aceptar");
            }
        }, operador));
    }

    public void deleteOperador(final String id_operador){
        queue.add(operatorService.delete(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Snackbar snackbar = Snackbar.make(root_layout, "Operador "+id_operador +" eliminado", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }, id_operador));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if(viewHolder instanceof ItemAdapter.ItemViewHolder){
            final Item deletedItem = items.get(position);
            itemAdapter.removeItem(position);

            AlertDialog.Builder alert = new AlertDialog.Builder(SearchOperatorActivity.this);
            alert.setTitle("Eliminar Operador");
            alert.setMessage("¿Seguro que quieres eliminar este operador?");
            alert.setCancelable(true);
            alert.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteOperador(deletedItem.Id);
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
