package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

import static android.app.PendingIntent.getActivity;


public class ExpressLearnActivity extends AppCompatActivity {

    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language_text_view;
    private TextView vocab_knowing_language_text_view;
    private Button vocab_counter_button;
    private boolean isBackVisible = false;
    private Handler handler;
    private ArrayList<VocItem> allVocab;
    private int counter_vocab = 0;
    private int counter_vocab_num = 1;
    private int listSize;
    private int counter_wrong_answer = 0;

    private AlertDialog alertDialog;

    private DBAdmin dbAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_learn);
        initDB();
        fillListFromDB();
        initUIElements();
        setupVocabAnimation();


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
        vocab_knowing_language_text_view.setText(allVocab.get(counter_vocab).getSourceVocab());
        vocab_learning_language_text_view.setText(allVocab.get(counter_vocab).getTargetVocab());

        vocab_counter_button = (Button) findViewById(R.id.vocab_counter);
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);
    }

    private void setupVocabAnimation(){

        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);


        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);


        final AnimatorSet setRightOut_1 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out_without_animation);


        final AnimatorSet setLeftIn_1 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in_without_animation);


        vocab_knowing_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBackVisible) {
                    counter_wrong_answer++;
                    vocab_knowing_language.setClickable(false);

                    setRightOut.setTarget(vocab_knowing_language_text_view);
                    setLeftIn.setTarget(vocab_learning_language_text_view);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;

                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            vocab_knowing_language.setClickable(true);


                        }
                    }, 500);

                }

                /*else{
                    setRightOut.setTarget(vocab_learning_language_text_view);
                    setLeftIn.setTarget(vocab_knowing_language_text_view);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = false;
                }*/
            }
        });

        vocab_knowing_language.setOnTouchListener(new OnSwipeTouchListener() {


            public boolean onSwipeLeft() {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        setRightOut_1.setTarget(vocab_learning_language_text_view);
                        setLeftIn_1.setTarget(vocab_knowing_language_text_view);
                        setRightOut_1.start();
                        setLeftIn_1.start();
                        isBackVisible = false;

                        counter_vocab++;

                        if (counter_vocab < listSize) {
                            vocab_knowing_language_text_view.setText(allVocab.get(counter_vocab).getSourceVocab());
                            vocab_learning_language_text_view.setText(allVocab.get(counter_vocab).getTargetVocab());
                            updateNumCounter();
                        }
                        else {
                            showAlertDialog();
                        }

                    }
                }, 150);

                return true;
            }



            @Override
            public boolean onSwipeRight() {
                return true;
            }

            @Override
            public boolean onSwipeBottom() {
                return true;
            }

            @Override
            public boolean onSwipeTop() {
                return true;
            }
                });
    }

    private void showAlertDialog() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(ExpressLearnActivity.this).inflate(R.layout.result_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpressLearnActivity.this);
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

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
        alertDialog.setCancelable(false);

        // Edit Design alertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.color_primary));

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.color_primary));

        String sourceString = "Glückwunsch!" + "<br>" + "Du hast " + "<b>" + (listSize-counter_wrong_answer) +  " / "  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        TextView textView_result = (TextView) alertDialog.findViewById(R.id.textView_result);
        textView_result.setText(Html.fromHtml(sourceString));
        RatingBar ratingBar = (RatingBar) alertDialog.findViewById(R.id.ratingBarResult);

        int numStars = 5;
        float rating = (float)((listSize-counter_wrong_answer) / listSize *numStars);

        ratingBar.setRating(rating);
        ratingBar.setStepSize(0.1f);
    }

}
