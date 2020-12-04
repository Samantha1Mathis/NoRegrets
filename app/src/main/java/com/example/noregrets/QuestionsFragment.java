package com.example.noregrets;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.media.Image;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class QuestionsFragment extends Fragment {
    public Activity containerActivity = null;
    private ArrayList<Integer> primeNumbers = new ArrayList<Integer>();
    private ArrayList<Character> mathSymbols = new ArrayList<Character>();
    Button rotate;
    Button next;
    ImageView iv;
    Animation animRotate;
    Animation animSlide;
    View v = null;
    String pref = "";
    EditText answerQuestion;
    public String question = "";
    public int answer;
    TextView questionView;
    public QuestionsFragment(String pref) {
        mathSymbols.add('+');
        mathSymbols.add('-');
        mathSymbols.add('*');
        mathSymbols.add('/');

        Pair<String, String> equation = createEquation();
        question = equation.first;
        new answerSearch().execute(equation.second);
        this.pref = pref;
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            this.v = inflater.inflate(R.layout.fragment_questions, container, false);
            rotate = (Button) v.findViewById(R.id.rotate);
            next = (Button) v.findViewById(R.id.next);

        //View v = inflater.inflate(R.layout.fragment_questions, container, false);
        questionView = (TextView) v.findViewById(R.id.question);
        questionView.setText(question);
        answerQuestion = (EditText) v.findViewById(R.id.answer);

        int width = (((MainActivity)getActivity()).metrics.widthPixels)/70;
        answerQuestion.setEms((int) width);
        // Inflate the layout for this fragment
        //return v;



        // Rotate
        rotate.setOnClickListener(new View.OnClickListener() {
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
        });
        // Slide
        next.setOnClickListener(new View.OnClickListener() {
            //private TextView tx = v.findViewById(R.id.question);
            @Override
            public void onClick(View v) {
                animSlide = AnimationUtils.loadAnimation(containerActivity,
                        R.anim.slide);

                questionView.startAnimation(animSlide);

               // ((MainActivity)getActivity()).nextClick(v);

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
        int difficulty = 2;
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