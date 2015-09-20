package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;
import app.mueller.schiller.weber.com.vicab.R;

import static android.app.PendingIntent.getActivity;


public class ExpressLearnActivity extends AppCompatActivity {

    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language_text_view;
    private TextView vocab_knowing_language_text_view;
    private boolean isBackVisible = false;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_learn);
        setupVocabAnimation();




    }

    private void setupVocabAnimation(){
        vocab_knowing_language = (RelativeLayout)findViewById(R.id.vocab_knowing_language);
        vocab_learning_language_text_view = (TextView)findViewById(R.id.vocab_learning_language_text_view);
        vocab_knowing_language_text_view =(TextView)findViewById(R.id.vocab_knowing_language_text_view);


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
                    setRightOut.setTarget(vocab_knowing_language_text_view);
                    setLeftIn.setTarget(vocab_learning_language_text_view);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;
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

                        vocab_knowing_language_text_view.setText("wissen");
                        vocab_learning_language_text_view.setText("to know");





                    }
                }, 170);

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
