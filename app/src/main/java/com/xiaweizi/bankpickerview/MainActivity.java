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
    private List<BankModel> mData;
    private TextView mBankName1;
    private TextView mBankName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickerView = findViewById(R.id.bank_picker_view);
        mBankName1 = findViewById(R.id.tv_bank_name);
        mBankName2 = findViewById(R.id.tv_bank_name1);
        initData();
        initPickerView();
        initPopupWindow();
    }

    private void initPopupWindow() {
        final BankPopupWindow popupWindow = new BankPopupWindow(this);
        popupWindow.setOnBankSelectListener(new BankPopupWindow.OnBankSelectListener() {
            @Override
            public void onBankSelect(BankModel model) {
                if (model == null) return;
                mBankName2.setText(model.bankName);
            }
        });
        findViewById(R.id.pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.show();
                popupWindow.setData(mData);
            }
        });
    }

    private void initPickerView() {
        mPickerView.setData(mData);
        mPickerView.setOffset(1);
        mPickerView.setOnBankSelectedListener(new BankPickerView.OnBankSelectedListener() {
            @Override
            public void onSelected(int selectedIndex, BankModel item) {
                if (item == null) return;
                mBankName1.setText(item.bankName);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            InputStream is = getAssets().open("bankInfo.json");
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            String result =new String(buffer, "utf8");
            Gson gson = new Gson();
            mData = gson.fromJson(result, BankModelInfo.class).data;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

}
