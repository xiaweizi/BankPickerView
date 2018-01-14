package com.xiaweizi.bankpickerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BankPickerView mPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPickerView = findViewById(R.id.bank_picker_view);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("数据" + i);
        }
        mPickerView.setOffset(1);
        mPickerView.setItems(data);
        mPickerView.setOnWheelViewListener(new BankPickerView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("BankPickerView:", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
    }

}
