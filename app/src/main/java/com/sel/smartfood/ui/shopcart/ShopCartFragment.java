package com.sel.smartfood.ui.shopcart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sel.smartfood.R;
import com.sel.smartfood.data.model.ShopCartModel;
import com.sel.smartfood.ui.shop.ShopFragment;
import com.sel.smartfood.viewmodel.PaymentShopcartViewModel;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCartFragment extends Fragment {

    ListView lvShopcart;
    TextView tvNotification;
    static TextView tvTotalPrice;
    Button btnPayment;
    Button btnContinue;
    Toolbar     toolbar_shopcart;
    ShopCartAdapter shopCartAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private static Long amountOfPrice;
    PaymentShopcartViewModel viewModel;


    public ShopCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         viewModel = new ViewModelProvider(getActivity()).get(PaymentShopcartViewModel.class);
        Maps(view);
        CheckData();
        EventUtil();
        CatchOnItemListView();
        EventButton();

    }

    private void EventButton() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.nav_shop);
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopFragment.orderProductList.size() > 0){
                    // lấy tài khoản user
                    viewModel.getBalance();
                    // chuyển màn hình để người dùng nhập vào
                    displayBottomSheet(view);
                } else{
                    Toast.makeText(getActivity(), "Giỏ hàng của bạn chưa có sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayBottomSheet(View view){
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_choose_payment_type, null, false);
        bottomSheetView.findViewById(R.id.ll_online_payment_type).setOnClickListener(v->{
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaction_to_choosePaymentServiceFragment);
        });
        bottomSheetView.findViewById(R.id.ll_payment_account_type).setOnClickListener(v->{

            if (!viewModel.isEnoughBalance(amountOfPrice)){
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setCancelable(false)
                        .setTitle("Lỗi")
                        .setMessage("Số dư không đủ")
                        .setNegativeButton("OK", (dialog1, which) -> {
                        }).create();
                dialog.show();
                bottomSheetDialog.dismiss();
                return;
            }

            viewModel.updateBalance(amountOfPrice);

            viewModel.getIsUpdatedSuccessful().observe(getViewLifecycleOwner(), isSuccessful -> {
                if (isSuccessful != null){
                    boolean first = !isSuccessful.isHandled();
                    Boolean flag = isSuccessful.getData();
                    if (flag != null && flag){
                        viewModel.saveTransHistories(amountOfPrice);
                        for(int i = 0; i < ShopFragment.orderProductList.size(); ++i){
                            ShopCartModel orderProduct = ShopFragment.orderProductList.get(i);

                            viewModel.saveOrderHistory(orderProduct.productName,
                                    orderProduct.productPrice * orderProduct.productNumbers,
                                    orderProduct.productNumbers,
                                    orderProduct.getProductImage());
                        }
                        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                .setCancelable(false)
                                .setTitle("Thông báo")
                                .setMessage("Đặt hàng thành công")
                                .setPositiveButton("OK", (dialog1, which) -> {
                                    ShopFragment.orderProductList.clear();
                                    shopCartAdapter.notifyDataSetChanged();
                                    EventUtil();
                                    tvNotification.setVisibility(View.VISIBLE);
                                    shopCartAdapter = new ShopCartAdapter(requireActivity(), ShopFragment.orderProductList);

                                }).create();
                        dialog.show();
                        bottomSheetDialog.dismiss();
                    }
                    else if (first){
                        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                .setCancelable(false)
                                .setTitle("Lỗi")
                                .setMessage("Đặt hàng thất bại")
                                .setNegativeButton("OK", (dialog1, which) -> {
//                                    NavHostFragment.findNavController(this).navigate(R.id.action_inputMoneyFragment_to_nav_transaction);
                                }).create();
                        dialog.show();
                        bottomSheetDialog.dismiss();
                    }
                }
            });

        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
    private void navigateToPaymentServiceChoice(View v){
        NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaction_to_choosePaymentServiceFragment);
    }

    private void CatchOnItemListView() {
        lvShopcart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xác nhận xóa sản phẩm");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ShopFragment.orderProductList.size() <= 0){
                            tvNotification.setVisibility(View.VISIBLE);
                        }else{
                            ShopFragment.orderProductList.remove(position);
                            shopCartAdapter.notifyDataSetChanged();

                            // cập nhật tiền
                            EventUtil();

                            if (ShopFragment.orderProductList.size() <= 0){
                                tvNotification.setVisibility(View.VISIBLE);
                            }
                            else{
                                tvNotification.setVisibility(View.INVISIBLE);
                                shopCartAdapter.notifyDataSetChanged();

                                // cập nhật tiền
                                EventUtil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shopCartAdapter.notifyDataSetChanged();
                        EventUtil();
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    public static void EventUtil() {
        long totalPrice = 0;
        for (int i = 0; i < ShopFragment.orderProductList.size(); ++i){
            totalPrice += ShopFragment.orderProductList.get(i).getProductPrice();
        }
        amountOfPrice = totalPrice;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTotalPrice.setText("Giá: " + decimalFormat.format(totalPrice) + " Đ");
    }

    private void CheckData() {
        if (ShopFragment.orderProductList.size() <= 0) {
            // update Adapter
            shopCartAdapter.notifyDataSetChanged();
            tvNotification.setVisibility(View.VISIBLE);
            lvShopcart.setVisibility(View.INVISIBLE);
        }else{
            // update Adapter
            shopCartAdapter.notifyDataSetChanged();
            tvNotification.setVisibility(View.INVISIBLE);
            lvShopcart.setVisibility(View.VISIBLE);

        }
    }

    private void Maps(View view) {
        lvShopcart = (ListView) view.findViewById(R.id.lv_shopcart);
        tvNotification = (TextView) view.findViewById(R.id.tv_shopcart_notification);
        tvTotalPrice = (TextView) view.findViewById(R.id.tv_total_price);
        btnPayment = (Button) view.findViewById(R.id.btn_payment);
        btnContinue = (Button) view.findViewById(R.id.btn_shopcart_continue);
        shopCartAdapter = new ShopCartAdapter( this.getActivity(), ShopFragment.orderProductList);
        lvShopcart.setAdapter(shopCartAdapter);



    }
}
