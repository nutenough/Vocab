package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private int counter = 0;
    private int listSize;

    private DBAdmin dbAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_learn);
        initDB();
        fillListFromDB();
        initUIElements();
        fillVocabCounter();
        setupVocabAnimation();


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
        vocab_counter_button = (Button) findViewById(R.id.vocab_counter);
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

                        if (counter < listSize) {
                            vocab_knowing_language_text_view.setText(allVocab.get(counter).getSourceVocab());
                            vocab_learning_language_text_view.setText(allVocab.get(counter).getTargetVocab());
                            updateCounter();
                        }
                        else {
                            vocab_knowing_language_text_view.setText("Finished!");
                            vocab_learning_language_text_view.setText("Great Job");
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



}
