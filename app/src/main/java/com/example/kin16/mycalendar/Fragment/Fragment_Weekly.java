package com.example.kin16.mycalendar.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kin16.mycalendar.Adapter.WeeklyAdapter;
import com.example.kin16.mycalendar.AddPlan;
import com.example.kin16.mycalendar.DB.DB_OpenHelper;
import com.example.kin16.mycalendar.Decorator.EventDecorator;
import com.example.kin16.mycalendar.Decorator.OneDayDecorator;
import com.example.kin16.mycalendar.Decorator.SaturdayDecorator;
import com.example.kin16.mycalendar.Decorator.SundayDecorator;
import com.example.kin16.mycalendar.Listener.OnSingleClickListener;
import com.example.kin16.mycalendar.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class Fragment_Weekly extends Fragment {
    private MaterialCalendarView cv_w;
    private TextView tv_w, addPlan_w;
    private ListView lv_w;

    private int dY, dM, dD;

    private DB_OpenHelper mDbOpenHelper;

    private List<String> arrayData;
    private WeeklyAdapter wa;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    public Fragment_Weekly(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);

        lv_w = view.findViewById(R.id.lv_W);
        cv_w = view.findViewById(R.id.cv_W);
        tv_w = view.findViewById(R.id.tv_W);
        addPlan_w = view.findViewById(R.id.addPlan_W);

        cv_w.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit();

        cv_w.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator
        );

        cv_w.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String d = date.toString();
                String dStr = d.substring(12,d.length() - 1);
                Log.d("주간달력 체크용", dStr);
                String[] time = dStr.split("-");
                dY = Integer.parseInt(time[0]);
                dM = Integer.parseInt(time[1]);
                dD = Integer.parseInt(time[2]);

                PlanView();

                tv_w.setText(dY + "년 " + (dM + 1) + "월 " + dD + "일의 일정");
            }
        });

        addPlan_w.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent pIntent = new Intent(getActivity(), AddPlan.class);

                pIntent.putExtra("year",dY);
                pIntent.putExtra("month",dM);
                pIntent.putExtra("day",dD);

                startActivity(pIntent);
            }
        });

        Calendar today = Calendar.getInstance();

        dY = today.get(Calendar.YEAR);
        dM = today.get(Calendar.MONTH);
        dD = today.get(Calendar.DAY_OF_MONTH);

        cv_w.setDateSelected(today,true);

        tv_w.setText(dY + "년 " + (dM + 1) + "월 " + dD + "일의 일정");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        PlanView();

        arrayData = new ArrayList<>();
        searchDate();
        new ApiSimulator(arrayData).executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private void PlanView(){
        wa = new WeeklyAdapter();
        arrayData = new ArrayList<>();
        lv_w.setAdapter(wa);
        searchPlan(dY + "-" + (dM + 1) + "-" + dD);
        for(int i=0;i<arrayData.size();i++){
            wa.addLW(arrayData.get(i));
        }
        if(arrayData.size() == 0){
            wa.addLW("일정이 없습니다.");
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        List<String> Time_Result;

        ApiSimulator(List<String> Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for(int i = 0 ; i < Time_Result.size() ; i ++){
                String[] time = Time_Result.get(i).split("-");
                int pYear = Integer.parseInt(time[0]);
                int pMonth = Integer.parseInt(time[1]);
                int pDay = Integer.parseInt(time[2]);

                calendar.set(pYear, pMonth - 1, pDay);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            cv_w.addDecorator(new EventDecorator(Color.RED, calendarDays, getActivity()));
        }
    }

    public void searchDate(){
        mDbOpenHelper = new DB_OpenHelper(getActivity());
        mDbOpenHelper.open();

        Cursor iCursor = mDbOpenHelper.selectColumns();

        if(iCursor != null) {
            while (iCursor.moveToNext()) {
                String d = iCursor.getString(iCursor.getColumnIndex("date"));
                arrayData.add(d);
            }
        }
    }

    public void searchPlan(String planDate){
        mDbOpenHelper = new DB_OpenHelper(getActivity());
        mDbOpenHelper.open();

        Cursor iCursor = mDbOpenHelper.selectColumns();

        if(iCursor != null) {
            while (iCursor.moveToNext()) {
                String d = iCursor.getString(iCursor.getColumnIndex("date"));
                if(d.equals(planDate)){
                    String t = iCursor.getString(iCursor.getColumnIndex("title"));
                    arrayData.add(t);
                }
            }
        }
    }
}
