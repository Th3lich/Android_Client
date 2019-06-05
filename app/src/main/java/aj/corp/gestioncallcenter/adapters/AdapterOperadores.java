package aj.corp.gestioncallcenter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aj.corp.gestioncallcenter.CallResultsActivity;
import aj.corp.gestioncallcenter.PlanillaActivity;
import aj.corp.gestioncallcenter.R;
import aj.corp.gestioncallcenter.models.Operador;
import aj.corp.gestioncallcenter.utilities.Functions;

public class AdapterOperadores extends RecyclerView.Adapter<AdapterOperadores.OperadoresViewHolder> {

    private ArrayList<Operador> operadores;
    private String fecha;
    private Context context;
    private boolean isPlanilla = false;
    private String mes;

    public AdapterOperadores(Context context, ArrayList<Operador> operadores){
        this.operadores = operadores;
        this.fecha = "";
        this.context = context;
    }

    public AdapterOperadores(Context context, ArrayList<Operador> operadores, String fecha){
        this.operadores = operadores;
        this.fecha = fecha;
        this.context = context;
    }

    public AdapterOperadores(Context context, ArrayList<Operador> operadores, String mes, boolean isPlanilla){
        this.operadores = operadores;
        this.context = context;
        this.mes = mes;
        this.isPlanilla = isPlanilla;
    }

    public class OperadoresViewHolder extends RecyclerView.ViewHolder{

        TextView tv_operador, tv_fecha;

        public OperadoresViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_operador = itemView.findViewById(R.id.tv_operador);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
        }

    }

    public OperadoresViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_operador, viewGroup, false);
        return new OperadoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperadoresViewHolder operadoresViewHolder, int i) {

        final Operador operador = operadores.get(i);
        operadoresViewHolder.tv_operador.setText(operador.Nombre);
        if(isPlanilla){
            operadoresViewHolder.tv_fecha.setText(mes);
        }else{
            operadoresViewHolder.tv_fecha.setText(Functions.DateSimpleConversion(fecha));
        }
        operadoresViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlanilla){
                    Intent intent = new Intent(context, PlanillaActivity.class);
                    intent.putExtra("mes", mes);
                    intent.putExtra("planilla", 3);
                    intent.putExtra("operador", operador.Id);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, CallResultsActivity.class);
                    if(fecha != ""){
                        intent.putExtra("search", 3);
                        intent.putExtra("operador", operador.Id);
                        intent.putExtra("date", fecha);
                    }else{
                        intent.putExtra("search", 2);
                        intent.putExtra("operador", operador.Id);
                    }
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return operadores.size();
    }
}
