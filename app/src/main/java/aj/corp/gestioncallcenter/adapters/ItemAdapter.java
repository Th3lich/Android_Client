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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import aj.corp.gestioncallcenter.AddEditEmployeeActivity;
import aj.corp.gestioncallcenter.R;
import aj.corp.gestioncallcenter.SearchOperatorActivity;
import aj.corp.gestioncallcenter.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public ArrayList<Item> items;
    public Context context;
    private int lastPosition = -1;
    private boolean isOperator = false;
    private SearchOperatorActivity searchOperatorActivity = new SearchOperatorActivity();

    public ItemAdapter(Context context, ArrayList<Item> items){
        this.context = context;
        this.items = items;
    }

    public ItemAdapter(Context context, ArrayList<Item> items, SearchOperatorActivity searchOperatorActivity, boolean isOperator){
        this.context = context;
        this.items = items;
        this.searchOperatorActivity = searchOperatorActivity;
        this.isOperator = isOperator;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_user;
        public TextView tv_item;
        public RelativeLayout viewBackground, viewForeground;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_user = itemView.findViewById(R.id.iv_user);
            tv_item = itemView.findViewById(R.id.tv_item);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder itemViewHolder, final int i) {
        final Item item = items.get(i);
        itemViewHolder.tv_item.setText(item.Contenido);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOperator){
                    searchOperatorActivity.OperatorDialogEdit(i);
                }else{
                    Intent intent = new Intent(context, AddEditEmployeeActivity.class);
                    intent.putExtra("empleado", Integer.valueOf(item.Id));
                    intent.putExtra("edit", true);
                    context.startActivity(intent);
                }
            }
        });

        setAnimation(itemViewHolder.itemView,  i);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(final int position){
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position){
        items.add(position, item);
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
