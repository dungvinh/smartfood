package com.sel.smartfood.ui.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.InfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private TextView switchInfoTv;
    private TextView infoNameTv;
    private Toolbar toolbar;
    private boolean isHistory = true;
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchInfoTv = view.findViewById(R.id.tv_switch_info);
        infoNameTv = view.findViewById(R.id.tv_info_name);
        toolbar = view.findViewById(R.id.info_toolbar);
        toolbar.setTitle("Tài khoản");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        InfoViewModel viewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        viewModel.getUserInfo();

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null){
                infoNameTv.setText(user.getFullname());
            }
        });

        toolbar.setNavigationOnClickListener(v -> switchFragment());
        switchInfoTv.setOnClickListener(v -> switchFragment());
    }
    private void switchFragment(){
        String content = isHistory ? "Xem chi tiết\nlịch sử" : "Xem chi tiết\nthông tin";
        Fragment fragment = isHistory ? new DetailInfoFragment() : new HistoryFragment();

        switchInfoTv.setText(content);
        getChildFragmentManager()
        .beginTransaction()
        .replace(R.id.fcv_container_info, fragment)
        .addToBackStack(null)
        .commit();

        isHistory = !isHistory;
    }
}
