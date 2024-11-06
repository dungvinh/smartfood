package com.sel.smartfood.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.ResetPasswordViewModel;

public class ResetPasswordFragment extends Fragment {

    private TextView emailTv;
    private Button sendEmailBtn;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);

        ResetPasswordViewModel viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        viewModel.isEmailValid().observe(getViewLifecycleOwner(), isEmailValid ->{
            if (isEmailValid == null || !isEmailValid){
                sendEmailBtn.setEnabled(false);
                emailTv.setError("Email không hợp lệ");
            }
            else{
                sendEmailBtn.setEnabled(true);
            }
        });
        emailTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.emailChanged(emailTv.getText().toString());
            }
        });
        sendEmailBtn.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            viewModel.resetPassword(emailTv.getText().toString());
            sendEmailBtn.setEnabled(false);
        });

        viewModel.isSentMail().observe(getViewLifecycleOwner(), isSent -> {
            if (isSent == null || isSent.getData() == null || (isSent.getData() != null &&!isSent.getData())){
                Toast.makeText(getActivity(), "Gửi mail thất bại. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                emailTv.setText("");
            }
            else{
                Toast.makeText(getActivity(), "Đã gửi thành công.", Toast.LENGTH_LONG).show();
                getParentFragmentManager()
                        .beginTransaction()

                        .replace(R.id.fcv_login_activity, new SigninFragment())
                        .addToBackStack(null)
                        .commit();
            }
            loading.setVisibility(View.GONE);
        });
    }
    private void findWidgets(View view){
        emailTv = view.findViewById(R.id.et_email);
        sendEmailBtn = view.findViewById(R.id.btn_send_email);
        loading = view.findViewById(R.id.pb_loading_send_mail);
    }
}
