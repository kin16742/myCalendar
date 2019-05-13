package com.example.kin16.mycalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPlan extends Activity {
    Calendar cal;

    TextView cancel;
    TextView enroll;

    TextView planTitle;
    TextView planDate;
    TextView planLocation;
    TextView planMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplan);


        cancel = findViewById(R.id.cancel);
        enroll = findViewById(R.id.enroll);

        planTitle = findViewById(R.id.planTitle);
        planDate = findViewById(R.id.planDate);
        planLocation = findViewById(R.id.planLocation);
        planMemo = findViewById(R.id.planMemo);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        enroll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                enrollment();
            }
        });
        planDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DialogDatePicker();
            }
        });
    }

    private void DialogDatePicker(){
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddPlan.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                // TODO Auto-generated method stub
                planDate.setText(y + "-" + (m + 1) + "-" + d);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private void enrollment(){
        if(!TextUtils.isEmpty(planTitle.getText()) && !TextUtils.isEmpty(planDate.getText())){
            Toast.makeText(getApplicationContext(), planTitle.getText().toString() + " 등록!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
