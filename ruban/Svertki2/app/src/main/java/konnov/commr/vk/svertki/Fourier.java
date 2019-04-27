package konnov.commr.vk.svertki;

final public class Fourier {
    public static long lastT = 0;

    public static void dpf(Complex[] f, Complex[] a) {
        lastT = 0;
        int n = f.length;
        for (int k = 0; k < n; k++) {
            a[k] = new Complex(0, 0);
            for (int j = 0; j < n; ++j) {
                Complex exp = new Complex(0, -2.0 * k * j * Math.PI / n);
                exp = exp.exp();
                a[k] = Complex.plus(a[k], exp.times(f[j]));
                lastT += 5;
            }
            a[k] = a[k].scale(1.0 / n);
        }
    }

    public static void dpfReverse(Complex[] a, Complex[] f) {
        lastT = 0;
        int n = a.length;
        for (int k = 0; k < n; k++) {
            f[k] = new Complex(0, 0);
            for (int j = 0; j < n; ++j) {
                Complex exp = new Complex(0, 2.0 * k * j * Math.PI / n);
                exp = exp.exp();
                f[k] = Complex.plus(f[k], exp.times(a[j]));
                lastT += 5;
            }
        }
    }

    public static void pbpf(Complex[] f, Complex[] a) {
        lastT = 0;
        int n = f.length;
        int p1 = (int) Math.sqrt(n * 1.0);
        int p2;
        while (n % p1 != 0)
            --p1;
        p2 = n / p1;

        Complex[][] a1 = new Complex[p1][p2];
        for (int k1 = 0; k1 < p1; ++k1) {
            for (int j2 = 0; j2 < p2; ++j2) {
                a1[k1][j2] = new Complex(0, 0);
                for (int j1 = 0; j1 < p1; ++j1) {
                    Complex exp = new Complex(0, -2.0 * k1 * j1 * Math.PI / p1);
                    exp = exp.exp();
                    a1[k1][j2] = Complex.plus(a1[k1][j2], exp.times(f[j2 + p2 * j1]));
                    lastT += 5;
                }
                a1[k1][j2] = a1[k1][j2].scale(1.0 / p1);
            }
        }

        Complex[][] a2 = new Complex[p1][p2];
        for (int k1 = 0; k1 < p1; ++k1) {
            for (int k2 = 0; k2 < p2; ++k2) {
                a2[k1][k2] = new Complex(0, 0);
                for (int j2 = 0; j2 < p2; ++j2) {
                    Complex exp = new Complex(0, -2.0 * (k1 + p1 * k2) * j2 * Math.PI / n);
                    exp = exp.exp();
                    a2[k1][k2] = Complex.plus(a2[k1][k2], exp.times(a1[k1][j2]));
                    lastT += 5;
                }
                a2[k1][k2] = a2[k1][k2].scale(1.0 / p2);
            }
        }

        for (int k = 0; k < n; k++) {
            int k1 = k % p1;
            int k2 = k / p1;
            a[k] = a2[k1][k2];
        }
    }

    public static void pbpfReverse(Complex[] a, Complex[] f) {
        lastT = 0;
        int n = a.length;
        int p1 = (int) Math.sqrt(n * 1.0);
        int p2;
        while (n % p1 != 0)
            --p1;
        p2 = n / p1;

        Complex[][] f1 = new Complex[p1][p2];
        for (int k1 = 0; k1 < p1; ++k1) {
            for (int j2 = 0; j2 < p2; ++j2) {
                f1[k1][j2] = new Complex(0, 0);
                for (int j1 = 0; j1 < p1; ++j1) {
                    Complex exp = new Complex(0, 2.0 * k1 * j1 * Math.PI / p1);
                    exp = exp.exp();
                    f1[k1][j2] = Complex.plus(f1[k1][j2], exp.times(a[j2 + p2 * j1]));
                    lastT += 5;
                }
            }
        }

        Complex[][] f2 = new Complex[p1][p2];
        for (int k1 = 0; k1 < p1; ++k1) {
            for (int k2 = 0; k2 < p2; ++k2) {
                f2[k1][k2] = new Complex(0, 0);
                for (int j2 = 0; j2 < p2; ++j2) {
                    Complex exp = new Complex(0, 2.0 * (k1 + p1 * k2) * j2 * Math.PI / n);
                    exp = exp.exp();
                    f2[k1][k2] = Complex.plus(f2[k1][k2], exp.times(f1[k1][j2]));
                    lastT += 5;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            int k1 = k % p1;
            int k2 = k / p1;
            f[k] = f2[k1][k2];
        }
    }


//    public static void test() {
//        int n = 2;
//        System.out.println("Размер массивов: " + n);
//        Complex[] f = new Complex[n];
//        for (int i = 0; i < n; ++i)
//            f[i] = new Complex(i % 2, 0);
//        Complex[] a = new Complex[n];
//        Complex[] fi = new Complex[n];
//
//        for (Complex c : f)
//            System.out.println(c + " ");
//        System.out.println();
//
//        long start = System.currentTimeMillis();
//        Fourier.dpf(f, a);
//        for (Complex c : a)
//            System.out.println(c + " ");
//        long finish = System.currentTimeMillis();
//        System.out.println("T = " + lastT + " Time: " + (finish - start) + "\n");
//
//        start = finish;
//        Fourier.dpfReverse(a, fi);
//        for (Complex c : fi)
//            System.out.println(c + " ");
//        finish = System.currentTimeMillis();
//        System.out.println("T = " + lastT + " Time: " + (finish - start) + "\n");
//
//        start = finish;
//        Fourier.pbpf(f, a);
//        for (Complex c : a)
//            System.out.println(c + " ");
//        finish = System.currentTimeMillis();
//        System.out.println("T = " + lastT + " Time: " + (finish - start) + "\n");
//
//        start = finish;
//        Fourier.pbpfReverse(a, fi);
//        for (Complex c : fi)
//            System.out.println(c + " ");
//        finish = System.currentTimeMillis();
//        System.out.println("T = " + lastT + " Time: " + (finish - start) + "\n");
//    }
}
