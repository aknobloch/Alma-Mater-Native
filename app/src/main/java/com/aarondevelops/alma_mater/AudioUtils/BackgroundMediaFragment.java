package com.aarondevelops.alma_mater.AudioUtils;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class BackgroundMediaFragment extends Fragment
{
    public static final String MEDIA_HELPER_TAG = "BackgroundMediaFragment";

    private Context appContext;
    private Integer mediaID;
    private MediaPlayer mediaPlayer;

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
        Log.i(MEDIA_HELPER_TAG, "Playing track.");
    }

    public void pauseMedia()
    {
        if(mediaPlayer == null)
        {
            Log.d(MEDIA_HELPER_TAG, "BackgroundMediaFragment not playing anything.");
            return;
        }

        mediaPlayer.pause();
        Log.i(MEDIA_HELPER_TAG, "Pausing track.");
    }

    public void stopMedia()
    {
        if(mediaPlayer == null)
        {
            Log.d(MEDIA_HELPER_TAG, "BackgroundMediaFragment not playing anything.");
            return;
        }

        mediaPlayer.stop();
        Log.i(MEDIA_HELPER_TAG, "Stopping track.");
    }

    public void setMediaID(int ID)
    {
        this.mediaID = ID;
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
}
