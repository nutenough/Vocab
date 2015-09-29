package app.mueller.schiller.weber.com.vicab;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

import static android.app.PendingIntent.getActivity;


public class ExpressLearnActivity extends AppCompatActivity{


    private RelativeLayout vocab_knowing_language;
    private TextView vocab_learning_language_text_view;
    private TextView vocab_knowing_language_text_view;
    private ImageButton imageButton_exit;
    private Button vocab_counter_button;
    private boolean isBackVisible = false;
    private Handler handler;
    private int counter_vocab = 0;
    private int counter_vocab_num = 1;
    private int listSize;
    private int counter_wrong_answer = 0;
    private String frontCard = "";
    private String backCard = "";
    private double randomNum;
    private ImageButton knowIB;
    private ImageButton dontKnowIB;
    private double result = 0.0;
    private boolean noun;
    private boolean verb;
    private boolean adjective;
    private boolean rest;

    private ArrayList<VocItem> allVocab;
    private ArrayList<VocItem> finalVocItems = new ArrayList<VocItem>();

    private AlertDialog alertDialog;

    private DBAdmin dbAdmin;

    private boolean learnList = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_learn);
        onBackPressed();
        initDB();
        fillListFromDB();
        learnDirection();
        initUIElements();
        setupVocabAnimation();
        setupExitButton();
        setupToastManual();
    }

    private void setupToastManual() {
        final Toast tag = Toast.makeText(getBaseContext(), getResources().getString(R.string.toast_manual), Toast.LENGTH_SHORT);

        tag.show();

        new CountDownTimer(4000, 1000)
        {

            public void onTick(long millisUntilFinished) {tag.show();}
            public void onFinish() {tag.show();}

        }.start();

    }

    private void learnDirection() {
        Bundle bundle = getIntent().getExtras();
        boolean knowing_to_learning = bundle.getBoolean("knowingToLearning");
        boolean learning_to_knowing = bundle.getBoolean("learningToKnowing");
        boolean mixed = bundle.getBoolean("mixed");
        noun = bundle.getBoolean("noun");
        verb = bundle.getBoolean("verb");
        adjective = bundle.getBoolean("adjective");
        rest = bundle.getBoolean("rest");

        String listName = bundle.getString("listName");


        if (listName != null){
            learnList = true;
            repopulateVocabArrayWithList(listName);
        }

        if (knowing_to_learning) {
            frontCard = allVocab.get(counter_vocab).getSourceVocab();
            backCard = allVocab.get(counter_vocab).getTargetVocab();
        }

        if (learning_to_knowing) {
            backCard = allVocab.get(counter_vocab).getSourceVocab();
            frontCard = allVocab.get(counter_vocab).getTargetVocab();
        }

        if (mixed) {

            randomNum = Math.random();

            if (randomNum < 0.5) {
                backCard = allVocab.get(counter_vocab).getSourceVocab();
                frontCard = allVocab.get(counter_vocab).getTargetVocab();
            }
            if (randomNum >= 0.5) {
                frontCard = allVocab.get(counter_vocab).getSourceVocab();
                backCard = allVocab.get(counter_vocab).getTargetVocab();
            }
        }
    }

    private void updateNumCounter() {
        counter_vocab_num++;
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);
    }

    private void fillListFromDB() {
        finalVocItems.clear();
        allVocab = new ArrayList<>();

        if(!noun&& !verb && !adjective && !rest){
            finalVocItems.addAll(NavigationFragmentThree.getVocItems());
        }

        allVocab.clear();
        allVocab.addAll(finalVocItems);
        listSize = allVocab.size();
        Collections.shuffle(allVocab);
        Collections.sort(allVocab, new CustomComparator());

    }

    private void initDB() {
        dbAdmin = new DBAdmin(this);
        dbAdmin.open();
    }

    protected void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void initUIElements() {
        vocab_knowing_language = (RelativeLayout) findViewById(R.id.vocab_knowing_language);

        knowIB = (ImageButton) findViewById(R.id.knowIB);
        knowIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dontKnowIB = (ImageButton) findViewById(R.id.dontKnowIB);
        dontKnowIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        vocab_learning_language_text_view = (TextView) findViewById(R.id.vocab_learning_language_text_view);
        vocab_knowing_language_text_view = (TextView) findViewById(R.id.vocab_knowing_language_text_view);
        vocab_knowing_language_text_view.setText(frontCard);
        vocab_learning_language_text_view.setText(backCard);

        vocab_counter_button = (Button) findViewById(R.id.vocab_counter);
        vocab_counter_button.setText(counter_vocab_num + " / " + listSize);

        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);
    }

    private void setupVocabAnimation() {

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
                    allVocab.get(counter_vocab).resetKnown();
                    dbAdmin.updateAskedAndKnown(allVocab.get(counter_vocab));
                    wrongResult();

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
                            vocab_counter_button.setTextColor(getResources().getColor(R.color.color_primary));
                            imageButton_exit.setImageResource(R.drawable.arrow_left_blue);
                            knowIB.setImageResource(R.drawable.advance);
                            knowIB.setScaleType(ImageView.ScaleType.FIT_END);
                        }
                    }, 500);

                }

            }
        });

        vocab_knowing_language.setOnTouchListener(new OnSwipeTouchListener() {

            public boolean onSwipeLeft() {
                allVocab.get(counter_vocab).increaseAsked();
                if (isBackVisible == false) {
                    allVocab.get(counter_vocab).increaseKnown();
                }
                dbAdmin.updateAskedAndKnown(allVocab.get(counter_vocab));
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        correctResult();
                        Log.d("vier", allVocab.get(counter_vocab - 1).getSourceVocab() + "______GEFRAGT_____" + String.valueOf(allVocab.get(counter_vocab - 1).getAsked()) + "______GEWUSST_____" + String.valueOf(allVocab.get(counter_vocab - 1).getKnown()));

                        setRightOut_1.setTarget(vocab_learning_language_text_view);
                        setLeftIn_1.setTarget(vocab_knowing_language_text_view);
                        setRightOut_1.start();
                        setLeftIn_1.start();
                        isBackVisible = false;


                        if (counter_vocab < listSize) {
                            learnDirection();
                            vocab_knowing_language_text_view.setText(frontCard);
                            vocab_learning_language_text_view.setText(backCard);
                            updateNumCounter();
                        } else {

                            showAlertDialogFinished();

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

        dontKnowIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allVocab.get(counter_vocab).resetKnown();
                dbAdmin.updateAskedAndKnown(allVocab.get(counter_vocab));
                if (!isBackVisible) {

                    wrongResult();

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
                            vocab_counter_button.setTextColor(getResources().getColor(R.color.color_primary));
                            imageButton_exit.setImageResource(R.drawable.arrow_left_blue);
                            knowIB.setImageResource(R.drawable.advance);
                            knowIB.setScaleType(ImageView.ScaleType.FIT_END);
                        }
                    }, 500);

                }
            }
        });

        knowIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allVocab.get(counter_vocab).increaseAsked();
                if (isBackVisible == false) {
                    allVocab.get(counter_vocab).increaseKnown();
                }
                dbAdmin.updateAskedAndKnown(allVocab.get(counter_vocab));
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        correctResult();

                        setRightOut_1.setTarget(vocab_learning_language_text_view);
                        setLeftIn_1.setTarget(vocab_knowing_language_text_view);
                        setRightOut_1.start();
                        setLeftIn_1.start();
                        isBackVisible = false;


                        if (counter_vocab < listSize) {
                            learnDirection();
                            vocab_knowing_language_text_view.setText(frontCard);
                            vocab_learning_language_text_view.setText(backCard);
                            updateNumCounter();
                        } else {
                            showAlertDialogFinished();
                        }

                    }
                }, 150);
            }
        });


    }

    private void correctResult() {


        counter_vocab++;

        vocab_counter_button.setTextColor(getResources().getColor(R.color.color_white));
        imageButton_exit.setImageResource(R.drawable.arrow_left_white);
        knowIB.setImageResource(R.drawable.ic_thumbs_up);
        knowIB.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    private void wrongResult() {


        counter_wrong_answer++;
        vocab_knowing_language.setClickable(false);
    }

    private void showAlertDialogFinished() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(ExpressLearnActivity.this).inflate(R.layout.result_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpressLearnActivity.this);
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

        String sourceString = "";

        ImageView smileyIV = (ImageView) alertDialog.findViewById(R.id.image_view_smiley);

        result= ((double) listSize - (double) counter_wrong_answer) / (double)listSize;

        if(result >= 0.90){
            smileyIV.setImageResource(R.drawable.smiley_cool);
            sourceString = "Super!" + "<br>" + "Du hast " + "<b>" + (listSize - counter_wrong_answer)+  "/"  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }else if(result < 0.90 && result >= 0.60){
            smileyIV.setImageResource(R.drawable.smiley_happy);
            sourceString = "Glückwunsch!" + "<br>" + "Du hast " + "<b>" + (listSize - counter_wrong_answer) + "/" + listSize + "</b> " + " Vokabeln richtig!" + "</br>";
        }
        else if(result < 0.60 && result >= 0.20){
            smileyIV.setImageResource(R.drawable.smiley_question);
            sourceString = "Das geht besser!" + "<br>" + "Du hast nur " + "<b>" + (listSize - counter_wrong_answer) +  "/"  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }
        else if(result < 0.20 && result > 0.00){
            smileyIV.setImageResource(R.drawable.smiley_sad);
            sourceString = "Schade!" + "<br>" + "Du hast nur " + "<b>" + (listSize - counter_wrong_answer) +  "/"  + listSize +  "</b> " + " Vokabeln richtig!"+ "</br>";
        }else {
            smileyIV.setImageResource(R.drawable.smiley_sad);
            sourceString = "Schade!" + "<br>" + "Du hast " + "<b>" + "keine einzige" + "</b> " + " Vokabel richtig!"+ "</br>";
        }
        TextView textView_result = (TextView) alertDialog.findViewById(R.id.textView_result);
        textView_result.setText(Html.fromHtml(sourceString));



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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpressLearnActivity.this);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_exit);
        alertDialogBuilder.setTitle(R.string.exit_title);
        alertDialogBuilder.setMessage(R.string.exit_message);

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
    }

    @Override
    public void onBackPressed() {
    }

    private void repopulateVocabArrayWithList(String listName) {
        allVocab = dbAdmin.getAllVocabForList(listName);
        listSize = allVocab.size();
    }


}
