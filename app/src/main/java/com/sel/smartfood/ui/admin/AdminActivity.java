package com.sel.smartfood.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.sel.smartfood.R;
import com.sel.smartfood.ui.login.LoginActivity;
import com.sel.smartfood.viewmodel.AdminViewModel;
import com.sel.smartfood.viewmodel.ShopViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView adminNameTv;
    private TextView adminEmailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AdminViewModel adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        ShopViewModel viewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        viewModel.getCategories();
        viewModel.getProducts();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_admin_view);
        adminNameTv = navigationView.getHeaderView(0).findViewById(R.id.tv_admin_name);
        adminEmailTv = navigationView.getHeaderView(0).findViewById(R.id.tv_admin_email);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.admin_nav_shop,
                R.id.admin_nav_statistics,
                R.id.admin_log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            adminViewModel.logout();
            startActivity(intent);
            finish();
            return true;
        });

        adminViewModel.getUser();

        adminViewModel.getUserInfo().observe(this, user->{
            if (user != null){
                adminNameTv.setText(user.getFullname());
                adminEmailTv.setText(user.getEmail());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_admin_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}