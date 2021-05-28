package com.example.guesscarmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String SWITCH_BUTTON_STATUS = "com.example.android.guesscarmaker.extra.MESSAGE";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchButton = findViewById(R.id.switchButton);

    }

    public void identifyCarMake(View view) {
        Log.d(LOG_TAG,"Identify the car make button clicked ! ");

        //Read switch button status
        boolean switchStatus = switchButton.isChecked();
        Log.i(LOG_TAG,"Switch Button status : "+switchStatus);

        //Launch IdentifyCarMakeActivity when button is clicked
        Intent firstIntent =  new Intent(MainActivity.this,IdentifyCarMakeActivity.class);
        //Add key, value pairs to the intent
        firstIntent.putExtra(SWITCH_BUTTON_STATUS,switchStatus);
        startActivity(firstIntent);
    }

    public void hintsCarMake(View view) {
        Log.d(LOG_TAG,"Hints button clicked ! ");
        //Read switch button status
        boolean switchStatus = switchButton.isChecked();
        Log.i(LOG_TAG,"Switch Button status : "+switchStatus);
        //Launch IdentifyCarMakeActivity when button is clicked
        Intent secondIntent = new Intent(MainActivity.this,HintsActivity.class);
        //Add key, value pairs to the intent
        secondIntent.putExtra(SWITCH_BUTTON_STATUS,switchStatus);
        startActivity(secondIntent);
    }

    public void identifyCarImage(View view) {
        Log.d(LOG_TAG,"Identify the car image button clicked ! ");
        //Read switch button status
        boolean switchStatus = switchButton.isChecked();
        //Launch IdentifyCarImageActivity when button is clicked
        Intent thirdIntent = new Intent(MainActivity.this,IdentifyCarImageActivity.class);
        //Add key, value pairs to the intent
        thirdIntent.putExtra(SWITCH_BUTTON_STATUS,switchStatus);
        startActivity(thirdIntent);
    }

    public void advancedLevel(View view) {
        Log.d(LOG_TAG,"Advanced level button clicked ! ");
        //Read switch button status
        boolean switchStatus = switchButton.isChecked();

        //Launch AdvancedLevelActivity when button is clicked
        Intent fourthIntent = new Intent(MainActivity.this,AdvancedLevelActivity.class);
        //Add key, value pairs to the intent
        fourthIntent.putExtra(SWITCH_BUTTON_STATUS,switchStatus);
        startActivity(fourthIntent);
    }
}

/*
 * References
 * 1.
 *  Java - Setting up the project | Hangman Android App. (no date).
 *  Available from https://www.youtube.com/watch?v=HgH1DfH14uk&list=PLgTkNlNsy9gVCkeaoudJJWr4P3Gc-AV7f
 *  [Accessed 14 March 2021].
 *
 * 2.
 *  How To Create CountDown Timer App In Android Studio: Step By Step Guide | Create Android App.
 *  (no date). Available from https://abhiandroid.com/createandroidapp/create-countdown-timer-app
 *  [Accessed 14 March 2021].
 * */