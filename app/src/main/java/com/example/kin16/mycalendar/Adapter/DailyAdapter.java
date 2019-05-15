package com.example.kin16.mycalendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kin16.mycalendar.Listitem.listDay;
import com.example.kin16.mycalendar.R;

import java.util.ArrayList;

public class DailyAdapter extends BaseAdapter {
    private ArrayList<listDay> ld = new ArrayList<listDay>();
    public DailyAdapter(){}

    public int getCount(){
        return ld.size();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daily_listview,parent,false);
        }

        TextView title = convertView.findViewById(R.id.dayTitle);
        TextView location = convertView.findViewById(R.id.dayLocation);
        TextView memo = convertView.findViewById(R.id.dayMemo);

        listDay ldItem = ld.get(position);

        title.setText(ldItem.getTitle());
        location.setText(ldItem.getLocation());
        memo.setText(ldItem.getMemo());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return ld.get(position);
    }

    public void addLD(String title, String location, String memo){
        listDay item = new listDay();

        item.setTitle(title);
        item.setLocation(location);
        item.setMemo(memo);

        ld.add(item);
    }
}
