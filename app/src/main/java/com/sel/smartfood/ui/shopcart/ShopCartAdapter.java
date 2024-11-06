package com.sel.smartfood.ui.shopcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sel.smartfood.R;
import com.sel.smartfood.ui.shop.ShopFragment;
import com.sel.smartfood.data.model.ShopCartModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShopCartAdapter extends BaseAdapter {
    Context context;
    ArrayList<ShopCartModel> arrayShopcart;

    public ShopCartAdapter(Context context, ArrayList<ShopCartModel> arrayShopcart) {
        this.context = context;
        this.arrayShopcart = arrayShopcart;
    }

    @Override
    public int getCount() {
        return arrayShopcart.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayShopcart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView tvShopcartName, tvShopcartPrice;
        public ImageView ivShopcartItem;
        public Button btnMinus, btnValues, btnPlus;


    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_shop_cart, null);
            viewHolder.tvShopcartName = (TextView) view.findViewById(R.id.tv_shopcart_name);
            viewHolder.tvShopcartPrice = (TextView) view.findViewById(R.id.tv_shopcart_price);
            viewHolder.ivShopcartItem = (ImageView) view.findViewById(R.id.iv_shopcart_item);
            viewHolder.btnMinus = (Button) view.findViewById(R.id.btn_minus);
            viewHolder.btnValues = (Button) view.findViewById(R.id.btn_value);
            viewHolder.btnPlus = (Button) view.findViewById(R.id.btn_plus);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        ShopCartModel shopcart = (ShopCartModel) getItem(i);
        viewHolder.tvShopcartName.setText(shopcart.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvShopcartPrice.setText(decimalFormat.format(shopcart.getProductPrice())+ " Đ");

        Picasso.get().load(shopcart.getProductImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(viewHolder.ivShopcartItem);
        viewHolder.btnValues.setText(String.valueOf(shopcart.getProductNumbers()));

        // xử lý phần chọn mua tối đa 10 sản phẩm
        int number_order = Integer.parseInt(viewHolder.btnValues.getText().toString());

        if (number_order > 10){
            viewHolder.btnPlus.setVisibility(View.INVISIBLE);
            viewHolder.btnMinus.setVisibility(View.VISIBLE);
        }else if (number_order <= 1){
            viewHolder.btnMinus.setVisibility(View.INVISIBLE);
            viewHolder.btnPlus.setVisibility(View.VISIBLE);
        }else{
            viewHolder.btnPlus.setVisibility(View.VISIBLE);
            viewHolder.btnMinus.setVisibility(View.VISIBLE);
        }

        ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curr_product_numbers = ShopFragment.orderProductList.get(i).getProductNumbers();
                int new_product_numbers = curr_product_numbers + 1;
                int curr_price = ShopFragment.orderProductList.get(i).getProductPrice();

                int new_price = (curr_price * new_product_numbers) / curr_product_numbers;

                // set new_product_numbers
                ShopFragment.orderProductList.get(i).setProductNumbers(new_product_numbers);
                ShopFragment.orderProductList.get(i).setProductPrice(new_price);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.tvShopcartPrice.setText(decimalFormat.format(new_price)+ " Đ");

                // Update: tổng tiền
                com.sel.smartfood.ui.shopcart.ShopCartFragment.EventUtil();

                if (new_product_numbers > 9){
                    finalViewHolder.btnPlus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnMinus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnValues.setText(String.valueOf(new_product_numbers));
                }
                else{
                    finalViewHolder.btnPlus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnMinus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnValues.setText(String.valueOf(new_product_numbers));
                }

            }
        });

        // khi bấm nút cộng thêm sản phẩm
        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curr_product_number = ShopFragment.orderProductList.get(i).getProductNumbers();
                int new_product_numbers = curr_product_number - 1;

                int curr_price = ShopFragment.orderProductList.get(i).getProductPrice();
                int new_price = (curr_price * new_product_numbers) / curr_product_number;

                // set new_product_numbers
                ShopFragment.orderProductList.get(i).setProductNumbers(new_product_numbers);
                ShopFragment.orderProductList.get(i).setProductPrice(new_price);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                finalViewHolder.tvShopcartPrice.setText(decimalFormat.format(new_price)+ " Đ");

                // Update: tổng tiền
                com.sel.smartfood.ui.shopcart.ShopCartFragment.EventUtil();

                if (new_product_numbers < 2){
                    finalViewHolder.btnMinus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnPlus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnValues.setText(String.valueOf(new_product_numbers));
                }
                else{
                    finalViewHolder.btnPlus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnMinus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnValues.setText(String.valueOf(new_product_numbers));
                }

            }
        });


        return view;
    }
}
