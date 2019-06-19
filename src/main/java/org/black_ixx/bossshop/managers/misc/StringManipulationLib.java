package org.black_ixx.bossshop.managers.misc;

import java.util.List;

public class StringManipulationLib {


    /**
     * Replace placeholders in a string
     * @param s input string
     * @param placeholder_name name of placeholder
     * @param replacement  replacement string
     * @param fromIndex index
     * @return replaced string
     */
    public static String replacePlaceholder(String s, String placeholder_name, String replacement, int fromIndex) {
        String complete = getCompleteVariable(s, placeholder_name, fromIndex);
        if (complete != null) {
            return s.replace(complete, replacement);
        }
        return s;
    }

    public static String figureOutVariable(String s, int fromIndex, String... placeholders_names) {
        for (String placeholder_name : placeholders_names) {
            String variable = figureOutVariable(s, placeholder_name, fromIndex);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }

    public static String figureOutVariable(String s, String placeholder_name, int fromIndex) {
        String symbol = "%";
        String start = symbol + placeholder_name + "_";
        String complete = getCompleteVariable(s, placeholder_name, fromIndex);
        if (complete != null) {
            String variable = complete.substring(start.length(), complete.length() - symbol.length());
            return variable;
        }
        return null;
    }

    public static String getCompleteVariable(String s, String placeholder_name, int fromIndex) {
        String symbol = "%";
        String start = symbol + placeholder_name + "_";
        if (s.contains(start)) {
            int first_occurence_start = s.indexOf(start, fromIndex);
            if (first_occurence_start != -1) {
                int first_occurence_end = s.indexOf(symbol, first_occurence_start + 1);
                if (first_occurence_end != -1) {
                    String complete = s.substring(first_occurence_start, first_occurence_end + 1);
                    return complete;
                }
            }
        }
        return null;
    }

    public static int getIndexOfVariableEnd(String s, String placeholder_name, int fromIndex) {
        String symbol = "%";
        String start = symbol + placeholder_name + "_";
        if (s.contains(start)) {
            int first_occurence_start = s.indexOf(start, fromIndex);
            int first_occurence_end = s.indexOf(symbol, first_occurence_start + 1);
            return first_occurence_end;
        }
        return -1;
    }

    public static String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        String output = null;
        for (String s : list) {
            if (output == null) {
                output = s;
            } else {
                output += "\n" + s;
            }
        }
        return output;
    }


    public static String getBlock(String s, String beginning, String end, int fromIndex) {
        if (s.contains(beginning) && s.contains(end)) {
            int first_occurence_start = s.indexOf(beginning, fromIndex);
            if (first_occurence_start != -1) {
                int first_occurence_end = s.indexOf(end, first_occurence_start + 1);
                if (first_occurence_end != -1) {
                    String complete = s.substring(first_occurence_start, first_occurence_end + 1);
                    return complete;
                }
            }
        }
        return null;
    }

    public static int getIndexOfBlockEnd(String s, String beginning, String end, int fromIndex) {
        if (s.contains(beginning) && s.contains(end)) {
            int first_occurence_start = s.indexOf(beginning, fromIndex);
            if (first_occurence_start != -1) {
                int first_occurence_end = s.indexOf(end, first_occurence_start + 1);
                return first_occurence_end;
            }
        }
        return -1;
    }


}
