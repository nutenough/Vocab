package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.mueller.schiller.weber.com.vicab.R;


public class ExpressLearnActivity extends AppCompatActivity {

    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language;
    private boolean isBackVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_learn);
        setupVocabAnimation();



    }

    private void setupVocabAnimation(){
        vocab_knowing_language = (RelativeLayout)findViewById(R.id.vocab_knowing_language);
        vocab_learning_language = (TextView)findViewById(R.id.vocab_learning_language);


        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);


        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);


        vocab_knowing_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBackVisible) {
                    setRightOut.setTarget(vocab_knowing_language);
                    setLeftIn.setTarget(vocab_learning_language);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;
                }

                else{
                    setRightOut.setTarget(vocab_learning_language);
                    setLeftIn.setTarget(vocab_knowing_language);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = false;
                }
            }
        });
    }



}
