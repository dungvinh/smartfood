package com.sel.smartfood.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.viewmodel.AdminViewModel;
import com.sel.smartfood.viewmodel.ShopViewModel;
import com.squareup.picasso.Picasso;

public class FragmentUpdateProduct extends Fragment {

    private ImageView productImageIv;
    private EditText productNameTv;
    private EditText productDescriptionTv;
    private EditText productPriceTv;
    private Button updateProductBtn;
    private Product savedProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_update_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findWidgets(view);
        AdminViewModel adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        ShopViewModel shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        Bundle bundle = getArguments();
        if (bundle != null){
            Picasso.get().load(bundle.getString("product_image")).into(productImageIv);
            productNameTv.setText(bundle.getString("product_name"));
            productDescriptionTv.setText(bundle.getString("product_description"));
            productPriceTv.setText(String.valueOf(bundle.getInt("product_price")));
        }
        else{
            return;
        }
        TextWatcher afterChanged = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adminViewModel.changeData();
            }
        };

        productNameTv.addTextChangedListener(afterChanged);
        productPriceTv.addTextChangedListener(afterChanged);
        productDescriptionTv.addTextChangedListener(afterChanged);

        updateProductBtn.setOnClickListener(v -> {
            Product product = new Product();
            product.setId(bundle.getInt("product_id"));
            product.setName(productNameTv.getText().toString());
            product.setPrice(Integer.parseInt(productPriceTv.getText().toString()));
            product.setDescription(productDescriptionTv.getText().toString());
            savedProduct = product;
            adminViewModel.updateProduct(product);
        });
        adminViewModel.isUpdateButtonEnabled().observe(getViewLifecycleOwner(), enabled -> {
            if (enabled != null && enabled){
                updateProductBtn.setEnabled(true);
            }
        });
        adminViewModel.isUpdateSucess().observe(getViewLifecycleOwner(), success -> {
            if (success != null){
                boolean flag = success.isHandled();
                Boolean value = success.getData();
                if (value != null && value) {
                    shopViewModel.changeProduct(savedProduct);
                    Toast.makeText(requireActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
                else if (flag){
                    Toast.makeText(requireActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            updateProductBtn.setEnabled(false);
        });

    }

    private void findWidgets(View view){
        productImageIv = view.findViewById(R.id.iv_update_product_image);
        productNameTv = view.findViewById(R.id.et_update_product_name);
        productDescriptionTv = view.findViewById(R.id.et_update_product_description);
        productPriceTv = view.findViewById(R.id.et_update_product_price);
        updateProductBtn = view.findViewById(R.id.btn_update_product);
    }
}
