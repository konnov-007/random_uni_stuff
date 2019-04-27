package konnov.commr.vk.multiplicationofmatrixproblem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    EditText inputEditText;
    TextView outputTextView;
    Sizes[] sizes;
    int numberOfMatrixes;
    int trudoemkost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEditText = findViewById(R.id.inputEditText);
        outputTextView = findViewById(R.id.outputTextView);
    }

    public void buttonClicked(View view) {
        if(inputEditText.getText().toString().isEmpty())
            return;
        outputTextView.setText("");
        getMatrixes();
//        outputTextView.setText("first \t second");
//        for(int i = 0; i < numberOfMatrixes; i++){
//            outputTextView.append("\n" + sizes[i].first);
//            outputTextView.append(" \t " + sizes[i].second);
//        }
        multiplication();

    }


     void getMatrixes(){
        String [] sizesString = inputEditText.getText().toString().split("\n");
        numberOfMatrixes = sizesString.length;
        sizes = new Sizes[numberOfMatrixes];
        for(int i = 0; i < numberOfMatrixes; i++)
            sizes[i] = new Sizes();
        for(int i = 0; i < numberOfMatrixes; i++){
            try {
                String eachMatrixString[] = sizesString[i].split(" ");
                sizes[i].first = Integer.parseInt(eachMatrixString[0]);
                sizes[i].second = Integer.parseInt(eachMatrixString[1]);

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void multiplication(){
        int i, j, M;
        int [] statR = new int[numberOfMatrixes + 1];
        for(i = 0; i<numberOfMatrixes; i++){
            statR[i] = sizes[i].first;
            if(i == numberOfMatrixes - 1)
                statR[i+1] = sizes[i].second;
        }
//        statR[0] = sizes[0].first;
//        statR[1] = sizes[1].first;
//        statR[2] = sizes[2].first;
//        statR[3] = sizes[3].first;
//        statR[4] = sizes[3].second;
        //outputTextView.setText(Arrays.toString(statR) + "\n\n");
        int []r = new int [numberOfMatrixes + 1];
        for(i = 0; i < numberOfMatrixes+1; ++i)
            r[i] = statR[i];
    int [][]f = new int [numberOfMatrixes][numberOfMatrixes];
        for(i = 0; i < numberOfMatrixes; ++i) {
            f[i] = new int [numberOfMatrixes]; ////????? //
            for(j = 0; j < numberOfMatrixes; ++j)
                f[i][j] = -1;
        }
        for(i = 0; i < numberOfMatrixes; ++i)
            f[i][i] = 0;

        int t, k, temp;
    int [][]jm = new int [numberOfMatrixes][numberOfMatrixes];
        for(i = 0; i < numberOfMatrixes; ++i) {
            jm[i] = new int [numberOfMatrixes]; ////????? //
            for(j = 0; j < numberOfMatrixes; ++j)
                jm[i][j] = -1;
        }
        for(t = 1; t < numberOfMatrixes; ++t) {
            for(k = 0; k < numberOfMatrixes-t; ++k) {
                jm[k][k+t] = k;
                f[k][k+t] = f[k][k] + f[k+1][k+t] + r[k]*r[k+1]*r[k+t+1];
                for(j = k + 1; j < k+t; ++j) {
                    temp = f[k][j] + f[j+1][k+t] + r[k]*r[j+1]*r[k+t+1];
                    if(temp < f[k][k+t]) {
                        jm[k][k+t] = j;
                        f[k][k+t] = temp;
                        trudoemkost = f[k][k+t];
                    }
                }
            //outputTextView.append("f(" + (int) (k+1) + "," +(int)(k+t+1) + ") = min<f(" + (int) (k+1) + "," + "jb+1" + ") + f(" + "jb" + 2 + "," + (int)k+t+1 + ") + " + r[k] + "*" + "r[jb+1]" + "*" +  r[k+t+1] + ">");
            }
        }


//        for(i = 0; i < numberOfMatrixes; ++i) {
//            for(j = 0; j < numberOfMatrixes; ++j)
//                outputTextView.append(f[i][j] + " ");
//            outputTextView.append("\n");
//        }
//        outputTextView.append("\n");
//        for(i = 0; i < numberOfMatrixes; ++i) {
//            for(j = 0; j < numberOfMatrixes; ++j)
//                outputTextView.append(jm[i][j] + " ");
//            outputTextView.append("\n");
//        }

        outputTextView.append("\n");
        outputTextView.append("Оптимальное выравнивание скобок: \n");
        print(f, jm, numberOfMatrixes, 0, numberOfMatrixes-1);
        outputTextView.append("\n\nТрудоемкость: " + trudoemkost);
    }

    void print(int [][]f, int [][]jm, int n, int up, int down) {
        switch (down - up) {
            case 0: {
                outputTextView.append(" M[" + (int) (up + 1) + "] ");
                return;
            }
            case 1: {
                outputTextView.append("( ");
                outputTextView.append("M[" + (int) (up + 1) + "] * M[" + (int) (down + 1) + "]");
                outputTextView.append(" )");
                return;
            }
            default: {
                outputTextView.append("( ");
                print(f, jm, n, up, jm[up][down]);
                print(f, jm, n, (int) (jm[up][down] + 1), down);
                outputTextView.append(" )");
                return;
            }
        }
    }



    class Sizes{
        int first;
        int second;
    }
}
