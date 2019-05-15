package com.example.kin16.mycalendar;

import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.kin16.mycalendar.Fragment.Fragment_Daily;
import com.example.kin16.mycalendar.Fragment.Fragment_Monthly;
import com.example.kin16.mycalendar.Fragment.Fragment_Weekly;
import com.example.kin16.mycalendar.Listener.OnSingleClickListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    TextView plan;
    TabLayout tl;
    TabItem tab1, tab2, tab3;
    FrameLayout fl;

    Intent sIntent, pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tl = findViewById(R.id.tl);

        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);

        tl.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0 :
                        callFragment(0);
                        break;
                    case 1 :
                        callFragment(1);
                        break;
                    case 2 :
                        callFragment(2);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        sIntent = new Intent(this, Splash.class);
        pIntent = new Intent(this, AddPlan.class);

        startActivity(sIntent);

        plan = findViewById(R.id.plan);
        plan.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                pIntent.putExtra("year",0);
                startActivity(pIntent);
            }
        });

        callFragment(0);

        File f = new File(getFilesDir(), "lastFragment.txt");
        FileReader fr = null;
        try{
            if(f.exists()){
                fr = new FileReader(f);
                int lastFragmentInt = fr.read();
                char lastFragmentChar = (char)lastFragmentInt;
                int curItem = Integer.parseInt(lastFragmentChar + "");
                Log.d("확인", lastFragmentInt + " " + lastFragmentChar + " " + curItem);
                tl.getTabAt(curItem).select();
                if(fr != null) fr.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void callFragment(int num){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (num){
            case 0:
                Fragment_Monthly frag_M = new Fragment_Monthly();
                transaction.replace(R.id.fl, frag_M);
                transaction.commit();
                break;

            case 1:
                Fragment_Weekly frag_W = new Fragment_Weekly();
                transaction.replace(R.id.fl, frag_W);
                transaction.commit();
                break;

            case 2:
                Fragment_Daily frag_D = new Fragment_Daily();
                transaction.replace(R.id.fl, frag_D);
                transaction.commit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        File f = new File(getFilesDir(), "lastFragment.txt");
        FileWriter fw = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f);
            fw.write(Integer.toString(tl.getSelectedTabPosition()));
            if(fw != null) fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
    }
}
