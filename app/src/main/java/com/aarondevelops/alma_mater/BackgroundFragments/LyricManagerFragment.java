package com.aarondevelops.alma_mater.BackgroundFragments;

import android.app.Fragment;
import android.widget.TextView;

import com.aarondevelops.alma_mater.Framework.MediaListener;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class LyricManagerFragment extends Fragment implements MediaListener
{
    public static final String LYRIC_MANAGER_TAG = "LyricManagerFragment";

    private TextView mPastLyric;
    private TextView mCurrentLyric;
    private TextView mNextLyric;
    private static LinkedList<String> nextLyricQueue;

    static
    {
        initializeQueue();
    }

    public LyricManagerFragment()
    {
        super();
        initializeQueue();
    }

    public void setLyricFields(TextView pastLyric, TextView currentLyric, TextView nextLyric)
    {
        mPastLyric = pastLyric;
        mCurrentLyric = currentLyric;
        mNextLyric = nextLyric;
    }

    @Override
    public void publishSongState(int progressMilliseconds)
    {
        int progressSeconds = progressMilliseconds / 1000;
        String currentLyric = getAppropriateLyric(progressSeconds);

        // continuously advance lyrics until the currently displayed
        // lyric matches what it should be. this will usually just be
        // once, unless orientation changes and it needs to catch back up
        while( ! mCurrentLyric.getText().equals(currentLyric))
        {
            advanceLyrics();
        }
    }

    private void advanceLyrics()
    {
        mPastLyric.setText(mCurrentLyric.getText());
        mCurrentLyric.setText(mNextLyric.getText());
        try
        {
            mNextLyric.setText(nextLyricQueue.poll());
        }
        catch(NoSuchElementException nse)
        {
            mNextLyric.setText("-");
        }
    }

    private String getAppropriateLyric(int progressSeconds)
    {
        if(progressSeconds < 10) return "-";
        if(progressSeconds < 15) return "We have gained wisdom and honor";
        if(progressSeconds < 19) return "From our home of green and gray";
        if(progressSeconds < 23) return "We will go forth and remember";
        if(progressSeconds < 27) return "All we've learned along the way";
        if(progressSeconds < 31) return "And with knowledge and compassion";
        if(progressSeconds < 35) return "We will build communities";
        if(progressSeconds < 40) return "Leading by example";
        if(progressSeconds < 44) return "And with dignity";
        if(progressSeconds < 52) return "Georgia Gwinnett, we'll never forget";
        if(progressSeconds < 60) return "How we have grown, and those that we've met";
        if(progressSeconds < 67) return "Georgia Gwinnett, with love and respect";
        if(progressSeconds < 76) return "Our alma mater, Georgia Gwinnett";
        if(progressSeconds < 85) return "Our alma mater, Georgia Gwinnett";
        return "-";
    }

    @Override
    public void reset()
    {
        mPastLyric.setText("");
        mCurrentLyric.setText("-");
        mNextLyric.setText("We have gained wisdom and honor");

        initializeQueue();
    }

    private static void initializeQueue()
    {
        nextLyricQueue = new LinkedList<>();

        nextLyricQueue.add("From our home of green and gray");
        nextLyricQueue.add("We will go forth and remember");
        nextLyricQueue.add("All we've learned along the way");
        nextLyricQueue.add("And with knowledge and compassion");
        nextLyricQueue.add("We will build communities");
        nextLyricQueue.add("Leading by example");
        nextLyricQueue.add("And with dignity");
        nextLyricQueue.add("Georgia Gwinnett, we'll never forget");
        nextLyricQueue.add("How we have grown, and those that we've met");
        nextLyricQueue.add("Georgia Gwinnett, with love and respect");
        nextLyricQueue.add("Our alma mater, Georgia Gwinnett");
        nextLyricQueue.add("Our alma mater, Georgia Gwinnett");
    }
}
