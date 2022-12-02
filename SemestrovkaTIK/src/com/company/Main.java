package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Encoder encoder = new Encoder();
        encoder.by("мама мыла раму");
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
        int index = 0;

        for (int i = 0; i < list.size(); i++) {
            String w = list.get(i);
            if (w.equals(word)) {
                index = i + 1;
            }
            lastColumn.append(w.charAt(word.length() - 1));
        }

        Encoder encoder = new Encoder();
        System.out.println(encoder.encode(lastColumn.toString()));
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
