package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Encoder encoder = new Encoder();
        //encoder.by("мама мыла раму");
        Decoder decoder = new Decoder();
        List<Double> list = Arrays.asList(0.1,0.15, 0.15, 0.15, 0.2, 0.25);
        decoder.decode("rbkpeo", list, "001110100");
    }
}

class Encoder {
    public static void by(String word) {
        List<String> list = new ArrayList<>(word.length());

        for (int i = 0; i < word.length(); i++) {
            String newWord = word.substring(i) + word.substring(0, i);
            list.add(newWord);
        }

        Collections.sort(list);
        StringBuilder lastColumn = new StringBuilder();
        int count = 0;

        for (int i = 0; i < list.size(); i++) {
            String w = list.get(i);
            if (w.equals(word)) {
                count = i + 1;
            }
            lastColumn.append(w.charAt(word.length() - 1));
        }

        Encoder encoder = new Encoder();
        System.out.println(encoder.encode(lastColumn.toString()) + " " + count);
    }

    public List<Integer> encode(String data) {
        List<Integer> result = new ArrayList<Integer>();

        int count = 0;

        HashMap<String, Integer> table = new HashMap<String, Integer>();

        //добавляем все символы
        for (int i = 0; i < data.length(); i++) {
            if(!table.containsKey(String.valueOf(data.charAt(i)))) {
                table.put(String.valueOf(data.charAt(i)), count);
                count ++;
            }
        }

        int appendCount = 0;
        //Добавляем всё остальное и формируем результат
        for (int i = 0; i < data.length(); i++) {
            StringBuilder sufix = new StringBuilder();
            StringBuilder word = new StringBuilder();
            sufix.append(data.charAt(i));
            appendCount = i;
            while (table.containsKey(sufix.toString())) {
                if (appendCount+1 == data.length()){
                    break;
                }
                appendCount++;
                sufix.append(data.charAt(appendCount));
                i = appendCount-1;
            }
            if (i+1 != data.length()){
                table.put(sufix.toString(), count);
            }
            word.append(sufix.deleteCharAt(sufix.length()-1));
            count++;
            if (i+1 != data.length()){
                result.add(table.get(word.toString()));
            } else result.add(table.get(String.valueOf(data.charAt(data.length()-1))));
        }
        return result;
    }
}

class Decoder{
    public void decode(String words, List<Double> probabilities, String code){
        List<Character> symbols = new ArrayList<>();
        for (int i = 0; i < words.length(); i++) {
            symbols.add(words.charAt(i));
        }

        boolean isSorted = false;
        double buf;
        char ret;
        while(!isSorted) {
            isSorted = true;
            for (int i = 0; i < probabilities.size() - 1; i++) {
                if (probabilities.get(i) < probabilities.get(i + 1)) {
                    isSorted = false;

                    buf = probabilities.get(i);
                    probabilities.set(i, probabilities.get(i + 1));
                    probabilities.set(i+1, buf);

                    ret = symbols.get(i);
                    symbols.set(i, symbols.get(i + 1));
                    symbols.set(i+1, ret);
                }
            }
        }

        //Q(i)
        double sum = 0.0;
        List<Double> qi = new ArrayList<>();
        for (int i = 0; i < probabilities.size(); i++) {
            qi.add(sum);
            sum+= probabilities.get(i);
        }

        //L(i)
        List<Integer> Li = new ArrayList<>();
        for (int i = 0; i < qi.size(); i++) {
            double count = 1;
            int q = 0;
            while (count>probabilities.get(i)){
                count = count/2;
                q++;
            }
            Li.add(q);
        }

        //Переводим в двоичный
        List<String> binary = new ArrayList<>();
        for (int i = 0; i < qi.size(); i++) {
            double count = 0.5;
            double plus = 0.25;
            boolean bool = false;
            StringBuilder bin = new StringBuilder();
            while (bin.length() < Li.get(i)){
                if(count <= qi.get(i)){
                    bin.append("1");
                    count += plus;
                    bool = true;
                } else{
                    bin.append("0");
                    if (bool) count += plus;
                    else count = plus;
                }
                plus = plus/2;
            }
            binary.add(bin.toString());
        }

        //Декодируем
        List<Character> result = new ArrayList<>();
        int left = 0;
        int right = 1;
        while (right != code.length() + 1){
            for (int i = 0; i < binary.size(); i++) {
                if(binary.get(i).equals(code.substring(left,right))){
                    result.add(symbols.get(i));
                    left = right;
                }
            }
            right++;
        }
        System.out.println(result);
    }
}