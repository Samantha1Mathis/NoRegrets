package com.example.noregrets;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


public class SingleAttemptResult extends Fragment {

    int attemptNumber = 1;
    public SingleAttemptResult(int attemptNumber) {
        System.out.println(attemptNumber);
        this.attemptNumber = attemptNumber;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        View v = inflater.inflate(R.layout.fragment_single_attempt_result, container, false);

        TextView title = (TextView)v.findViewById(R.id.AttemptNumber);
        title.setText(Integer.toString(attemptNumber-1));
        TextView QuestionAsked1 = (TextView)v.findViewById(R.id.QuestionAskedReplace1);
        QuestionAsked1.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_Question_" + 1,"Could not find"));
        TextView CorrectAnswer1 = (TextView)v.findViewById(R.id.CorrectAnswer1);
        CorrectAnswer1.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectAnswer_" + 1,""));
        TextView UserAnswer1 = (TextView)v.findViewById(R.id.UserAnswer1);
        UserAnswer1.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_UserAnswer_" + 1,""));
        TextView CorrectOrIncorrect1 = (TextView)v.findViewById(R.id.CorrectOrIncorrect1);
        CorrectOrIncorrect1.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectOrIncorrect_" + 1,"")+"\n");

        TextView QuestionAsked2 = (TextView)v.findViewById(R.id.QuestionAskedReplace2);
        QuestionAsked2.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_Question_" + 2,""));
        TextView CorrectAnswer2 = (TextView)v.findViewById(R.id.CorrectAnswer2);
        CorrectAnswer2.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectAnswer_" + 2,""));
        TextView UserAnswer2 = (TextView)v.findViewById(R.id.UserAnswer2);
        UserAnswer2.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_UserAnswer_" + 2,""));
        TextView CorrectOrIncorrect2 = (TextView)v.findViewById(R.id.CorrectOrIncorrect2);
        CorrectOrIncorrect2.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectOrIncorrect_" + 2,"")+"\n");

        TextView QuestionAsked3 = (TextView)v.findViewById(R.id.QuestionAskedReplace3);
        QuestionAsked3.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_Question_" + 3,""));
        TextView CorrectAnswer3 = (TextView)v.findViewById(R.id.CorrectAnswer3);
        CorrectAnswer3.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectAnswer_" + 3,""));
        TextView UserAnswer3 = (TextView)v.findViewById(R.id.UserAnswer3);
        UserAnswer3.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_UserAnswer_" + 3,""));
        TextView CorrectOrIncorrect3 = (TextView)v.findViewById(R.id.CorrectOrIncorrect3);
        CorrectOrIncorrect3.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectOrIncorrect_" + 3,"")+"\n");

        TextView QuestionAsked4 = (TextView)v.findViewById(R.id.QuestionAskedReplace4);
        QuestionAsked4.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_Question_" + 4,""));
        TextView CorrectAnswer4 = (TextView)v.findViewById(R.id.CorrectAnswer4);
        CorrectAnswer4.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectAnswer_" + 4,""));
        TextView UserAnswer4 = (TextView)v.findViewById(R.id.UserAnswer4);
        UserAnswer4.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_UserAnswer_" + 4,""));
        TextView CorrectOrIncorrect4 = (TextView)v.findViewById(R.id.CorrectOrIncorrect4);
        CorrectOrIncorrect4.setText(preferences.getString("Attempt_" + (attemptNumber-1)+ "_CorrectOrIncorrect_" + 4,"") +"\n");

        TextView QuestionAsked5 = (TextView)v.findViewById(R.id.QuestionAskedReplace5);
        QuestionAsked5.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_Question_" + 5,""));
        TextView CorrectAnswer5 = (TextView)v.findViewById(R.id.CorrectAnswer5);
        CorrectAnswer5.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_CorrectAnswer_" + 5,""));
        TextView UserAnswer5 = (TextView)v.findViewById(R.id.UserAnswer5);
        UserAnswer5.setText(preferences.getString("Attempt_" + (attemptNumber-1) + "_UserAnswer_" + 5,""));
        TextView CorrectOrIncorrect5 = (TextView)v.findViewById(R.id.CorrectOrIncorrect5);
        CorrectOrIncorrect5.setText(preferences.getString("Attempt_" + (attemptNumber-1)+ "_CorrectOrIncorrect_" + 5,"")+"\n" );
        return v;
    }
}