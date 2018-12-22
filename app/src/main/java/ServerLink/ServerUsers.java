package ServerLink;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dushyanth.quikdeladmin.HomeActivity;
import com.example.dushyanth.quikdeladmin.LoginActivity;
import com.example.dushyanth.quikdeladmin.ProfileActivity;
import com.example.dushyanth.quikdeladmin.R;
import com.example.dushyanth.quikdeladmin.UserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Fragments.FragmentProducts;
import Fragments.FragmentRequest;
import Fragments.FragmentUsers;
import Models.ClassProducts;
import Models.ClassRequest;
import Models.ClassUsers;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;

/**
 * Created by Dushyanth on 2018-12-13.
 */

public class ServerUsers {

    public ServerUsers() {

    }

    public void checkLogin(final LoginActivity loginActivity) {
        String requestName = "validateUser";
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!loginActivity.pDialog.isShowing()) loginActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (loginActivity.pDialog.isShowing()) loginActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                if (loginActivity.usernameType.equals("phone"))
                                    ShowDialog.showToast(loginActivity.getApplicationContext(), "Please check your Phone/Password correctly");
                                else
                                    ShowDialog.showToast(loginActivity.getApplicationContext(), "Please check your Email/Password correctly");

                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(loginActivity.getApplicationContext(), "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);
                                SessionData.userId = explrObject.get("user_id").toString();
                                SessionData.userName = explrObject.get("name").toString();
                                SessionData.designation = explrObject.get("designation").toString();

                                loginActivity.GotoHome();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loginActivity.pDialog.isShowing()) loginActivity.pDialog.dismiss();
                ShowDialog.showToast(loginActivity.getApplicationContext(), "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", loginActivity.username);
                params.put("password", loginActivity.password);
                params.put("usernameType", loginActivity.usernameType);
                params.put("userType", "adm"); // it can be either adm or stf, doesn't really matter
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(loginActivity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(loginActivity).addToRequestQueue(stringRequest);

    }

