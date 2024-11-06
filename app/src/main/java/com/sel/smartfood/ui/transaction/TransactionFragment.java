package com.sel.smartfood.ui.transaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sel.smartfood.R;
import com.sel.smartfood.viewmodel.TransactionViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    private CardView withdrawCv;
    private CardView depositCv;
    private TextView balanceTv;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_transaction, container, false);
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        bottomSheetDialog = new BottomSheetDialog(requireContext());

        TransactionViewModel viewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        viewModel.getBalance();

        viewModel.getPaymentAccount().observe(getViewLifecycleOwner(), paymentAccount -> {
            if (paymentAccount != null){
                balanceTv.setText("$ " + String.valueOf(paymentAccount.getBalance()) + " VND");
            }
        });

        withdrawCv.setOnClickListener(v -> {
            if (viewModel.isEmptyBalance()){
                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                                    .setTitle("Lỗi")
                                    .setMessage("Số dư bằng 0")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialog1, which) -> {})
                                    .create();
                dialog.show();
                return;
            }
            viewModel.setWithdraw(true);
            navigateToPaymentServiceChoice(v);
        });
        depositCv.setOnClickListener(v -> {
            viewModel.setWithdraw(false);
            navigateToPaymentServiceChoice(v);
        });
    }
    public void displayBottomSheet(View view){
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_choose_payment_type, null, false);
        bottomSheetView.findViewById(R.id.ll_online_payment_type).setOnClickListener(this::navigateToPaymentServiceChoice);
        bottomSheetView.findViewById(R.id.ll_payment_account_type).setOnClickListener(v->{
            Toast.makeText(requireContext(), "Payment account", Toast.LENGTH_SHORT).show();
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    private void navigateToPaymentServiceChoice(View v){
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaction_to_choosePaymentServiceFragment);
//        bottomSheetDialog.dismiss();
    }

    private void findWidgets(View view){

        depositCv = view.findViewById(R.id.include_deposit);
        balanceTv = view.findViewById(R.id.tv_remaining_balance);
    }
}
