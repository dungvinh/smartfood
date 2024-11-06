package com.sel.smartfood.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.RegisterFormState;
import com.sel.smartfood.viewmodel.RegisterViewModel;
import com.sel.smartfood.data.model.Result;

public class RegisterFragment extends Fragment {
    private EditText fullnameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText repasswordEt;
    private EditText phoneNumberEt;
    private TextView signinhintTv;
    private ProgressBar loadingPb;
    private RegisterViewModel registerViewModel;
    private Button registerBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.layout_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        registerViewModel.getRegisterFormState().observe(getViewLifecycleOwner(), this::handleRegisterFormState);
        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), this::handleRegisterResult);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(fullnameEt.getText().toString(),
                                                      emailEt.getText().toString(),
                                                      phoneNumberEt.getText().toString(),
                                                      passwordEt.getText().toString(),
                                                      repasswordEt.getText().toString());
            }
        };
        fullnameEt.addTextChangedListener(afterTextChangedListener);
        emailEt.addTextChangedListener(afterTextChangedListener);
        phoneNumberEt.addTextChangedListener(afterTextChangedListener);
        passwordEt.addTextChangedListener(afterTextChangedListener);
        repasswordEt.addTextChangedListener(afterTextChangedListener);

        registerBtn.setOnClickListener(v -> {
            registerViewModel.register(emailEt.getText().toString(),
                                        passwordEt.getText().toString(),
                                        fullnameEt.getText().toString(),
                                        phoneNumberEt.getText().toString());

            registerBtn.setEnabled(false);
            loadingPb.setVisibility(View.VISIBLE);
        });
        signinhintTv.setOnClickListener(v -> {
            getParentFragmentManager()
            .beginTransaction()
            .replace(R.id.fcv_login_activity, new SigninFragment())
            .addToBackStack(null)
            .commit();
        });
    }

    private void handleRegisterResult(Result<Boolean> booleanResult) {
        if (booleanResult == null)
            return;
        if (booleanResult instanceof Result.Success){
            Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            loadingPb.setVisibility(View.INVISIBLE);
            getParentFragmentManager()
            .beginTransaction()

            .replace(R.id.fcv_login_activity, new SigninFragment())
            .addToBackStack(null)
            .commit();
        }
        else{
            Toast.makeText(getActivity(), ((Result.Error)booleanResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
            loadingPb.setVisibility(View.INVISIBLE);
        }
    }

    private void handleRegisterFormState(RegisterFormState registerFormState){
        if (registerFormState == null)
            return;

        registerBtn.setEnabled(registerFormState.isDataValid());
        if (registerFormState.getFullnameError() != null){
            fullnameEt.setError(getString(registerFormState.getFullnameError()));
        }
        if (registerFormState.getEmailError() != null){
            emailEt.setError(getString(registerFormState.getEmailError()));
        }
        if (registerFormState.getPhoneNumberError() != null){
            phoneNumberEt.setError(getString(registerFormState.getPhoneNumberError()));
        }
        if (registerFormState.getPasswordError() != null){
            passwordEt.setError(getString(registerFormState.getPasswordError()));
        }
        if (registerFormState.getRepasswordError() != null){
            repasswordEt.setError(getString(registerFormState.getRepasswordError()));
        }
    }

    void findWidgets(View v){
        this.fullnameEt = v.findViewById(R.id.et_new_fullname);
        this.emailEt = v.findViewById(R.id.et_new_email);
        this.phoneNumberEt = v.findViewById(R.id.et_new_phone_number);
        this.passwordEt = v.findViewById(R.id.et_new_password);
        this.repasswordEt = v.findViewById(R.id.et_renew_password);
        this.signinhintTv = v.findViewById(R.id.tv_sign_in_hint);
        this.registerBtn = v.findViewById(R.id.btn_register);
        this.loadingPb  = v.findViewById(R.id.pb_loading_register);
    }
}
