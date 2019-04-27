package konnov.commr.vk.svertki;


import android.widget.TextView;

import java.util.Arrays;

final public class Svertki {
    public static long lastT = 0;

    static public long[] basic(long[] a, long[] b)
    {
        lastT = 0;
        int n = a.length;
        long[] c = new long[n * 2];
        for (int i = 0; i < n; ++i)
            for (int k = 0; k < n; ++k)
            {
                c[i + k] += a[i] * b[k];
                ++lastT;
            }
        return c;
    }

    static public long[] dpfSvertka(long[] a, long[] b)
    {
        lastT = 0;
        int n = a.length;

        Complex[] af = new Complex[n * 2];
        Complex[] aa = new Complex[n * 2];
        for (int i = 0; i < n; ++i)
            af[i] = new Complex(a[i], 0);
        for (int i = n; i < 2 * n; ++i)
            af[i] = new Complex(0, 0);
        Complex[] bf = new Complex[n * 2];
        Complex[] ba = new Complex[n * 2];
        for (int i = 0; i < n; ++i)
            bf[i] = new Complex(b[i], 0);
        for (int i = n; i < 2 * n; ++i)
            bf[i] = new Complex(0, 0);

        Fourier.dpf(af, aa);
        lastT += Fourier.lastT;
        Fourier.dpf(bf, ba);
        lastT += Fourier.lastT;

        Complex[] ca = new Complex[2 * n];
        Complex[] cf = new Complex[2 * n];
        for (int i = 0; i < 2 * n; ++i)
            ca[i] = aa[i].times(ba[i]).scale(2 * n);
        lastT += 2 * n;
        Fourier.dpfReverse(ca, cf);
        lastT += Fourier.lastT;

        long[] c = new long[2 * n];
        for (int i = 0; i < 2 * n; ++i)
            c[i] = Math.round(cf[i].re());
        return c;
    }

    static public long[] pbpfSvertka(long[] a, long[] b)
    {
        lastT = 0;
        int n = a.length;

        Complex[] af = new Complex[n * 2];
        Complex[] aa = new Complex[n * 2];
        for (int i = 0; i < n; ++i)
            af[i] = new Complex(a[i], 0);
        for (int i = n; i < 2 * n; ++i)
            af[i] = new Complex(0, 0);
        Complex[] bf = new Complex[n * 2];
        Complex[] ba = new Complex[n * 2];
        for (int i = 0; i < n; ++i)
            bf[i] = new Complex(b[i], 0);
        for (int i = n; i < 2 * n; ++i)
            bf[i] = new Complex(0, 0);

        Fourier.pbpf(af, aa);
        lastT += Fourier.lastT;
        Fourier.pbpf(bf, ba);
        lastT += Fourier.lastT;

        Complex[] ca = new Complex[2 * n];
        Complex[] cf = new Complex[2 * n];
        for (int i = 0; i < 2 * n; ++i)
            ca[i] = aa[i].times(ba[i]).scale(2 * n);
        lastT += 2 * n;
        Fourier.pbpfReverse(ca, cf);
        lastT += Fourier.lastT;

        long[] c = new long[2 * n];
        for (int i = 0; i < 2 * n; ++i)
            c[i] = Math.round(cf[i].re());

        return c;
    }


    public static void test(long[] a, long[] b, TextView outputTextView) {
        outputTextView.setText("Исходный массив а: " + Arrays.toString(a) + "\n");
        outputTextView.append("Исходный массив b: " + Arrays.toString(b) + "\n");
        long[] c;
//        long [] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        long [] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        c = Svertki.basic(a, b);
        outputTextView.append("\nОбычная свертка:\n");
        for (int i = 0; i < c.length-1; i++)
            outputTextView.append(c[i] + " ");
        outputTextView.append("\n");
        outputTextView.append("Tрудоемкость = " + lastT + "\n");

        outputTextView.append("\nСвертка ДПФ:\n");
        c = Svertki.dpfSvertka(a, b);
        for (int i = 0; i < c.length-1; i++)
            outputTextView.append(c[i] + " ");
        outputTextView.append("\n");
        outputTextView.append("Tрудоемкость = " + lastT + "\n");

        outputTextView.append("\nСвертка ПБПФ:\n");
        c = Svertki.pbpfSvertka(a, b);
        for (int i = 0; i < c.length-1; i++)
            outputTextView.append(c[i] + " ");
        outputTextView.append("\n");
        outputTextView.append("Трудоемкость = " + lastT + "\n");
    }
}
