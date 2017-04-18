package com.aarondevelops.alma_mater;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeSpinner();
    }

    private void initializeSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.songDropdown);
        new TrackChoiceAdapter(this, spinner);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
}
