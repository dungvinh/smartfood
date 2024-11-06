package com.sel.smartfood.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.TransactionViewModel;

public class ChoosePaymentServiceFragment extends Fragment {
    private ListView paymentServiceLv;
    private TransactionViewModel paymentServiceViewModel;
    private PaymentServiceAdapter paymentServiceAdapter;
    private Button nextPaymentBtn;
    private int lastPosition;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_payment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        paymentServiceViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);

        paymentServiceAdapter = new PaymentServiceAdapter(getContext());
        paymentServiceLv.setAdapter(paymentServiceAdapter);


        // bắt sự kiện khi click
        paymentServiceLv.setOnItemClickListener((adapter, v, position, arg)->{
            lastPosition = position;
            paymentServiceViewModel.chooseOnePaymentService(position);
        });

        paymentServiceViewModel.getPaymentService().observe(getViewLifecycleOwner(), paymentServiceList->{
            if (paymentServiceList == null){
                Toast.makeText(getContext(), R.string.payment_service_choice_error, Toast.LENGTH_SHORT).show();
                return;
            }
            paymentServiceAdapter.changeDataset(paymentServiceList);
        });
        paymentServiceViewModel.getIsNextButtonEnabled().observe(getViewLifecycleOwner(), isButtonEnabled->{
            if (isButtonEnabled == null)
                return;
            nextPaymentBtn.setEnabled(isButtonEnabled);
        });


        nextPaymentBtn.setOnClickListener(v -> {
            paymentServiceViewModel.setPaymentService(lastPosition);
            NavHostFragment.findNavController(this).navigate(R.id.action_choosePaymentServiceFragment_to_inputMoneyFragment);
        });
    }

    private void findWidgets(View view){
        paymentServiceLv = view.findViewById(R.id.lv_payment_service);
        nextPaymentBtn = view.findViewById(R.id.btn_next_payment);
    }
}
