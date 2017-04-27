package com.aarondevelops.alma_mater.Boundary;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aarondevelops.alma_mater.AudioUtils.MusicManager;
import com.aarondevelops.alma_mater.BackgroundFragments.BackgroundMediaFragment;
import com.aarondevelops.alma_mater.BackgroundFragments.LyricManagerFragment;
import com.aarondevelops.alma_mater.BackgroundFragments.NoteRecognitionFragment;
import com.aarondevelops.alma_mater.R;
import com.aarondevelops.alma_mater.Utils.MessageHelper;

public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback
{
    private BackgroundMediaFragment mMediaFragment;
    private NoteRecognitionFragment mNoteRecognitionFragment;
    private LyricManagerFragment mLyricManagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMediaFragment = new BackgroundMediaFragment();
        mNoteRecognitionFragment = new NoteRecognitionFragment();
        mLyricManagerFragment = new LyricManagerFragment();

        initializeSpinner();
        initializeMusicFragment();
        initializeNoteFragment();
        initializeProgressBar();
        initializeLyricFragment();
        initializeProgressUpdater();
    }

    public void onPlayButton(View v)
    {
        setSong();
        mMediaFragment.playMedia();
    }

    public void onPauseButton(View v)
    {
        mMediaFragment.pauseMedia();
    }

    public void onStopButton(View v)
    {
        mMediaFragment.stopMedia();
    }

    private void setSong()
    {
        int songID = MusicManager.getResourceID((Spinner) findViewById(R.id.songDropdown));
        mMediaFragment.setMediaID(songID);
    }

    private void initializeSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.songDropdown);
        new TrackChoiceAdapter(this, spinner);
    }

    private void initializeMusicFragment()
    {
        if(fragmentExists(mMediaFragment.MEDIA_HELPER_TAG))
        {
            mMediaFragment = (BackgroundMediaFragment)
                    getFragmentManager().findFragmentByTag(mMediaFragment.MEDIA_HELPER_TAG);
        }
        else
        {
            bindFragment(mMediaFragment, BackgroundMediaFragment.MEDIA_HELPER_TAG);
        }

        setSong();
    }

    private void initializeNoteFragment()
    {
        if(fragmentExists(mNoteRecognitionFragment.NOTE_HELPER_TAG))
        {
            mNoteRecognitionFragment = (NoteRecognitionFragment)
                    getFragmentManager().findFragmentByTag(mNoteRecognitionFragment.NOTE_HELPER_TAG);
            mNoteRecognitionFragment.reinitialize();
        }
        else
        {
            bindFragment(mNoteRecognitionFragment, NoteRecognitionFragment.NOTE_HELPER_TAG);
        }

        mNoteRecognitionFragment.setNeedleView((ImageView) findViewById(R.id.needle));
    }

    private void initializeProgressBar()
    {
        ProgressBar scrubBar = (ProgressBar) findViewById(R.id.scrubBar);
        mMediaFragment.registerScrubBar(scrubBar);
    }

    private void initializeLyricFragment()
    {
        if(fragmentExists(mLyricManagerFragment.LYRIC_MANAGER_TAG))
        {
            mLyricManagerFragment = (LyricManagerFragment)
                    getFragmentManager().findFragmentByTag(mLyricManagerFragment.LYRIC_MANAGER_TAG);
        }
        else
        {
            bindFragment(mLyricManagerFragment, LyricManagerFragment.LYRIC_MANAGER_TAG);
        }

        TextView pastLyrics = (TextView) findViewById(R.id.pastLyric);
        TextView currentLyrics = (TextView) findViewById(R.id.currentLyric);
        TextView nextLyrics = (TextView) findViewById(R.id.nextLyric);

        mLyricManagerFragment.setLyricFields(pastLyrics, currentLyrics, nextLyrics);
        mMediaFragment.registerMediaListener(mLyricManagerFragment);
    }

    private void initializeProgressUpdater()
    {
        TextView progressLabel = (TextView) findViewById(R.id.currentPositionLabel);
        TextView durationLabel = (TextView) findViewById(R.id.durationLabel);

        ProgressUpdater updater = new ProgressUpdater(progressLabel, durationLabel);
        mMediaFragment.registerMediaListener(updater);
    }

    private boolean fragmentExists(String tag)
    {
        FragmentManager fragmentManager = getFragmentManager();

        if(fragmentManager.findFragmentByTag(tag) != null)
        {
            return true;
        }

        return false;
    }

    public void bindFragment(Fragment bindingFragment, String tag)
    {
        if(fragmentExists(tag))
        {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(bindingFragment, tag)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_about)
        {
            MessageHelper.showDialogSplash(this, getString(R.string.about_dialog));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode != NoteRecognitionFragment.RECORD_AUDIO_PERMISSION)
        {
            return;
        }

        if(grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            MessageHelper.makeToast(this,
                    "Audio permission is required to run Note Recognizer",
                    Toast.LENGTH_LONG);
            finishAndRemoveTask();
        }
        else
        {
            mNoteRecognitionFragment.notifyRecordPermissionGranted();
        }
    }



}
