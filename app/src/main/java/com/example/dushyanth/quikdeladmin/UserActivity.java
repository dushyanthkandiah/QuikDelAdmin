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
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerUsers;

@SuppressLint({"NewApi", "ResourceType"})
public class UserActivity extends AppCompatActivity {

    private TextView lblTitle, lblUniqueId, lblDateJoined, lblDob, lblGender;
    private EditText txtName, txtEmail, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword;
    public ImageView imgProfilePicture;
    public String type = "update";
    private byte[] imgByte;
    public ProgressDialog pDialog;
    private Validation vd;
    public ClassUsers classUsers;
    Button btnSave;
    ServerUsers serverUsers = new ServerUsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getStringExtra("clickedType");

        lblTitle = findViewById(R.id.lblTitle);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtNic = findViewById(R.id.txtNic);
        txtAddress = findViewById(R.id.txtAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        lblUniqueId = findViewById(R.id.lblUniqueId);
        lblDateJoined = findViewById(R.id.lblDateJoined);
        lblDob = findViewById(R.id.lblDob);
        lblGender = findViewById(R.id.lblGender);
        btnSave = findViewById(R.id.btnSave);
        imgProfilePicture = findViewById(R.id.imgProfilePicture);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait...");

        vd = new Validation(getApplicationContext());
        classUsers = new ClassUsers();

        if (type.equals("update")) {
            serverUsers.getUserDetails(this);

        } else {
            lblTitle.setText("Create a new User");
        }

        if (!"adm".equals(SessionData.designation)){
            btnSave.setVisibility(View.GONE);
        }

        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });

    }

    public void btnSave(View c) {
        checkValidation();
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

        if ("update".equals(type)) {

            if (!classUsers.getDefaultPicture().equals("Custom")) { // if profile is changed, from default will not go inside
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
            serverUsers.updateUser(this);
        }else {
            if (imgByte == null){
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
                classUsers.setDefaultPicture(gender);
                classUsers.setPicture(imgByte);
            }
            serverUsers.createUser(this);
        }

//        System.out.println(classUsers.getName() + "\n" + classUsers.getEmail() + "\n" + classUsers.getPhone() + "\n" + classUsers.getNic() + "\n" + classUsers.getGender() + "\n" + classUsers.getDob() + "\n" + classUsers.getAddress() + "\n" + classUsers.getPassword() + "\n" + classUsers.getPicture());



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
        finishActivity(106);
        super.onBackPressed();
    }

}
