package com.example.guesscarmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class HintsActivity extends AppCompatActivity {
    private static final String LOG_TAG = HintsActivity.class.getSimpleName();

    private ImageView randomCarImage;
    private TextView wordNeedToGuessView;
    private static String wordNeedToGuess;
    private static String onScreenWord;
    private static char[] guessingWordCharArray;
    private EditText editInput;
    private TextView statusView;
    private TextView hintsResultView;
    private Button hintSubmitButton;
    private final String WINNING_MESSAGE = "CORRECT!";
    private final String LOOSING_MESSAGE = "WRONG!";
    private static int randomNumber;
    private static String resource;
    private static int remainingGuess;
    private static final ArrayList<Integer> randomNumberList = new ArrayList<>();
    private static boolean switchButtonStatus;
    private static Handler handler;
    private static Runnable runnable;
    private TextView timerCountView;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        //Get the intent
        Intent secondIntent = getIntent();
        //Get boolean message from the intent extras
        switchButtonStatus = secondIntent.getBooleanExtra(MainActivity.SWITCH_BUTTON_STATUS, false);

        //Get references to the views
        randomCarImage = findViewById(R.id.identify_car_image_view_2);
        wordNeedToGuessView = findViewById(R.id.word_need_to_guess);
        editInput = findViewById(R.id.edit_input);
        statusView = findViewById(R.id.status_view);
        hintsResultView = findViewById(R.id.hints_result_view);
        hintSubmitButton = findViewById(R.id.hints_submit_button);
        timerCountView = findViewById(R.id.timerCountView);

        //Restore the current state
        if (savedInstanceState != null) {

            //Reload car image
            if (savedInstanceState.getInt("image_view") != 0) {
                randomCarImage.setImageResource(savedInstanceState.getInt("image_view"));
            } else {
                Log.e(LOG_TAG, "Problem ! Unable to reload previous page");
                Toast toast = Toast.makeText(this, R.string.toast_message, Toast.LENGTH_SHORT);
                toast.show();

                //Initialize game
                startGame();
            }

            //Reload TextView data
            if (savedInstanceState.getBoolean("status_view_visible")) {
                statusView.setText(savedInstanceState.getString("status_value"));
                hintSubmitButton.setText(savedInstanceState.getString("button_name"));

                if (savedInstanceState.getBoolean("result_view_visible")) {
                    statusView.setTextColor(Color.parseColor("#990000"));
                } else {
                    statusView.setTextColor(Color.parseColor("#008000"));
                }
                statusView.setVisibility(View.VISIBLE);
            }

            if (savedInstanceState.getBoolean("result_view_visible")) {
                hintsResultView.setText(savedInstanceState.getString("result_value"));
                hintsResultView.setTextColor(Color.parseColor("#FBC101"));
                hintsResultView.setVisibility(View.VISIBLE);
            }

            if (savedInstanceState.getBoolean("guessing_word_visible")) {
                wordNeedToGuessView.setText(savedInstanceState.getString("guessing_word_value"));
            }
        } else {
            if (switchButtonStatus) {
                //Set count down timer
                countDownTimerStart();
            }
            //Initialize the game
            startGame();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (randomNumber != 0) {
            outState.putInt("image_view", randomCarImage.getResources().getIdentifier(resource, "drawable", "com.example.guesscarmaker"));
        }

        if (statusView.getVisibility() == View.VISIBLE) {
            outState.putBoolean("status_view_visible", true);
            outState.putString("status_value", statusView.getText().toString());
            outState.putString("button_name", hintSubmitButton.getText().toString());
        }

        if (hintsResultView.getVisibility() == View.VISIBLE) {
            outState.putBoolean("result_view_visible", true);
            outState.putString("result_value", hintsResultView.getText().toString());
        }
        if (wordNeedToGuessView.length() != 0) {
            outState.putBoolean("guessing_word_visible", true);
            outState.putString("guessing_word_value", wordNeedToGuessView.getText().toString());
        }
    }

    //New method to set timer count down
    private void countDownTimerStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    countDownTimer = new CountDownTimer(20 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String message = "Time Remaining(s) : " + millisUntilFinished / 1000;
                            timerCountView.setText(message);
                            timerCountView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onFinish() {
                            displayFinalResult();

                        }
                    };
                    countDownTimer.start();
                } catch (Exception exception) {
                    Log.e(LOG_TAG, "Exception occurred ! " + exception);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    //Method to display result after times out
    private void displayFinalResult() {
        //Get user enter text value
        if (editInput.length() != 0) {
            String letter = editInput.getText().toString();
            checkCharacter(letter);
            editInput.setText("");
        }

        countDownTimer.start();
        String statusViewTest = statusView.getText().toString();
        if (statusViewTest.equals("CORRECT!")) {
            countDownTimer.cancel();
        }

        if (remainingGuess == 0) {
            //Get text value of user guesses
            String userGivenAnswer = wordNeedToGuessView.getText().toString().toLowerCase();
            char[] charArrayOfUserAnswer = userGivenAnswer.toCharArray();

            StringBuilder stringBuilder = new StringBuilder();
            for (char value : charArrayOfUserAnswer) {
                if (!String.valueOf(value).equals(" ")) {
                    stringBuilder.append(value);
                }
            }

            String correctAnswer = wordNeedToGuess.toLowerCase();

            if (stringBuilder.toString().equals(correctAnswer)) {
                statusView.setText(WINNING_MESSAGE);
                statusView.setTextColor(Color.parseColor("#008000"));
                statusView.setVisibility(View.VISIBLE);

            } else {
                statusView.setText(LOOSING_MESSAGE);
                String answer = "Correct answer is " + wordNeedToGuess;
                hintsResultView.setText(answer);
                statusView.setTextColor(Color.parseColor("#990000"));
                hintsResultView.setTextColor(Color.parseColor("#FBC101"));
                statusView.setVisibility(View.VISIBLE);
                hintsResultView.setVisibility(View.VISIBLE);
            }
            editInput.setEnabled(false);
            hintSubmitButton.setText(R.string.next_button_label);
            countDownTimer.cancel();
        }
    }

    //Method to initialize new game
    private void startGame() {
        //Set submit button test
        hintSubmitButton.setText(R.string.submit_button_label);

        //Generate random number and set random image
        if (randomNumberList.size() == 28) {
            randomNumberList.clear();
        }
        int number;
        do {
            number = (int) ((Math.random() * ((30 - 1) + 1)) + 1);

        } while (randomNumberList.contains(number));

        randomNumber = number;
        randomNumberList.add(randomNumber);
        resource = "car_image_" + randomNumber;
        int resource_id = getResources().getIdentifier(resource, "drawable", "com.example.guesscarmaker");
        randomCarImage.setImageResource(resource_id);

        //Set word need to guess
        if (randomNumber < 6 && randomNumber > 0) {
            wordNeedToGuess = "BMW";
        } else if (randomNumber < 11 && randomNumber > 5) {
            wordNeedToGuess = "Audi";
        } else if (randomNumber < 17 && randomNumber > 10) {
            wordNeedToGuess = "Toyota";
        } else if (randomNumber < 19 && randomNumber > 16) {
            wordNeedToGuess = "Ford";
        } else if (randomNumber < 24 && randomNumber > 18) {
            wordNeedToGuess = "Honda";
        } else if (randomNumber < 29 && randomNumber > 23) {
            wordNeedToGuess = "Mercedes";
        } else if (randomNumber < 31 && randomNumber > 28) {
            wordNeedToGuess = "Suzuki";
        }


        //Initialize character array
        guessingWordCharArray = wordNeedToGuess.toCharArray();

        //Add underscores to the character array
        for (int i = 0; i < guessingWordCharArray.length; i++) {
            guessingWordCharArray[i] = '_';
        }

        //Initialize a string from character array
        onScreenWord = String.valueOf(guessingWordCharArray);

        //Dispaly word with underscores
        displayWord();

        //Set Edit text field as empty field
        editInput.setText("");

        //Initialize tne number of incorrect guesses
        remainingGuess = 3;

        //Set status view  and result view invisible
        statusView.setVisibility(View.INVISIBLE);
        hintsResultView.setVisibility(View.INVISIBLE);
    }

    //Create a method to replace user correct guesses with underscores
    private void replaceLetterInWord(String letter) {
        String letterInLowerCase = letter.toLowerCase();
        String wordInLowerCase = wordNeedToGuess.toLowerCase();
        int letterIndex = wordInLowerCase.indexOf(letterInLowerCase);

        //Find position if index is 0 or positive
        while (letterIndex >= 0) {
            guessingWordCharArray[letterIndex] = wordNeedToGuess.charAt(letterIndex);
            //reset letterIndex with next index value
            letterIndex = wordInLowerCase.indexOf(letterInLowerCase, letterIndex + 1);
        }

        //Update the string value of displayed text
        onScreenWord = String.valueOf(guessingWordCharArray);
    }

    //Create a method to display word on screen
    private void displayWord() {
        StringBuilder guessingWord = new StringBuilder();
        for (char letter : guessingWordCharArray) {
            guessingWord.append(letter).append(" ");
        }

        //Set guessing word in the TextField
        wordNeedToGuessView.setText(guessingWord);
    }

    //Method to handle button onClick action
    public void confirmGuessLetter(View view) {
        String buttonText = hintSubmitButton.getText().toString();
        if (buttonText.equals("Submit")) {
            if (editInput.length() != 0) {
                String letter = editInput.getText().toString();
                checkCharacter(letter);
                editInput.setText("");
            }
            if (countDownTimer != null) {
                countDownTimer.cancel();
                if (statusView.getText().toString().equals("CORRECT!")) {
                    countDownTimer.cancel();
                } else {
                    if (remainingGuess > 0) {
                        countDownTimer.start();
                    }
                }
            }
        }

        if (buttonText.equals("Next")) {
            //Start new game
            // startGame();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    //Method to check user given letter
    private void checkCharacter(String character) {
        String targetWord = wordNeedToGuess.toLowerCase();
        String displayWord = onScreenWord.toLowerCase();
        String letter = character.toLowerCase();

        //Check user enter character is in the word
        if (targetWord.indexOf(letter) >= 0) {
            //Check that character already displayed or not
            if (displayWord.indexOf(letter) < 0) {
                //Replace undersore with character
                replaceLetterInWord(character);

                //Update changes on screen
                displayWord();

                //Check is user win the game
                if (!onScreenWord.contains("_")) {
                    statusView.setText(WINNING_MESSAGE);
                    statusView.setTextColor(Color.parseColor("#008000"));
                    statusView.setVisibility(View.VISIBLE);
                    hintSubmitButton.setText(R.string.next_button_label);
                    editInput.setEnabled(false);
                }
            }
        } else {
            //Decrease remaining guesses by one
            remainingGuess -= 1;

            //Check is user lost the game
            if (remainingGuess == 0) {
                statusView.setText(LOOSING_MESSAGE);
                String correctAnswer = "Correct answer is " + wordNeedToGuess;
                hintsResultView.setText(correctAnswer);
                statusView.setTextColor(Color.parseColor("#990000"));
                hintsResultView.setTextColor(Color.parseColor("#FBC101"));
                statusView.setVisibility(View.VISIBLE);
                hintsResultView.setVisibility(View.VISIBLE);
                editInput.setEnabled(false);
                hintSubmitButton.setText(R.string.next_button_label);
            }
        }
    }
}
