package com.example.guesscarmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdvancedLevelActivity extends AppCompatActivity {
    private static final String LOG_TAG = AdvancedLevelActivity.class.getSimpleName();
    private static final long SET_TIMER_TIME = 20*1000;

    private ImageView carImage_1,carImage_2,carImage_3;
    private EditText inputField_1,inputField_2,inputField_3;
    private TextView correctAnswerView_1,correctAnswerView_2,correctAnswerView_3;
    private TextView resultView;
    private TextView pointView;
    private Button submitButton;
    private static String correctAnswer_1,correctAnswer_2,correctAnswer_3;
    private static String imageResource_1,imageResource_2,imageResource_3;
    private static int randomNo_1,randomNo_2,randomNo_3;
    private static final ArrayList<Integer> randomNumberList = new ArrayList<>();
    private static final ArrayList<String> carMakerList = new ArrayList<>();
    private static int triesLeft,noOfIncorrectAnswers,points;
    private static Handler handler;
    private static Runnable runnable;
    private CountDownTimer countDownTimer;
    private TextView timerView;
    private long remainingTime = SET_TIMER_TIME;
    private boolean timerRunningStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        //Get reference to the view
        carImage_1 = findViewById(R.id.image_1);
        carImage_2 = findViewById(R.id.image_2);
        carImage_3 = findViewById(R.id.image_3);
        inputField_1 = findViewById(R.id.input_field_1);
        inputField_2 = findViewById(R.id.input_field_2);
        inputField_3 = findViewById(R.id.input_field_3);
        correctAnswerView_1 = findViewById(R.id.correct_answer_view_1);
        correctAnswerView_2 = findViewById(R.id.correct_answer_view_2);
        correctAnswerView_3 = findViewById(R.id.correct_answer_view_3);
        resultView = findViewById(R.id.result_text_view);
        pointView = findViewById(R.id.point_view);
        submitButton = findViewById(R.id.submit_answers_button);
        timerView = findViewById(R.id.timer_view_advanced_level);

        //Get the intent
        Intent fourthIntent = getIntent();
        //Get boolean message from the intent extras
        boolean switchButtonStatus = fourthIntent.getBooleanExtra(MainActivity.SWITCH_BUTTON_STATUS,false);

        if (savedInstanceState != null){

            //Load image views
            if (savedInstanceState.getInt("image_view_1") !=0){
                carImage_1.setImageResource(savedInstanceState.getInt("image_view_1"));
            }
            if (savedInstanceState.getInt("image_view_2") !=0){
                carImage_2.setImageResource(savedInstanceState.getInt("image_view_2"));
            }
            if (savedInstanceState.getInt("image_view_3") !=0){
                carImage_3.setImageResource(savedInstanceState.getInt("image_view_3"));
            }

            //Load correct answer views
            if (savedInstanceState.getBoolean("correct_answer_1_visibility")){
                correctAnswerView_1.setText(savedInstanceState.getString("correct_answer_1"));
                correctAnswerView_1.setVisibility(View.VISIBLE);
            }
            if (savedInstanceState.getBoolean("correct_answer_2_visibility")){
                correctAnswerView_2.setText(savedInstanceState.getString("correct_answer_2"));
                correctAnswerView_2.setVisibility(View.VISIBLE);
            }
            if (savedInstanceState.getBoolean("correct_answer_3_visibility")){
                correctAnswerView_3.setText(savedInstanceState.getString("correct_answer_3"));
                correctAnswerView_3.setVisibility(View.VISIBLE);
            }
            if (savedInstanceState.getBoolean("result_view_visibility")){
                resultView.setText(savedInstanceState.getString("result_view_value"));
                resultView.setVisibility(View.VISIBLE);
                resultView.setTextColor(savedInstanceState.getInt("result_view_text_color"));
                submitButton.setText(savedInstanceState.getString("button_text"));
            }
            if (savedInstanceState.getBoolean("field_1")){
                inputField_1.setHintTextColor(savedInstanceState.getInt("field_1_hintTextColor"));
                inputField_1.setTextColor(savedInstanceState.getInt("field_1_textColor"));
                if (savedInstanceState.getInt("field_1_textColor")==-16744448){
                    inputField_1.setEnabled(false);
                }
            }
            if (savedInstanceState.getBoolean("field_2")){
                inputField_2.setHintTextColor(savedInstanceState.getInt("field_2_hintTextColor"));
                inputField_2.setTextColor(savedInstanceState.getInt("field_2_textColor"));
                if (savedInstanceState.getInt("field_2_textColor")==-16744448){
                    inputField_2.setEnabled(false);
                }
            }
            if (savedInstanceState.getBoolean("field_3")){
                inputField_3.setHintTextColor(savedInstanceState.getInt("field_3_hintTextColor"));
                inputField_3.setTextColor(savedInstanceState.getInt("field_3_textColor"));
                if (savedInstanceState.getInt("field_3_textColor")==-16744448){
                    inputField_3.setEnabled(false);
                }
            }
            if (savedInstanceState.getBoolean("point_view_visibility")){
                pointView.setText(savedInstanceState.getString("point_view_value"));
                pointView.setVisibility(View.VISIBLE);
            }

            //Set  timer view visibility
            if (savedInstanceState.getBoolean("timer_view_visibility")){
                timerRunningStatus = savedInstanceState.getBoolean("timer_running_status");
                remainingTime = savedInstanceState.getLong("remaining_time");
                timerView.setVisibility(View.VISIBLE);
                if (timerRunningStatus){
                    countDownTimerStart();
                }
            }

        }else {
            if (switchButtonStatus){
                //Set count down timer
                countDownTimerStart();
            }
            //Initialize game
            startGame();
        }
    }

    //Method to set timer count down
    private void countDownTimerStart() {
        handler = new Handler();
        runnable =  new Runnable() {
            @Override
            public void run() {
                try {
                    countDownTimer =  new CountDownTimer(remainingTime,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            remainingTime = millisUntilFinished;
                            String message = "Time Remaining(s) : "+millisUntilFinished/1000;
                            timerView.setText(message);
                            timerView.setVisibility(View.VISIBLE);
                            timerRunningStatus = true;
                        }

                        @Override
                        public void onFinish() {
                            setTimerFinishResult();
                            timerRunningStatus = false;
                        }
                    };
                    countDownTimer.start();
                   // timerRunningStatus = true;
                }catch (Exception exception){
                    Log.e(LOG_TAG,"Error occurred ! " +exception);
                }
            }
        };
        handler.postDelayed(runnable,1000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saved remaining attempts and number of incorrect answers
         outState.putInt("remaining_attempts",triesLeft);
         outState.putInt("incorrect_answers",noOfIncorrectAnswers);

        if (randomNo_1 !=0){
            outState.putInt("image_view_1",carImage_1.getResources().getIdentifier(imageResource_1,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (randomNo_2 !=0){
            outState.putInt("image_view_2",carImage_2.getResources().getIdentifier(imageResource_2,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (randomNo_3 !=0){
            outState.putInt("image_view_3",carImage_3.getResources().getIdentifier(imageResource_3,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (correctAnswerView_1.getVisibility() == View.VISIBLE){
            outState.putBoolean("correct_answer_1_visibility",true);
            outState.putString("correct_answer_1",correctAnswerView_1.getText().toString());
        }
        if (correctAnswerView_2.getVisibility() == View.VISIBLE){
            outState.putBoolean("correct_answer_2_visibility",true);
            outState.putString("correct_answer_2",correctAnswerView_2.getText().toString());
        }
        if (correctAnswerView_3.getVisibility() == View.VISIBLE){
            outState.putBoolean("correct_answer_3_visibility",true);
            outState.putString("correct_answer_3",correctAnswerView_3.getText().toString());
        }
        if (resultView.getVisibility() == View.VISIBLE){
            outState.putBoolean("result_view_visibility",true);
            outState.putString("result_view_value",resultView.getText().toString());
            outState.putInt("result_view_text_color",resultView.getCurrentTextColor());
            outState.putString("button_text",submitButton.getText().toString());
        }
        if (inputField_1.length() >=0){
            outState.putBoolean("field_1",true);
            outState.putInt("field_1_textColor",inputField_1.getCurrentTextColor());
            outState.putInt("field_1_hintTextColor",inputField_1.getCurrentHintTextColor());
        }
        if (inputField_2.length()>=0){
            outState.putBoolean("field_2",true);
            outState.putInt("field_2_textColor",inputField_2.getCurrentTextColor());
            outState.putInt("field_2_hintTextColor",inputField_2.getCurrentHintTextColor());

        }
        if (inputField_3.length()>=0){
            outState.putBoolean("field_3",true);
            outState.putInt("field_3_textColor",inputField_3.getCurrentTextColor());
            outState.putInt("field_3_hintTextColor",inputField_3.getCurrentHintTextColor());

        }
        if (pointView.getVisibility() == View.VISIBLE){
            outState.putBoolean("point_view_visibility",true);
            outState.putString("point_view_value",pointView.getText().toString());
            outState.putInt("score",points);
        }
        if (timerView.getVisibility() == View.VISIBLE){
            outState.putBoolean("timer_view_visibility",true);
            outState.putBoolean("timer_running_status",timerRunningStatus);
            outState.putLong("remaining_time",remainingTime);
        }
    }

    //Method to initialize new game
    private void startGame() {
        //Set Text view's visibility and values
        correctAnswerView_1.setVisibility(View.INVISIBLE);
        correctAnswerView_2.setVisibility(View.INVISIBLE);
        correctAnswerView_3.setVisibility(View.INVISIBLE);
        resultView.setVisibility(View.INVISIBLE);
        pointView.setVisibility(View.INVISIBLE);
        submitButton.setText("Submit");
        inputField_1.setText("");
        inputField_2.setText("");
        inputField_3.setText("");

        //Set Edit text fields editable
        inputField_1.setEnabled(true);
        inputField_2.setEnabled(true);
        inputField_3.setEnabled(true);

        //Generate random images
        if (randomNumberList.size()==28){
            randomNumberList.clear();
        }

        carMakerList.clear();

        //Set random car image 1
        randomNo_1 = generateRandomNumber();
        imageResource_1 = setRandomImage(1,randomNo_1);
        correctAnswer_1 =findCarMakerName(randomNo_1);

        //Set random car image 2
        randomNo_2 = generateRandomNumber();
        imageResource_2 = setRandomImage(2,randomNo_2);
        correctAnswer_2 = findCarMakerName(randomNo_2);

        //Set random car image 3
        randomNo_3 = generateRandomNumber();
        imageResource_3 = setRandomImage(3,randomNo_3);
        correctAnswer_3 = findCarMakerName(randomNo_3);

        //Initialize tne number of incorrect guesses
        noOfIncorrectAnswers = 0;

        //Initialize the number of tries left
        triesLeft = 3;

        //Initialize the number of points
        points = 0;
    }

    //Method to get car maker name
    private String findCarMakerName(int number){
        String carMaker = "";
        if (number<6 && number>0){
            carMaker = "BMW";
        }else if (number<11 && number>5){
            carMaker = "Audi";
        }else  if (number<17 && number>10){
            carMaker = "Toyota";
        }else if (number<19 && number>16){
            carMaker = "Ford";
        }else if (number<24 && number>18){
            carMaker = "Honda";
        }else if (number<29 && number>23){
            carMaker = "Mercedes";
        }else if (number<31 && number>28){
            carMaker = "Suzuki";
        }else {
            carMaker = "";
        }
        return  carMaker;
    }

    //Method to generate random number
    private int generateRandomNumber() {
        int generateNumber;
        String carMaker;
        do {
            generateNumber = (int) ((Math.random() * ((30 - 1) + 1)) + 1);
            carMaker = findCarMakerName(generateNumber);
        } while (randomNumberList.contains(generateNumber) || carMakerList.contains(carMaker));

        randomNumberList.add(generateNumber);
        carMakerList.add(carMaker);
        return generateNumber;
    }

    //Method to set random image into the view
    private String setRandomImage(int position,int generateNumber){

        ImageView imageView ;
        if (position==1){
            imageView = carImage_1;
        }else if (position==2){
            imageView = carImage_2;
        }else {
            imageView = carImage_3;
        }
        Log.d(LOG_TAG, "Image view : " + position+" successfully set");
        String resource = "car_image_" + generateNumber;
        int resource_id = getResources().getIdentifier(resource, "drawable", "com.example.guesscarmaker");
        imageView.setImageResource(resource_id);
        return resource;
    }

    //Method to handle button onClick action
    public void submitInputAnswers(View view) {
        String buttonStringValue = submitButton.getText().toString();
        if (buttonStringValue.equals("Submit")){
           clickSubmitButton();
           if (countDownTimer != null) {
               countDownTimer.cancel();
               timerRunningStatus = false;
              if (triesLeft >0){
                  remainingTime = SET_TIMER_TIME;
                  countDownTimerStart();
                  timerRunningStatus = true;
              }
           }
        }

        if (buttonStringValue.equals("Next")){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    //New method to get Input field value
    private String getInput(EditText editText) {
        String inputValue = "";

        if (editText.isEnabled()){
            //Get input field value if input field is editable
            inputValue = editText.getText().toString().toLowerCase();
        }
        return inputValue;
    }

    //New method to check user given answer
    private void checkInput(String userGivenAnswer,int position) {
        String correctAnswer;
        if (position == 1){
            correctAnswer = correctAnswer_1.toLowerCase();
        }else if (position == 2){
            correctAnswer = correctAnswer_2.toLowerCase();
        }else {
            correctAnswer = correctAnswer_3.toLowerCase();
        }

        EditText editText;
        if (position == 1){
            editText = inputField_1;
        }else if (position == 2 ){
            editText = inputField_2;
        }else {
            editText = inputField_3;
        }
        //Check user given answer
        if (editText.isEnabled()){
            //Do this if Edit field is editable
            if (correctAnswer.equals(userGivenAnswer)){
                editText.setEnabled(false);
                editText.setTextColor(Color.parseColor("#008000"));
                points = points+1;

            }else{
                noOfIncorrectAnswers = noOfIncorrectAnswers+1;
                editText.setTextColor(Color.parseColor("#990000"));
                editText.setHintTextColor(Color.parseColor("#990000"));

                if (triesLeft==1){
                    //Display result  : WRONG
                    resultView.setText(R.string.result_wrong);
                    resultView.setTextColor(Color.parseColor("#990000"));
                    resultView.setVisibility(View.VISIBLE);
                    submitButton.setText(R.string.next_button_label);
                }
            }
        }
    }

    //Display correct answers
    private void displayCorrectAnswer() {
        if (inputField_1.isEnabled()){
            //Do this if input fields are editable
            correctAnswerView_1.setText(correctAnswer_1);
            correctAnswerView_1.setVisibility(View.VISIBLE);
        }
        if (inputField_2.isEnabled()){
            correctAnswerView_2.setText(correctAnswer_2);
            correctAnswerView_2.setVisibility(View.VISIBLE);
        }
        if (inputField_3.isEnabled()){
            correctAnswerView_3.setText(correctAnswer_3);
            correctAnswerView_3.setVisibility(View.VISIBLE);
        }
    }

    //Display CORRECT message if all inputs are correct
    private void displayCorrectStatus(){
        if (!inputField_1.isEnabled()){
            if (!inputField_2.isEnabled()){
                if (!inputField_3.isEnabled()){
                    resultView.setText(R.string.result_correct);
                    resultView.setTextColor(Color.parseColor("#008000"));
                    resultView.setVisibility(View.VISIBLE);
                    submitButton.setText(R.string.next_button_label);
                }
            }
        }
    }

    //New method to handle Submit button action
    private void clickSubmitButton(){
        //Get input fields values
        String userGivenAnswer_1 = getInput(inputField_1);
        String userGivenAnswer_2 = getInput(inputField_2);
        String userGivenAnswer_3 = getInput(inputField_3);

        //Check inputs
        checkInput(userGivenAnswer_1,1);
        checkInput(userGivenAnswer_2,2);
        checkInput(userGivenAnswer_3,3);

        //Display correct answers
        if (triesLeft==1) {
            displayCorrectAnswer();
        }

        displayCorrectStatus();
        String button = submitButton.getText().toString();
        if (button.equals("Next")){
            inputField_1.setEnabled(false);
            inputField_2.setEnabled(false);
            inputField_3.setEnabled(false);
        }
        //Decrease remaining tries by one
        if (noOfIncorrectAnswers>0){
            triesLeft = triesLeft-1;
        }

        //Display Score
        String totalView = "Score : "+points;
        pointView.setText(totalView);
        pointView.setVisibility(View.VISIBLE);
    }

    //New method to set timer onFinish action
    private void setTimerFinishResult(){
        if (triesLeft<=0){
            countDownTimer.cancel();
            timerRunningStatus = false;
       }

        String userGivenAnswer_1 = getInput(inputField_1);
        String userGivenAnswer_2 = getInput(inputField_2);
        String userGivenAnswer_3 = getInput(inputField_3);

        //Check inputs
        checkInput(userGivenAnswer_1,1);
        checkInput(userGivenAnswer_2,2);
        checkInput(userGivenAnswer_3,3);

        remainingTime = SET_TIMER_TIME;
        if (triesLeft>1) {
            countDownTimerStart();
        }
        timerRunningStatus = true;

        //Decrease remaining tries by one
        if (noOfIncorrectAnswers>0){
            triesLeft = triesLeft-1;
        }

        //Display Score
        String totalView = "Score : "+points;
        pointView.setText(totalView);
        pointView.setVisibility(View.VISIBLE);
        if (triesLeft == 3){
            displayCorrectStatus();
            countDownTimer.cancel();
            timerRunningStatus = false;
        }

        if (triesLeft ==0){
            checkInput(userGivenAnswer_1,1);
            checkInput(userGivenAnswer_2,2);
            checkInput(userGivenAnswer_3,3);
            //Display correct answers
            displayCorrectAnswer();
            displayCorrectStatus();
            submitButton.setText(R.string.next_button_label);
            String button = submitButton.getText().toString();
            if (button.equals("Next")){
                inputField_1.setEnabled(false);
                inputField_2.setEnabled(false);
                inputField_3.setEnabled(false);
            }
            countDownTimer.cancel();
            timerRunningStatus = false;
        }
    }
}