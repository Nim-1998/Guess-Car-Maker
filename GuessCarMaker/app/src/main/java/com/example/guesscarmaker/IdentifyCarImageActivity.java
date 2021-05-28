package com.example.guesscarmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class IdentifyCarImageActivity extends AppCompatActivity {
    private static final String LOG_TAG = IdentifyCarImageActivity.class.getSimpleName();
    private static final long SET_TIMER_TIME = 20*1000;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private TextView resultView;
    private TextView answerView;
    private Button nextButton;
    private TextView findImageNameView;
    private static String findImageName;
    private TextView descriptionLabel;
    private static final ArrayList<Integer> randomNumberList = new ArrayList<>();
    private static final ArrayList<String> carMakerList = new ArrayList<>();
    private static int randomNo_1,randomNo_2,randomNo_3;
    private static String resource_1,resource_2,resource_3;
    private String userGivenAnswer;
    private static Handler handler;
    private static Runnable runnable;
    private CountDownTimer countDownTimer;
    private TextView timerView;
    private long remainingTime = SET_TIMER_TIME;
    private boolean timerRunningStatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);

        //Get reference to the views
        imageView1 = findViewById(R.id.car_image_1);
        imageView2 = findViewById(R.id.car_image_2);
        imageView3 = findViewById(R.id.car_image_3);
        resultView = findViewById(R.id.result_text_view);
        answerView = findViewById(R.id.correct_answer_text_view);
        nextButton = findViewById(R.id.nextButton);
        findImageNameView = findViewById(R.id.correct_car_maker_label);
        timerView = findViewById(R.id.timer_view_car_image_activity);

        //Get the intent
        Intent thirdIntent = getIntent();
        //Get boolean message from the intent extras
        boolean switchButtonStatus = thirdIntent.getBooleanExtra(MainActivity.SWITCH_BUTTON_STATUS,false);
        Log.i(LOG_TAG,"Button : "+switchButtonStatus);

        //Restore the current state
        if (savedInstanceState != null){

            //Load car images
            if (savedInstanceState.getInt("image_view_1")!= 0){
                imageView1.setImageResource(savedInstanceState.getInt("image_view_1"));
            }
            if (savedInstanceState.getInt("image_view_2")!= 0){
                imageView2.setImageResource(savedInstanceState.getInt("image_view_2"));
            }
            if (savedInstanceState.getInt("image_view_3")!= 0){
                imageView3.setImageResource(savedInstanceState.getInt("image_view_3"));
            }

            //Load Image view clicked status
            if (savedInstanceState.getBoolean("image_1_selected")){
                imageView1.setClickable(false);
                imageView2.setClickable(false);
                imageView3.setClickable(false);
            }
            if (savedInstanceState.getBoolean("image_2_selected")){
                imageView1.setClickable(false);
                imageView2.setClickable(false);
                imageView3.setClickable(false);
            }
            if (savedInstanceState.getBoolean("image_3_selected")){
                imageView1.setClickable(false);
                imageView2.setClickable(false);
                imageView3.setClickable(false);
            }

            //Load Text view data
            if (savedInstanceState.getBoolean("find_image_name_view_visibility")){
                findImageNameView.setText(savedInstanceState.getString("find_image_name"));
            }
            if (savedInstanceState.getBoolean("result_view_visibility")){
                resultView.setText(savedInstanceState.getString("result_view_value"));
                if (savedInstanceState.getBoolean("answer_view_visibility")){
                    resultView.setTextColor(Color.parseColor("#990000"));
                }else{
                    resultView.setTextColor(Color.parseColor("#008000"));
                }
                resultView.setVisibility(View.VISIBLE);
            }

            if (savedInstanceState.getBoolean("answer_view_visibility")){
                answerView.setText(savedInstanceState.getString("answer_view_value"));
                answerView.setTextColor(Color.parseColor("#FBC101"));
                answerView.setVisibility(View.VISIBLE);
            }

            //Set  timer view visibility
            if (savedInstanceState.getBoolean("timer_view_visibility")){
                timerRunningStatus = savedInstanceState.getBoolean("timer_running_status");
                remainingTime = savedInstanceState.getLong("remaining_time");
                timerView.setText(savedInstanceState.getString("timer_view_value"));
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
            //Initialize the new game
            initializeGame();
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
                            resultView.setText(R.string.result_wrong);
                            resultView.setTextColor(Color.parseColor("#990000"));
                            String answer = "Correct answer is "+findImageName;
                            answerView.setText(answer);
                            answerView.setTextColor(Color.parseColor("#FBC101"));
                            resultView.setVisibility(View.VISIBLE);
                            answerView.setVisibility(View.VISIBLE);
                            timerRunningStatus = false;
                        }
                    };
                    countDownTimer.start();
                    timerRunningStatus = true;
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

        if (findImageNameView.length()!=0){
            outState.putBoolean("find_image_name_view_visibility",true);
            outState.putString("find_image_name",findImageNameView.getText().toString());
        }
        if (resultView.getVisibility() == View.VISIBLE){
            outState.putBoolean("result_view_visibility" ,true);
            outState.putString("result_view_value",resultView.getText().toString());
        }
        if (answerView.getVisibility() ==View.VISIBLE){
            outState.putBoolean("answer_view_visibility",true);
            outState.putString("answer_view_value",answerView.getText().toString());
        }
        if (randomNo_1 !=0){
            outState.putInt("image_view_1",imageView1.getResources().getIdentifier(resource_1,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (randomNo_2 !=0){
            outState.putInt("image_view_2",imageView1.getResources().getIdentifier(resource_2,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (randomNo_3 !=0){
            outState.putInt("image_view_3",imageView1.getResources().getIdentifier(resource_3,
                    "drawable", "com.example.guesscarmaker"));
        }
        if (imageView1.isPressed()){
            outState.putBoolean("image_1_selected",true);
        }
        if (imageView2.isPressed()){
            outState.putBoolean("image_2_selected",true);
        }
        if (imageView3.isPressed()){
            outState.putBoolean("image_3_selected",true);
        }
        if (timerView.getVisibility() == View.VISIBLE){
            outState.putBoolean("timer_view_visibility",true);
            outState.putBoolean("timer_running_status",timerRunningStatus);
            outState.putLong("remaining_time",remainingTime);
        }
    }

    //New method to initialize the new game
    private void initializeGame() {
        //Set result view and correct answer view invisible
        resultView.setVisibility(View.INVISIBLE);
        answerView.setVisibility(View.INVISIBLE);
        imageView1.setClickable(true);
        imageView2.setClickable(true);
        imageView3.setClickable(true);

        if (randomNumberList.size()==28){
            randomNumberList.clear();
        }

        carMakerList.clear();

        //Generate random car image 1
        randomNo_1 = generateRandomNumber();
        resource_1 = setRandomImage(1,randomNo_1);

        //Generate random car image 2
        randomNo_2 =  generateRandomNumber();
        resource_2 = setRandomImage(2,randomNo_2);

        //Generate random car image 2
        randomNo_3 = generateRandomNumber();
        resource_3 = setRandomImage(3,randomNo_3);

        //Set find image name
        int correctAnswerPosition = (int) ((Math.random() * ((3 - 1) + 1)) + 1);
        String findName = carMakerList.get(correctAnswerPosition-1);
        findImageName = findName;
        findImageNameView.setText(findImageName);
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
            imageView = imageView1;
        }else if (position==2){
            imageView = imageView2;
        }else {
            imageView = imageView3;
        }
        Log.d(LOG_TAG, "Image view : " + position+" successfully set !");
        String resource = "car_image_" + generateNumber;
        int resource_id = getResources().getIdentifier(resource, "drawable", "com.example.guesscarmaker");
        imageView.setImageResource(resource_id);
        return resource;
    }

    //Check user given answer and display result
    private void checkAnswer(String selectAnswer){

        // set result views
        if (selectAnswer.equals(findImageName)){
            resultView.setText(R.string.result_correct);
            resultView.setTextColor(Color.parseColor("#008000"));
            resultView.setVisibility(View.VISIBLE);

        }else {
            resultView.setText(R.string.result_wrong);
            resultView.setTextColor(Color.parseColor("#990000"));
            String answer = "Correct answer is "+findImageName;
            answerView.setText(answer);
            answerView.setTextColor(Color.parseColor("#FBC101"));
            resultView.setVisibility(View.VISIBLE);
            answerView.setVisibility(View.VISIBLE);
        }

    }

    //Method to handle image onclick action
    public void confirmAnswer(View view) {
        if (imageView1.isPressed()){
            //userSelectAnswerNo = randomNo_1;
            userGivenAnswer = findCarMakerName(randomNo_1);
            Log.i(LOG_TAG,"Image 01 clicked : "+userGivenAnswer);
            imageView1.setClickable(false);
            imageView2.setClickable(false);
            imageView3.setClickable(false);

        }else if (imageView2.isPressed()){
            userGivenAnswer = findCarMakerName(randomNo_2);
            Log.i(LOG_TAG,"Image 02 clicked : "+userGivenAnswer);
            //Lock image views
            imageView1.setClickable(false);
            imageView2.setClickable(false);
            imageView3.setClickable(false);

        }else if (imageView3.isPressed()){
            userGivenAnswer = findCarMakerName(randomNo_3);
            Log.i(LOG_TAG,"Image 03 clicked : "+userGivenAnswer);
            imageView1.setClickable(false);
            imageView2.setClickable(false);
            imageView3.setClickable(false);
        }
        checkAnswer(userGivenAnswer);

        //Stop timer when user selects any car image
        if (countDownTimer != null){
            countDownTimer.cancel();
            timerRunningStatus = false;
        }
    }

    //Set button onClick action to start a new game
    public void startNewGame(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}