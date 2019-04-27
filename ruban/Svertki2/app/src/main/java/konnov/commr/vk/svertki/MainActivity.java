package konnov.commr.vk.svertki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText arrayAEditText;
    EditText arrayBEditText;
    TextView outputTextView;
    long [] a;
    long [] b;
    long [] c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayAEditText = findViewById(R.id.arrayAEditText);
        arrayBEditText = findViewById(R.id.arrayBEditText);
        outputTextView = findViewById(R.id.outputTextView);
        arrayAEditText.setText("1,2,3,4,5,6,7,8,9,10");
        arrayBEditText.setText("1,2,3,4,5,6,7,8,9,10");
    }

    public void buttonClicked(View view) {
        getInputArrays();
        Svertki.test(a, b, outputTextView);
    }

    void getInputArrays(){
        if(arrayAEditText.getText().toString().isEmpty() || arrayBEditText.getText().toString().isEmpty())
            return;
        String [] aString = arrayAEditText.getText().toString().split(",");
        String [] bString = arrayBEditText.getText().toString().split(",");
        if(aString.length != bString.length)
            return;
        a = new long[aString.length];
        b = new long[bString.length];

        for(int i = 0; i < aString.length; i++){
            a[i] = Integer.parseInt(aString[i]);
            b[i] = Integer.parseInt(bString[i]);
        }
    }
}
