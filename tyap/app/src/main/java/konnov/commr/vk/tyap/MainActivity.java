package konnov.commr.vk.tyap;

import android.app.AlertDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText alphabetEt;
    private EditText initPodtsepochkaEt;
    private EditText endPodstepochkaEt;
    private EditText kratnostTsepochkiEt;
    private EditText regularExpressionEt;
    private TextView generatedTsepochkaTv;
    private EditText rangeFromEt;
    private EditText rangeToEt;
    private Button loadAlphabetBtn;
    private Button createRegularExpressionBtn;
    private Button saveToFileBtn;
    private Button generateTsepochkiBtn;
    private Button clearBtn;
    private Solution solution;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alphabetEt = findViewById(R.id.alphabet_et);
        initPodtsepochkaEt = findViewById(R.id.init_podtsepochka_et);
        endPodstepochkaEt = findViewById(R.id.end_podtsepochka_et);
        kratnostTsepochkiEt = findViewById(R.id.kratnost_tsepochki_et);
        regularExpressionEt = findViewById(R.id.regular_expression_et);
        generatedTsepochkaTv = findViewById(R.id.generated_tsepochki_tv);
        rangeFromEt = findViewById(R.id.range_from_et);
        rangeToEt = findViewById(R.id.range_to_et);
        loadAlphabetBtn = findViewById(R.id.load_alphabet_btn);
        loadAlphabetBtn.setOnClickListener(this);
        createRegularExpressionBtn = findViewById(R.id.create_regular_expression_btn);
        createRegularExpressionBtn.setOnClickListener(this);
        clearBtn = findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);
        saveToFileBtn = findViewById(R.id.save_to_file_btn);
        saveToFileBtn.setOnClickListener(this);
        generateTsepochkiBtn = findViewById(R.id.generate_tsepochki_btn);
        generateTsepochkiBtn.setOnClickListener(this);
        solution = new Solution(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_alphabet_btn: { //Загрузить алфавит
                solution.loadAlphabetBtnClick(alphabetEt);
                break;
            }
            case R.id.create_regular_expression_btn : { //Создать регулярное выражение
                solution.createRegularExpressionBtnClick(initPodtsepochkaEt, endPodstepochkaEt,
                        regularExpressionEt, alphabetEt, kratnostTsepochkiEt);
                break;
            }
            case R.id.save_to_file_btn: { //Сохранить в файл
                solution.saveToFileBtnClick(alphabetEt, initPodtsepochkaEt, endPodstepochkaEt,
                        kratnostTsepochkiEt, regularExpressionEt, generatedTsepochkaTv);
                break;
            }
            case R.id.generate_tsepochki_btn: { //Сгенерировать цепочки
                solution.generateTsepochkiBtnClick(generatedTsepochkaTv, regularExpressionEt,
                        rangeFromEt, rangeToEt);
                break;
            }
            case R.id.clear_btn: { //Очистить все поля
                solution.clearBtnClick(alphabetEt, initPodtsepochkaEt, endPodstepochkaEt, kratnostTsepochkiEt, regularExpressionEt,
                        generatedTsepochkaTv, rangeFromEt, rangeToEt);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.description : {
                showCustomDialog("Написать программу, которая по предложенному описанию языка построит регулярную " +
                        "грамматику, задающую этот язык, и позволит сгенерировать с её помощью все цепочки языка в " +
                        "заданном диапазоне длин. Предусмотреть возможность поэтапного отображения на экране процесса " +
                        "генерации цепочек. Алфавит, заданные начальная и конечная подцепочки и кратность длины всех " +
                        "цепочек языка.");
                break;
            }
            case R.id.about : {
                showCustomDialog("Автор Коннов И.В.\nГруппа ИП-512\nВариант 8");
                break;
            }
        }
        return true;
    }


    private void showCustomDialog(String message){
        AlertDialog.Builder builder1;
        if(Build.VERSION.SDK_INT >= 21) {
            builder1 = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        }
        else {
            builder1 = new AlertDialog.Builder(this);
        }
        builder1.setMessage(message);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
