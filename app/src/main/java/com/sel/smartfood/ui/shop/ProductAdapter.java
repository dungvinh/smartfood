package com.sel.smartfood.ui.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;


    private List<Product> productList;
    private LayoutInflater layoutInflater;

    public List<Product> getProductList() {
        return productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = layoutInflater.inflate(R.layout.item_product, parent, false);
            return new ProductHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING){
            view = layoutInflater.inflate(R.layout.item_loading_product, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductHolder){
            Product product = productList.get(position);
            ProductHolder productHolder = (ProductHolder)holder;
            productHolder.tvName.setText(product.getName());
            productHolder.tvPrice.setText(String.valueOf(product.getPrice()));
            productHolder.ratingBar.setRating(product.getRatingScore());
            productHolder.tvPreparationTime.setText(String.valueOf(product.getPreparationTime()));
            Picasso.get().load(product.getUrl()).into(productHolder.ivImage);
        }


    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return productList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setDataChanged(@Nullable List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }
    public void addNewData(List<Product> productList){
//        if (this.productList.size() > 0 && this.productList.get(this.productList.size() - 1) == null)
        this.productList.remove(this.productList.size() - 1);
        if (productList.size() > 30)
            productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    public void setLoadingState(){
        this.productList.add(null);
        notifyDataSetChanged();
    }

     static class ProductHolder extends RecyclerView.ViewHolder {
        CircleImageView ivImage;
        TextView tvName;
        TextView tvPreparationTime;
        TextView tvPrice;
        AppCompatRatingBar ratingBar;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPreparationTime = itemView.findViewById(R.id.tv_preparation_time);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            ratingBar = itemView.findViewById(R.id.rb_product_score);
        }
    }
    static class LoadingHolder extends RecyclerView.ViewHolder{
        ProgressBar loading;
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
