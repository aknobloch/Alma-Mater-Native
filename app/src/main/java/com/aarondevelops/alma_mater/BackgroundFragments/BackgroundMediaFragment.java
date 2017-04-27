package com.aarondevelops.alma_mater.BackgroundFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;

import com.aarondevelops.alma_mater.Framework.MediaListener;

import java.util.ArrayList;

public class BackgroundMediaFragment extends Fragment implements MediaPlayer.OnCompletionListener
{
    public static final String MEDIA_HELPER_TAG = "BackgroundMediaFragment";

    private Context appContext;
    private Integer mediaID;
    private MediaPlayer mediaPlayer;
    private ProgressBar mScrubBar;
    private ArrayList<MediaListener> mMediaListener;
    private boolean mPlaying;

    public BackgroundMediaFragment()
    {
        super();
        mMediaListener = new ArrayList<>();
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appContext = getActivity().getApplicationContext();
        setRetainInstance(true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void playMedia()
    {
        if(mediaID == null)
        {
            Log.e(MEDIA_HELPER_TAG, "BackgroundMediaFragment not initialized with resource ID.");
            return;
        }

        if(mediaPlayer == null)
        {
            new MediaHelperLoader().execute();
            return;
        }

        if(mediaPlayer.isPlaying())
        {
            return;
        }

        mediaPlayer.start();
        mPlaying = true;

        for(MediaListener listener : mMediaListener)
        {
            listener.publishSongDuration(mediaPlayer.getDuration());
        }

        mediaPlayer.setOnCompletionListener(this);
        initializeScrubBar();

        Log.i(MEDIA_HELPER_TAG, "Playing track.");
    }

    private void initializeScrubBar()
    {
        if(mScrubBar == null)
        {
            Log.i(MEDIA_HELPER_TAG, "No scrub bar registered.");
            return;
        }

        mScrubBar.setMax(getAudioLengthMilliseconds());
        mScrubBar.setProgress(mediaPlayer.getCurrentPosition());
        new ProgressTracker().execute();
    }

    public void pauseMedia()
    {
        if(mediaPlayer == null)
        {
            Log.d(MEDIA_HELPER_TAG, "BackgroundMediaFragment not mPlaying anything.");
            return;
        }

        mediaPlayer.pause();
        mPlaying = false;
        Log.i(MEDIA_HELPER_TAG, "Pausing track.");
    }

    public void stopMedia()
    {
        if(mediaPlayer == null)
        {
            Log.d(MEDIA_HELPER_TAG, "BackgroundMediaFragment not mPlaying anything.");
            return;
        }

        mediaPlayer.release();
        mPlaying = false;
        mediaPlayer = null;

        resetScrubBar();

        if(mMediaListener != null)
        {
            for(MediaListener listener : mMediaListener)
            {
                listener.reset();
            }
        }

        Log.i(MEDIA_HELPER_TAG, "Stopping track.");
    }

    private void resetScrubBar()
    {
        if(mScrubBar == null)
        {
            return;
        }

        mScrubBar.setProgress(0);
    }

    public int getAudioLengthMilliseconds()
    {
        if(mediaPlayer == null)
        {
            Log.e(MEDIA_HELPER_TAG, "Media player does not have a track associated with it.");
            return -1;
        }

        return mediaPlayer.getDuration();
    }

    public void setMediaID(int ID)
    {
        this.mediaID = ID;
    }

    public void registerScrubBar(ProgressBar scrubBar)
    {
        mScrubBar = scrubBar;
    }

    public void registerMediaListener(MediaListener listener)
    {
        mMediaListener.add(listener);
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        stopMedia();
    }

    class MediaHelperLoader extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                mediaPlayer = MediaPlayer.create(appContext, mediaID);
                mediaPlayer.setLooping(false);
                return true;
            }
            catch(Resources.NotFoundException rnfe)
            {
                Log.e(MEDIA_HELPER_TAG, "Media with resourceID " + mediaID + " not found.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean initializedProperly)
        {
            if( ! initializedProperly)
            {
                return;
            }

            playMedia();
        }
    }

    class ProgressTracker extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            while (mPlaying)
            {
                publishProgress();
                SystemClock.sleep(250);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);

            int songPosition = mediaPlayer.getCurrentPosition();
            mScrubBar.setProgress(songPosition);
            for(MediaListener listener : mMediaListener)
            {
                listener.publishSongState(songPosition);
            }
        }
    }
}
