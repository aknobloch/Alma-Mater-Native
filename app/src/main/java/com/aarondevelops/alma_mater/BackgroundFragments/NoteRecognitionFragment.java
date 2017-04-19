package com.aarondevelops.alma_mater.BackgroundFragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.aarondevelops.alma_mater.AudioUtils.Note;
import com.aarondevelops.alma_mater.AudioUtils.PitchCallback;
import com.aarondevelops.alma_mater.AudioUtils.PitchHandler;

public class NoteRecognitionFragment extends Fragment implements PitchCallback
{

    public static final String NOTE_HELPER_FRAG = "NoteRecognitionFragment";
    public static final int RECORD_AUDIO_PERMISSION = 001;

    private Context mAppContext;
    private PitchHandler mPitchHandler;
    private ImageView mNeedle;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
        setRetainInstance(true);
        mPitchHandler = new PitchHandler(getActivity(), this);

        beginNoteRecognition();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mPitchHandler.release();
    }

    public void notifyRecordPermissionGranted()
    {
        beginNoteRecognition();
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

    private void startAudioRecording()
    {
        mPitchHandler.startPitchDetection();
    }

    public void setNeedleView(ImageView needle)
    {
        mNeedle = needle;
    }

    @Override
    public void onPitchDetected(Note detectedNote)
    {
        if(mNeedle == null)
        {
            Log.e("NoteRecognitionFragment", "Needle not moving, ImageView was never set for fragment.");
            return;
        }

        double range = 70;
        float normalizedDegree = (float)
                ((detectedNote.getNormalizedValue() * range) - 35);

        mNeedle.setPivotX(mNeedle.getWidth() / 2);
        mNeedle.setPivotY(mNeedle.getHeight() - (mNeedle.getHeight() / 10));
        mNeedle.setRotation(normalizedDegree);
    }

    private boolean hasAudioPermission()
    {
        return ContextCompat.checkSelfPermission(mAppContext, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void getAudioPermission()
    {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_AUDIO_PERMISSION);

    }

}
