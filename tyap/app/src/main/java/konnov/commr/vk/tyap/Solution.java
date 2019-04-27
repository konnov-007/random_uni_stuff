package konnov.commr.vk.tyap;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static konnov.commr.vk.tyap.Tools.GENERATED_TSEPOCHKI_FILE_LOCATION;
import static konnov.commr.vk.tyap.Tools.INPUT_ALPHABET_FILE_LOCATION;
import static konnov.commr.vk.tyap.Tools.REG_EXP_FILE_LOCATION;
import static konnov.commr.vk.tyap.Tools.TOTAL_OUTPUT_FILE_LOCATION;
import static konnov.commr.vk.tyap.Tools.readFromFile;
import static konnov.commr.vk.tyap.Tools.saveToFile;

public class Solution {

    private Context context;
    public Solution(Context context){
        this.context = context;
        fl_gen = false;
    }

    class RegMn { //регулярное множество
        ArrayList<String> s = new ArrayList<>();
        protected int top;
        protected char y;

        public RegMn(int top){
            this.top = top;
        }

        void insert(String str){
            if(str.length() <= top) {
                s.add(str);
            }
            Collections.sort(s, new Tools.StringLengthComparator());
        }

        int length(){
            return s.size();
        }

        void toStringF(int l){
            int k = 1;
            StringBuilder stringToSave = new StringBuilder();
            if(y > 0 && l == 0) {
                stringToSave.append(k++).append(".lam").append("; ");
            }
            for(String str : s){
                if(str.length() >= l) {
                    stringToSave.append("  ").append(k++).append(".").append(str).append("; ");
                }
            }
            saveToFile(GENERATED_TSEPOCHKI_FILE_LOCATION, stringToSave.toString());
        }

        void setTop(int top){
            if(top > this.top) {
                this.top = top;
            }
        }

        RegMn zamk(){
            RegMn rez = new RegMn(top);
            RegMn l = new RegMn(top);
            l.y = 1;
            rez.y = 1;
            int k;
            while(true){
                k = rez.length();
                l = this.multiply(l);
                rez = rez.sum(l);
                if(k == rez.length()){
                    break;
                }
            }
            return rez;
        }

        RegMn sum(RegMn val){
            int k;
            if(top > val.top) k = top;
            else k = val.top;
            RegMn rez = new RegMn(k);
            if(y > 0 || val.y > 0){
                rez.y = 1;
            }
            for(int i = 0; i < s.size(); i++){
                rez.insert(s.get(i));
            }
            for(int i = 0; i != val.s.size() ; i++){
                rez.insert(val.s.get(i));
            }
            return rez;
        }

        RegMn multiply(RegMn val){
            int k;
            if(top > val.top){
                k = top;
            }
            else {
                k = val.top;
            }
            RegMn rez = new RegMn(k);
            String str;
            int i, j;
            for( i = 0; i < s.size(); i++) {
                for (j = 0; j < val.s.size(); j++) {
                    str = s.get(i) + val.s.get(j);
                    rez.insert(str);
                }
            }
            if(y > 0) {
                for (i = 0; i < val.s.size(); i++) {
                    rez.insert(val.s.get(i));
                }
            }
            if(val.y > 0) {
                for (i = 0; i < s.size(); i++) {
                    rez.insert(s.get(i));
                }
            }
            if(y > 0 && val.y > 0) {
                rez.y = 1;
            }
            return rez;
        }

    }

