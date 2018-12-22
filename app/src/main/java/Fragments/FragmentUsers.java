package Fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

import Adapters.UserViewAdapter;
import Models.ClassUsers;
import ServerLink.ServerUsers;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentUsers extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public HomeActivity homeActivity;
    public ServerUsers serverUsers;
    public String inputStr = "", designation = "cus", gender = "";
    int check = 0;
    private View iView;
    private Spinner spinnerDesignation, spinnerGender;
    private UserViewAdapter userViewAdapter;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    public ArrayList<ClassUsers> list;
    private GridLayoutManager manager;
    private Boolean isScrolling = false;
    public int currentItems, totalItems, scrollOutItems, page = 0;


    public FragmentUsers(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_users, container, false);

        spinnerDesignation = iView.findViewById(R.id.spinnerDesignation);
        spinnerGender = iView.findViewById(R.id.spinnerGender);
        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        serverUsers = new ServerUsers();

        loadDesignationArray();
        loadGenderArray();

        list = new ArrayList<>();
        userViewAdapter = new UserViewAdapter(list, this);
        manager = new GridLayoutManager(getActivity(), 2);
        rcyView.setAdapter(userViewAdapter);
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

        spinnerDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                if (++check > 2) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("Customers"))
                        designation = "cus";
                    else
                        designation = "stf";

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                if (++check > 2) {

                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("All")) {
                        gender = "";
                    } else
                        gender = selectedItemText;

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swp2Rfsh.setOnRefreshListener(this);
        swp2Rfsh.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_orange_dark);
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

    private void fetchData() {
        userViewAdapter.notifyDataSetChanged();
        serverUsers.getAllUser(this);
    }

    public void PopulateList() {
        userViewAdapter.notifyDataSetChanged();
        swp2Rfsh.setRefreshing(false);
        page += 1;
    }

    private void loadDesignationArray() {
        String[] sortArray = new String[]{"Customers", "Staffs"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDesignation.setAdapter(spinnerArrayAdapter);
    }

    private void loadGenderArray() {
        String[] sortArray = new String[]{"All", "Male", "Female"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerGender.setAdapter(spinnerArrayAdapter);
    }

}
