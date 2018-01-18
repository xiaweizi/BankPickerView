package com.xiaweizi.bankpickerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BankPickerView mPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickerView = findViewById(R.id.bank_picker_view);

        final List<BankModel> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BankModel model = new BankModel();
            model.bankDesc = "desc" + i;
            model.bankName = "name" + i;
            model.bankLogo = "https://cdn2.jianshu.io/assets/web/jianyouquan-2fb0cd72e35147c79d6507c3a3a2591b.png";
            data.add(model);
        }

        mPickerView.setItems(data);
        final BankPopupWindow popupWindow = new BankPopupWindow(this);
        popupWindow.setOnBankSelectListener(new BankPopupWindow.OnBankSelectListener() {
            @Override
            public void onBankSelect(BankModel model) {

            }
        });

        findViewById(R.id.pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.show();
                popupWindow.setData(data);
            }
        });
    }

}
