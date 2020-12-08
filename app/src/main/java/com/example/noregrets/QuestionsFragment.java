package com.example.noregrets;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class QuestionsFragment extends Fragment {
    private int difficulty;
    public Activity containerActivity = null;
    private ArrayList<Integer> primeNumbers = new ArrayList<Integer>();
    private ArrayList<Character> mathSymbols = new ArrayList<Character>();
    private Button next;
    private Animation animRotate;
    private Animation animSlide;
    private View v = null;
    private String pref = "";
    private EditText answerQuestion;
    public String question = "";
    public int answer;
    private TextView questionView;
    private Context contextHolder;
    private ImageView iv = null;


    public QuestionsFragment(String pref, int difficulty, Context context) {
        mathSymbols.add('+');
        mathSymbols.add('-');
        mathSymbols.add('*');
        mathSymbols.add('/');

        this.difficulty = difficulty;
        Pair<String, String> equation = createEquation();
        question = equation.first;
        new answerSearch().execute(equation.second);
        this.pref = pref;
        contextHolder = context;

    }
    /**
     * PURPOSE: This method connects this fragment
     * to the activity that created it
     *
     * @param containerActivity, which would be MainActivity
     */
    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.fragment_questions, container, false);
        next = (Button) v.findViewById(R.id.next);

        questionView = (TextView) v.findViewById(R.id.question);
        questionView.setText(question);

        //Slide animation to slide in the questions
        animSlide = AnimationUtils.loadAnimation(containerActivity,
                R.anim.slide);
        questionView.startAnimation(animSlide);

        answerQuestion = (EditText) v.findViewById(R.id.answer);

        int width = (((MainActivity)getActivity()).metrics.widthPixels)/70;
        answerQuestion.setEms((int) width);


        // Change BECAUSE ROTATE WILL BE DELTED
        /*rotate.setOnClickListener(new View.OnClickListener() {
            private ImageView iv = v.findViewById(R.id.timer);
            @Override
            public void onClick(View v) {
                if (pref.equals("LIGHT")) {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.timerblack));
                }else{
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.timerwhite));

                }
                animRotate = AnimationUtils.loadAnimation(containerActivity,
                        R.anim.rotate);
                this.iv.startAnimation(animRotate);
            }
        });*/
        // Change to make questions
        next.setOnClickListener(new View.OnClickListener() {
            //private TextView tx = v.findViewById(R.id.question);
            private ImageView iv = v.findViewById(R.id.timer);
            @Override
            public void onClick(View v) {
                int input = 0;
                try{
                    input = Integer.parseInt(answerQuestion.getText().toString());
                    ArrayList<String> sendQuestion = new ArrayList<String>();
                    sendQuestion.add(question);
                    sendQuestion.add(Integer.toString(answer));
                    sendQuestion.add(answerQuestion.getText().toString());
                    ((MainActivity) getActivity()).NumberAnswered +=1;
                    if (input == answer){
                        ((MainActivity) getActivity()).NumberCorrect +=1;
                        sendQuestion.add("Correct");
                    }
                    else{
                        sendQuestion.add("Incorrect");
                    }
                    ((MainActivity) getActivity()).addAnswer(sendQuestion);
                    if (((MainActivity) getActivity()).getNumberAnswered() == 5){
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextHolder);
                        SharedPreferences.Editor editor = preferences.edit();
                        int i=1;
                        int AttemptNumber = preferences.getInt("AttemptNumber",1);
                        for(ArrayList<String> answers: ((MainActivity) getActivity()).allAnswers){
                            editor.putString("Attempt_" + AttemptNumber + "_Question_" + i, answers.get(0)); // question
                            editor.commit();
                            editor.putString("Attempt_" + AttemptNumber + "_CorrectAnswer_" + i, answers.get(1)); // correct answer
                            editor.commit();
                            editor.putString("Attempt_" + AttemptNumber + "_UserAnswer_" + i, answers.get(2)); // user answer
                            editor.commit();
                            editor.putString("Attempt_" + AttemptNumber + "_CorrectOrIncorrect_" + i, answers.get(3)); // Correct or Incorrect
                            editor.commit();
                            i++;
                        }
                        System.out.println(preferences.getString("Attempt_" + AttemptNumber + "_Question_" + 5,"Not Found"));
                        editor.remove("AttemptNumber");
                        editor.commit();
                        editor.putInt("AttemptNumber",AttemptNumber+1);
                        editor.commit();
                        System.out.println(preferences.getInt("AttemptNumber",100));

                        if (((MainActivity) getActivity()).getNumberCorrect() >= 4){
                            ((MainActivity) getActivity()).createTextFrag();
                        }
                        else{
                            if (pref.equals("LIGHT")) {
                                iv.setImageDrawable(getResources().getDrawable(R.drawable.timerblack));
                            }else{
                                iv.setImageDrawable(getResources().getDrawable(R.drawable.timerwhite));

                            }
                            animRotate = AnimationUtils.loadAnimation(containerActivity,
                                    R.anim.rotate);
                            iv.startAnimation(animRotate);


                            //Freeze the app if got it wrong


                        }
                        ((MainActivity) getActivity()).NumberAnswered = 0;
                        ((MainActivity) getActivity()).NumberCorrect = 0;
                        ((MainActivity) getActivity()).allAnswers.clear();

                    }
                    else{
                        ((MainActivity)getActivity()).nextClick(v);
                    }
                } catch (NumberFormatException e) {

                }

            }
        });
        return v;
    }


    /**
     * checks if a number is a prime number
     * @param n the number that is being checked if prime
     * @return boolean   true if the number is prime, false if it is not
     */
    private boolean isPrime(int n){
        if (primeNumbers.contains(n))
            return true;
        if (n<=1)
            return false;
        for(int i=2; i<=n/2;i++){
            if (n%i == 0){
                return false;
            }
        }
        primeNumbers.add(n);
        return true;
    }



    ArrayList<Integer> factors = new ArrayList<Integer>();
    boolean complete = false;
    /**
     * This method creates a random equation and returns a Pair
     * @return Pair   pair.first is the equation to show on the screen, pair.second is the part of the url
     */
    private Pair<String,String> createEquation(){
        Random rand = new Random();
        ArrayList<Character> operations = new ArrayList<Character>();
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        // check difficulty, difficulty 1 = sober, difficulty 2 = drunk
        //int difficulty = ;
        int numbersInEquation = 3;
        String equation = "";
        String urlName = "";
        if (difficulty == 1){
            for(int i = 0; i<numbersInEquation;i++){
                int number = rand.nextInt(1001);
                char operation = mathSymbols.get(rand.nextInt(2));
                numbers.add(number);
                operations.add(operation);
                equation = equation.concat(Integer.toString(number));
                urlName = urlName.concat(Integer.toString(number));
                if (i!=numbersInEquation-1) {
                    equation = equation.concat(Character.toString(operation));
                    if (operation == '+'){
                        urlName = urlName.concat("%2B");
                    }
                    else if(operation == '-'){
                        urlName = urlName.concat("-");
                    }
                }
            }
            System.out.println(equation);
            return new Pair(equation,urlName);
        }
        else if (difficulty==2){
            LinkedHashMap<Pair<Integer,Integer>,Character> combination = new LinkedHashMap<Pair<Integer,Integer>,Character>();
            for(int i=0;i<3;i++){
                int number1 = rand.nextInt(1001);
                char operation = mathSymbols.get(rand.nextInt(4));
                int number2;
                if (operation == '/'){
                    while(isPrime(number1)){
                        number1 = rand.nextInt(1001);
                    }
                    System.out.println("number1 = " + number1);
                    factors.clear();
                    createFactors(number1);
                    System.out.println("factors size is: " + factors.size() + " for " + number1);
                    number2 = factors.get(rand.nextInt(factors.size()));
                    combination.put(new Pair<Integer,Integer>(number1,number2),operation);
                }
                else if(operation == '*'){
                    number2 = rand.nextInt(5000/number1);
                    combination.put(new Pair<Integer,Integer>(number1,number2),operation);
                }
                else{
                    number2 = rand.nextInt(1001);
                    combination.put(new Pair<Integer,Integer>(number1, number2),operation);
                }
            }
            for (Pair p : combination.keySet()){
                equation = equation.concat("(" + ((Integer)p.first).toString() + ((Character)combination.get(p)).toString());
                equation = equation.concat(((Integer)p.second).toString() + ")");
                equation = equation.concat(Character.toString(mathSymbols.get(rand.nextInt(2))));
            }
            equation = equation.substring(0,equation.length()-1);
            urlName = equation.replaceAll("\\+","%2B");
            urlName = urlName.replaceAll("/","%2F");
            return new Pair(equation,urlName);
        }
        return new Pair("","");

    }

    public void createFactors(int number){
        for (int i=2;i<number/2;i++)
            if (number%i==0)
                factors.add(i);
    }



    private class answerSearch extends AsyncTask<String, Void, JSONObject> {

        private Context context = null;
        public answerSearch(){

        }


        @Override
        /**
         * This method reads in a String from the flickr API with a custom link we use and
         * returns the string as a json object.
         */
        protected JSONObject doInBackground(String... strings) {
            JSONObject json = null;
            try {
                String jsonString = "";
                String line;
                System.out.println(strings[0]);
                URL url = new URL("https://api.mathjs.org/v4/?expr=" + strings[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                while((line = in.readLine()) != null){
                    System.out.println("JSON LINE" + line);
                    jsonString += line;
                }
                System.out.println("json string is " + jsonString.toString());
                answer = Integer.parseInt(jsonString);
                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * This method parses the string for important info such as the photo url, owner, id, format
         * tags, and date taken. It then sets this info into its correct locations to be displayed.
         * @param json
         */
        protected void onPostExecute(JSONObject json){

        }



    }

}