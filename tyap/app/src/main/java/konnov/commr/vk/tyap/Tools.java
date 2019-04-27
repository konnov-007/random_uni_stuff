package konnov.commr.vk.tyap;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Tools {
    public static final String INPUT_ALPHABET_FILE_LOCATION = "tyap/alphabet.txt";
    public static final String GENERATED_TSEPOCHKI_FILE_LOCATION = "tyap/tsepochki.txt";
    public static final String REG_EXP_FILE_LOCATION = "tyap/regexp.txt";
    public static final String TOTAL_OUTPUT_FILE_LOCATION = "tyap/output_total.txt";

    public static class StringLengthComparator implements java.util.Comparator<String> {
        public int compare(String s1, String s2) {
            int dist1 = Math.abs(s1.length());
            int dist2 = Math.abs(s2.length());
            return dist1 - dist2;
        }
    }

    public static void saveToFile(String filename, String text){
        try (PrintWriter out = new PrintWriter(Environment.getExternalStorageDirectory().getPath() + "/" + filename)){
            out.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath() + "/" + filename))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            for(int i = 0; i < sb.length(); i++){ //удаляем пробелы
                if(sb.charAt(i) == ' ' || sb.charAt(i) == '\n') {
                    sb.deleteCharAt(i);
                }
            }
            String input = sb.toString();
            return input;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
