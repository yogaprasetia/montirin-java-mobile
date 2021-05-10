package com.montirin.app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.montirin.app.R;
import com.montirin.app.model.HomeItem;
import com.montirin.app.service.ServiceHpActivity;
import com.montirin.app.service.ServisAcActivity;
import com.montirin.app.service.ServisElektronikActivity;
import com.montirin.app.service.ServisLainnyaActivity;
import com.montirin.app.service.ServisMobilActivity;
import com.montirin.app.service.ServisMotorActivity;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private ArrayList<HomeItem> listdata;

    public HomeAdapter(ArrayList<HomeItem> listdata){
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, final int position) {
        final HomeItem getData = listdata.get(position);
        String titlehome = getData.getTitle();
        String logohome = getData.getImg();
        holder.imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent = new Intent(view.getContext(), ServisMobilActivity.class);
                        break;
                    case 1:
                        intent = new Intent(view.getContext(), ServisMotorActivity.class);
                        break;
                    case 2:
                        intent = new Intent(view.getContext(), ServisElektronikActivity.class);
                        break;
                    case 3:
                        intent = new Intent(view.getContext(), ServisAcActivity.class);
                        break;
                    case 4:
                        intent = new Intent(view.getContext(), ServiceHpActivity.class);
                        break;
                    case 5:
                        intent = new Intent(view.getContext(), ServisLainnyaActivity.class);
                        break;
                }
                view.getContext().startActivity(intent);
            }
        });
        holder.titleHome.setText(titlehome);
        if (logohome.equals("logohome1")){
            holder.imgHome.setImageResource(R.drawable.car);
        }else if (logohome.equals("logohome2")) {
            holder.imgHome.setImageResource(R.drawable.bike);
        }else if (logohome.equals("logohome3")) {
            holder.imgHome.setImageResource(R.drawable.electronics);
        }else if (logohome.equals("logohome4")) {
            holder.imgHome.setImageResource(R.drawable.ac);
        }else if (logohome.equals("logohome5")) {
            holder.imgHome.setImageResource(R.drawable.phone);
        }else if (logohome.equals("logohome6")) {
            holder.imgHome.setImageResource(R.drawable.others);
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class HomeHolder extends RecyclerView.ViewHolder {

        TextView titleHome;
        ImageView imgHome;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);

            titleHome = itemView.findViewById(R.id.txt_home);
            imgHome = itemView.findViewById(R.id.ic_home);
        }
    }
}
