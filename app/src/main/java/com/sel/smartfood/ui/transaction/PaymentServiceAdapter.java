package com.sel.smartfood.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sel.smartfood.R;
import com.sel.smartfood.data.model.PaymentService;

import java.util.List;

public class PaymentServiceAdapter extends BaseAdapter {
    private List<PaymentService> paymentServiceList;
    private Context context;
    public PaymentServiceAdapter(Context context){
        this.context = context;
    }
    public void changeDataset(List<PaymentService> paymentServiceList){
        this.paymentServiceList = paymentServiceList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (paymentServiceList == null)
            return 0;
        return this.paymentServiceList.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentServiceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_service, null);
            holder = new ViewHolder();
            holder.paymentLogo = convertView.findViewById(R.id.iv_payment_service_logo);
            holder.paymentName = convertView.findViewById(R.id.tv_payment_service_name);
            holder.paymentState = convertView.findViewById(R.id.iv_payment_service_state);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        PaymentService paymentService = paymentServiceList.get(position);

        holder.paymentLogo.setImageDrawable(this.context.getDrawable(paymentService.getImageSource()));
        holder.paymentName.setText(paymentService.getName());
        if (paymentService.isChoosed()){
            holder.paymentState.setImageDrawable(this.context.getDrawable(R.drawable.item_fill_circle));
            holder.paymentName.setTextColor(R.color.primaryTextColor);
            holder.paymentName.setTypeface(holder.paymentName.getTypeface(), Typeface.BOLD_ITALIC);
        }
        else{
            holder.paymentState.setImageDrawable(this.context.getDrawable(R.drawable.item_circle));
            holder.paymentName.setTextColor(R.color.blackTextColor);
            holder.paymentName.setTypeface(Typeface.DEFAULT);
        }

        return convertView;
    }
    class ViewHolder{
        ImageView paymentLogo;
        ImageView paymentState;
        TextView paymentName;
    }
}
