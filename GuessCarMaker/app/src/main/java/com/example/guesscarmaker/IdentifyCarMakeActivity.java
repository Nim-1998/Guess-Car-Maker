package com.example.guesscarmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class IdentifyCarMakeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = IdentifyCarMakeActivity.class.getSimpleName();
    private static final long SET_TIMER_TIME = 20*1000;

    private ImageView car_image_view;
    private static int randomNumber;
    private TextView resultView;
    private TextView correctAnswerView;
    private String result;
    private String correctCarMaker;
    private Button confirmButton;
    private static String resource;
    private static ArrayList<Integer> randomNumberList= new ArrayList<>();
    private static Handler handler;
    private static Runnable runnable;
    private CountDownTimer countDownTimer;
    private TextView timerView;
    private static String spinnerSelection;
    private static String correctAnswer;
    private long remainingTime = SET_TIMER_TIME;
    private boolean timerRunningStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_identify_car_make);

        //Get the intent
        Intent firstIntent = getIntent();
        //Get boolean message from the intent extras
        boolean switchButtonStatus = firstIntent.getBooleanExtra(MainActivity.SWITCH_BUTTON_STATUS,false);

        //Get reference for the views
        resultView = findViewById(R.id.result_view);
        correctAnswerView = findViewById(R.id.correct_answer_view);
        confirmButton = findViewById(R.id.submit_button);
        car_image_view = findViewById(R.id.identify_car_image_view);
        timerView = findViewById(R.id.timer_view);


        //Restore the current state
        if (savedInstanceState != null) {

            if (savedInstanceState.getInt("image_view") != 0) {
                car_image_view.setImageResource(savedInstanceState.getInt("image_view"));
            } else {
                setRandomImage();
                Log.e(LOG_TAG, "Problem ! Unable to reload previous image");
            }

            //Set timer view value and visibility
            if (savedInstanceState.getBoolean("timer_view_visibility")){
                timerRunningStatus = savedInstanceState.getBoolean("timer_running_status");
                remainingTime = savedInstanceState.getLong("remaining_time");
                timerView.setText(savedInstanceState.getString("timer_view_value"));
                timerView.setVisibility(View.VISIBLE);
                if (timerRunningStatus){
                    countDownTimerStart();
                }
            }
            //Get current visibility from the Bundle
            boolean isVisible = savedInstanceState.getBoolean("reply_visible");

            if (isVisible) {
                //Restore and set Visibility
                confirmButton.setText(savedInstanceState.getString("button_text"));
                resultView.setText(savedInstanceState.getString("result_text"));
                resultView.setVisibility(View.VISIBLE);
                correctAnswerView.setText(savedInstanceState.getString("answer_text"));
                correctAnswerView.setVisibility(View.VISIBLE);

                //After restore set TextView colours
                if (correctAnswerView.getText().equals("")) {
                    resultView.setTextColor(Color.parseColor("#008000"));
                } else {
                    resultView.setTextColor(Color.parseColor("#990000"));
                    correctAnswerView.setTextColor(Color.parseColor("#FBC101"));
                }
            }
        } else {
            if (switchButtonStatus){
                //Set count down timer
                countDownTimerStart();
            }
            //Display random image
            setRandomImage();
        }

        //Instantiate Spinner
        Spinner spinner = findViewById(R.id.label_spinner);
        if (spinner != null) {
            //set Spinner's listener
            spinner.setOnItemSelectedListener(this);
        }

        //Create Array Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_car_maker, android.R.layout.simple_spinner_item);

        //Specify layout for the Spinner choices
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the adapter to the spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

    }

    //Method to set timer count down
    private void countDownTimerStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    countDownTimer = new CountDownTimer(remainingTime,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            remainingTime = millisUntilFinished;
                            String message = "Time Remaining(s) : "+millisUntilFinished/1000;
                            timerView.setText(message);
                            timerView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinish() {
                            displayFinalResult();
                            timerRunningStatus = false;

                        }
                    };
                    countDownTimer.start();
                    timerRunningStatus =true;
                }catch (Exception exception){
                    Log.e(LOG_TAG,"Error occurred ! " +exception);
                }
            }
        } ;
        handler.postDelayed(runnable,1000);
    }

    //Method to handle timer onFinish action
    private void displayFinalResult(){
        generateResult(spinnerSelection,correctAnswer,randomNumber);
        resultView.setText(result);
        resultView.setVisibility(View.VISIBLE);
        correctAnswerView.setText(correctCarMaker);
        correctAnswerView.setVisibility(View.VISIBLE);

        confirmButton.setText(R.string.next_button_label);
    }

    //Save Current state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (resultView.getVisibility() == View.VISIBLE) {
            outState.putBoolean("reply_visible", true);
            outState.putString("result_text", resultView.getText().toString());
            outState.putString("answer_text", correctAnswerView.getText().toString());
            outState.putString("button_text", confirmButton.getText().toString());
            outState.putInt("random_number", randomNumber);
        }
        if (randomNumber != 0) {
            outState.putInt("image_view", car_image_view.getResources().getIdentifier(resource,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (timerView.getVisibility() ==View.VISIBLE){
            outState.putBoolean("timer_view_visibility",true);
            outState.putBoolean("timer_running_status",timerRunningStatus);
            outState.putLong("remaining_time",remainingTime);
        }
    }

    //Find random car image to set ImageView
    public void setRandomImage() {
        //Generate random number between 1 and 30
        if (randomNumberList.size()==28){
            randomNumberList.clear();
        }
        int number;
        do {
            number = (int) ((Math.random() * ((30 - 1) + 1)) + 1);

        }while (randomNumberList.contains(number));

        randomNumber = number;
        randomNumberList.add(randomNumber);
        Log.d(LOG_TAG, "Random Number : " + randomNumber);
        resource = "car_image_" + randomNumber;
        int resource_id = getResources().getIdentifier(resource,
                "drawable", "com.example.guesscarmaker");
        car_image_view.setImageResource(resource_id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Retrieve the user selected item and assign it to spinner label
        spinnerSelection = parent.getItemAtPosition(position).toString();
        Log.i(LOG_TAG, "User given answer : " + spinnerSelection);

        if (randomNumber < 6 && randomNumber > 0) {
            correctAnswer = "BMW";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        } else if (randomNumber < 11 && randomNumber > 5) {
            correctAnswer = "Audi";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        }else  if (randomNumber<17 && randomNumber>10){
            correctAnswer = "Toyota";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        }else if (randomNumber<19 && randomNumber>16){
            correctAnswer = "Ford";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        }else if (randomNumber<24 && randomNumber>18){
            correctAnswer = "Honda";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        }else if (randomNumber<29 && randomNumber>23){
            correctAnswer = "Mercedes";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        }else if (randomNumber<31 && randomNumber>28){
            correctAnswer = "Suzuki";
            generateResult(spinnerSelection, correctAnswer, randomNumber);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Method to check user given answer and generate final result
    public void generateResult(String selection, String answer, int randomNumber) {
        if (selection.equals(answer)) {
            Log.i(LOG_TAG, "User given answer is CORRECT ! ");
            result = "CORRECT!";
            correctCarMaker = "";
            resultView.setTextColor(Color.parseColor("#008000"));

        } else {
            Log.i(LOG_TAG, "User given answer is NOT CORRECT ! ");
            result = "WRONG!";
            correctCarMaker = "Correct answer is " + answer;
            resultView.setTextColor(Color.parseColor("#990000"));
            correctAnswerView.setTextColor(Color.parseColor("#FBC101"));

        }

    }

    //Method to handle button onClick action
    public void displayResult(View view) {
        String buttonName = confirmButton.getText().toString();
        if (buttonName.equals("Identify")) {

            Log.i(LOG_TAG, "Identify Button Clicked ! ");
            resultView.setText(result);
            resultView.setVisibility(View.VISIBLE);
            correctAnswerView.setText(correctCarMaker);
            correctAnswerView.setVisibility(View.VISIBLE);
            if (countDownTimer!=null) {
                //Stop timer when user clicks Identify button
                countDownTimer.cancel();
                timerRunningStatus = false;
            }

            confirmButton.setText(R.string.next_button_label);
        }
        if (buttonName.equals("Next")) {
            Log.i(LOG_TAG, "Next Button Clicked ! ");
            resultView.setVisibility(View.INVISIBLE);
            correctAnswerView.setVisibility(View.INVISIBLE);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
            confirmButton.setText(R.string.identify_button_label);
        }
    }
}