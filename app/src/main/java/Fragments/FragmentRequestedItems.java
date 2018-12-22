package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.R;

import java.util.ArrayList;

import Adapters.RequestedItemViewAdapter;
import Models.ClassRequestList;
import OtherClasses.SessionData;
import ServerLink.ServerRequest;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentRequestedItems extends Fragment {

    private TextView lblRequestId, lblTotal;
    public FragmentRequest fragmentRequest;
    private View iView;
    public ServerRequest serverRequest;
    private RequestedItemViewAdapter requestedItemViewAdapter;
    private RecyclerView rcyView;
    public ArrayList<ClassRequestList> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems;
    private int totalItems;
    private int scrollOutItems;
    public int page = 0;
    public int requestId = 0;

    public FragmentRequestedItems(FragmentRequest fragmentRequest) {
        this.fragmentRequest = fragmentRequest;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_requested_items, container, false);

        rcyView = iView.findViewById(R.id.rcyView);
        lblRequestId = iView.findViewById(R.id.lblRequestId);
        lblTotal = iView.findViewById(R.id.lblTotal);

        serverRequest = new ServerRequest();

        list = new ArrayList<>();
        requestedItemViewAdapter = new RequestedItemViewAdapter(list, this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcyView.setAdapter(requestedItemViewAdapter);
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
        setFragDetails();
        fetchData();

        return iView;
    }

    public void setFragDetails(){
        requestId = SessionData.classRequest.getReqId();
        lblRequestId.setText("Request ID : " + SessionData.classRequest.getReqId());
        lblTotal.setText("Total : " + SessionData.classRequest.getTotal());
    }

    private void fetchData() {
        isScrolling = false;
        page = 0;


        requestedItemViewAdapter.notifyDataSetChanged();
        serverRequest.getBilledItemData(this);

    }

    public void PopulateList() {
        requestedItemViewAdapter.notifyDataSetChanged();
        page += 1;
    }

}
