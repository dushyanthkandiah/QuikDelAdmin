package Adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.R;

import java.util.ArrayList;

import Dialogs.DialogProducts;
import Fragments.FragmentProducts;
import Models.ClassProducts;


@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.VHolder> {
    private ArrayList<ClassProducts> data;
    private FragmentProducts fragmentProducts;

    public ProductViewAdapter(ArrayList<ClassProducts> data, FragmentProducts fragmentProducts) {
        this.data = data;
        this.fragmentProducts = fragmentProducts;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentProducts.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_product, parent, false);

        return new VHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {

        holder.lblName.setText("" + data.get(position).getName());
        holder.lblPrice.setText("" + data.get(position).getPrice());
        holder.lblDetails.setText("Details : " + data.get(position).getDetails());
        holder.lblQty.setText("" + data.get(position).getQuantity());

        String typeValue = ""; // typeValue resembles to the full form database value e.g. det = Detergent

        if (data.get(position).getType().equals("det")) {
            typeValue = "Detergent";
        }else if (data.get(position).getType().equals("sta")) {
            typeValue = "Stationary";
        }

        holder.lblType.setText("Type : " + typeValue);

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogProducts dialogProducts = DialogProducts.newInstance(fragmentProducts, data.get(position), "update");
                dialogProducts.show(fragmentProducts.getFragmentManager(), "dialog");
            }
        });

        holder.cardDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                fragmentProducts.serverProduct.deleteProduct(fragmentProducts, data.get(position).getId(), data.get(position).getName());

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentProducts.getActivity());
                builder.setMessage("Are you sure you want to delete "+data.get(position).getName()+"?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblName, lblPrice, lblType, lblDetails, lblQty;
        CardView cardClick, cardDeleteItem;
        View divider;

        public VHolder(View itemView) {
            super(itemView);
            lblName = itemView.findViewById(R.id.lblName);
            lblPrice = itemView.findViewById(R.id.lblPrice);
            lblType = itemView.findViewById(R.id.lblType);
            lblDetails = itemView.findViewById(R.id.lblDetails);
            lblQty = itemView.findViewById(R.id.lblQty);
            cardClick = itemView.findViewById(R.id.cardClick);
            divider = itemView.findViewById(R.id.divider);
            cardDeleteItem = itemView.findViewById(R.id.cardDeleteItem);
        }
    }

}
