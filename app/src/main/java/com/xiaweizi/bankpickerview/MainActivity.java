package com.xiaweizi.bankpickerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BankPickerView mPickerView;
    private List<BankModel> data;
    private TextView mTvBankInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickerView = findViewById(R.id.bank_picker_view);
        mTvBankInfo = findViewById(R.id.bank_info);
        initData();
        mPickerView.setItems(data);
        final BankPopupWindow popupWindow = new BankPopupWindow(this);
        popupWindow.setOnBankSelectListener(new BankPopupWindow.OnBankSelectListener() {
            @Override
            public void onBankSelect(BankModel model) {
                mTvBankInfo.setText(model.toString());
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

    private void initData() {
        try {
            InputStream is = getAssets().open("bankInfo.json");
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            String result =new String(buffer, "utf8");
            Gson gson = new Gson();
            data = gson.fromJson(result, BankModelInfo.class).data;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

}
