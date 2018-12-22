package Adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.HomeActivity;
import com.example.dushyanth.quikdeladmin.ProfileActivity;
import com.example.dushyanth.quikdeladmin.R;
import com.example.dushyanth.quikdeladmin.UserActivity;

import java.util.ArrayList;

import Fragments.FragmentUsers;
import Models.ClassUsers;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class UserViewAdapter extends RecyclerView.Adapter<UserViewAdapter.VHolder> {
    private ArrayList<ClassUsers> data;
    private FragmentUsers fragmentUsers;

    public UserViewAdapter(ArrayList<ClassUsers> data, FragmentUsers fragmentUsers) {
        this.data = data;
        this.fragmentUsers = fragmentUsers;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentUsers.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_users, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        holder.lblName.setText(data.get(position).getName());
        holder.lblEmail.setText(data.get(position).getEmail() + "");
        holder.lblPhone.setText(data.get(position).getPhone() + "");
        holder.imgProfilePicture.setImageBitmap(Utils.getImage(data.get(position).getPicture()));

        if (data.get(position).getStatus().equals("deactive")) {
            holder.imgEnableDisable.setImageDrawable(fragmentUsers.getResources().getDrawable(R.drawable.eye_disable));
        } else
            holder.imgEnableDisable.setImageDrawable(fragmentUsers.getResources().getDrawable(R.drawable.eye_enable));

        holder.imgEnableDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentUsers.serverUsers.enableDisableCustomer(fragmentUsers, data.get(position).getId(), holder.imgEnableDisable);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(fragmentUsers.getActivity()).setTitle("Account Deletion Confirmation")
                        .setMessage("Are you sure you wan't to delete "+ data.get(position).getName() +"'s account?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        fragmentUsers.serverUsers.deleteUser(fragmentUsers, data.get(position).getId(), data.get(position).getName());
                                        dialog.dismiss();

                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionData.selectedUserId = data.get(position).getId();

                Intent intent = new Intent(fragmentUsers.homeActivity, UserActivity.class);
                intent.putExtra("clickedType", "update");
                fragmentUsers.homeActivity.startActivityForResult(intent, 106);
                fragmentUsers.homeActivity.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);

            }
        });

        if (!"adm".equals(SessionData.designation)){
                holder.imgDelete.setVisibility(View.INVISIBLE);
                holder.imgEnableDisable.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblName, lblEmail, lblGender, lblPhone;
        CardView cardClick;
        ImageView imgProfilePicture, imgEnableDisable, imgDelete;

        public VHolder(View itemView) {
            super(itemView);
            lblName = itemView.findViewById(R.id.lblName);
            lblEmail = itemView.findViewById(R.id.lblEmail);
            lblGender = itemView.findViewById(R.id.lblGender);
            lblPhone = itemView.findViewById(R.id.lblPhone);
            imgProfilePicture = itemView.findViewById(R.id.imgProfilePicture);
            imgEnableDisable = itemView.findViewById(R.id.imgEnableDisable);
            cardClick = itemView.findViewById(R.id.cardClick);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

}
