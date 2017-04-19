package com.aarondevelops.alma_mater;

import android.widget.Spinner;

/**
 * Created by Aaron K on 4/18/2017.
 */

public class MusicManager
{
    public static int getResourceID(Spinner spinnerView)
    {
        String selectedName = spinnerView.getSelectedItem().toString();
        switch(selectedName)
        {
            case "Instrumental" :
                return R.raw.instrumental;
            case "Low Vocals" :
                return R.raw.low_vocals;
            case "High Vocals" :
                return R.raw.high_vocals;
            case "Vocals Only" :
                return R.raw.vocals_only;
            default:
                throw new IllegalStateException
                        ("Spinner state could not be processed. It might not have been initialized.");
        }
    }
}
