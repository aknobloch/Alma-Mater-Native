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

import com.aarondevelops.alma_mater.AudioUtils.MediaListener;

public class BackgroundMediaFragment extends Fragment
{
    public static final String MEDIA_HELPER_TAG = "BackgroundMediaFragment";

    private Context appContext;
    private Integer mediaID;
    private MediaPlayer mediaPlayer;
    private ProgressBar mScrubBar;
    private boolean mPlaying = false;
    private MediaListener mMediaListener;

    public BackgroundMediaFragment()
    {
        super();
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

        mScrubBar.setMax(mediaPlayer.getDuration());
        mScrubBar.setProgress(mediaPlayer.getCurrentPosition());
        new ScrubBarUpdater().execute();
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

        mPlaying = false;
        mediaPlayer.release();
        mediaPlayer = null;

        resetScrubBar();

        if(mMediaListener != null)
        {
            mMediaListener.reset();
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
        mMediaListener = listener;
    }

    class MediaHelperLoader extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                mediaPlayer = MediaPlayer.create(appContext, mediaID);
                mediaPlayer.setLooping(true);
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

    class ScrubBarUpdater extends AsyncTask<Void, Void, Void>
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

            mScrubBar.setMax(mediaPlayer.getDuration());
            mScrubBar.setProgress(songPosition);
            mMediaListener.publishSongState(songPosition);
        }


    }
}
