package com.example.kin16.mycalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kin16.mycalendar.DB.DB_OpenHelper;
import com.example.kin16.mycalendar.Listener.OnSingleClickListener;

import java.util.Calendar;

public class AddPlan extends Activity {
    TextView cancel;
    TextView enroll;

    TextView planTitle;
    TextView planDate;
    TextView planLocation;
    TextView planMemo;

    String pYear, pMonth, pDay;

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

        Intent intent = getIntent();

        int y = intent.getExtras().getInt("year");
        if(y != 0) {
            int m = intent.getExtras().getInt("month");
            int d = intent.getExtras().getInt("day");

            planDate.setText(y + "-" + (m + 1) + "-" + d);
            pYear = Integer.toString(y);
            pMonth = Integer.toString((m + 1));
            pDay = Integer.toString(d);
        }

        cancel.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        enroll.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                enrollment();
            }
        });

        planDate.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
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
                pYear = Integer.toString(y);
                pMonth = Integer.toString((m + 1));
                pDay = Integer.toString(d);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private void enrollment(){
        if(TextUtils.isEmpty(planTitle.getText())){
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요!!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(planDate.getText())){
            Toast.makeText(getApplicationContext(), "날짜를 선택해주세요!!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(planLocation.getText())){
            Toast.makeText(getApplicationContext(), "위치를 입력해주세요!!", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(planMemo.getText())){
            Toast.makeText(getApplicationContext(), "메모를 입력해주세요!!", Toast.LENGTH_LONG).show();
        }

        if(!TextUtils.isEmpty(planTitle.getText()) && !TextUtils.isEmpty(planDate.getText())
                && !TextUtils.isEmpty(planLocation.getText()) && !TextUtils.isEmpty(planMemo.getText())){
            DB_OpenHelper db = new DB_OpenHelper(this);
            db.open();
            db.insertColumn(
                    planTitle.getText().toString(),
                    planDate.getText().toString(),
                    pYear,
                    pMonth,
                    pDay,
                    planLocation.getText().toString(),
                    planMemo.getText().toString()
            );
            Toast.makeText(getApplicationContext(), pYear + "년 " + pMonth + "월 " + pDay + "일에 새로운 일정 '"
                    + planTitle.getText().toString() + "' 등록!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
