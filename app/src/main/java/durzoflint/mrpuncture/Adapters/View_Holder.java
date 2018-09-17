package durzoflint.mrpuncture.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import durzoflint.mrpuncture.R;

public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    TextView name;
    TextView distance;
    ImageView badge;
    LinearLayout store;

    private WeakReference<RecyclerViewItemClickListener> listenerRef;

    View_Holder(View itemView, RecyclerViewItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        distance = itemView.findViewById(R.id.distance);
        badge = itemView.findViewById(R.id.badge);
        store = itemView.findViewById(R.id.store);
        listenerRef = new WeakReference<>(listener);

        store.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listenerRef.get().onClick(view, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}