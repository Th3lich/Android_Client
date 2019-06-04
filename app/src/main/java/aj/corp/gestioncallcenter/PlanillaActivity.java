package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import aj.corp.gestioncallcenter.services.PlanillasService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class PlanillaActivity extends AppCompatActivity {

    private RequestQueue queue = Volley.newRequestQueue(ApplicationContext.getAppContext());
    private PlanillasService planillasService = new PlanillasService();
    private UtilService utilService = new UtilService();
    private String url = planillasService.getServiceURL();

    private String year;
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

        year = getIntent().getStringExtra("year");

        Uri path = Uri.parse(url+"planilla_anual/"+year+"/pdf");
        Intent pdfIntent = new Intent(Intent.ACTION_QUICK_VIEW);
        Bundle bundle = new Bundle();
        bundle.putString("authorization","Bearer "+utilService.getTokenFromSharedPreferences());
        pdfIntent.putExtra(Browser.EXTRA_HEADERS, bundle);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(pdfIntent);



        //webView.getSettings().setJavaScriptEnabled(true);
        //Map<String, String> headers = new HashMap<String, String>();
        //headers.put("Authorization", "Bearer "+utilService.getTokenFromSharedPreferences());
        //System.out.println(url+"planilla_anual/"+year+"/pdf");

        //webView.loadUrl(url+"planilla_anual/"+year+"/html", headers);
//        webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url+"planilla_anual/"+year+"/pdf", headers);
        //loadYearlyGeneralReport();

    }

    public void loadMonthlyGeneralReport(){

    }

    public void loadMonthlyMarketingReport(){

    }

    public void loadMonthlyOperatorReport(){

    }

    public void loadYearlyGeneralReport(){
        queue.add(planillasService.getPlanillaAnualGeneral(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ONRESPONSE");


                        //File file = new File("");
                        //Map<String, String> headers = new HashMap<String, String>();
                        //headers.put("Authorization", "Bearer "+utilService.getTokenFromSharedPreferences());
                        //webView.setWebChromeClient(new WebChromeClient());
                        //webView.setWebViewClient(new WebViewClient());
                        //webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url+"planilla_anual/"+year+"/pdf", headers);
                        //webView.loadUrl("http://google.es");
                        //webView.loadDataWithBaseURL(url+"planilla_anual/"+year+"/pdf", response, "application/pdf", "utf-8", null);
                        //webView.loadData(response, "application/pdf","utf-8");
                    }
                }, year));
    }

}
