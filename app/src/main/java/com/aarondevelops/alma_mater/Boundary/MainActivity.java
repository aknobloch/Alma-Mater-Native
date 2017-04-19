package com.aarondevelops.alma_mater.Boundary;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.aarondevelops.alma_mater.BackgroundFragments.BackgroundMediaFragment;
import com.aarondevelops.alma_mater.AudioUtils.MusicManager;
import com.aarondevelops.alma_mater.BackgroundFragments.NoteRecognitionFragment;
import com.aarondevelops.alma_mater.R;
import com.aarondevelops.alma_mater.Utils.MessageHelper;

public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback
{
    private BackgroundMediaFragment mMediaFragment;
    private NoteRecognitionFragment mNoteRecognitionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMediaFragment = new BackgroundMediaFragment();
        mNoteRecognitionFragment = new NoteRecognitionFragment();

        initializeSpinner();
        initializeMusicFragment();
        initializeNoteFragment();
    }

    private void initializeSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.songDropdown);
        new TrackChoiceAdapter(this, spinner);
    }

    private void initializeMusicFragment()
    {
        setSong();
        bindFragment(mMediaFragment, BackgroundMediaFragment.MEDIA_HELPER_TAG);
    }

    private void initializeNoteFragment()
    {
        mNoteRecognitionFragment.setNeedleView((ImageView) findViewById(R.id.needle));
        bindFragment(mNoteRecognitionFragment, NoteRecognitionFragment.NOTE_HELPER_FRAG);
    }

    private void setSong()
    {
        int songID = MusicManager.getResourceID((Spinner) findViewById(R.id.songDropdown));
        mMediaFragment.setMediaID(songID);
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
