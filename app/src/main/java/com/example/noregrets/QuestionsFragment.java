package com.example.noregrets;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class QuestionsFragment extends Fragment {
    public Activity containerActivity = null;
    private ArrayList<Integer> primeNumbers = new ArrayList<Integer>();
    private ArrayList<Character> mathSymbols = new ArrayList<Character>();
    Button rotate;
    ImageView iv;
    Animation animRotate;
    View v = null;
    public QuestionsFragment() {
        mathSymbols.add('+');
        mathSymbols.add('-');
        mathSymbols.add('*');
        mathSymbols.add('/');
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.v = inflater.inflate(R.layout.fragment_questions, container, false);
        rotate = (Button) v.findViewById(R.id.rotate);


        // Rotate
        rotate.setOnClickListener(new View.OnClickListener() {
            private ImageView iv = v.findViewById(R.id.timer);
            @Override
            public void onClick(View v) {
                iv.setImageDrawable(getResources().getDrawable(R.drawable.timer));
                animRotate = AnimationUtils.loadAnimation(containerActivity,
                        R.anim.rotate);
                this.iv.startAnimation(animRotate);
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

    /**
     * This method creates a random equation and returns a Pair
     * @return Pair   pair.first is the equation to show on the screen, pair.second is the part of the url
     */
    private Pair<String,String> createEquation(){
        Random rand = new Random();
        ArrayList<Character> operations = new ArrayList<Character>();
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        // check difficulty, difficulty 1 = sober, difficulty 2 = drunk
        int difficulty = 1;
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
                    equation.concat(Character.toString(operation));
                    if (operation == '+'){
                        urlName = urlName.concat("%2B");
                    }
                    else if(operation == '-'){
                        urlName = urlName.concat("-");
                    }
                }
            }
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
                    // make call to factor API and create arrayList of factors
                    // API website https://helloacm.com/tools/factor/?server=
                    ArrayList<Integer> factors = new ArrayList<Integer>(); // set to arrayList of factors
                    int numberOfFactorsMultiplied = rand.nextInt(factors.size());
                    number2 = 1;
                    for(int j=0;i<numberOfFactorsMultiplied;i++){
                        int index = rand.nextInt(factors.size());
                        number2*=factors.get(index);
                        factors.remove(index);
                    }
                    combination.put(new Pair<Integer,Integer>(number1,number2),operation);
                }
                else if(operation == '*'){
                    number2 = rand.nextInt(2000/number1);
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
                equation.concat(Character.toString(operations.get(rand.nextInt(2))));
            }
            equation = equation.substring(0,equation.length()-1);
            urlName = equation.replaceAll("\\+","%2B");
            urlName = urlName.replaceAll("/","%2F");
            return new Pair(equation,urlName);
        }
            return new Pair("","");

    }
}