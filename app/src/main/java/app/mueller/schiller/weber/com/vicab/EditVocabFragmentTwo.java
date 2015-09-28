package app.mueller.schiller.weber.com.vicab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

public class EditVocabFragmentTwo extends Fragment implements AdapterView.OnItemSelectedListener {
    View contentView;
    private RatingBar ratingBar;
    private TextView rateMessage;
    private Spinner spinner;
    private static String spinnerValue = "";
    private static String updatedRating= "";
    private String[] wordClassArray = {"Wortart auswählen", "Substantiv", "Verb", "Adjektiv", "Sonstiges"};

    private static String RATING = "";
    private static String WORD_TYPE = "";

    // The view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.edit_vocab_fragment_two, null);
        setupUIComponents();
        fillSpinner();
        updateSpinner();
        updateRating();
        return contentView;
    }

    private void updateSpinner() {
        if (!spinnerValue.equals("")) {
            spinner.setSelection(getIndex(spinner, spinnerValue));
        }
    }

    private void updateRating() {
        if (!updatedRating.equals("")) {
            ratingBar.setRating(Float.parseFloat(updatedRating));
        }
    }

    public static void setSpinnerValue(String value){
        spinnerValue = value;
    }

    public static void setRating(String rating){
        updatedRating = rating;
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void setupUIComponents() {
        ratingBar = (RatingBar) contentView.findViewById(R.id.ratingBar);
        spinner = (Spinner) contentView.findViewById(R.id.wordClassSpinner);
       // rateMessage = (TextView) contentView.findViewById(R.id.);
    }

    // Page content ("findViewById" has to be "view.findViewById"; "getApplicationContext" has to be "getActivity()")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleEvents();
    }

    private void handleEvents() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RATING = String.valueOf(ratingBar.getRating());
               // rateMessage.setText("Rating : " + ratedValue + "/3");
            }
        });
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
    }

    private void fillSpinner() {
        // adapter for spinner
        ArrayAdapter<?> dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, wordClassArray);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        WORD_TYPE = spinner.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public static String getRating() {
        return RATING;
    }

    public static String getWordType() {
        return WORD_TYPE;
    }

}
