package com.vpage.vcars.tools.utils;

import android.text.SpannableStringBuilder;
import android.widget.TextView;


public class StringUtils {

    private static final String TAG = "OustAndroid:" + StringUtils.class.getName();

    static public String capitalizeFirstChar(String input) {
        if (input == null || input.length() == 0)
            return input;

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    static public String camelCapsToString(String input) {
        if (input == null || input.length() == 0)
            return input;

        String output = "";
        String[] inputStringArray = input.split(" ");
        for (String anInputStringArray : inputStringArray) {
            output = output + capitalizeFirstChar(anInputStringArray) + " ";
        }

        return output;
    }

    public static void makeTextViewHyperlink(TextView helpLink) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(helpLink.getText());
        helpLink.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

    }

    public static String setTextLength(String data, int textLength)
    {
        String textData;
        if (data.length() >= textLength) {

            textData = data.substring(0, textLength)+ "..";

        }else {

            textData = data;

        }
        return textData;
    }
}
