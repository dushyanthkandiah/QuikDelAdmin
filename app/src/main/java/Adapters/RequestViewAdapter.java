package Adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
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

import Fragments.FragmentRequest;
import Models.ClassRequest;
import OtherClasses.SessionData;


@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class RequestViewAdapter extends RecyclerView.Adapter<RequestViewAdapter.VHolder> {
    private ArrayList<ClassRequest> data;
    private FragmentRequest fragmentRequest;

    public RequestViewAdapter(ArrayList<ClassRequest> data, FragmentRequest fragmentRequest) {
        this.data = data;
        this.fragmentRequest = fragmentRequest;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentRequest.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_view_requests, parent, false);

        return new VHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        holder.lblItemId.setText(data.get(position).getReqId() + "");
        holder.lblRequestDate.setText("Date/Time : " + data.get(position).getReqDate() + "");
        holder.lblRequestTotal.setText(data.get(position).getTotal() + "");
        holder.lblRemarks.setText("Remarks : " + data.get(position).getRemarks());
        holder.lblUserName.setText("" + data.get(position).getClassUsers().getName());

        if (data.get(position).getStatus() == 0) {
            holder.markBill.setVisibility(View.VISIBLE);
            holder.lblStatus.setText("Pending");
        } else  if (data.get(position).getStatus() == 1){
            holder.markBill.setVisibility(View.GONE);
            holder.lblStatus.setText("Cleared");
        } else if (data.get(position).getStatus() == 2){
            holder.markBill.setVisibility(View.GONE);
            holder.lblStatus.setText("Cancel");

        }


        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.markBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                fragmentRequest.serverRequest.markStatusPending(fragmentRequest, data.get(position).getReqId());

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentRequest.getActivity());
                builder.setMessage("Are you sure this Request is paid?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionData.classRequest = data.get(position);
                fragmentRequest.homeActivity.changeFragment("view_requested_item");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblItemId, lblRequestDate, lblUserName, lblRequestTotal, lblStatus, lblRemarks;
        View divider;
        CardView cardClick, markBill;

        public VHolder(View itemView) {
            super(itemView);
            lblItemId = itemView.findViewById(R.id.lblItemId);
            lblRequestDate = itemView.findViewById(R.id.lblRequestDate);
            lblRequestTotal = itemView.findViewById(R.id.lblRequestTotal);
            lblUserName = itemView.findViewById(R.id.lblUserName);
            lblStatus = itemView.findViewById(R.id.lblStatus);
            lblRemarks = itemView.findViewById(R.id.lblRemarks);
            divider = itemView.findViewById(R.id.divider);
            cardClick = itemView.findViewById(R.id.cardClick);
            markBill = itemView.findViewById(R.id.markBill);
        }
    }

}
