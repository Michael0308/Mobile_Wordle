package edu.cuhk.csci3310.wordle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GameCompleteFragment extends Fragment {

    private String message;
    private WordleActivity wordleActivity;
    private boolean endlessMode;
    public GameCompleteFragment() {
        // Required empty public constructor
    }


    public static GameCompleteFragment newInstance(String newMessage, WordleActivity wordleActivity, boolean isEndlessMode) {
        GameCompleteFragment fragment = new GameCompleteFragment();
        fragment.wordleActivity = wordleActivity;
        fragment.message = newMessage;
        fragment.endlessMode= isEndlessMode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_complete, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView title = (TextView) view.findViewById(R.id.gc_fragment_text);
        title.setText(this.message);

//      These lines handle the start new game and prevent user from getting back
        AppCompatButton okButton = view.findViewById(R.id.okStartNewButton);
        okButton.setOnClickListener(view1 -> {
            wordleActivity.clearPreference();
            wordleActivity.addScore();
            wordleActivity.updateAchievement();
            Context context = view.getContext();
            Intent intent = new Intent(context,endlessMode? WordleActivity.class:HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

}