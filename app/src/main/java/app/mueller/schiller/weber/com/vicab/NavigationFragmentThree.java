package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationFragmentThree extends Fragment {

    private View view;
    private Button button_express;
    private Button button_text_input;

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
    }

    private void setupUIComponents() {
        button_express = (Button) getActivity().findViewById(R.id.button_express);
        button_text_input = (Button) getActivity().findViewById(R.id.button_text_input);
    }

    private void handleEvents() {
        button_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExpressLearnActivity.class);
                startActivity(intent);
            }
        });

        button_text_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TextInputLearnActivity.class);
                startActivity(intent);
            }
        });
    }
}