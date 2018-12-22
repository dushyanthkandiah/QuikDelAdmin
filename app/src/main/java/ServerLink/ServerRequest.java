package ServerLink;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dushyanth.quikdeladmin.ProfileActivity;
import com.example.dushyanth.quikdeladmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Fragments.FragmentRequest;
import Fragments.FragmentRequestedItems;
import Models.ClassRequest;
import Models.ClassRequestList;
import Models.ClassUsers;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;

/**
 * Created by Dushyanth on 2018-12-09.
 */

public class ServerRequest {

    public ServerRequest() {
    }


    public void getBillData(final FragmentRequest fragmentRequest) {
        final String requestName = "getBillData";
        StringRequest stringRequest;
        final Activity activity = fragmentRequest.getActivity();
        String url = SessionData.serverUrl + "billing.php?func=" + requestName;

        fragmentRequest.homeActivity.showProgress();
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
                                fragmentRequest.requestViewAdapter.notifyDataSetChanged();
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    fragmentRequest.list.add(new ClassRequest(
                                            Integer.parseInt(explrObject.get("req_id").toString()),
                                            Integer.parseInt(explrObject.get("user_id").toString()),
                                            Integer.parseInt(explrObject.get("status").toString()),
                                            explrObject.get("name").toString(),
                                            explrObject.get("req_date").toString(),
                                            explrObject.get("remarks").toString(),
                                            Double.parseDouble(explrObject.get("total").toString())

                                    ));

                                }

                                fragmentRequest.PopulateList();

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
                params.put("user_id", fragmentRequest.userId);
                params.put("from_date", fragmentRequest.fromDate);
                params.put("to_date", fragmentRequest.toDate);
                params.put("status", fragmentRequest.status);
                params.put("limit", String.valueOf(fragmentRequest.page));

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

    public void markStatusPending(final FragmentRequest fragmentRequest, final int reqId) {
        final String requestName = "markStatusPending";
        final Context activity = fragmentRequest.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "billing.php?func=" + requestName;

        fragmentRequest.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentRequest.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " "  + requestName);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Clearing the Request");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Request Cleared Successfully");
                                fragmentRequest.onRefresh();
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

                params.put("req_id", String.valueOf(reqId));

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

    public void getBilledItemData(final FragmentRequestedItems fragmentRequestedItems) {
        String requestName = "getBilledItemData";
        StringRequest stringRequest;
        final Activity activity = fragmentRequestedItems.getActivity();
        String url = SessionData.serverUrl + "billing.php?func=" + requestName;



        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentRequestedItems.fragmentRequest.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "No Records Found");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                fragmentRequestedItems.list.clear();
                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    fragmentRequestedItems.list.add(new ClassRequestList(
                                            Integer.parseInt(explrObject.get("req_id").toString()),
                                            Integer.parseInt(explrObject.get("prd_id").toString()),
                                            explrObject.get("name").toString(),
                                            Double.parseDouble(explrObject.get("qty").toString()),
                                            Double.parseDouble(explrObject.get("sub_total").toString())

                                    ));
                                }

                                fragmentRequestedItems.PopulateList();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentRequestedItems.fragmentRequest.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("req_id", String.valueOf(fragmentRequestedItems.requestId));
                params.put("limit", String.valueOf(fragmentRequestedItems.page));

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
