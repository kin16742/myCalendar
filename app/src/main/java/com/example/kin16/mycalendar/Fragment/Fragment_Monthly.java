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

import com.example.kin16.mycalendar.AddPlan;
import com.example.kin16.mycalendar.DB.DB_OpenHelper;
import com.example.kin16.mycalendar.Decorator.EventDecorator;
import com.example.kin16.mycalendar.Decorator.OneDayDecorator;
import com.example.kin16.mycalendar.Decorator.SaturdayDecorator;
import com.example.kin16.mycalendar.Decorator.SundayDecorator;
import com.example.kin16.mycalendar.Decorator.WeekdayDecorator;
import com.example.kin16.mycalendar.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class Fragment_Monthly extends Fragment {
    private MaterialCalendarView cv_m;

    private int dY, dM, dD;

    private DB_OpenHelper mDbOpenHelper;

    private List<String> arrayData;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    public Fragment_Monthly(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly,container,false);

        cv_m = view.findViewById(R.id.cv_M);

        cv_m.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        cv_m.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new WeekdayDecorator(),
                oneDayDecorator
        );

        cv_m.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String d = date.toString();
                String dStr = d.substring(12,d.length() - 1);

                String[] time = dStr.split("-");
                dY = Integer.parseInt(time[0]);
                dM = Integer.parseInt(time[1]);
                dD = Integer.parseInt(time[2]);

                Intent pIntent = new Intent(getActivity(), AddPlan.class);

                pIntent.putExtra("year",dY);
                pIntent.putExtra("month",dM);
                pIntent.putExtra("day",dD);

                startActivity(pIntent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        arrayData = new ArrayList<>();
        searchDate();
        new ApiSimulator(arrayData).executeOnExecutor(Executors.newSingleThreadExecutor());
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

            cv_m.addDecorator(new EventDecorator(Color.RED, calendarDays, getActivity()));
        }
    }

    public void searchDate(){
        mDbOpenHelper = new DB_OpenHelper(getActivity());
        mDbOpenHelper.open();

        Cursor iCursor = mDbOpenHelper.selectColumns();

        if(iCursor != null) {
            while (iCursor.moveToNext()) {
                String d = iCursor.getString(iCursor.getColumnIndex("date"));
                Log.d("date : ", d);
                arrayData.add(d);
            }
        }
    }
}
