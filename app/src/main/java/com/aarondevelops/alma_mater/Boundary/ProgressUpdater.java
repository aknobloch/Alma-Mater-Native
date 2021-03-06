package com.aarondevelops.alma_mater.Boundary;

import android.widget.TextView;

import com.aarondevelops.alma_mater.Framework.MediaListener;
import com.aarondevelops.alma_mater.R;

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
        mPositionLabel.setText(R.string._00_00);
        mDurationLabel.setText(R.string._00_00);
    }

    @Override
    public void publishSongDuration(int duration)
    {
        String[] timeStamp = getTime(duration);
        this.mDurationLabel.setText(timeStamp[0] + ":" + timeStamp[1]);
    }

    private String[] getTime(int duration)
    {
        int seconds = duration / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        String minutesString = (minutes < 10) ? "0" + minutes : "" + minutes;
        String secondString = (seconds < 10) ? "0" + seconds : "" + seconds;

        return new String[] {minutesString, secondString};
    }
}
