package ServerLink;

import android.app.Activity;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Dialogs.DialogProducts;
import Fragments.FragmentProducts;
import Fragments.FragmentUsers;
import Models.ClassProducts;
import OtherClasses.OtherShortcuts;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;

/**
 * Created by Dushyanth on 2018-12-09.
 */

public class ServerProduct {

    public ServerProduct() {
    }

    public void addProduct(final DialogProducts dialogProducts) {
        String requestName = "addProduct";
        StringRequest stringRequest;
        final Activity activity = dialogProducts.getActivity();
        String url = SessionData.serverUrl + "products.php?func=" + requestName;

        dialogProducts.progress.setVisibility(View.VISIBLE);

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogProducts.progress.setVisibility(View.INVISIBLE);
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "Error while Adding the Product");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Product added successfully");
                                dialogProducts.fragmentProducts.onRefresh();
                                OtherShortcuts.hideKeyboard(activity);
                                dialogProducts.dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProducts.progress.setVisibility(View.INVISIBLE);
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassProducts classProducts = dialogProducts.classProducts;

                params.put("name", classProducts.getName());
                params.put("details", classProducts.getDetails() + "");
                params.put("type", classProducts.getType());
                params.put("quantity", String.valueOf(classProducts.getQuantity()));
                params.put("price", String.valueOf(classProducts.getPrice()));

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

    public void updateProduct(final DialogProducts dialogProducts) {
        String requestName = "updateProduct";
        StringRequest stringRequest;
        final Activity activity = dialogProducts.getActivity();
        String url = SessionData.serverUrl + "products.php?func=" + requestName;

        dialogProducts.progress.setVisibility(View.VISIBLE);

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogProducts.progress.setVisibility(View.INVISIBLE);
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "Error while Updating the Product");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Product Updated successfully");
                                dialogProducts.fragmentProducts.onRefresh();
                                OtherShortcuts.hideKeyboard(activity);
                                dialogProducts.dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogProducts.progress.setVisibility(View.INVISIBLE);
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassProducts classProducts = dialogProducts.classProducts;

                params.put("prd_id", String.valueOf(classProducts.getId()));
                params.put("name", classProducts.getName());
                params.put("details", classProducts.getDetails() + "");
                params.put("type", classProducts.getType());
                params.put("quantity", String.valueOf(classProducts.getQuantity()));
                params.put("price", String.valueOf(classProducts.getPrice()));

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

    public void getProductData(final FragmentProducts fragmentProducts) {
        String requestName = "getProductData";
        StringRequest stringRequest;
        final Activity activity = fragmentProducts.getActivity();
        String url = SessionData.serverUrl + "products.php?func=" + requestName;


        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentProducts.homeActivity.hideProgress();
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

                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    fragmentProducts.list.add(new ClassProducts(
                                            Integer.parseInt(explrObject.get("prd_id").toString()),
                                            explrObject.get("name").toString(),
                                            explrObject.get("type").toString(),
                                            explrObject.get("details").toString(),
                                            Double.parseDouble(explrObject.get("quantity").toString()),
                                            Double.parseDouble(explrObject.get("price").toString())
                                    ));
                                }

                                fragmentProducts.PopulateList();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentProducts.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("search_input", fragmentProducts.inputStr);
                params.put("limit", String.valueOf(fragmentProducts.page));
                params.put("type", fragmentProducts.type);
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

    public void deleteProduct(final FragmentProducts fragmentProducts, final int prdId, final String prdName) {
        final String requestName = "deleteProduct";
        final Activity activity = fragmentProducts.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "products.php?func=" + requestName;

        fragmentProducts.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentProducts.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " "+ requestName);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Deleting " + prdName );
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, prdName + " has been Deleted Successfully");
                                fragmentProducts.onRefresh();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentProducts.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("prd_id", String.valueOf(prdId));

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
