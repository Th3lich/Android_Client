package aj.corp.gestioncallcenter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import aj.corp.gestioncallcenter.EditCallActivity;
import aj.corp.gestioncallcenter.R;
import aj.corp.gestioncallcenter.models.Llamada;
import aj.corp.gestioncallcenter.utilities.Functions;

public class AdapterLlamadas extends RecyclerView.Adapter<AdapterLlamadas.LlamadasViewHolder> {

    public ArrayList<Llamada> llamadas;
    public Context context;
    private int lastPosition = -1;

    public AdapterLlamadas(Context context, ArrayList<Llamada> llamadas){
        this.context = context;
        this.llamadas = llamadas;
    }

    public class LlamadasViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_llamada;
        public RelativeLayout viewBackground, viewForeground;

        public LlamadasViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_llamada = itemView.findViewById(R.id.tv_llamada);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    @NonNull
    @Override
    public LlamadasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_call, viewGroup, false);
        return new LlamadasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LlamadasViewHolder llamadasViewHolder, int i) {
        final Llamada llamada = llamadas.get(i);
        llamadasViewHolder.tv_llamada.setText(Functions.DateSimpleConversion(llamada.Dia)  +" - " +llamada.Operador +" - " +llamada.Minutos +" min");
        llamadasViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCallActivity.class);
                intent.putExtra("llamada", llamada.Id);
                context.startActivity(intent);
            }
        });

        setAnimation(llamadasViewHolder.itemView,  i);

    }

    @Override
    public int getItemCount() {
        return llamadas.size();
    }

    public void removeItem(final int position){
        llamadas.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Llamada llamada, int position){
        llamadas.add(position, llamada);
        notifyItemRemoved(position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
