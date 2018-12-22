package com.example.dushyanth.quikdeladmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import Models.ClassUsers;
import OtherClasses.RotateBitmap;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerUsers;

@SuppressLint({"NewApi", "ResourceType"})
public class ProfileActivity extends AppCompatActivity {

    public EditText txtName, txtEmail, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword;
    public TextView lblUniqueId, lblDateJoined, lblDob, lblGender;
    public ImageView imgProfilePicture;
    private Button btnDelete, btnUpdate;
    private Validation vd;
    public ClassUsers classUsers;
    ServerUsers serverUsers = new ServerUsers();
    private byte[] imgByte;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtNic = findViewById(R.id.txtNic);
        txtAddress = findViewById(R.id.txtAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        lblUniqueId = findViewById(R.id.lblUniqueId);
        lblDateJoined = findViewById(R.id.lblDateJoined);
        lblDob = findViewById(R.id.lblDob);
        lblGender = findViewById(R.id.lblGender);
        imgProfilePicture = findViewById(R.id.imgProfilePicture);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");

        vd = new Validation(getApplicationContext());
        classUsers = new ClassUsers();

        serverUsers.getUserDetails(this);

        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        String[] fieldName = {"Name", "Phone Number", "NIC", "Address", "Password", "Confirm Password"};
        EditText[] field = {txtName, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            if (vd.PhoneCheck(txtPhone.getText().toString().trim())) {
                if (vd.PasswordCheck(txtPassword)) {
                    if (vd.PasswordCheck(txtPassword, txtConfirmPassword)) {
                        if (txtNic.getText().toString().trim().length() == 10) {
                            if (vd.NicCheck(txtNic.getText().toString().trim().toLowerCase()))
                                getData();
                        } else if (txtNic.getText().toString().trim().length() == 12) {
                            if (vd.NicCheck12(txtNic.getText().toString().trim().toLowerCase()))
                                getData();
                        } else {
                            ShowDialog.showToast(getApplicationContext(), "Incorrect NIC Number");
                        }
                    }
                }
            }
        }
    }


    private void getData() {
        String gender = vd.getGender(txtNic.getText().toString().trim());
        classUsers.setName(txtName.getText().toString().trim().replace("'", "''"));
        classUsers.setEmail(txtEmail.getText().toString().trim().replace("'", "''"));
        classUsers.setPhone(Long.parseLong(txtPhone.getText().toString().trim().replace("'", "''")));
        classUsers.setNic(txtNic.getText().toString().trim());
        classUsers.setGender(gender);

        classUsers.setDob(vd.getBirthDate(txtNic.getText().toString().trim()));
        classUsers.setAddress(txtAddress.getText().toString().trim().replace("'", "''"));
        classUsers.setPassword(txtPassword.getText().toString().replace("'", "''"));

        if (!classUsers.getDefaultPicture().equals("Custom")){ // if profile is changed from default will not go inside
            classUsers.setDefaultPicture(gender);
            InputStream inputstream;
            if (gender.equals("Male"))
                inputstream = getResources().openRawResource(R.drawable.profile_male);
            else
                inputstream = getResources().openRawResource(R.drawable.profile_female);

            byte[] imgByte = new byte[0];

            try {
                imgByte = Utils.getBytes(inputstream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            classUsers.setPicture(imgByte);
        }

//        System.out.println(classUsers.getName() + "\n" + classUsers.getEmail() + "\n" + classUsers.getPhone() + "\n" + classUsers.getNic() + "\n" + classUsers.getGender() + "\n" + classUsers.getDob() + "\n" + classUsers.getAddress() + "\n" + classUsers.getPassword() + "\n" + classUsers.getPicture());

        serverUsers.updateUser(this);

    }

    public void populateFields() {
        txtName.setText(classUsers.getName());
        txtEmail.setText(classUsers.getEmail());
        if (classUsers.getEmail().equals("null"))
            txtEmail.setText("");
        txtPhone.setText(classUsers.getPhone() + "");
        txtNic.setText(classUsers.getNic());
        txtAddress.setText(classUsers.getAddress());
        txtPassword.setText(classUsers.getPassword());
        txtConfirmPassword.setText(classUsers.getPassword());

        lblUniqueId.setText(classUsers.getId() + "");
        lblDateJoined.setText(classUsers.getDateJoined().substring(0, 10));
        lblDob.setText(classUsers.getDob());
        lblGender.setText(classUsers.getGender());
        imgProfilePicture.setImageBitmap(Utils.getImage(classUsers.getPicture()));
        imgByte = classUsers.getPicture();
    }


    private void startGallery() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
        } else {
            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 1000);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            classUsers.setDefaultPicture("Custom");
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {

                    RotateBitmap rotateBitmap = new RotateBitmap();
                    bitmapImage = rotateBitmap.HandleSamplingAndRotationBitmap(getApplicationContext(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int quality = 20;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    long fileSize = bitmapImage.getAllocationByteCount();
                    System.out.println(fileSize + " file");
                }

                imgByte = Utils.getImageBytes(bitmapImage, quality);
                classUsers.setPicture(imgByte);
                imgProfilePicture.setImageBitmap(Utils.getImage(imgByte));
            }
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finishActivity(105);
        super.onBackPressed();
    }
}
