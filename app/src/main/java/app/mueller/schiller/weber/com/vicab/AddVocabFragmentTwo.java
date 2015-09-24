package app.mueller.schiller.weber.com.vicab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class AddVocabFragmentTwo extends Fragment {
    View contentView;
    private RatingBar ratingBar;
    private String ratedValue;
    private TextView rateMessage;

    // The view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.add_vocab_fragment_two, null);
        setupUIComponents();
        return contentView;
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
                ratedValue = String.valueOf(ratingBar.getRating());
                rateMessage.setText("Rating : " + ratedValue + "/3");
            }
        });
    }

    private void setupUIComponents() {
        ratingBar = (RatingBar) contentView.findViewById(R.id.ratingBar);
        rateMessage = (TextView) contentView.findViewById(R.id.ratingMessage);
    }
}
