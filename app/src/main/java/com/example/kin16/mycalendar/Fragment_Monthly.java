package com.example.kin16.mycalendar;

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

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class Fragment_Monthly extends Fragment {
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView cv_m;
    DB_OpenHelper mDbOpenHelper;
    List<String> arrayData;

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
        iCursor.moveToFirst();

        if(iCursor != null) {
            while (iCursor.moveToNext()) {
                String d = iCursor.getString(iCursor.getColumnIndex("date"));
                Log.d("date : ", d);
                arrayData.add(d);
            }
        }
    }
}
