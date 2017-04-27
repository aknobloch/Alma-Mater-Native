package com.aarondevelops.alma_mater.Boundary;

import android.widget.TextView;

import com.aarondevelops.alma_mater.Framework.MediaListener;

public class ProgressUpdater implements MediaListener
{
    TextView mPositionLabel;
    TextView mDurationLabel;

    public ProgressUpdater(TextView currentPositionLabel, TextView durationLabel)
    {
        mPositionLabel = currentPositionLabel;
        mDurationLabel = durationLabel;
    }

    @Override
    public void publishSongState(int progress)
    {
        String[] timeStamp = getTime(progress);
        this.mPositionLabel.setText(timeStamp[0] + ":" + timeStamp[1]);
    }

    @Override
    public void reset()
    {
        mPositionLabel.setText("-");
        mDurationLabel.setText("-");
    }

    @Override
    public void publishSongDuration(int duration)
    {
        String[] timeStamp = getTime(duration);
        this.mDurationLabel.setText(timeStamp[0] + ":" + timeStamp[1]);
    }

    private String[] getTime(int duration)
    {
        int minutes = 0;
        int seconds = duration / 1000;

        while(seconds >= 60)
        {
            minutes++;
            seconds /= 60;
        }
        String minutesString = (minutes < 10) ? "0" + minutes : "" + minutes;
        String secondString = (seconds < 10) ? "0" + seconds : "" + seconds;

        return new String[] {minutesString, secondString};
    }
}
