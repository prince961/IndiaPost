package com.example.mohit.indiapost;

import android.database.AbstractWindowedCursor;

public class Tracking {
    String AwbNumber;
    String Date;
    String Tag;

    public Tracking(String AwbNumber, String lastUpdateDate, String tag) {
        this.AwbNumber = AwbNumber;
        this.Date = lastUpdateDate;
        this.Tag = tag;
    }
}
