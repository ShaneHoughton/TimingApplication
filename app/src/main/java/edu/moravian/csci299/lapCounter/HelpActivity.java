package edu.moravian.csci299.lapCounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * The Activity entered when the user clicks the Help button. Explains how to use the app
 */
public class HelpActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * Called when the HelpActivity is created. Set an onclicklistener for the back button
     *
     * @param savedInstanceState  If the activity is being re-initialized after previously
     *                            being shut down then this Bundle contains the data it most
     *                            recently supplied in onSaveInstanceState. Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.backButton).setOnClickListener(this);
    }

    /**
     * Method called when a registered onclick listener is clicked
     *
     * @param v the View clicked
     */
    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.backButton)){
            finish();
        }
    }
}