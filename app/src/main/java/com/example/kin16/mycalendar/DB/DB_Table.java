package com.example.kin16.mycalendar.DB;

import android.provider.BaseColumns;

public final class DB_Table {

    public static final class CreateDB implements BaseColumns{
        public static final String TITLE = "title";
        public static final String DATE = "date";
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String LOCATION = "location";
        public static final String MEMO = "memo";
        public static final String _TABLENAME = "PLAN";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +TITLE+" text not null , "
                        +DATE+" text not null , "
                        +YEAR+" text not null , "
                        +MONTH+" text not null , "
                        +DAY+" text not null , "
                        +LOCATION+" text not null, "
                        +MEMO+" text not null);";
    }
}
