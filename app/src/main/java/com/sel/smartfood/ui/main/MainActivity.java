package com.sel.smartfood.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.ShopViewModel;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShopViewModel shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCategories();
        shopViewModel.getProducts();

        BottomNavigationView bnvTab = findViewById(R.id.bnv_tab);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bnvTab, navController);


    }

}
