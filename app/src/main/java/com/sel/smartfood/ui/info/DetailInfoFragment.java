package com.sel.smartfood.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.R;
import com.sel.smartfood.ui.login.LoginActivity;
import com.sel.smartfood.viewmodel.InfoViewModel;

public class DetailInfoFragment extends Fragment{

    private TextView infoNameTv;
    private TextView infoPhoneTv;
    private TextView infoEmailTv;
    private RelativeLayout logoutRL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_detail_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        InfoViewModel viewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user->{
            if (user != null){
                infoNameTv.setText(user.getFullname());
                infoPhoneTv.setText(user.getPhone());
                infoEmailTv.setText(user.getEmail());
            }
            else{
                Toast.makeText(getActivity(), "Đã có lỗi xảy ra. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            }
        });

        logoutRL.setOnClickListener(v -> {
            viewModel.logOut();
            Handler handler = new Handler();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            handler.post(()-> startActivity(intent));
        });
    }
    void findWidgets(View view){
        infoNameTv = view.findViewById(R.id.tv_userinfo_name);
        infoPhoneTv = view.findViewById(R.id.tv_userinfo_phone);
        infoEmailTv = view.findViewById(R.id.tv_userinfo_email);
        logoutRL = view.findViewById(R.id.rl_log_out);
    }
}
