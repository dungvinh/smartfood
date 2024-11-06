package com.sel.smartfood.ui.transaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.TransactionViewModel;

public class InputMoneyFragment extends Fragment {
    private EditText inputMoneyEt;
    private Button transBtn;
    private Long amountOfMoney;
    private TextView typeTransTv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_input_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);

        TransactionViewModel viewModel = new ViewModelProvider(getActivity()).get(TransactionViewModel.class);

        typeTransTv.setText(!viewModel.isWithdraw() ? "cần nạp" : "cần rút");

        inputMoneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.amountOfMoneyChange(inputMoneyEt.getText().toString());
            }
        });

        viewModel.getIsTransactionButtonEnabled().observe(getViewLifecycleOwner(), isEnabled -> {
            if (isEnabled == null || !isEnabled){
                inputMoneyEt.setError("Số tiền phải là số nguyên dương");
            }
            else{
                transBtn.setEnabled(true);
            }
        });

        transBtn.setOnClickListener(v -> {
            amountOfMoney = Long.parseLong(inputMoneyEt.getText().toString());

            if (viewModel.isWithdraw()){
                if (!viewModel.isEnoughBalance(amountOfMoney)){
                    AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                                                        .setTitle("Lỗi")
                                                        .setMessage("Số dư tài khoản không đủ")
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", (dialog12, which) -> {})
                                                        .create();
                    viewModel.setTransactionButtonEnabled(false);
                    dialog.show();
                    return;
                }
            }

            viewModel.updateBalance(amountOfMoney);
            transBtn.setEnabled(false);
        });
        viewModel.getIsUpdatedSuccessful().observe(getViewLifecycleOwner(), isSuccessful -> {
            if (isSuccessful != null){
                boolean first = !isSuccessful.isHandled();
                Boolean flag = isSuccessful.getData();
                if (flag != null && flag){
                    viewModel.saveTransHistories(amountOfMoney);
                    AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                        .setCancelable(false)
                                        .setTitle("Thông báo")
                                        .setMessage("Giao dịch thành công")
                                        .setPositiveButton("OK", (dialog1, which) -> {
                                            viewModel.setTransactionButtonEnabled(false);
                                            NavHostFragment.findNavController(this).navigate(R.id.action_inputMoneyFragment_to_nav_transaction);
                                        }).create();
                    dialog.show();
                }
                else if (first){
                    AlertDialog dialog = new AlertDialog.Builder(requireContext())
                            .setCancelable(false)
                            .setTitle("Lỗi")
                            .setMessage("Giao dịch thất bại")
                            .setNegativeButton("OK", (dialog1, which) -> {
                                NavHostFragment.findNavController(this).navigate(R.id.action_inputMoneyFragment_to_nav_transaction);
                            }).create();
                    dialog.show();
                }
            }
        });
    }

    private void findWidgets(View view){
        inputMoneyEt = view.findViewById(R.id.et_input_money);
        transBtn = view.findViewById(R.id.btn_transaction);
        typeTransTv = view.findViewById(R.id.tv_type_trans);
    }
}
