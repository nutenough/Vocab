package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;


public class TextInputLearnActivity extends AppCompatActivity {


    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language_text_view;
    private TextView vocab_knowing_language_text_view;
    private Button vocab_counter_button;
    private boolean isBackVisible = false;
    private Handler handler;
    private ArrayList<VocItem> allVocab;
    private int counter = 0;
    private int listSize;
    private FloatingActionButton vocab_fab;
    private EditText editTextInput;
    private String textInput;
    private ImageView imageView;
    private int click_counter_fab = 0;
    private TextView textViewInput;

    private DBAdmin dbAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input_learn);
        initDB();
        fillListFromDB();
        initUIElements();
        fillVocabCounter();
        setupVocabAnimation();



    }

    private void compareSolution(){
        textInput = editTextInput.getText().toString();
        String currentSolution = allVocab.get(counter).getTargetVocab();
        textInput.trim();
        currentSolution.trim();

        if(currentSolution.trim().equalsIgnoreCase(textInput.trim())){
            imageView.setImageResource(R.drawable.smiley_happy);
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setImageResource(R.drawable.smiley_question);
            imageView.setVisibility(View.VISIBLE);
        }

        updateCounter();
    }



    private void fillVocabCounter() {
        listSize = allVocab.size();
        vocab_counter_button.setText("0 / " + listSize);
    }

    private void updateCounter() {
        counter++;
        vocab_counter_button.setText(counter + " / " + listSize);
    }

    private void fillListFromDB(){
        allVocab = new ArrayList<>();
        allVocab.addAll(dbAdmin.getAllVocab());
        Log.d("Learn", "allItems: " + allVocab);
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

        vocab_knowing_language_text_view.setText(allVocab.get(counter).getSourceVocab());
        vocab_learning_language_text_view.setText(allVocab.get(counter).getTargetVocab());

        vocab_counter_button = (Button) findViewById(R.id.vocab_counter);
        vocab_fab = (FloatingActionButton) findViewById(R.id.vocab_fab_next);
        editTextInput = (EditText)findViewById(R.id.vocab_edit_text);
        imageView = (ImageView) findViewById(R.id.image_view_correct);
        imageView.setVisibility(View.INVISIBLE);

        textViewInput = (TextView) findViewById(R.id.vocab_text_view);

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

                if(click_counter_fab == 0) {
                    removeKeyboard();
                    editTextToTextView();
                    vocab_fab.setImageResource(R.drawable.ic_arrow_right);

                    compareSolution();
                    click_counter_fab++;
                }else {

                    setRightOut.setTarget(vocab_learning_language_text_view);
                    setLeftIn.setTarget(vocab_knowing_language_text_view);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = false;

                    handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (counter < listSize) {
                                vocab_knowing_language_text_view.setText(allVocab.get(counter).getSourceVocab());
                                vocab_learning_language_text_view.setText(allVocab.get(counter).getTargetVocab());
                            } else {
                                vocab_knowing_language_text_view.setText("Finished!");
                                vocab_learning_language_text_view.setText("Great Job");
                            }


                        }
                    }, 250);

                    click_counter_fab--;

                    textViewToEditText();


                    vocab_fab.setImageResource(R.drawable.ic_done);
                    imageView.setVisibility(View.INVISIBLE);

                }
            }
        });
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
}
