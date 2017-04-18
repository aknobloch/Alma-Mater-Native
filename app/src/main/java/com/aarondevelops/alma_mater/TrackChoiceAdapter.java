package com.aarondevelops.alma_mater;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Aaron on 4/18/2017.
 */

public class TrackChoiceAdapter extends ArrayAdapter<CharSequence>
{
    private static String[] trackOptions = {"Instrumental", "Low Vocals", "High Vocals", "Vocals Only"};

    public TrackChoiceAdapter(Context context, Spinner target)
    {
        super(context, android.R.layout.simple_spinner_item, trackOptions);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(this);
    }

}
