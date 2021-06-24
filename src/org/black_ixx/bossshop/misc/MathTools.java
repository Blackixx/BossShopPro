package org.black_ixx.bossshop.misc;

import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MathTools {

    public final static String BEGIN = "{";
    public final static String END = "}";
    private static DecimalFormat df;

    public static void init(String loc, int grouping_size) {
        try {
            String[] parts = loc.split("_");
            Locale l = Locale.forLanguageTag(loc);
            if (parts.length >= 2) {
                l = new Locale(parts[0].trim(), parts[1].trim());
            } else {
                l = new Locale(parts[0].trim());
            }
            if (l != null && l.getCountry() != null) {
                df = (DecimalFormat) NumberFormat.getInstance(l);
            }
        } catch (NullPointerException e) {
            df = (DecimalFormat) NumberFormat.getInstance();
        }

        if (grouping_size > 0) {
            df.setGroupingUsed(true);
            df.setGroupingSize(grouping_size);
        } else {
            df.setGroupingUsed(false);
        }
    }


    public static String transform(String s) {
        if (s.contains(BEGIN) && s.contains(END)) {
            int fromIndex = 0;
            String block = StringManipulationLib.getBlock(s, BEGIN, END, fromIndex);
            int endIndex = StringManipulationLib.getIndexOfBlockEnd(s, BEGIN, END, fromIndex);

            while (block != null && endIndex != -1) {

                double result = calculate(block.replace(BEGIN, "").replace(END, ""), Double.MIN_VALUE);
                if (result != Double.MIN_VALUE) {
                    s = s.replace(block, displayNumber(result, 2));
                }

                fromIndex = endIndex;
                block = StringManipulationLib.getBlock(s, BEGIN, END, fromIndex);
                endIndex = StringManipulationLib.getIndexOfBlockEnd(s, BEGIN, END, fromIndex);

            }
        }
        return s;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double cutNumber(double d, int to_cut, int decimal_place) {
        if (to_cut == 0) {
            return round(d, decimal_place);
        } else {
            long a = (long) Math.pow(10, to_cut);
            d = d / a;
            return round(d, decimal_place);
        }
    }


    public static String displayNumber(double d, BSPriceType pricetype) {
        List<String> formatting = null;
        boolean integer_value = isIntegerValue(pricetype);

        if (pricetype == BSPriceType.Money) {
            formatting = ClassManager.manager.getSettings().getMoneyFormatting();
        } else if (pricetype == BSPriceType.Points) {
            formatting = ClassManager.manager.getSettings().getPointsFormatting();
        }

        return displayNumber(d, formatting, integer_value);
    }

    public static List<String> getFormatting(BSPriceType pricetype) {
        if (pricetype == BSPriceType.Money) {
            return ClassManager.manager.getSettings().getMoneyFormatting();
        } else if (pricetype == BSPriceType.Points) {
            return ClassManager.manager.getSettings().getPointsFormatting();
        }
        return null;
    }

    public static boolean isIntegerValue(BSPriceType pricetype) {
        if (pricetype == BSPriceType.Points) {
            if (!ClassManager.manager.getPointsManager().usesDoubleValues()) {
                return true;
            }
        }
        if (pricetype == BSPriceType.Exp) {
            return true;
        }
        return false;
    }

    public static String displayNumber(double d, List<String> formatting, boolean integer_value) {
        if (d == 0) {
            return "0";
        }

        if (formatting != null) {
            for (String line : formatting) {
                String[] parts = line.split(":");
                if (parts.length == 4) {
                    double number_needed = InputReader.getDouble(parts[0].trim(), -1);
                    double positive = Math.abs(d);

                    if (positive >= number_needed) {
                        int to_cut = InputReader.getInt(parts[1].trim(), -1);
                        int decimal_place = InputReader.getInt(parts[2].trim(), -1);
                        double number = cutNumber(d, to_cut, decimal_place);

                        String output = parts[3].trim().replace("%number%", MathTools.displayNumber(number, decimal_place));
                        return output;
                    }
                }
            }
        }

        if (integer_value) {
            return displayNumber(d, 0);
        }

        return displayNumber(d, 2);
    }


    public static String displayNumber(double d, int decimal_place) {
        synchronized (df) {
            df.setMaximumFractionDigits(decimal_place);
            df.setMinimumFractionDigits(decimal_place);
            return df.format(d);
        }
    }


    public static double calculate(String string, double exception) {
        try {
            return calculate(string);
        } catch (NumberFormatException e) {
            return exception;
        }
    }

    public static double calculate(String string) {
        if (string == null || string.isEmpty())
            return 0;
        String copy = string.replaceAll("^0-9+\\-*/\\.\\^\\%]", "");
        if (copy.isEmpty())
            return 0;
        for (char character : new char[]{'^', '*', '/', '+', '-', '%'})
            copy = calculate(copy, character);
        return Double.parseDouble(copy);
    }

    private static String calculate(String copy, char type) {
        String[] vals = copy.split("\\" + type);
        double outcome = 0.0;
        for (int i = 0; i < vals.length - 1; i++) {
            String val1 = vals[i];
            String val2 = vals[i + 1];
            String[] first = val1.split("[+\\-/*\\^\\%]");
            String[] second = val2.split("[+\\-/*\\^\\%]");
            String secondRaw = val2.replace(second[0], "");
            double firstVal;

            if (val1.trim().isEmpty()) { //Example when this happens: "-5": character before first number.
                firstVal = 0;
            } else {
                firstVal = Double.parseDouble(first[first.length - 1]);
            }

            double secondVal = Double.parseDouble(second[0]);
            outcome = firstVal - secondVal;

            switch (type) {
                case '^':
                    outcome = Math.pow(firstVal, secondVal);
                    break;
                case '*':
                    outcome = firstVal * secondVal;
                    break;
                case '/':
                    outcome = firstVal / secondVal;
                    break;
                case '+':
                    outcome = firstVal + secondVal;
                    break;
                case '-':
                    outcome = firstVal - secondVal;
                    break;
                case '%':
                    outcome = firstVal % secondVal;
                    break;
            }

            copy = copy.replaceFirst(Pattern.quote(first[first.length - 1] + type + second[0]),
                    String.valueOf(outcome));
            vals[i + 1] = outcome + secondRaw;
        }
        return copy;
    }
}
