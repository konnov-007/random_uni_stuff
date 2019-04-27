package konnov.commr.vk.backpacktask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    EditText massesEditText;
    EditText pricesEditText;
    EditText fixedMassEditText;
    TextView outputTextView;
    int[] masses;
    int[] prices;
    int fixedMass;
    int ind;
    int trudoemkost;
    ArrayList<Integer> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        massesEditText = findViewById(R.id.backpackmassesEditText);
        pricesEditText = findViewById(R.id.backpackpricesEditText);
        fixedMassEditText = findViewById(R.id.fixedmassEditText);
        outputTextView = findViewById(R.id.outputTextView);
        massesEditText.setText("6,4,3,2,5");
        pricesEditText.setText("5,3,1,3,6");
        fixedMassEditText.setText("15");
    }

    public void calculateButtonClicked(View view) {
        trudoemkost = 0;
        String massesString = massesEditText.getText().toString();
        String pricesString = pricesEditText.getText().toString();
        String fixedMassString = fixedMassEditText.getText().toString();
        if(massesString.isEmpty() || pricesString.isEmpty() || fixedMassString.isEmpty())
            Toast.makeText(this, "Заполнены не все поля", Toast.LENGTH_SHORT).show();
        else{
            masses = stringsToIntArray(massesString.split(","));
            prices = stringsToIntArray(pricesString.split(","));
            if(masses.length != prices.length){
                Toast.makeText(this, "количество масс должно соответствовать количеству цен", Toast.LENGTH_SHORT).show();
            }
            else {
                fixedMass = Integer.parseInt(fixedMassString);
                outputTextView.setText("Массы: " + Arrays.toString(masses) + "\nЦены: " + Arrays.toString(prices) + "\nФиксированная масса " + fixedMass + "\n\n");
                double maxCost = task_about_bag(masses, prices, fixedMass, masses.length);
                for (int i = 0; i < set.size(); i++) {
                    outputTextView.append(String.format("%d) %d-ый товар с массой m = %d, c = %d\n", i + 1, set.get(i) + 1, masses[set.get(i)], prices[set.get(i)]));
                }
                outputTextView.append("\nМаксимальная стоимость текущего W: " + maxCost + "\n");
                outputTextView.append("Трудоемкость: " + trudoemkost + "\n\n");
            }
        }

    }


    private int[] stringsToIntArray(String[] strings) {
        if(strings.length == 0)
            return null;
        int[] intArray = new int[strings.length];
        for(int i = 0; i < strings.length; i++){
            intArray[i] = Integer.parseInt(strings[i]);
        }
        return intArray;
    }


    double task_about_bag(int[] m, int[] c, int W, int n) {
        ArrayList<Double> f = new ArrayList<>();
        ArrayList<Double> sums = new ArrayList<>();

        ArrayList<Integer> prev = new ArrayList<>();
        int min_mass = search_min(m); //находим минимальую массу
        for (int i = 0; i < W; i++) { //заполняем массивы нулями
            if (i < min_mass) {
                f.add(0D);
                prev.add(0);
            }
            else break;
        }
        for (int i = f.size(); i <= W; i++) {
            trudoemkost++;
            for (int k = 0; k < n; k++) {
                trudoemkost++;
                int temp = i - m[k];
                if (temp < 0) sums.add(-1D);
                else sums.add(f.get(i - m[k]) + c[k]);
            }
            double max = search_max(sums); //находим максимальную сумму
            prev.add(ind);
            f.add(max);
            sums.clear();
            ind = 0;
        }
        set = get_set_products(prev, m, W);
        return f.get(f.size() - 1);
    }


    ArrayList<Integer> get_set_products(ArrayList<Integer> prev, int[] m, int W) {
        ArrayList<Integer> indexes = new ArrayList<>();
        int weight = W;
        while (weight != 0) {
            int temp = m[prev.get(weight)];
            if (weight - temp < 0) break;
            indexes.add(prev.get(weight));
            weight -= temp;
        }
        return indexes;
    }


    int search_min(int[] m) {
        int min = m[0];
        for (int i = 1; i < m.length; i++) {
            if (m[i] < min) min = m[i];
        }
        return min;
    }


    double search_max(ArrayList<Double> m) {
        double max = m.get(0);
        for (int i = 1; i < m.size(); i++) {
            if (m.get(i) > max) {
                max = m.get(i);
                ind = i;
            }
        }
        return max;
    }



}

///F(M) = max(F(M-mi)+ci)
