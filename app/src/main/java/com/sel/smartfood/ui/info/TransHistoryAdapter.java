package com.sel.smartfood.ui.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.TransHistory;

import java.util.List;

public class TransHistoryAdapter extends RecyclerView.Adapter<TransHistoryAdapter.TransHistoryHolder> {
    private LayoutInflater layoutInflater;
    private List<TransHistory> transHistoryList;
    public TransHistoryAdapter(){

    }
    @NonNull
    @Override
    public TransHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = layoutInflater.inflate(R.layout.item_detail_transhistory, parent, false);
        return new TransHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransHistoryHolder holder, int position) {
        TransHistory transHistory = transHistoryList.get(position);
        holder.serviceNameTv.setText(transHistory.getService());
        holder.amountOfMoneyTv.setText(String.valueOf(transHistory.getAmountOfMoney()) + " VND");
        holder.timeTv.setText(transHistory.getDate().split(" GMT")[0]);
        holder.typeTv.setText(transHistory.isWithdraw() ? "Rút tiền" : "Nạp tiền");
    }

    @Override
    public int getItemCount() {
        return transHistoryList == null ? 0 : transHistoryList.size();
    }
    public void setDataChanged(List<TransHistory> transHistories){
        this.transHistoryList = transHistories;
        notifyDataSetChanged();
    }

    public static class TransHistoryHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTv;
        TextView amountOfMoneyTv;
        TextView timeTv;
        TextView typeTv;
        public TransHistoryHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTv = itemView.findViewById(R.id.tv_transhistory_service);
            amountOfMoneyTv = itemView.findViewById(R.id.tv_transhistory_amount);
            timeTv = itemView.findViewById(R.id.tv_transhistory_time);
            typeTv = itemView.findViewById(R.id.tv_transhistory_type);
        }
    }
}
