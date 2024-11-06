package com.sel.smartfood.ui.transaction;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.sel.smartfood.R;

public class PaymentChplay extends AppCompatActivity {
    private PaymentHelper paymentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_transaction); // Thay bằng tên layout của bạn

        // Khởi tạo PaymentHelper
        paymentHelper = new PaymentHelper(this);

        // Gán sự kiện OnClickListener cho nút include_chplay
        findViewById(R.id.include_chplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentHelper.initiatePurchase(); // Gọi hàm thanh toán
            }
        });
    }
}
