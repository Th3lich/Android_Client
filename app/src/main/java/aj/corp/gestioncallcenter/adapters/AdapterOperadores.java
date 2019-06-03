package aj.corp.gestioncallcenter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aj.corp.gestioncallcenter.R;

public class AdapterOperadores extends RecyclerView.Adapter<AdapterOperadores.OperadoresViewHolder> {

    private ArrayList<String> operadores;
    private Context context;

    public AdapterOperadores(Context context, ArrayList<String> operadores){
        this.operadores = operadores;
        this.context = context;
    }

    public class OperadoresViewHolder extends RecyclerView.ViewHolder{

        TextView tv_operador;

        public OperadoresViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_operador = itemView.findViewById(R.id.tv_operador);
        }

    }

    public OperadoresViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_operador, viewGroup, false);
        return new OperadoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperadoresViewHolder operadoresViewHolder, int i) {

        final String operador = operadores.get(i);
        operadoresViewHolder.tv_operador.setText(operador);
        operadoresViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return operadores.size();
    }
}
