package Fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.HomeActivity;
import com.example.dushyanth.quikdeladmin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import Adapters.RequestViewAdapter;
import Models.ClassRequest;
import OtherClasses.SessionData;
import ServerLink.ServerRequest;
import ServerLink.ServerUsers;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentRequest extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public HomeActivity homeActivity;
    private View iView;
    public String fromDate, toDate, status = "0", userId = "";
    public ServerRequest serverRequest = new ServerRequest();
    public int check = 0;
    public Spinner spinnerStatus, spinnerCustomerSort;
    private EditText txtFromDate, txtToDate;
    public RequestViewAdapter requestViewAdapter;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    public ArrayList<ClassRequest> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems;
    private int totalItems;
    private int scrollOutItems;
    public int page = 0;
    private SimpleDateFormat sdf;
    private Calendar fromCalendar, toCalendar;

    public FragmentRequest(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_request, container, false);

        spinnerStatus = iView.findViewById(R.id.spinnerStatus);
        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);
        txtFromDate = iView.findViewById(R.id.txtFromDate);
        txtToDate = iView.findViewById(R.id.txtToDate);
        spinnerCustomerSort = iView.findViewById(R.id.spinnerCustomerSort);

        loadStatusArray();
        spinnerStatus.setSelection(1);

        ServerUsers serverUsers = new ServerUsers();
        serverUsers.getAllUserSpinner(this);

        list = new ArrayList<>();
        requestViewAdapter = new RequestViewAdapter(list, this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcyView.setAdapter(requestViewAdapter);
        rcyView.setLayoutManager(manager);

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        fromCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();

         /* setting a custom from date */
        fromCalendar.set(Calendar.YEAR, 2018);
        fromCalendar.set(Calendar.MONTH, 11);
        fromCalendar.set(Calendar.DAY_OF_MONTH, 10);

        txtFromDate.setText(sdf.format(fromCalendar.getTime()));
        toCalendar.add(Calendar.DATE, 1);
        txtToDate.setText(sdf.format(toCalendar.getTime()));

        fromDate = txtFromDate.getText().toString();
        toDate = txtToDate.getText().toString();

        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                fromCalendar.set(Calendar.YEAR, year);
                fromCalendar.set(Calendar.MONTH, monthOfYear);
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fromDate = sdf.format(fromCalendar.getTime());
                txtFromDate.setText(fromDate);
                onRefresh();
            }

        };

        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                toCalendar.set(Calendar.YEAR, year);
                toCalendar.set(Calendar.MONTH, monthOfYear);
                toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                toDate = sdf.format(toCalendar.getTime());
                txtToDate.setText(toDate);

                onRefresh();
            }

        };

        txtFromDate.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), dateFrom,
                        fromCalendar.get(Calendar.YEAR),
                        fromCalendar.get(Calendar.MONTH),
                        fromCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), dateTo,
                        toCalendar.get(Calendar.YEAR),
                        toCalendar.get(Calendar.MONTH),
                        toCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

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

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));
                if (++check > 2) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("Pending"))
                        status = "0";
                    else if (selectedItemText.equals("Cleared"))
                        status = "1";
                    else if (selectedItemText.equals("Cancelled"))
                        status = "2";
                    else
                        status = "";

                    onRefresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCustomerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                if (++check > 2) {

                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("All")) {
                        userId = "";
                    } else
                        userId = String.valueOf(Integer.parseInt(selectedItemText.split("-")[0].trim()));

                    onRefresh();
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
        list.clear();
        page = 0;
        isScrolling = false;
        fetchData();
    }

    private void fetchData() {

        serverRequest.getBillData(this);
    }

    public void PopulateList() {
        requestViewAdapter.notifyDataSetChanged();
        swp2Rfsh.setRefreshing(false);
        page += 1;
    }

    private void loadStatusArray() {
        String[] sortArray = new String[]{"All", "Pending", "Cleared", "Cancelled"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerStatus.setAdapter(spinnerArrayAdapter);
    }



}
