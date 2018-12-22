package com.example.dushyanth.quikdeladmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Dialogs.DialogAboutDeveloper;
import Dialogs.DialogProducts;
import Fragments.*;
import Models.ClassUsers;
import OtherClasses.SessionData;
import OtherClasses.Utils;
import ServerLink.ServerUsers;
import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    private GifImageView progressBar;
    private Toolbar toolbar;
    public ClassUsers classUsers = new ClassUsers();
    public ServerUsers serverUsers = new ServerUsers();
    public View rootView;
    DrawerLayout drawer;
    public EditText txtSearch;
    public TextView lblFragmentTitle;
    private FragmentProducts fragmentProducts;
    private FragmentRequest fragmentRequest;
    private FragmentRequestedItems fragmentRequestedItems;
    private FragmentUsers fragmentUsers;
    public ImageView imgAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        txtSearch = findViewById(R.id.txtSearch);
        lblFragmentTitle = findViewById(R.id.lblFragmentTitle);
        imgAddBtn = findViewById(R.id.imgAddBtn);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchRecord(txtSearch.getText().toString().trim().replace("'", "''"));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        rootView = navigationView.getHeaderView(0);

        serverUsers.getUserDetails(this);

        changeFragment("view_request"); // def frag
    }

    public void populateNavProfile(View rootView) {
        ImageView userImageView;
        TextView lblUserName, lblUserEmail;
        LinearLayout layoutUserProfile;

        layoutUserProfile = rootView.findViewById(R.id.layoutUserProfile);
        userImageView = rootView.findViewById(R.id.userImageView);
        lblUserName = rootView.findViewById(R.id.lblUserName);
        lblUserEmail = rootView.findViewById(R.id.lblUserEmail);

        lblUserEmail.setText(classUsers.getEmail());
        lblUserName.setText(classUsers.getName());
        userImageView.setImageBitmap(Utils.getImage(
                classUsers.getPicture()
        ));

        layoutUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivityForResult(intent, 105);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

    }

    public void SearchRecord(String inputStr) {

        if (SessionData.currentFragment.equals("product")) {
            fragmentProducts.inputStr = inputStr;
            fragmentProducts.SearchData();
        } else if (SessionData.currentFragment.equals("users")) {
            fragmentUsers.inputStr = inputStr;
            fragmentUsers.SearchData();
        }
    }

    public void changeFragment(String frag) {
        lblFragmentTitle.setVisibility(View.GONE);
        txtSearch.setVisibility(View.GONE);
        imgAddBtn.setVisibility(View.GONE);

        if (frag.equals("product")) {
            imgAddBtn.setVisibility(View.VISIBLE);
            txtSearch.setVisibility(View.VISIBLE);
            fragmentProducts = new FragmentProducts(this);
            SessionData.currentFragment = "product";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentProducts, "fragment");
            ft.commit();
        } else if (frag.equals("view_request")) {
            lblFragmentTitle.setVisibility(View.VISIBLE);
            fragmentRequest = new FragmentRequest(this);
            navigationView.getMenu().getItem(0).setChecked(true);
            SessionData.currentFragment = "view_request";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentRequest, "fragment");
            ft.commit();
        } else if (frag.equals("view_requested_item")) {
            fragmentRequestedItems = new FragmentRequestedItems(fragmentRequest);
            lblFragmentTitle.setVisibility(View.VISIBLE);
            fragmentRequest = new FragmentRequest(this);
            SessionData.currentFragment = "view_requested_item";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_leave);
            ft.replace(R.id.content_frame, fragmentRequestedItems, "fragment");
            ft.commit();
        } else if (frag.equals("users")) {
            if ("adm".equals(SessionData.designation))
                imgAddBtn.setVisibility(View.VISIBLE);
            txtSearch.setVisibility(View.VISIBLE);
            fragmentUsers = new FragmentUsers(this);
            SessionData.currentFragment = "users";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentUsers, "fragment");
            ft.commit();
        }
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (SessionData.currentFragment.equals("view_requested_item")) {
            changeFragment("view_request");
        }  else {
            if (SessionData.currentFragment.equals("product") || SessionData.currentFragment.equals("users"))
                changeFragment("view_request");
            else {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            new AlertDialog.Builder(HomeActivity.this).setTitle("Log Out Confirmation")
                    .setMessage("Are you sure you want to Sign Out?")
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    logOut();
                                    dialog.dismiss();

                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("userId", "");
        editor.putString("userName", "");
        editor.putString("designation", "");
        editor.commit();

        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_requests) {
            changeFragment("view_request");
        } else if (id == R.id.nav_products) {
            changeFragment("product");
        } else if (id == R.id.nav_users) {
            changeFragment("users");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void imgAddBtn(View view) {

        if (SessionData.currentFragment.equals("product")) {
            DialogProducts dialogProducts = DialogProducts.newInstance(fragmentProducts, null, "add");
            dialogProducts.show(fragmentProducts.getFragmentManager(), "dialog");
        }else if (SessionData.currentFragment.equals("users")) {
            Intent intent = new Intent(fragmentUsers.homeActivity, UserActivity.class);
            intent.putExtra("clickedType", "add");
            fragmentUsers.homeActivity.startActivityForResult(intent, 106);
            fragmentUsers.homeActivity.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }

    }

}
