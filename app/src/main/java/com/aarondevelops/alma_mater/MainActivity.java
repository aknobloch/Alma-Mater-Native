package com.aarondevelops.alma_mater;

import android.Manifest;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        PitchCallback
{
    public final int RECORD_AUDIO = 001;
    private PitchHandler mPitchHandler;
    private BackgroundMediaFragment mMediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPitchHandler = new PitchHandler(this, this);
        mMediaFragment = new BackgroundMediaFragment();

        initializeSpinner();
        beginNoteRecognition();
        initializeMusicFragment();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPitchHandler.release();
    }

    private void initializeSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.songDropdown);
        new TrackChoiceAdapter(this, spinner);
    }

    private void beginNoteRecognition()
    {
        if(hasAudioPermission())
        {
            startAudioRecording();
        }
        else
        {
            getAudioPermission();
        }
    }

    private void initializeMusicFragment()
    {
        setSong();
        bindFragment(mMediaFragment, BackgroundMediaFragment.MEDIA_HELPER_TAG);
    }

    private void setSong()
    {
        int songID = MusicManager.getResourceID((Spinner) findViewById(R.id.songDropdown));
        mMediaFragment.setMediaID(songID);
    }

    private void startAudioRecording()
    {
        mPitchHandler.startPitchDetection();
    }

    private void getAudioPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_AUDIO);

    }

    public void bindFragment(Fragment bindingFragment, String tag)
    {
        FragmentManager fragmentManager = getFragmentManager();

        if(fragmentManager.findFragmentByTag(tag) != null)
        {
            // fragment already created
            return;
        }

        fragmentManager.beginTransaction()
                .add(bindingFragment, tag)
                .commit();

    }

    private boolean hasAudioPermission()
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED;
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
            showAboutMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutMessage()
    {
        new AlertDialog.Builder(this)

                .setMessage(getString(R.string.about_dialog))

                .setPositiveButton(getString(R.string.about_confirmation),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        })

                .show();
    }

    @Override
    public void onPitchDetected(Note detectedNote)
    {
        double range = 70;
        float normalizedDegree = (float)
                ((detectedNote.getNormalizedValue() * range) - 35);

        ImageView needle = (ImageView) findViewById(R.id.needle);
        needle.setPivotX(needle.getWidth() / 2);
        needle.setPivotY(needle.getHeight() - (needle.getHeight() / 10));
        needle.setRotation(normalizedDegree);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode != RECORD_AUDIO)
        {
            return;
        }

        if(grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            MessageHelper.makeToast(this,
                    "Audio permission is required to run Note Recognizer",
                    Toast.LENGTH_LONG);
            this.finishAndRemoveTask();
        }
        else
        {
            beginNoteRecognition();
        }
    }
}
