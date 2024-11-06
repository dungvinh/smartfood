package com.sel.smartfood.ui.slashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.ui.login.LoginActivity;
import com.sel.smartfood.ui.admin.AdminActivity;
import com.sel.smartfood.ui.main.MainActivity;
import com.sel.smartfood.utils.NetworkStatus;
import com.sel.smartfood.viewmodel.SigninViewModel;

public class SlashScreenAcvitity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SigninViewModel signinViewModel = new ViewModelProvider(this).get(SigninViewModel.class);

        if (!NetworkStatus.isNetworkConnected(this)){
            AlertDialog dialog = new AlertDialog.Builder(this)
                                    .setTitle("Lỗi")
                                    .setMessage("Không thể kết nối với internet")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialog1, which) ->  finish()).create();
            dialog.show();
            return;
        }

        signinViewModel.checkLoggedInState();
        // moi lan live data isloggedin thay doi, goi ham ben trong
        signinViewModel.IsLoggedIn().observe(this, isLoggedIn ->{
            Handler handler = new Handler();
            Intent intent;
            // Handler: chuyển activities
            if (isLoggedIn == null || !isLoggedIn){
                intent = new Intent(this, LoginActivity.class);
            }
            else{
                if (signinViewModel.isAdmin()){
                    intent = new Intent(this, AdminActivity.class);
                }
                else{
                    intent = new Intent(this, MainActivity.class);
                }
            }
            handler.post(() -> {
                startActivity(intent);
                finish();
            });
        });

    }
}
