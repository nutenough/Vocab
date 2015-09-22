package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;


public class TextInputLearnActivity extends AppCompatActivity {


    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language_text_view;
    private TextView vocab_knowing_language_text_view;
    private Button vocab_counter_button;
    private ImageButton imageButton_exit;
    private boolean isBackVisible = false;
    private Handler handler;
    private ArrayList<VocItem> allVocab;
    private int counter_vocab = 0;
    private int counter_vocab_num = 1;
    private int listSize;
    private FloatingActionButton vocab_fab;
    private EditText editTextInput;
    private String textInput;
    private ImageView imageView;
    private int click_counter_fab = 0;
    private TextView textViewInput;
    private int counter_correct_answer = 0;
    private String frontCard = "";
    private String backCard ="";
    private double randomNum;


    private AlertDialog alertDialog;

    private DBAdmin dbAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input_learn);
        onBackPressed();
        initDB();
        fillListFromDB();
        learnDirection();
        initUIElements();
        setupVocabAnimation();
        setupExitButton();

    }

    private void learnDirection() {
        Bundle bundle = getIntent().getExtras();
        boolean knowing_to_learning = bundle.getBoolean("knowingToLearning");
        boolean learning_to_knowing = bundle.getBoolean("learningToKnowing");
        boolean mixed = bundle.getBoolean("mixed");

        if (knowing_to_learning){
            frontCard = allVocab.get(counter_vocab).getSourceVocab();
            backCard = allVocab.get(counter_vocab).getTargetVocab();
        }

        if (learning_to_knowing){
            backCard = allVocab.get(counter_vocab).getSourceVocab();
            frontCard = allVocab.get(counter_vocab).getTargetVocab();
        }

        if (mixed){

            randomNum =  Math.random();

            if(randomNum < 0.5) {
                backCard = allVocab.get(counter_vocab).getSourceVocab();
                frontCard = allVocab.get(counter_vocab).getTargetVocab();
            }
            if(randomNum >= 0.5) {
                frontCard = allVocab.get(counter_vocab).getSourceVocab();
                backCard = allVocab.get(counter_vocab).getTargetVocab();
            }
        }
    }

    private void compareSolution(){
        textInput = editTextInput.getText().toString();
        String currentSolution = allVocab.get(counter_vocab).getTargetVocab();
        textInput.trim();
        currentSolution.trim();

        if(currentSolution.trim().equalsIgnoreCase(textInput.trim())){
            imageView.setImageResource(R.drawable.smiley_happy);
            imageView.setVisibility(View.VISIBLE);
            counter_correct_answer++;
        }else{
            imageView.setImageResource(R.drawable.smiley_question);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateNumCounter() {
        counter_vocab_num++;
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);
    }

    private void fillListFromDB(){
        allVocab = new ArrayList<>();
        allVocab.addAll(dbAdmin.getAllVocab());
        Log.d("Learn", "allItems: " + allVocab);
        listSize = allVocab.size();
    }

    private void initDB(){
        dbAdmin = new DBAdmin(this);
        dbAdmin.open();
    }

    protected void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void initUIElements() {
        vocab_knowing_language = (RelativeLayout)findViewById(R.id.vocab_knowing_language);
        vocab_learning_language_text_view = (TextView)findViewById(R.id.vocab_learning_language_text_view);
        vocab_knowing_language_text_view =(TextView)findViewById(R.id.vocab_knowing_language_text_view);

        vocab_knowing_language_text_view.setText(frontCard);
        vocab_learning_language_text_view.setText(backCard);

        vocab_counter_button = (Button) findViewById(R.id.vocab_counter);
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);
        vocab_fab = (FloatingActionButton) findViewById(R.id.vocab_fab_next);
        editTextInput = (EditText)findViewById(R.id.vocab_edit_text);
        imageView = (ImageView) findViewById(R.id.image_view_correct);
        imageView.setVisibility(View.INVISIBLE);

        textViewInput = (TextView) findViewById(R.id.vocab_text_view);

        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);
    }

    private void setupVocabAnimation(){

        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);

        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);


        vocab_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isBackVisible) {

                    vocab_fab.setClickable(false);

                    setRightOut.setTarget(vocab_knowing_language_text_view);
                    setLeftIn.setTarget(vocab_learning_language_text_view);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            vocab_fab.setClickable(true);
                        }
                    }, 500);
                }

                if (click_counter_fab == 0) {
                    removeKeyboard();
                    editTextToTextView();
                    vocab_fab.setImageResource(R.drawable.ic_arrow_right);
                    compareSolution();
                    click_counter_fab++;
                } else {
                    counter_vocab++;

                    if (counter_vocab < listSize) {
                        vocab_fab.setClickable(false);

                        setRightOut.setTarget(vocab_learning_language_text_view);
                        setLeftIn.setTarget(vocab_knowing_language_text_view);
                        setRightOut.start();
                        setLeftIn.start();
                        isBackVisible = false;

                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                learnDirection();
                                vocab_knowing_language_text_view.setText(frontCard);
                                vocab_learning_language_text_view.setText(backCard);
                                click_counter_fab--;

                                textViewToEditText();

                                vocab_fab.setImageResource(R.drawable.ic_done);
                                imageView.setVisibility(View.INVISIBLE);

                                updateNumCounter();
                            }
                        }, 250);

                    } else {
                        showAlertDialog();
                    }
                }


                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vocab_fab.setClickable(true);
                    }
                }, 500);
            }
        });
    }



    private void showAlertDialog() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(TextInputLearnActivity.this).inflate(R.layout.result_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TextInputLearnActivity.this);
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(false);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_trophies);
        alertDialogBuilder.setTitle(R.string.result);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.finished, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // Edit Design alertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.color_primary));

        String sourceString = "Glückwunsch!" + "<br>" + "Du hast " + "<b>" + counter_correct_answer +  " / "  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        TextView textView_result = (TextView) alertDialog.findViewById(R.id.textView_result);
        textView_result.setText(Html.fromHtml(sourceString));
        RatingBar ratingBar = (RatingBar) alertDialog.findViewById(R.id.ratingBarResult);

        int numStars = 5;
        float rating = (float)(counter_correct_answer / listSize *numStars);

        ratingBar.setRating(rating);
        ratingBar.setStepSize(0.1f);
    }


    private void setupExitButton() {
        imageButton_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogExit();

            }
        });
    }

    private void showAlertDialogExit() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(TextInputLearnActivity.this).inflate(R.layout.exit_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TextInputLearnActivity.this);
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_exit);
        alertDialogBuilder.setTitle(R.string.exit_title);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.exit_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.language_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        // Edit Design alertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.color_primary));

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.color_primary));

        String sourceString1 = "Wollen Sie die Abfrage wirklich verlassen?";
        TextView textView_exit = (TextView) alertDialog.findViewById(R.id.textView_exit);
        textView_exit.setText(Html.fromHtml(sourceString1));

    }

    private void textViewToEditText() {
        textViewInput.setVisibility(View.GONE);
        editTextInput.setVisibility(View.VISIBLE);
        editTextInput.setText("");
    }

    private void editTextToTextView() {
        editTextInput.setVisibility(View.GONE);
        textViewInput.setVisibility(View.VISIBLE);
        textViewInput.setText(editTextInput.getText().toString());
    }

    private void removeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextInput.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
    }
}
