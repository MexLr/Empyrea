package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class StringNormalizer {

    public String normalizeString(String string) {

        List<String> words = Arrays.stream(string.split(" ")).toList();
        String outputString = "";

        for (String word : words) {
            String temp = ChatColor.stripColor(word.toLowerCase());
            String replaceChar = "" + temp.charAt(0);
            outputString += temp.replaceFirst(replaceChar, replaceChar.toUpperCase()) + " ";
        }
        return outputString;
    }
}
