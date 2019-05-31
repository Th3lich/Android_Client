package aj.corp.gestioncallcenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aj.corp.gestioncallcenter.services.CallService;

public class CallRegisterFragment extends Fragment {

    private final CallService callService = new CallService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_call_register, container, false);

        /*Llamada llamada = new Llamada(1, "", "", 0, "", 0, false, "");
        Llamada llamada = new Llamada(1, "estrella", "09/07/2019", 2, "movil", 1, false, "1");

        callService.post(getContext(),llamada.toJSON(), new Response.Listener<JSONObject>() {
            // ESTO DE AQUI ES LO QUE TIENE QUE HACER CON LA RESPUESTA, JSONOBJECT RESPONSE
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        });*/

        return view;
    }

}
