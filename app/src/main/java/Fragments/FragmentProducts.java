package Fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.HomeActivity;
import com.example.dushyanth.quikdeladmin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.ProductViewAdapter;
import Models.ClassProducts;
import ServerLink.ServerProduct;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentProducts extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public HomeActivity homeActivity;
    View iView;
    private RecyclerView rcyView;
    private ProductViewAdapter productViewAdapter;
    public ArrayList<ClassProducts> list;
    private LinearLayoutManager manager;
    private Spinner spinnerSelectType;
    private SwipeRefreshLayout swp2Rfsh;
    public String inputStr = "", type = "";
    public int page = 0;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, check = 0;
    public ServerProduct serverProduct = new ServerProduct();

    public FragmentProducts(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_products, container, false);

        spinnerSelectType = iView.findViewById(R.id.spinnerSelectType);
        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        loadTypetArray();

        list = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        productViewAdapter = new ProductViewAdapter(list, this);
        rcyView.setAdapter(productViewAdapter);
        rcyView.setLayoutManager(manager);

        rcyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;

                    fetchData();

                }

            }
        });

        spinnerSelectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("Detergent"))
                        type = "det";
                    else if (selectedItemText.equals("Stationary"))
                        type = "sta";
                    else
                        type = "";

                    ((TextView) spinnerSelectType.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swp2Rfsh.setOnRefreshListener(this);
        swp2Rfsh.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        onRefresh();

        return iView;

    }

    @Override
    public void onRefresh() {
        isScrolling = false;
        page = 0;
        list.clear();
        fetchData();
    }

    public void SearchData() {
        page = 0;
        list.clear();
        fetchData();

    }

    public void fetchData() {
        homeActivity.showProgress();
        productViewAdapter.notifyDataSetChanged();
        serverProduct.getProductData(this);

    }

    public void PopulateList() {
        productViewAdapter.notifyDataSetChanged();
        swp2Rfsh.setRefreshing(false);
        page += 1;
    }

    private void loadTypetArray() {
        String[] sortArray = new String[]{"All", "Detergent", "Stationary"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerSelectType.setAdapter(spinnerArrayAdapter);
    }

}
