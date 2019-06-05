package aj.corp.gestioncallcenter;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.PlanillasService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class PlanillaActivity extends AppCompatActivity {

    private EmployeeService employeeService = new EmployeeService();
    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private PlanillasService planillasService = new PlanillasService();
    private UtilService utilService = new UtilService();
    private String url = planillasService.getServiceURL();
    private ApiService apiService = new ApiService();

    private String year;
    private String id_operador;
    private String mes;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planilla);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.coloractionBarTabTextStyle), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = findViewById(R.id.wb_planilla);

        setFilter();
        checkUser();
    }

    public void setFilter(){
        Intent intent = getIntent();
        int search = intent.getIntExtra("planilla", 0);

        switch (search){
            case 1:
                mes = intent.getStringExtra("mes");
                System.out.println("FILTRO MES: "+mes);
                descargarPlanilla("Planilla Mes", "planilla_mensual/", "/pdf");
                break;
            case 2:
                mes = intent.getStringExtra("mes");
                System.out.println("FILTRO MES PUBLI/CLI: "+mes);
                descargarPlanilla("Planilla Mes Publi/Cli", "planilla_mensual_publi/", "/pdf");
                break;
            case 3:
                mes = intent.getStringExtra("mes");
                id_operador = intent.getStringExtra("operador");
                System.out.println("FILTRO COMBINADO: "+mes +" " +id_operador);
                descargarPlanilla("Planilla Mes y Operadores", "planilla_mensual_operador/"+mes+"/"+id_operador, "/pdfv");
                break;
            case 4:
                System.out.println("SIN FILTRO");
                year = intent.getStringExtra("year");
                descargarPlanilla("Planilla Anual", "planilla_anual/", "/pdf");
                break;
        }
    }

    private void checkUser(){
        queue.add(apiService.checkUser(PlanillaActivity.this, new Response.Listener<JSONObject>() {
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

    public void descargarPlanilla(String title, String url_planilla, String formato){
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url + url_planilla + mes + formato));
        if(url_planilla.compareTo("planilla_anual/") == 0){
            request = new DownloadManager.Request(
                    Uri.parse(url + url_planilla + year + "/pdf"));
        }
        request.setMimeType("application/pdf");
        request.addRequestHeader("Authorization", "Bearer " + utilService.getTokenFromSharedPreferences());
        request.setDescription("Descargando Planilla...");
        request.allowScanningByMediaScanner();
        if(title.compareTo("Planilla Anual") == 0){
            request.setTitle(title +" " +year);
        }else{
            request.setTitle(title +" " +mes);
        }

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(PlanillaActivity.this,
                Environment.DIRECTORY_DOWNLOADS, ".pdf");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        Toast.makeText(getApplicationContext(), "Descargando Planilla...", Toast.LENGTH_LONG).show();
    }

}
