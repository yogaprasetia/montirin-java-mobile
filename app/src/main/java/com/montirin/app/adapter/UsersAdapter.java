package com.montirin.app.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.montirin.app.R;
import com.montirin.app.activity.HomeActivity;
import com.montirin.app.model.UsersItem;

public class UsersAdapter extends FirebaseRecyclerAdapter<UsersItem, UsersAdapter.myViewHolder> {

    private static final int EARTH_RADIUS = 6371;

    public UsersAdapter(FirebaseRecyclerOptions<UsersItem> options) {
        super(options);
    }
    @SuppressLint("DefaultLocale")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i, @NonNull final UsersItem user) {

        Double lat,longi,lat2,longi2;
        lat = HomeActivity.latitude;
        longi = HomeActivity.longitude;
        lat2 = user.getLatitude();
        longi2 = user.getLongitude();
        double dLat  = Math.toRadians((lat2 - lat));
        double dLong = Math.toRadians((longi2 - longi));

        lat = Math.toRadians(lat);
        lat2   = Math.toRadians(lat2);

        double a = haversin(dLat) + Math.cos(lat) * Math.cos(lat2) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Double jaraku = EARTH_RADIUS * c;

        myViewHolder.name.setText(user.getName());
        myViewHolder.jasa.setText(user.getJasa());
        myViewHolder.desc.setText(user.getDesc());

        myViewHolder.jarak.setText(String.format("Jarak Darimu: %.1f Kilometer", jaraku));
        myViewHolder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://maps.google.com/maps?q=loc:" + user.getLatitude() + "," + user.getLongitude();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                view.getContext().startActivity(i);
            }
        });
        myViewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="+"+62"+user.getPhone() + "&text=Halo, Saya Pelanggan dari Montirin ingin Pesan Layanan "+ user.getJasa()+ " Anda";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                view.getContext().startActivity(i);
            }
        });
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_service,parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView name,jasa,desc,jarak;
        Button phone,loc;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_user);
            jasa = itemView.findViewById(R.id.judul_jasa);
            desc = itemView.findViewById(R.id.desc_jasa);
            phone = itemView.findViewById(R.id.no_hp);
            loc = itemView.findViewById(R.id.lokasi);
            jarak = itemView.findViewById(R.id.jarak);
        }
    }
}
