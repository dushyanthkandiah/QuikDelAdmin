package Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.R;

import java.util.ArrayList;

import Fragments.FragmentRequestedItems;
import Models.ClassRequestList;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class RequestedItemViewAdapter extends RecyclerView.Adapter<RequestedItemViewAdapter.VHolder> {
    private ArrayList<ClassRequestList> data;
    private FragmentRequestedItems fragmentRequestedItems;

    public RequestedItemViewAdapter(ArrayList<ClassRequestList> data, FragmentRequestedItems fragmentRequestedItems) {
        this.data = data;
        this.fragmentRequestedItems = fragmentRequestedItems;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentRequestedItems.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_request_item, parent, false);

        return new VHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        holder.lblPrdId.setText("" + data.get(position).getClassProducts().getId());
        holder.lblPrdName.setText("" + data.get(position).getClassProducts().getName());
        holder.lblQty.setText("" + data.get(position).getQty());

        holder.lblSubTotal.setText("" + data.get(position).getSubTotal());

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {

        TextView lblPrdId, lblPrdName, lblQty, lblSubTotal;
        CardView cardClick;
        View divider;

        public VHolder(View itemView) {
            super(itemView);

            lblPrdId = itemView.findViewById(R.id.lblPrdId);
            lblPrdName = itemView.findViewById(R.id.lblPrdName);
            lblQty = itemView.findViewById(R.id.lblQty);
            lblSubTotal = itemView.findViewById(R.id.lblSubTotal);
            cardClick = itemView.findViewById(R.id.cardClick);
            divider = itemView.findViewById(R.id.divider);

        }
    }

}