    private RegMn getRegMn(String regvr, int len){
        RegMn rez = new RegMn(len);
        RegMn v1 = new RegMn(len);
        RegMn v2 = new RegMn(len);
        String str1 = "";
        String str2 = "";
        char ch,chpred = '+';
        int fl, i, sum, mul, zam, k, m;
        sum = mul = zam = -1;
        fl = 0;
        k = 0;
        for(i = 0 ; i < regvr.length(); i++){
            ch = regvr.charAt(i);
            if((ch < 'a' || ch > 'z')){
                fl = 1;
            }
            if(chpred != '+' && chpred != '(' && ch != '+' && ch != ')' && ch != '*' && mul == -1 &&
                    k == 0 && ( ch<'a' || ch>'z' || chpred<'a' || chpred>'z' )){
                mul = i;
            }
            if(ch == '+' && sum == -1 && k == 0){
                sum = i;
            }
            if(ch == '*' && zam == -1 && k == 0){
                zam = i;
            }
            if(ch == '('){
                k++;
            }
            if(ch == ')'){
                k--;
            }
            chpred = ch;
        }

        if(fl == 0){
            rez.insert(regvr);
            return rez;
        }
        try{
            if(sum != -1){
                str1 = regvr.substring(0, sum);
                str2 = regvr.substring(sum+1);
                v1 = getRegMn(str1,len);
                v2 = getRegMn(str2,len);
                return v1.sum(v2);
            }
            if(mul != -1){
                str1 = regvr.substring(0 ,mul);
                str2 = regvr.substring(mul);
                k = getZamTop(str1);
                m = getZamTop(str2);
                v1 = getRegMn(str1,(len-m>0)?len-m:1);
                v2 = getRegMn(str2,(len-k>0)?len-k:1);
                v1.setTop(len);
                v2.setTop(len);
                return v1.multiply(v2);
            }
            if(zam != -1){
                str1 = regvr.substring(0, zam);
                v1 = getRegMn(str1,len);
                return v1.zamk();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if(regvr.charAt(0) == '(' && regvr.charAt(regvr.length()-1) == ')'){
            str1 = regvr.substring(1, 1 + regvr.length()-2);
            return getRegMn(str1,len);
        }
        return null;
    }

    private int getZamTop(String regvr){
        int rez, v1, v2;
        String str1 = "";
        String str2 = "";
        char ch,chpred = '+';
        int fl,i,sum,mul,zam,k;
        sum = mul = zam = -1;
        fl = 0;
        k = 0;
        for( i=0 ; i < regvr.length(); i++){
            ch = regvr.charAt(i);
            if(ch < 'a' || ch > 'z'){
                fl = 1;
            }
            if(chpred != '+' && chpred != '(' && ch != '+' && ch != ')' && ch != '*' && mul == -1 &&
                    k == 0 && ( ch<'a' || ch>'z' || chpred<'a' || chpred>'z'  )){
                mul = i;
            }
            if(ch == '+' && sum == -1 && k == 0){
                sum = i;
            }
            if(ch == '*' && zam == -1 && k == 0){
                zam = i;
            }
            if(ch == '('){
                k++;
            }
            if(ch == ')'){
                k--;
            }
            chpred = ch;
        }

        if(fl == 0){
            return regvr.length();
        }
        if(sum != -1){
            str1 = regvr.substring(0 ,sum);
            str2 = regvr.substring(sum+1);
            v1 = getZamTop(str1);
            v2 = getZamTop(str2);
            if( v1 < v2 ){
                return v1;
            }
            else {
                return v2;
            }
        }
        if(mul != -1){
            str1 = regvr.substring(0 ,mul);
            str2 = regvr.substring(mul);
            v1 = getZamTop(str1);
            v2 = getZamTop(str2);
            return v1 + v2;
        }
        if(zam != -1){
            return 0;
        }
        if(regvr.charAt(0) == '(' && regvr.charAt(regvr.length()-1) == ')'){
            str1 = regvr.substring(1,1 + regvr.length()-2);
            return getZamTop(str1);
        }
        return 0;
    }

    private int num1;
    private int num2;
    private boolean fl_gen;

//В методе описан алгоритм формирования регулярного выражения.
    //Для этого мы анализируем алфавит и в зависимости от кратности формируем цепочки вида
        // ((a+b+…+z)…(a+b+…+z))*, где количество вхождений равно кратности.
    public void createRegularExpressionBtnClick(EditText init_podtsepochka_et, EditText end_podtsepochka_et,
                                                EditText regular_expression_et, EditText alphabet_et,
                                                EditText kratnost_tsepochki_et) {
        if(alphabet_et.getText().toString().isEmpty()) {
            showToast("Сначала введите алфавит");
            return;
        }
        String initPodtepochkaStr = init_podtsepochka_et.getText().toString();
        StringBuilder endPodtsepochkaStr = new StringBuilder(end_podtsepochka_et.getText().toString());
        regular_expression_et.setText("");
        boolean flag_VT = false;
        String[] VT; //множество терминальный символов
        VT = alphabet_et.getText().toString().split(",");
        int initPodtsepochkaLength = initPodtepochkaStr.length();
        int endPodtsepochkaLength = endPodtsepochkaStr.length();
        int nVT = 0;
        if (VT[0].equals("")) {
            flag_VT = true;
        }
        for(String letter : VT) {
            if (flag_VT == false) {
                if (letter.length() > 1) {
                    flag_VT = true;
                    regular_expression_et.setText("Введите алфавит правильно. Каждая буква алфавита через запятую, " +
                            "без повторений");
                }
                else if (letter.length() == 1) {
                    int eq = 0;
                    for(String letter2 : VT) {
                        if (letter2.equals(letter)) {
                            eq++;
                            if (eq == 2) {
                                flag_VT = true;
                                regular_expression_et.setText("Введите алфавит правильно. Каждая буква алфавита через " +
                                        "запятую, без повторений");
                                break;
                            }
                        }
                    }
                    if (flag_VT == false) {
                        nVT++;
                    }
                }
            }
        }

        if (flag_VT == false) {
            int co = 0, co2 = 0;
            for (int i = 0; i < initPodtsepochkaLength; i++) {
                for(String letter : VT){
                    if (letter.charAt(0) == initPodtepochkaStr.charAt(i)) {
                        co++;
                    }
                }
            }
            for (int i = 0; i < endPodtsepochkaLength; i++) {
                for(String  letter : VT) {
                    if (letter.charAt(0) == endPodtsepochkaStr.charAt(i)) {
                        co2++;
                    }
                }
            }

            if (co != initPodtsepochkaLength) {
                regular_expression_et.setText("Вводите начальную подцепочку с помощью символов алфавита!");
                if (co2 != endPodtsepochkaLength) {
                    regular_expression_et.append("Вводите конечную подцепочку с помощью символов алфавита!");
                }
            }
            else {
                if (co2 != endPodtsepochkaLength){
                    regular_expression_et.setText("Вводите конечную подцепочку с помощью символов алфавита!");
                }
            }
            if (co == initPodtsepochkaLength && co2 == endPodtsepochkaLength) {
                boolean fl_krat = true;
                int krat;
                if (kratnost_tsepochki_et.getText().toString().isEmpty()) {
                    krat = 1;
                    kratnost_tsepochki_et.setText(String.valueOf(krat));
                }
                else {
                    krat = Integer.parseInt(kratnost_tsepochki_et.getText().toString());
                    if(krat == 0) {
                        krat = 1;
                    }
                }
                if (fl_krat == true) {
                    String buf = " ";
                    String bufout = "";
                    if (initPodtsepochkaLength != 0) {
                        bufout += "(" + initPodtepochkaStr + ")";
                    }
                    int i = 0;
                    String abuf = "(";
                    for(String letter : VT) {
                        if (i == 0){
                            abuf += letter + "+";
                        }
                        if (i != 0 && i != nVT - 1){
                            abuf += letter + "+";
                        }
                        if (i == nVT - 1){
                            abuf += letter + ")";
                        }
                        i++;
                    }
                    int schyot;
                    schyot = (initPodtsepochkaLength + endPodtsepochkaLength)% krat;
                    if(schyot > 0) {
                        schyot = krat - (initPodtsepochkaLength + endPodtsepochkaLength)% krat;
                    }
                    if(krat != 1)for(i = 1; i <= schyot; i++) {
                        bufout += abuf;
                    }
                    buf = "(";
                    for (i = 1; i <= krat; i++) {
                        buf += abuf;
                    }
                    buf += ")*";
                    if (nVT == 0){
                        buf = "";
                    }
                    //TODO разобрать
                    //скобки слева добивают до кратности
                    //+abc потому что он тоже подходит
                    //суффикс ab, префикс bc. Когда аб = бс делаем + abc. Если длина цепочки 3 то не подохдит
                    //+abc это пересечение цепочек кратное 3
                    bufout += buf; ////
                    if (buf != "") {
                        fl_gen = true;
                    }
                    if (endPodtsepochkaLength != 0) {
                        bufout += "(" + endPodtsepochkaStr + ")";
                    }
                    String c;
                    String temp;
                    for (int l = 0; l < initPodtepochkaStr.length(); ++l) {
                        if(endPodtsepochkaStr.length() >= l + 1) {
                            c = endPodtsepochkaStr.substring(0,l + 1);
                        }
                        else {
                            break;
                        }
                        int k = 0;
                        for(int j = 0; j < c.length(); j++){
                            if(c.charAt(j) == initPodtepochkaStr.charAt(j+(initPodtepochkaStr.length() - c.length()))){
                                k++;
                            }
                            if(k == c.length()){
                                temp = endPodtsepochkaStr.delete(0, c.length()).toString();
                                if((temp.length() + initPodtepochkaStr.length()) % krat == 0) {
                                    bufout += "+" + initPodtepochkaStr + temp;
                                }
                            }
                        }
                    }
                    regular_expression_et.append(bufout);
                }
            }
            saveToFile(REG_EXP_FILE_LOCATION, regular_expression_et.getText().toString());
        }
    }

    //В методе описана загрузка алфавита.
    public void loadAlphabetBtnClick(EditText alphabet_et) {
        String input = readFromFile(INPUT_ALPHABET_FILE_LOCATION);
        if(input != null) {
            alphabet_et.setText(input);
        } else {
            showToast("Ошибка чтения файла!");
        }
    }

    //В методе описан алгоритм формирования цепочек (начало рекурсии) и снятие данных на ограничения цепочек.
    public void generateTsepochkiBtnClick(TextView generated_tsepochki_tv, EditText regular_expression_et,
                                          EditText range_from_et, EditText range_to_et) {
        saveToFile(REG_EXP_FILE_LOCATION, regular_expression_et.getText().toString());
        generated_tsepochki_tv.setText("");
        num1 = Integer.parseInt(range_from_et.getText().toString());
        num2 = Integer.parseInt(range_to_et.getText().toString());
        if (num1 > num2) {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }
        if(regular_expression_et.getText().toString().length() == 0) {
            fl_gen = false;
        }
        if (fl_gen == true) {
            int r = num2 , l = num1;
            String regvr = readFromFile(REG_EXP_FILE_LOCATION);
            RegMn a = new RegMn(r);
            int fl = 0;
            try {
                System.out.println("Производятся вычисления");/////
                a = getRegMn(regvr, r);
            }
            catch(Exception e) {
                fl = 1;
                e.printStackTrace();
            }
            if(fl==0) {
                a.toStringF(l);
            }
            String str = readFromFile(GENERATED_TSEPOCHKI_FILE_LOCATION);
            generated_tsepochki_tv.setText(str);
        }
        else {
             showToast("Сначала сгенерируйте регулярное выражение!");
        }
    }

    //В методе происходит сохранение всех данных: грамматики, регулярных выражений и построенных цепочек.
    public void saveToFileBtnClick(EditText alphabet_et, EditText init_podtsepochka_et,
                                   EditText end_podtsepochka_et, EditText kratnost_tsepochki_et,
                                   EditText regular_expression_et, TextView generated_tsepochki_tv) {
        String stringToOutput = "Алфавит:\n" +
                alphabet_et.getText().toString() + "\n" +
                "Начальная подцепочка:\n" +
                init_podtsepochka_et.getText().toString() + "\n" +
                "Конечная подцепочка:\n" +
                end_podtsepochka_et.getText().toString() + "\n" +
                "Кратность:\n" +
                kratnost_tsepochki_et.getText().toString() + "\n" +
                "Регулярное выражение:\n" +
                regular_expression_et.getText().toString() + "\n" +
                "Длинна от:\n" +
                String.valueOf(num1) + "\n" +
                "до:\n" +
                String.valueOf(num2) + "\n" +
                "Генерируемые цепочки:\n" +
                generated_tsepochki_tv.getText().toString() + "\n";
        saveToFile(TOTAL_OUTPUT_FILE_LOCATION, stringToOutput);
        showToast("Сохранено");
    }


    public void clearBtnClick(EditText alphabetEt, EditText initPodtsepochkaEt, EditText endPodstepochkaEt,
                              EditText kratnostTsepochkiEt, EditText regularExpressionEt,
                              TextView generatedTsepochkaTv, EditText rangeFromEt, EditText rangeToEt){
        alphabetEt.setText("");
        initPodtsepochkaEt.setText("");
        endPodstepochkaEt.setText("");
        kratnostTsepochkiEt.setText("");
        regularExpressionEt.setText("");
        generatedTsepochkaTv.setText("");
        rangeFromEt.setText("");
        rangeToEt.setText("");
        fl_gen = false;
    }

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}


