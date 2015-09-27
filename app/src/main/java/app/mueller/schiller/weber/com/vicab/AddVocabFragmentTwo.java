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

public class AddVocabFragmentTwo extends Fragment implements AdapterView.OnItemSelectedListener {
    View contentView;
    private RatingBar ratingBar;
    private TextView rateMessage;
    private Spinner spinner;
    private String[] wordClassArray = {"Wortart auswählen", "Substantiv", "Verb", "Adjektiv", "Sonstiges"};

    public static String RATING = "";
    public static String WORD_TYPE = "";

    // The view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.add_vocab_fragment_two, null);
        setupUIComponents();
        fillSpinner();
        return contentView;
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
}