    public void createUser(final UserActivity userActivity) {
        String requestName = "createUser";
        final Context activity = userActivity.getApplicationContext();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!userActivity.pDialog.isShowing()) userActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (userActivity.pDialog.isShowing()) userActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Creating the account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Account created successfully");
                                userActivity.onBackPressed();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userActivity.pDialog.isShowing()) userActivity.pDialog.dismiss();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassUsers classUsers = userActivity.classUsers;

                String encodedImage = Base64.encodeToString(classUsers.getPicture(), Base64.DEFAULT);

                params.put("name", classUsers.getName());
                params.put("email", classUsers.getEmail());
                params.put("phone", String.valueOf(classUsers.getPhone()));
                params.put("nic", classUsers.getNic());
                params.put("gender", classUsers.getGender());
                params.put("default_picture", classUsers.getDefaultPicture());
                params.put("dob", classUsers.getDob());
                params.put("address", classUsers.getAddress());
                params.put("picture", encodedImage);
                params.put("password", classUsers.getPassword());
                params.put("designation", "stf");

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void getUserDetails(final HomeActivity homeActivity) {
        String requestName = "getUserDetails";
        StringRequest stringRequest;
        final Context activity = homeActivity.getApplicationContext();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "Cannot load Profile Data");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Cannot load Profile Data");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);

                                byte[] bytes = Base64.decode(explrObject.get("photo_byte").toString(), Base64.DEFAULT);

                                homeActivity.classUsers.setName(explrObject.get("name").toString());
                                homeActivity.classUsers.setEmail(explrObject.get("email").toString());
                                homeActivity.classUsers.setPicture(bytes);

                                homeActivity.populateNavProfile(homeActivity.rootView);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Cannot load Profile Data");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SessionData.userId);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void getUserDetails(final ProfileActivity profileActivity) {
        String requestName = "getUserDetails";
        StringRequest stringRequest;
        final Context activity = profileActivity.getApplicationContext();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!profileActivity.pDialog.isShowing()) profileActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (profileActivity.pDialog.isShowing()) profileActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "Cannot load Profile Data");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Cannot load Profile Data");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);

                                byte[] bytes = Base64.decode(explrObject.get("photo_byte").toString(), Base64.DEFAULT);

                                profileActivity.classUsers.setName(explrObject.get("name").toString());
                                profileActivity.classUsers.setEmail(explrObject.get("email").toString());
                                profileActivity.classUsers.setPhone(Long.parseLong(explrObject.get("phone").toString()));
                                profileActivity.classUsers.setNic(explrObject.get("nic").toString());
                                profileActivity.classUsers.setAddress(explrObject.get("address").toString());
                                profileActivity.classUsers.setPassword(explrObject.get("password").toString());
                                profileActivity.classUsers.setId(Integer.parseInt(explrObject.get("user_id").toString()));
                                profileActivity.classUsers.setDateJoined(explrObject.get("date_joined").toString());
                                profileActivity.classUsers.setDob(explrObject.get("dob").toString());
                                profileActivity.classUsers.setGender(explrObject.get("gender").toString());
                                profileActivity.classUsers.setDefaultPicture(explrObject.get("default_picture").toString());
                                profileActivity.classUsers.setPicture(bytes);
                                profileActivity.classUsers.setPicturePath(explrObject.get("picture").toString());

                                profileActivity.populateFields();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (profileActivity.pDialog.isShowing()) profileActivity.pDialog.dismiss();
                ShowDialog.showToast(activity, "Cannot load Profile Data");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SessionData.userId);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void getUserDetails(final UserActivity userActivity) {
        String requestName = "getUserDetails";
        StringRequest stringRequest;
        final Context activity = userActivity.getApplicationContext();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!userActivity.pDialog.isShowing()) userActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (userActivity.pDialog.isShowing()) userActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "Cannot load Profile Data");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Cannot load Profile Data");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);

                                byte[] bytes = Base64.decode(explrObject.get("photo_byte").toString(), Base64.DEFAULT);

                                userActivity.classUsers.setName(explrObject.get("name").toString());
                                userActivity.classUsers.setEmail(explrObject.get("email").toString());
                                userActivity.classUsers.setPhone(Long.parseLong(explrObject.get("phone").toString()));
                                userActivity.classUsers.setNic(explrObject.get("nic").toString());
                                userActivity.classUsers.setAddress(explrObject.get("address").toString());
                                userActivity.classUsers.setPassword(explrObject.get("password").toString());
                                userActivity.classUsers.setId(Integer.parseInt(explrObject.get("user_id").toString()));
                                userActivity.classUsers.setDateJoined(explrObject.get("date_joined").toString());
                                userActivity.classUsers.setDob(explrObject.get("dob").toString());
                                userActivity.classUsers.setGender(explrObject.get("gender").toString());
                                userActivity.classUsers.setDefaultPicture(explrObject.get("default_picture").toString());
                                userActivity.classUsers.setPicture(bytes);
                                userActivity.classUsers.setPicturePath(explrObject.get("picture").toString());

                                userActivity.populateFields();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (userActivity.pDialog.isShowing()) userActivity.pDialog.dismiss();
                ShowDialog.showToast(activity, "Cannot load Profile Data");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(SessionData.selectedUserId));
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void updateUser(final ProfileActivity profileActivity) {
        String requestName = "updateUser";
        final Context activity = profileActivity.getApplicationContext();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!profileActivity.pDialog.isShowing()) profileActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (profileActivity.pDialog.isShowing()) profileActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Updating your Account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Your Account Updated Successfully");
                                getUserDetails(profileActivity);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (profileActivity.pDialog.isShowing()) profileActivity.pDialog.dismiss();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassUsers classUsers = profileActivity.classUsers;

                String encodedImage = Base64.encodeToString(classUsers.getPicture(), Base64.DEFAULT);

                params.put("user_id", SessionData.userId);
                params.put("name", classUsers.getName());
                params.put("email", classUsers.getEmail());
                params.put("phone", String.valueOf(classUsers.getPhone()));
                params.put("nic", classUsers.getNic());
                params.put("gender", classUsers.getGender());
                params.put("default_picture", classUsers.getDefaultPicture());
                params.put("dob", classUsers.getDob());
                params.put("address", classUsers.getAddress());
                params.put("picture", encodedImage);
                params.put("password", classUsers.getPassword());
                params.put("designation", "adm");
                params.put("picture_path", classUsers.getPicturePath());

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void updateUser(final UserActivity profileActivity) {
        String requestName = "updateUser";
        final Context activity = profileActivity.getApplicationContext();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!profileActivity.pDialog.isShowing()) profileActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (profileActivity.pDialog.isShowing()) profileActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Updating your Account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Your Account Updated Successfully");
                                getUserDetails(profileActivity);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (profileActivity.pDialog.isShowing()) profileActivity.pDialog.dismiss();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassUsers classUsers = profileActivity.classUsers;

                String encodedImage = Base64.encodeToString(classUsers.getPicture(), Base64.DEFAULT);

                params.put("user_id", String.valueOf(SessionData.selectedUserId));
                params.put("name", classUsers.getName());
                params.put("email", classUsers.getEmail());
                params.put("phone", String.valueOf(classUsers.getPhone()));
                params.put("nic", classUsers.getNic());
                params.put("gender", classUsers.getGender());
                params.put("default_picture", classUsers.getDefaultPicture());
                params.put("dob", classUsers.getDob());
                params.put("address", classUsers.getAddress());
                params.put("picture", encodedImage);
                params.put("password", classUsers.getPassword());
                params.put("designation", "adm");
                params.put("picture_path", classUsers.getPicturePath());

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void getAllUserSpinner(final FragmentRequest fragmentRequest) {
        final String requestName = "getAllUserSpinner";
        StringRequest stringRequest;
        final Activity activity = fragmentRequest.getActivity();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentRequest.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " " + requestName);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "No Records Found");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                ArrayList<String> tempArr = new ArrayList<>();
                                tempArr.add("All");


                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    ClassUsers classUsers = new ClassUsers();
                                    classUsers.setId(Integer.parseInt(explrObject.get("user_id").toString()));
                                    classUsers.setName(explrObject.get("name").toString());

                                    tempArr.add(classUsers.getId() + " - " + classUsers.getName());

                                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, tempArr);

                                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                                    fragmentRequest.spinnerCustomerSort.setAdapter(spinnerArrayAdapter);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentRequest.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void getAllUser(final FragmentUsers fragmentUsers) {
        final String requestName = "getAllUser";
        StringRequest stringRequest;
        final Activity activity = fragmentUsers.getActivity();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        fragmentUsers.homeActivity.showProgress();
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentUsers.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " " + requestName);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "No Records Found");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    byte[] bytes = Base64.decode(explrObject.get("photo_byte").toString(), Base64.DEFAULT);

                                    ClassUsers classUsers = new ClassUsers();
                                    classUsers.setId(Integer.parseInt(explrObject.get("user_id").toString()));
                                    classUsers.setName(explrObject.get("name").toString());
                                    classUsers.setEmail(explrObject.get("email").toString());
                                    classUsers.setPhone(Long.parseLong(explrObject.get("phone").toString()));
                                    classUsers.setStatus(explrObject.get("status").toString());
                                    classUsers.setPicture(bytes);

                                    fragmentUsers.list.add(classUsers);
                                }

                                fragmentUsers.PopulateList();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentUsers.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("search_input", fragmentUsers.inputStr);
                params.put("designation", fragmentUsers.designation);
                params.put("gender", fragmentUsers.gender);
                params.put("limit", String.valueOf(fragmentUsers.page));
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void enableDisableCustomer(final FragmentUsers fragmentUsers, final int cusId, final ImageView imgEnableDisable) {
        final String requestName = "enableDisableCustomer";
        final Activity activity = fragmentUsers.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        fragmentUsers.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentUsers.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " " + requestName);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Blocking/Unblocking Account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);

                                String currentStatus = explrObject.get("currentStatus").toString();
                                if ("active".equals(currentStatus)) {
                                    ShowDialog.showToast(activity, explrObject.get("name").toString() + " was Unblocked Successfully");
                                    imgEnableDisable.setImageDrawable(fragmentUsers.getResources().getDrawable(R.drawable.eye_enable));
                                } else {
                                    ShowDialog.showToast(activity, explrObject.get("name").toString() + " was Blocked Successfully");
                                    imgEnableDisable.setImageDrawable(fragmentUsers.getResources().getDrawable(R.drawable.eye_disable));
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentUsers.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(cusId));

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void deleteUser(final FragmentUsers fragmentUsers, final int cusId, final String cusName) {
        String requestName = "deleteUser";
        final Activity activity = fragmentUsers.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        fragmentUsers.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentUsers.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Deleting " + cusName + " Account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, cusName + " has been Deleted Successfully");
                                fragmentUsers.onRefresh();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentUsers.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(cusId));

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

}
