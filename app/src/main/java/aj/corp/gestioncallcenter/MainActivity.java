package aj.corp.gestioncallcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.Response;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import aj.corp.gestioncallcenter.models.Empleado;
import aj.corp.gestioncallcenter.services.ApiService;
import aj.corp.gestioncallcenter.services.EmployeeService;
import aj.corp.gestioncallcenter.services.UtilService;
import aj.corp.gestioncallcenter.shared.ApplicationContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final ApiService apiService = new ApiService();
    private final UtilService utilService = new UtilService();
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initEmpleado();
        checkUser();

        setFragment(0);
    }

    public void initEmpleado(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Inicio");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void checkUser(){
        apiService.checkUser(ApplicationContext.getAppContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    utilService.saveTokenToSharedPreferences(response.getString("token"));
                    utilService.saveRefreshTokenToSharedPreferences(response.getString("refresh"));
                    employeeService.saveEmpleadoToSharedPreferences(new Gson().fromJson(response.getString("empleado"), Empleado.class));

                    if(response.getInt("permisos") > 5){
                        System.out.println("ADMINISTRADOR");
                        
                    }else {
                        System.out.println("USUARIO");

                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.nav_home:
                setFragment(0);
                return true;
            case R.id.nav_call_register:
                setFragment(1);
                return true;
            case R.id.nav_perfil:
                setFragment(2);
                return true;
            case R.id.nav_administration:
                setFragment(3);
                return true;
            case R.id.nav_logout:
                return true;
        }
        return true;
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MenuFragment menuFragment = new MenuFragment();
                fragmentTransaction.replace(R.id.fragment, menuFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                CallRegisterFragment callRegisterFragment = new CallRegisterFragment();
                fragmentTransaction.replace(R.id.fragment, callRegisterFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PerfilFragment perfilFragment = new PerfilFragment();
                fragmentTransaction.replace(R.id.fragment, perfilFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PlanillasFragment planillasFragment = new PlanillasFragment();
                fragmentTransaction.replace(R.id.fragment, planillasFragment);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                AdministrationFragment administrationFragment = new AdministrationFragment();
                fragmentTransaction.replace(R.id.fragment, administrationFragment);
                fragmentTransaction.commit();
                break;
        }
    }

}
