package com.montirin.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montirin.app.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText editNama, editPhone, editDesc, editJasa;
    Spinner editKategori;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        editNama = findViewById(R.id.reg_name);
        editPhone = findViewById(R.id.reg_phoneNo);
        editJasa = findViewById(R.id.reg_jasa);
        editDesc = findViewById(R.id.reg_desc_jasa);
        editKategori = findViewById(R.id.reg_kategori);
        Button btnRegister = findViewById(R.id.reg_btn);

        btnRegister.setOnClickListener(register);
    }

    private View.OnClickListener register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            latitude = HomeActivity.latitude;
            longitude = HomeActivity.longitude;
            final String nama = editNama.getText().toString();
            final String phone = editPhone.getText().toString();
            final String jasa = editJasa.getText().toString();
            final String desc = editDesc.getText().toString();
            final String kategori = editKategori.getSelectedItem().toString();
            if (!validateForm(nama,phone,desc,jasa)){
                return;
            }
            final String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            final String email = mAuth.getCurrentUser().getEmail();
            mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DatabaseReference newUser = mDatabase.child("Users").child(uid);
                    newUser.child("name").setValue(nama);
                    newUser.child("phone").setValue(phone);
                    newUser.child("email").setValue(email);
                    newUser.child("jasa").setValue(jasa);
                    newUser.child("desc").setValue(desc);
                    newUser.child("kategori").setValue(kategori);
                    newUser.child("latitude").setValue(latitude);
                    newUser.child("longitude").setValue(longitude);
                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    RegisterActivity.this.finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    private boolean validateForm(String nama, String phone,String jasa, String desc){
        boolean result = true;
        if (TextUtils.isEmpty(nama)){
            editNama.setError("Harus Diisi");
            result = false;
        }else {
            editNama.setError(null);
        }

        if (TextUtils.isEmpty(desc)){
            editDesc.setError("Harus Diisi");
            result = false;
        }else {
            editDesc.setError(null);
        }

        if (TextUtils.isEmpty(jasa)){
            editJasa.setError("Harus Diisi");
            result = false;
        }else {
            editJasa.setError(null);
        }

        if (TextUtils.isEmpty(phone)){
            editPhone.setError("Harus Diisi");
            result = false;
        }else {
            editPhone.setError(null);
        }

        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}