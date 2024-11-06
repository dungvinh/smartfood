package com.sel.smartfood.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sel.smartfood.R;
import com.sel.smartfood.ui.shop.ProductDetailFragmentArgs;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class UpdateProductFragment extends Fragment {

    // TODO: Rename and change types of parameters
    Toolbar toolbarProductDetails;
    ImageView ivProductDetails;

    EditText etName, etPrice, etDescriptions;
    View view;

    int productId;
    String productName;
    int productPrice;
    float productPreparationTime;
    int productRatingscore;
    String productImg;
    String productDescription;
    final int MAX_PRODUCT_NUMBER = 10;


    public UpdateProductFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_shop, container, false);
        Maps(view);
        GetInformation();
        return view;
    }

    private void GetInformation() {
        if (getArguments() != null) {

            ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
            productId = args.getProductId();
            productName = args.getProductName();
            productPrice = args.getProductPrice();
            productImg = args.getProductImage();

            etName.setText(productName);
            etDescriptions.setText(productDescription);

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            etPrice.setText("Giá : " + decimalFormat.format(productPrice) + " Đồng ");

            // hình ảnh sản phẩm
            String imgUrl = args.getProductImage();
            Picasso.get().load(productImg)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.error)
                    .into(ivProductDetails);

        }
    }

    private void Maps(View view) {
        toolbarProductDetails = (Toolbar)view.findViewById(R.id.ad_toolbar_product_details);
        ivProductDetails = (ImageView) view.findViewById(R.id.iv_ad_product_details);
        etName = (EditText) view.findViewById(R.id.et_ad_name_product_details);
        etPrice = (EditText) view.findViewById(R.id.et_ad_price_product_details);
        etDescriptions = (EditText) view.findViewById(R.id.et_ad_description_product_details);
    }
}
