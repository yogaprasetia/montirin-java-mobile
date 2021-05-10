package com.montirin.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montirin.app.R;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    private TextView usrNama;
    private TextView usrEmail;
    private TextView usrPhone;
    private TextView usrJasa;
    private TextView usrDesc;
    private EditText inputPhone, inputNama, inputJasa, inputDesc;
    Button savePhone, saveNama, saveJasa,saveDesc;
    RelativeLayout btnLogout;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Dialog customDialogPhone, customDialogNama, customDialogJasa, customDialogDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        usrNama = findViewById(R.id.usr_namauser);
        usrEmail = findViewById(R.id.usr_emailuser);
        usrPhone = findViewById(R.id.usr_phone);
        TextView edtPhone = findViewById(R.id.edit_phone);
        usrDesc = findViewById(R.id.usr_desc);
        TextView edtDesc = findViewById(R.id.edit_desc);
        usrJasa = findViewById(R.id.usr_jasa);
        TextView edtJasa = findViewById(R.id.edit_jasa);
        TextView edtNamaLengkap = findViewById(R.id.edit_namalengkap);
        btnLogout = findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getNama = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String getEmail = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                String getPhone = Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString();
                String getJasa = Objects.requireNonNull(dataSnapshot.child("jasa").getValue()).toString();
                String getDesc = Objects.requireNonNull(dataSnapshot.child("desc").getValue()).toString();

                usrNama.setText(getNama);
                usrEmail.setText(getEmail);
                usrPhone.setText(getPhone);
                usrJasa.setText(getJasa);
                usrDesc.setText(getDesc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        NamaCustomDialog();
        PhoneCustomDialog();
        JasaCustomDialog();
        DescCustomDialog();
        edtPhone.setOnClickListener(openDialogPhone);
        edtNamaLengkap.setOnClickListener(openDialogNama);
        edtJasa.setOnClickListener(openDialogJasa);
        edtDesc.setOnClickListener(openDialogDesc);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    private void NamaCustomDialog () {
        customDialogNama = new Dialog(AccountActivity.this);
        customDialogNama.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialogNama.setContentView(R.layout.edit_nama_dialog);
        customDialogNama.setCancelable(true);

        inputNama = customDialogNama.findViewById(R.id.input_namalengkap);
        saveNama = customDialogNama.findViewById(R.id.save_namalengkap);
        saveNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newnama = inputNama.getText().toString();
                if (TextUtils.isEmpty(newnama)) {
                    inputNama.setError("Tidak Boleh Kosong");
                } else {
                    inputNama.setError(null);
                    String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DatabaseReference refChild = mDatabase.child("Users");
                    refChild.child(uid).child("name").setValue(newnama)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    customDialogNama.dismiss();
                                }
                            });
                }

            }
        });
    }

    private void PhoneCustomDialog () {
        customDialogPhone = new Dialog(AccountActivity.this);
        customDialogPhone.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialogPhone.setContentView(R.layout.edit_phone_dialog);
        customDialogPhone.setCancelable(true);

        inputPhone = customDialogPhone.findViewById(R.id.input_phone);
        savePhone = customDialogPhone.findViewById(R.id.save_phone);
        savePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newPhone = inputPhone.getText().toString();
                final String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                final DatabaseReference refChild = mDatabase.child("Users");
                refChild.orderByChild("phone").equalTo(newPhone).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    inputPhone.setError("Sudah digunakan");
                                } else {
                                    refChild.child(uid).child("phone").setValue(newPhone).addOnSuccessListener(
                                            new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                    customDialogPhone.dismiss();
                                                }
                                            }
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
            }
        });
    }

    private void JasaCustomDialog () {
        customDialogJasa = new Dialog(AccountActivity.this);
        customDialogJasa.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialogJasa.setContentView(R.layout.edit_jasa_dialog);
        customDialogJasa.setCancelable(true);

        inputJasa = customDialogJasa.findViewById(R.id.input_jasa);
        saveJasa = customDialogJasa.findViewById(R.id.save_jasa);
        saveJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newjasa = inputJasa.getText().toString();
                if (TextUtils.isEmpty(newjasa)) {
                    inputJasa.setError("Tidak Boleh Kosong");
                } else {
                    inputJasa.setError(null);
                    String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DatabaseReference refChild = mDatabase.child("Users");
                    refChild.child(uid).child("jasa").setValue(newjasa)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    customDialogJasa.dismiss();
                                }
                            });
                }

            }
        });
    }

    private void DescCustomDialog () {
        customDialogDesc = new Dialog(AccountActivity.this);
        customDialogDesc.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialogDesc.setContentView(R.layout.edit_desc_dialog);
        customDialogDesc.setCancelable(true);

        inputDesc = customDialogDesc.findViewById(R.id.input_desc);
        saveDesc = customDialogDesc.findViewById(R.id.save_desc);
        saveDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newdesc = inputDesc.getText().toString();
                if (TextUtils.isEmpty(newdesc)) {
                    inputDesc.setError("Tidak Boleh Kosong");
                } else {
                    inputDesc.setError(null);
                    String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DatabaseReference refChild = mDatabase.child("Users");
                    refChild.child(uid).child("desc").setValue(newdesc)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    customDialogDesc.dismiss();
                                }
                            });
                }

            }
        });
    }

    private View.OnClickListener openDialogNama = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customDialogNama.show();
        }
    };

    private View.OnClickListener openDialogPhone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customDialogPhone.show();
        }
    };

    private View.OnClickListener openDialogJasa = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customDialogJasa.show();
        }
    };

    private View.OnClickListener openDialogDesc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customDialogDesc.show();
        }
    };

}