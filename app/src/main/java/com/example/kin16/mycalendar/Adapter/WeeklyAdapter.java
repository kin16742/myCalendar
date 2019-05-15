package com.example.kin16.mycalendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kin16.mycalendar.Listitem.listWeek;
import com.example.kin16.mycalendar.R;

import java.util.ArrayList;

public class WeeklyAdapter extends BaseAdapter{
    private ArrayList<listWeek> lw = new ArrayList<listWeek>();

    public WeeklyAdapter(){}

    public int getCount(){
        return lw.size();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.weekly_listview,parent,false);
        }

        TextView title = convertView.findViewById(R.id.weekTitle);
        listWeek lwItem = lw.get(position);
        title.setText(lwItem.getTitle());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return lw.get(position);
    }

    public void addLW(String title){
        listWeek item = new listWeek();

        item.setTitle(title);

        lw.add(item);
    }
}
