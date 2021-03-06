package app.mueller.schiller.weber.com.vicab;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.Database.ViCabContract;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class NavigationFragmentThree extends Fragment {

    private View view;
    private RelativeLayout express_mode;
    private RelativeLayout text_input_mode;
    private CheckBox checkBox_knowing_to_learning;
    private CheckBox checkBox_learning_to_knowing;
    private CheckBox checkBox_mixed;
    private CheckBox checkBox_noun;
    private CheckBox checkBox_verb;
    private CheckBox checkBox_adjective;
    private CheckBox checkBox_rest;
    private boolean knowing_to_learning;
    private boolean learning_to_knowing;
    private boolean mixed;
    private boolean noun;
    private boolean verb;
    private boolean adjective;
    private boolean rest;
    private TextView word_class;
    private RelativeLayout relativeLayout_wordclass;



    private DBAdmin dbAdmin;
    private  ArrayList<VocItem> vocItems = new ArrayList<>();
    private static ArrayList<VocItem> finalVocItems = new ArrayList<>();
    private boolean noVocabs = true;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.navigation_fragment_three, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        handleEvents();
        initDB();
        setupCheckBoxes();
        setupCheckBoxListener();
        setupLayoutForListLearning();
    }

    private void setupLayoutForListLearning() {
        Bundle bundle2 = getActivity().getIntent().getExtras();
        if (bundle2 != null) {
            String listName = bundle2.getString("listName");
            String title = "Du lernst " + "&#160&#160&#160&#8226&#160" + listName + "&#160&#8226";
            getActivity().setTitle(Html.fromHtml(title));
            word_class = (TextView) getActivity().findViewById(R.id.word_class);
            relativeLayout_wordclass = (RelativeLayout) getActivity().findViewById(R.id.relative_layout_checkbox_word_class);
            word_class.setVisibility(View.INVISIBLE);
            relativeLayout_wordclass.setVisibility(View.INVISIBLE);

        }
    }

    public static ArrayList<VocItem> getVocItems(){
        return finalVocItems;
    }


    private void setupCheckBoxListener() {
        checkBox_knowing_to_learning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox_learning_to_knowing.setChecked(false);
                checkBox_mixed.setChecked(false);
                knowing_to_learning = true;
                learning_to_knowing = false;
                mixed = false;
            }
        });

        checkBox_learning_to_knowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox_knowing_to_learning.setChecked(false);
                checkBox_mixed.setChecked(false);
                knowing_to_learning = false;
                learning_to_knowing = true;
                mixed = false;
            }
        });

        checkBox_mixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox_learning_to_knowing.setChecked(false);
                checkBox_knowing_to_learning.setChecked(false);
                knowing_to_learning = false;
                learning_to_knowing = false;
                mixed = true;
            }
        });

        checkBox_noun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_noun.isChecked()){
                    noun = true;
                }else{
                    noun = false;
                }
            }
        });

        checkBox_verb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(checkBox_verb.isChecked()){
                    verb = true;
                }else{
                    verb = false;
                }
                }
            });

        checkBox_adjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(checkBox_adjective.isChecked()){
                adjective = true;
            }else{
                adjective = false;
                }
                }
            }  );

        checkBox_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_rest.isChecked()){
                    rest = true;
                }else{
                    rest = false;
                }
            }
        });


    }

    private void setupCheckBoxes() {
        checkBox_knowing_to_learning = (CheckBox) getActivity().findViewById(R.id.checkBox_learning_1_2);
        checkBox_learning_to_knowing = (CheckBox) getActivity().findViewById(R.id.checkBox_learning_2_1);
        checkBox_mixed = (CheckBox) getActivity().findViewById(R.id.checkBox_learning_mixed);
        checkBox_noun = (CheckBox) getActivity().findViewById(R.id.word_class_noun);
        checkBox_verb = (CheckBox) getActivity().findViewById(R.id.word_class_verb);
        checkBox_adjective = (CheckBox) getActivity().findViewById(R.id.word_class_adjective);
        checkBox_rest = (CheckBox) getActivity().findViewById(R.id.word_class_rest);

        String knowing_to_learning_String = "&#160&#160&#160&#160" + ViCabContract.CHOSEN_LANGUAGE_SOURCE +  "&#160&#160&#160&#160" + "&#10141"  + "&#160&#160&#160&#160" + ViCabContract.CHOSEN_LANGUAGE_TARGET +"&#160&#160&#160&#160";
        String learning_to_knowing_String = "&#160&#160&#160&#160" + ViCabContract.CHOSEN_LANGUAGE_TARGET + "&#160&#160&#160&#160" + "&#10141" + "&#160&#160&#160&#160" + ViCabContract.CHOSEN_LANGUAGE_SOURCE +"&#160&#160&#160&#160";
        String mixed_String = "&#160&#160&#160&#160" + "gemischt" + "&#160&#160&#160&#160";
        checkBox_knowing_to_learning.setText(Html.fromHtml(knowing_to_learning_String));
        checkBox_learning_to_knowing.setText(Html.fromHtml(learning_to_knowing_String));
        checkBox_mixed.setText(Html.fromHtml(mixed_String));
    }

    private void initDB(){
        dbAdmin = new DBAdmin(getActivity());
        dbAdmin.open();
    }

    public void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void setupUIComponents() {
        express_mode = (RelativeLayout) getActivity().findViewById(R.id.express_mode);
        text_input_mode = (RelativeLayout) getActivity().findViewById(R.id.text_input_mode);
    }

    private void handleEvents() {
        express_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkVocabs();

                if (finalVocItems.size() != 0 && (!noVocabs)) {
                    if (checkBox_mixed.isChecked() || checkBox_learning_to_knowing.isChecked() || checkBox_knowing_to_learning.isChecked()) {

                        Intent intent = new Intent(getActivity(), ExpressLearnActivity.class);
                        Bundle bundle = setupBundle();

                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_choose_learn_direction), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(noVocabs){
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_vocabs), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_unknown_vocabs), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        text_input_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVocabs();

                if(finalVocItems.size() != 0 && (!noVocabs)) {
                    if (checkBox_mixed.isChecked() || checkBox_learning_to_knowing.isChecked() || checkBox_knowing_to_learning.isChecked()) {
                        Intent intent = new Intent(getActivity(), TextInputLearnActivity.class);
                         Bundle bundle = setupBundle();
                         intent.putExtras(bundle);
                         startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_choose_learn_direction), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (noVocabs) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_vocabs), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_unknown_vocabs), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private Bundle setupBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("knowingToLearning", knowing_to_learning);
        bundle.putBoolean("learningToKnowing", learning_to_knowing);
        bundle.putBoolean("mixed", mixed);
        bundle.putBoolean("noun", noun);
        bundle.putBoolean("verb", verb);
        bundle.putBoolean("adjective", adjective);
        bundle.putBoolean("rest", rest);

        //Intent from Navigationfragment 2 zum lernen von Listen
        Bundle bundle2 = getActivity().getIntent().getExtras();
        if (bundle2 != null) {
            String listName = bundle2.getString("listName");

            bundle.putString("listName", listName);
            Log.d("12345", listName);
        }
        
        return bundle;
    }

    private void checkVocabs(){
        finalVocItems.clear();
        vocItems.clear();
        vocItems.addAll(dbAdmin.getAllVocabForLanguage());
        if(vocItems.size() > 0){
            noVocabs = false;
        }

        for(int i = 0; i < vocItems.size(); i++) {

            if (vocItems.get(i).getImportance().equals("1.0")) {
                if (vocItems.get(i).getKnown() > 4) {
                    vocItems.remove(i);
                    i = -1;
                }
            } else if (vocItems.get(i).getImportance().equals("2.0")) {
                if (vocItems.get(i).getKnown() > 6) {
                    vocItems.remove(i);
                    i = -1;
                }
            } else if (vocItems.get(i).getImportance().equals("3.0")) {
                if (vocItems.get(i).getKnown() > 7) {
                    vocItems.remove(i);
                    i = -1;
                }
            } else {
                if (vocItems.get(i).getKnown() > 5) {
                    vocItems.remove(i);
                    i = -1;
                }
            }
        }

        if(!noun&& !verb && !adjective && !rest){
            finalVocItems.addAll(vocItems);
        }else {

            if (noun) {
                for (int i = 0; i < vocItems.size(); i++) {
                    if (vocItems.get(i).getWordType().equals("Substantiv")) {
                        finalVocItems.add(vocItems.get(i));
                    }
                }
            }
            if (verb) {
                for (int i = 0; i < vocItems.size(); i++) {
                    if (vocItems.get(i).getWordType().equals("Verb")) {
                        finalVocItems.add(vocItems.get(i));
                    }
                }
            }
            if (adjective) {
                for (int i = 0; i < vocItems.size(); i++) {
                    if (vocItems.get(i).getWordType().equals("Adjektiv")) {
                        finalVocItems.add(vocItems.get(i));
                    }
                }
            }
            if (rest) {
                for (int i = 0; i < vocItems.size(); i++) {
                    if (vocItems.get(i).getWordType().equals("Sonstiges")) {
                        finalVocItems.add(vocItems.get(i));
                    }
                }
            }

        }

    }

}




