package com.example.dushyanth.quikdeladmin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import OtherClasses.OtherShortcuts;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Validation;
import ServerLink.Database;
import ServerLink.MySingleton;
import ServerLink.ServerUsers;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailPhone, txtPassword;
    private Button btnLogin;
    private Validation vd;
    public String usernameType = "phone", username = "", password = "";
    public ProgressDialog pDialog;
    private ServerUsers serverUsers = new ServerUsers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new checkTrial().execute();



    }

    private void checkRememberUser() {
        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);

        if (myPrefs.contains("userId")) {

            SessionData.userId = myPrefs.getString("userId", "0");
            SessionData.userName = myPrefs.getString("userName", "No Name");
            SessionData.designation = myPrefs.getString("designation", "adm");

            if (!SessionData.userId.equals("")) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                finish();
            } else
                loadLayout();

        } else
            loadLayout();
    }

    private void loadLayout() {
        txtEmailPhone = findViewById(R.id.txtEmailPhone);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        vd = new Validation(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in...");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {

        String[] fieldName = {"Email/Phone", "Password"};
        EditText[] field = {txtEmailPhone, txtPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            boolean flag = checkPhone();

            if (flag)
                usernameType = "phone";
            else
                usernameType = "email";

            checkDb();
        }

    }

    private boolean checkPhone() {
        String input = txtEmailPhone.getText().toString();
        boolean flag = false;

        if (vd.PhoneCheckWithoutToast(input)) {
            if (Character.isDigit(input.charAt(input.length() - 1)) && !input.contains("@"))
                flag = true;
        }

        return flag;
    }

    private void checkDb() {
        password = txtPassword.getText().toString().replace("'", "''");
        username = txtEmailPhone.getText().toString().trim().replace("'", "''");

        if (usernameType.equals("phone")) {
            username = username.substring(username.length() - 9, username.length());
        }

        serverUsers.checkLogin(this);
    }

    public void GotoHome() {
        ShowDialog.showToast(getApplicationContext(), "Hello " + SessionData.userName);
        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("userId", SessionData.userId);
        editor.putString("userName", SessionData.userName);
        editor.putString("designation", SessionData.designation);
        editor.commit();

        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();

    }

    private class checkTrial extends AsyncTask<Void, Void, Void> {

        private String result = "", flag = "";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Initializing Quik Del...");
            if (!pDialog.isShowing()) pDialog.show();

            OtherShortcuts.hideKeyboard(LoginActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Database db = new Database();
            String sql = "select `key` from test where id = 1";


            if (db.getConn() != null) {

                ResultSet rs = db.executeQuery(sql);

                try {
                    flag = "nodata";
                    while (rs.next()) {
                        flag = "success";
                        result = rs.getString("key");

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    flag = "nodata";
                }

            } else {
                flag = "failed";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pDialog.isShowing()) pDialog.hide();

            if (flag.equals("success")) {

                if (result.equals("allow")) {
                    checkRememberUser();
                } else {
                    finish();
                }

            } else if (flag.equals("nodata")) {

            } else if (flag.equals("failed")) {
                ShowDialog.showToast(getApplicationContext(), "Connection not Available");

            }

        }

    }

//    public void checkTrial() {
//        final String requestName = "getAllUser";
//        StringRequest stringRequest;
//        final Context activity = getApplicationContext();
//        String url = SessionData.blockUrl + "test.php";
//
//        pDialog = new ProgressDialog(this);
//        pDialog.setCancelable(false);
//        pDialog.setMessage("Initializing Quik Del...");
//        if (!pDialog.isShowing()) pDialog.show();
//
//        stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (pDialog.isShowing()) pDialog.hide();
//                        try {
//                            System.out.println(response + " " + requestName);
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("response");
//                            JSONObject explrObject = jsonArray.getJSONObject(0);
//
//                            if (explrObject.get("queryResult").toString().equals("nodata")) {
//                                ShowDialog.showToast(activity, "No Records Found");
//                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
//                                ShowDialog.showToast(activity, "Connection not Available!");
//                            } else if (explrObject.get("queryResult").toString().equals("success")) {
//
//
//                                explrObject = jsonArray.getJSONObject(1);
//
//                                String key = explrObject.get("key").toString();
//
//                                if (key.equals("allow")) {
//                                    checkRememberUser();
//                                } else {
//                                    finish();
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (pDialog.isShowing()) pDialog.hide();
//                ShowDialog.showToast(activity, "Connection not Available");
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                return params;
//            }
//        };
//
//        stringRequest.setTag(requestName);
//
//        try {
//            MySingleton.getmInstance(this).cancelRequest(requestName);
//        } catch (Exception e) {
//        }
//
//        MySingleton.getmInstance(this).addToRequestQueue(stringRequest);
//    }

}
