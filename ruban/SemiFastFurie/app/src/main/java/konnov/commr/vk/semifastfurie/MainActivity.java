package konnov.commr.vk.semifastfurie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView output = findViewById(R.id.textView);
        runTheProgram(output);
    }



    final int n = 5;
    int p_1 = 5, p_2 = 1;
    int count_1 = 0, count_2 = 0;
    final double M_PI = 3.14159265358979323846264338327950288;


    double [] array;
    Complex [] res;
    Complex [] back_res;

    Complex multiple_complex(Complex a, Complex b) {
        Complex result = new Complex();
        result.real = 0;
        result.image = 0;
        result.real = a.real * b.real - a.image * b.image;
        result.image = a.real * b.image + a.image * b.real;
        return result;
    }

    Complex first_transform(double []array, int k_1, int j_2) {
        double coef;
        Complex sum = new Complex();
        Complex temp = new Complex();
        sum.real = 0;
        sum.image = 0;
        for (int j_1 = 0; j_1 < p_1; j_1++) {
            count_1 += 5;
            coef = (double) (j_1 * k_1) / p_1;
            temp.real = Math.cos(-2 * M_PI * coef) * array[j_2 + p_2 * j_1];
            temp.image = Math.sin(-2 * M_PI * coef) * array[j_2 + p_2 * j_1];
            sum.real += temp.real;
            sum.image += temp.image;
        }
        sum.real /= p_1;
        sum.image /= p_1;
        return sum;
    }

    Complex second_transform(Complex [][]array, int k_1, int k_2) {
        int k;
        double coif;
        Complex sum  = new Complex();
        Complex temp  = new Complex();
        Complex res_mul;
        sum.real = 0;
        sum.image = 0;
        for (int j_2 = 0; j_2 < p_2; j_2++) {
            count_2 += 7;
            k = k_1 + p_1 * k_2;
            coif = (double) (j_2 * k) / (p_1 * p_2);
            temp.real = Math.cos(-2 * M_PI * coif);
            temp.image = Math.sin(-2 * M_PI * coif);
            res_mul = multiple_complex(array[k_1][j_2], temp);
            sum.real += res_mul.real;
            sum.image += res_mul.image;
        }
        sum.real /= p_2;
        sum.image /= p_2;
        return sum;
    }

    Complex back_first_transform(Complex []array, int k_1, int j_2) {
        double coef;
        Complex sum = new Complex();
        Complex temp = new Complex();
        Complex res_mul;
        sum.real = 0;
        sum.image = 0;
        for (int j_1 = 0; j_1 < p_1; j_1++) {
            coef = (double) (j_1 * k_1) / p_1;
            temp.real = Math.cos(2 * M_PI * coef);
            temp.image = Math.sin(2 * M_PI * coef);
            res_mul = multiple_complex(temp, array[j_2 + p_2 * j_1]);
            sum.real += res_mul.real;
            sum.image += res_mul.image;
        }
        return sum;
    }

    Complex back_second_transform(Complex []array, int k_1, int k_2) {
        int k;
        double coef;
        Complex sum = new Complex();
        Complex temp = new Complex();
        Complex res_mul;
        Complex a_1;
        sum.real = 0;
        sum.image = 0;
        for (int j_2 = 0; j_2 < p_2; j_2++) {
            a_1 = back_first_transform(array, k_1, j_2);
            k = k_1 + p_1 * k_2;
            coef = (double) (j_2 * k) / (p_1 * p_2);
            temp.real = Math.cos(2 * M_PI * coef);
            temp.image = Math.sin(2 * M_PI * coef);
            res_mul = multiple_complex(a_1, temp);
            sum.real += res_mul.real;
            sum.image += res_mul.image;
        }
        return sum;
    }

    void half_quick_transform(double []source, Complex []result) {
        Complex [][]a1;
        a1 = new Complex[n*n][n*n]; /////////////////////////по идее должно быть a1 = new Complex[][n];
//        for (i = 0; i < n; i++) {
//            a1[i] = new Complex [n];
//        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               a1[i][j] = new Complex();
            }
        }

        for (int j_2 = 0; j_2 < p_2; j_2++) {
            for (int k_1 = 0; k_1 < p_1; k_1++) {
                a1[k_1][j_2].real = 0;
                a1[k_1][j_2].image = 0;
                a1[k_1][j_2] = first_transform(source, k_1, j_2);
            }
        }
        int i = 0;
        for (int k_2 = 0; k_2 < p_2; k_2++) {
            for (int k_1 = 0; k_1 < p_1; k_1++) {
                result[i] = second_transform(a1, k_1, k_2);
                i++;
            }
        }
    }

    void back_half_quick_transform(Complex []source, Complex []result) {
        int i = 0;
        for (int k_2 = 0; k_2 < p_2; k_2++) {
            for (int k_1 = 0; k_1 < p_1; k_1++) {
                result[i] = back_second_transform(source, k_1, k_2);
                i++;
            }
        }
    }

    void runTheProgram(TextView output) {
        output.append("Полу-быстрое преобразование Фурье\n\n");
        array = new double[n];
        res = new Complex[n];
        back_res = new Complex[n];
        output.append("Исходный массив ");
        array[0] = 0;
        array[1] = 1;
        array[2] = 0;
        array[3] = 1;
        array[4] = 0;
        for (int i = 0; i < n; i++) {
            output.append(array[i] + " ");
        }
        half_quick_transform(array, res);
        output.append("\n\n");
        output.append("Реальная часть полубыстрого преобразования \n");
        for (int i = 0; i < n; i++) {
            output.append(String.format("%.6f", res[i].real) + " \n");
        }
        output.append("\n\n");
        output.append("Мнимая часть полубыстрого преобразования: \n");
        for (int i = 0; i < n; i++) {
            output.append(String.format("%.6f", res[i].image) + " \n");
        }
        output.append("\n\n");
        back_half_quick_transform(res, back_res);
        output.append("Реальная часть обратного полубыстрого преобразования: \n");
        for (int i = 0; i < n; i++) {
            output.append(String.format("%.6f", back_res[i].real) + " \n");
        }
//        output.append("0.000000 " + "1.000000" + );
        output.append("\n\n");
        output.append("Мнимая часть обратного полубыстрого преобразования: \n");
        for (int i = 0; i < n; i++) {
            output.append(String.format("%.6f", back_res[i].image) + " \n");
        }
        output.append("\n\n");
        int trudoemkost = count_1 + count_2;
        output.append("Трудоемкость: " + trudoemkost + "\n");
    }
}
