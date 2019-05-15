package com.example.kin16.mycalendar.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kin16.mycalendar.Adapter.DailyAdapter;
import com.example.kin16.mycalendar.AddPlan;
import com.example.kin16.mycalendar.DB.DB_OpenHelper;
import com.example.kin16.mycalendar.Listener.OnSingleClickListener;
import com.example.kin16.mycalendar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment_Daily extends Fragment {
    private LinearLayout left, right, center;
    private TextView year_tv, date_tv, addPlan_d;
    private ListView lv_d;

    private Calendar today, current;
    private int dY, dM, dD;
    private String dW;

    private DB_OpenHelper mDbOpenHelper;

    private List<String> arrayData;
    private DailyAdapter da;

    public Fragment_Daily(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        year_tv = view.findViewById(R.id.yearTV);
        date_tv = view.findViewById(R.id.dateTV);
        left = view.findViewById(R.id.ll_left);
        right = view.findViewById(R.id.ll_right);
        lv_d = view.findViewById(R.id.lv_D);
        center = view.findViewById(R.id.ll_center);
        addPlan_d = view.findViewById(R.id.addPlan_D);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yesterday();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomorrow();
            }
        });
        center.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DialogDatePicker();
            }
        });
        addPlan_d.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
               Intent pIntent = new Intent(getActivity(), AddPlan.class);

               pIntent.putExtra("year",dY);
               pIntent.putExtra("month",dM);
               pIntent.putExtra("day",dD);

               startActivity(pIntent);
            }
        });
        today = Calendar.getInstance();
        current = Calendar.getInstance();

        dY = today.get(Calendar.YEAR);
        dM = today.get(Calendar.MONTH);
        dD = today.get(Calendar.DAY_OF_MONTH);

        dateView(today);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewList();
    }

    private void yesterday(){
        current.set(dY,dM,dD - 1);
        dateView(current);
        viewList();
    }

    private void tomorrow(){
        current.set(dY,dM,dD + 1);
        dateView(current);
        viewList();
    }

    private void viewList(){
        da = new DailyAdapter();
        arrayData = new ArrayList<>();
        lv_d.setAdapter(da);
        searchPlan(dY, dM, dD);
        for (int i = 0; i < arrayData.size(); i+=3) {
            da.addLD(arrayData.get(i), arrayData.get(i + 1), arrayData.get(i + 2));
        }
        if(arrayData.size() == 0){
            da.addLD(null,"일정이 없습니다.",null);
        }
    }

    private void dateView(Calendar cal){
        dY = cal.get(Calendar.YEAR);
        dM = cal.get(Calendar.MONTH);
        dD = cal.get(Calendar.DAY_OF_MONTH);

        switch(cal.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                dW = "월요일";
                break;
            case Calendar.TUESDAY:
                dW = "화요일";
                break;
            case Calendar.WEDNESDAY:
                dW = "수요일";
                break;
            case Calendar.THURSDAY:
                dW = "목요일";
                break;
            case Calendar.FRIDAY:
                dW = "금요일";
                break;
            case Calendar.SATURDAY:
                dW = "토요일";
                break;
            case Calendar.SUNDAY:
                dW = "일요일";
        }

        if(dY == today.get(Calendar.YEAR) && dM == today.get(Calendar.MONTH) && dD == today.get(Calendar.DAY_OF_MONTH)){
            date_tv.setTextColor(Color.rgb(80,188,223));
        }
        else{
            date_tv.setTextColor(Color.BLACK);
        }

        year_tv.setText(dY + "년");
        date_tv.setText((dM + 1) + "월 " + dD + "일 " + dW);
    }

    public void searchPlan(int year, int month, int day){
        mDbOpenHelper = new DB_OpenHelper(getActivity());
        mDbOpenHelper.open();

        Cursor iCursor = mDbOpenHelper.selectColumns();

        if(iCursor != null) {
            while (iCursor.moveToNext()) {
                String y = iCursor.getString(iCursor.getColumnIndex("year"));
                String m = iCursor.getString(iCursor.getColumnIndex("month"));
                String d = iCursor.getString(iCursor.getColumnIndex("day"));

                if(y.equals(String.valueOf(year)) && m.equals(String.valueOf(month + 1)) && d.equals(String.valueOf(day))){
                    String title = iCursor.getString(iCursor.getColumnIndex("title"));
                    String location = iCursor.getString(iCursor.getColumnIndex("location"));
                    String memo = iCursor.getString(iCursor.getColumnIndex("memo"));

                    arrayData.add(title);
                    arrayData.add(location);
                    arrayData.add(memo);
                    Log.d("넣은거",title +" "+ location +" "+ memo);
                }
            }
        }
    }

    private void DialogDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                current.set(y, m, d);
                dateView(current);
                viewList();
            }
        }, dY, dM, dD);

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }
}
