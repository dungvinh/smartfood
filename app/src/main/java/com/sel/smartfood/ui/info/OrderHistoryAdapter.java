package com.sel.smartfood.ui.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.OrderHistory;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder> {
    private LayoutInflater layoutInflater;
    private List<OrderHistory> orderHistoryList;
    public OrderHistoryAdapter(){}

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.item_detail_orderhistory, parent, false);
        return new OrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);
        holder.orderHisItemNameTv.setText(orderHistory.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.orderHisItemTotalPriceTv.setText(decimalFormat.format(orderHistory.getProductTotalPrice()) + "Đ");
        holder.orderHisItemNumberTv.setText(String.valueOf(orderHistory.getProductNumber()));
        holder.orderHisItemDateTv.setText(orderHistory.getDate().split(" GMT")[0]);
        Picasso.get().load(orderHistory.getProductImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.error)
                .into(holder.orderHisItemIv);
    }

    class sortByDate implements Comparator<OrderHistory>{

        @Override
        public int compare(OrderHistory orderHistory1, OrderHistory orderHistory2) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss");
            try {
                Date date1 = dateFormat.parse(orderHistory1.getDate());
                Date date2 = dateFormat.parse(orderHistory2.getDate());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return -1;
        }


    }

    @Override
    public int getItemCount() {
        return orderHistoryList == null ? 0 : orderHistoryList.size();
    }
    public void setDataChanged(List<OrderHistory> orderHistories){
        Collections.sort(orderHistories, new sortByDate());

        this.orderHistoryList = orderHistories;
        notifyDataSetChanged();
    }



    public static class OrderHistoryHolder extends RecyclerView.ViewHolder {
        ImageView   orderHisItemIv;
        TextView    orderHisItemNameTv;
        TextView    orderHisItemNumberTv;
        TextView    orderHisItemTotalPriceTv;
        TextView    orderHisItemDateTv;


        public OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            orderHisItemIv = itemView.findViewById(R.id.iv_order_history_item);
            orderHisItemNameTv = itemView.findViewById(R.id.tv_order_history_item_name);
            orderHisItemNumberTv = itemView.findViewById(R.id.tv_order_history_item_number);
            orderHisItemTotalPriceTv = itemView.findViewById(R.id.tv_order_history_item_total_price);
            orderHisItemDateTv = itemView.findViewById(R.id.tv_order_history_item_date);
        }
    }
}
