import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class hw1_PervinMine_Goksen {
    public static int tek_yorum_isaret = 1;
    public static Scanner inputStream = null;
    public static PrintWriter outputStream = null;

    public static void main(String[] args) {
        //args[0] = sample1.ETU
        //args[1] = output.txt
        String input_file = args[0];
        String output_file = args[1];

        try {
            inputStream = new Scanner(new File(input_file));
            outputStream = new PrintWriter(new File(output_file));

            while(inputStream.hasNextLine()){
                String line = inputStream.nextLine(); // butun satiri aldi.
                String new_line = line.trim();
                String islem_line = new_line.replaceAll("( )+", " "); //whitespaces are not a problem now.
                checker(islem_line);
            }
            outputStream.write("Next token is EOF       Next lexeme is end of file");
            inputStream.close();
            outputStream.close();

        }
        catch (FileNotFoundException e){
            System.out.println("File could not open.");
            System.exit(0);
        }
    }

    public static void checker(String s){
        String[] str = s.split(" ");
        int str_uzunluk = str.length;
        for (int i =0; i<str_uzunluk; i++){
            for(int j =0; j<str[i].length(); j++){
                char ch = str[i].charAt(j);
                if(tek_yorum_isaret % 2 == 0){
                    while (str[i].indexOf('%') == -1) {
                        i++;
                        if (i >= str_uzunluk) {
                            break;
                        }
                    }
                    j = 0;
                    ch = str[i].charAt(j);
                    while (ch != '%'){
                        j++;
                        if(j >= str[i].length()){
                            break;
                        }
                        ch = str[i].charAt(j);
                    }
                    tek_yorum_isaret++;
                }

                else if(ch == ')'){
                    outputStream.write("Next token is RPARANT      Next lexeme is "+ ch + "\n");
                }
                else if(ch == '(') {
                    outputStream.write("Next token is LPARANT      Next lexeme is "+ ch + "\n");
                }
                else if(ch == '+') {
                    outputStream.write("Next token is ADD      Next lexeme is "+ ch + "\n");
                }
                else if(ch == '-') {
                    outputStream.write("Next token is SUBT      Next lexeme is "+ ch + "\n");
                }
                else if(ch == '/') {
                    outputStream.write("Next token is DIV      Next lexeme is "+ ch + "\n");
                }
                else if(ch == '*') {
                    outputStream.write("Next token is MULT      Next lexeme is "+ ch + "\n");
                }
                else if(ch == ';') {
                    outputStream.write("Next token is SEMICOLON      Next lexeme is "+ ch + "\n");
                }
                else if(ch == ',') {
                    outputStream.write("Next token is COMA      Next lexeme is "+ ch + "\n");
                }
                else if(ch == ':') {
                    if (j == str[i].length()-1) {
                        outputStream.write("Next token is COLON      Next lexeme is "+ ch + "\n");
                    } else if (str[i].charAt(j + 1) == '=') {
                        ++j;
                        outputStream.write("Next token is ASSIGNM      Next lexeme is :="+ "\n");
                    }
                    else{
                        outputStream.write("Next token is COLON      Next lexeme is "+ ch + "\n");
                    }
                }
                else if(ch == '<') {
                    if (j == str[i].length()-1) {
                        outputStream.write("Next token is LESS      Next lexeme is "+ ch + "\n");
                    } else if (str[i].charAt(j + 1) == '=') {
                        outputStream.write("Next token is LESS_EQ      Next lexeme is <="+"\n");
                        j++;
                    } else if (str[i].charAt(j + 1) == '>') {
                        outputStream.write("Next token is NOTEQ      Next lexeme is <>"+"\n");
                        j++;
                    }
                    else{
                        outputStream.write("Next token is LESS      Next lexeme is "+ ch + "\n");
                    }
                }
                else if(ch == '>') {
                    if (j == str[i].length()-1) {
                        outputStream.write("Next token is GREATER      Next lexeme is "+ ch + "\n");
                    } else if (str[i].charAt(j+1) == '=') {
                        outputStream.write("Next token is GRE_EQ      Next lexeme is >="+ "\n");
                        j++;
                    }
                    else{
                        outputStream.write("Next token is GREATER      Next lexeme is "+ ch + "\n");
                    }
                }
                else if(Character.isLetter(ch)){
                    int baslangic = j; // kelimenin baslangic indexi
                    int k = 0;
                    String symbol = "+*-%/:=<>();,";
                    while (Character.isLetterOrDigit(ch) || symbol.indexOf(ch) == -1) {
                        j++;
                        if (j >= str[i].length()) {
                            break;
                        }
                        ch = str[i].charAt(j);
                        if(symbol.indexOf(ch) == -1 && !Character.isLetterOrDigit(ch)){
                            k++;
                        }
                    }

                    if(k != 0) {
                        outputStream.write("Next token is UNKNOWN      Next lexeme is "+ str[i].substring(baslangic, j) + "\n");
                    }
                    else if(check_RESW(str[i].substring(baslangic, j))){
                        outputStream.write("Next token is RES_WORD      Next lexeme is "+ str[i].substring(baslangic, j) + "\n");
                    }
                    else if (check_IDEN(str[i].substring(baslangic, j))){
                        outputStream.write("Next token is IDENTIFIER      Next lexeme is "+ str[i].substring(baslangic, j) + "\n");
                    }
                    else{
                        outputStream.write("Next token is UNKNOWN      Next lexeme is "+ str[i].substring(baslangic, j) + "\n");
                    }
                    j--;
                }
                else if(Character.isDigit(ch)){
                    int basla = j;
                    int flag = 0;
                    while(Character.isDigit(ch)){
                        j++;
                        if(j >= str[i].length()){
                            break;
                        }
                        ch = str[i].charAt(j);
                        if(Character.isLetter(ch)){
                            while(Character.isLetter(ch)) {
                                j++;
                                if(j >= str[i].length()){
                                    break;
                                }
                                ch = str[i].charAt(j);
                                flag++;
                            }
                        }
                    }
                    if(flag == 0) {
                        outputStream.write("Next token is INT_LIT      Next lexeme is "+ str[i].substring(basla, j) + "\n");
                    }
                    else {
                        outputStream.write("Next token is UNKNOWN      Next lexeme is "+ str[i].substring(basla, j) + "\n");
                    }
                    j--;
                }
                //////////////////////////////////////////////////////////
                else if (ch == '%'){
                    int ilk_flag = s.indexOf('%');
                    int son_flag = s.lastIndexOf('%');
                    if((ilk_flag != son_flag) && ilk_flag != -1 && son_flag != -1 ) {// % %
                        i++;
                        if (i >= str_uzunluk) {
                            break;
                        }
                        while (str[i].indexOf('%') == -1) {
                            i++;
                            if (i >= str_uzunluk) {
                                break;
                            }
                        }
                        j = 0;
                        ch = str[i].charAt(j);
                        while (ch != '%'){
                            j++;
                            if(j >= str[i].length()){
                                break;
                            }
                            ch = str[i].charAt(j);
                        }
                    }

                    else if(ilk_flag == son_flag && ilk_flag != -1 && son_flag != -1){ // %
                        if(tek_yorum_isaret % 2 == 1){
                            tek_yorum_isaret++;
                            return ;
                        }
                    }
                }

                //////////////////////////////////////////////////////////
                else {
                    int basla = j;
                    j++;
                    if(j >= str[i].length()){
                        break;
                    }
                    ch = str[i].charAt(j);
                    while(Character.isLetterOrDigit(ch)) {
                        j++;
                        if(j >= str[i].length()){
                            break;
                        }
                        ch = str[i].charAt(j);
                    }
                    outputStream.write("Next token is UNKNOWN.      Next lexeme is "+ str[i].substring(basla, j) + "\n");
                    j--;
                }
            }
        }
    }

    public static boolean check_RESW(String str){
        if(str.equalsIgnoreCase("begin"))
            return true;
        if(str.equalsIgnoreCase("end"))
            return true;
        if(str.equalsIgnoreCase("if"))
            return true;
        if(str.equalsIgnoreCase("then"))
            return true;
        if(str.equalsIgnoreCase("else"))
            return true;
        if(str.equalsIgnoreCase("while"))
            return true;
        if(str.equalsIgnoreCase("program"))
            return true;
        if(str.equalsIgnoreCase("integer"))
            return true;
        if(str.equalsIgnoreCase("var"))
            return true;
        return false;
    }

    public static boolean check_IDEN(String str){
        if(str.length() <= 15) {
            return true;
        }
        return false;

    }
    public static boolean check_unknown(String str){
        String letters = "ABCDEFGHIJKLMNOPRSTUVYZXQWabcdefghijklmnoprstuvyzxqw";
        String rakam = "0123456789";
        String symbol = "+*%/:=<>();,:";
        if(letters.contains(str) || rakam.contains(rakam) || rakam.contains(symbol)){
            return false;
        }
        return true;
    }
}
