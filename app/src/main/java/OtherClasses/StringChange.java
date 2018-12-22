package OtherClasses;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.R;

public class StringChange {

    public static String capitalizeEachWord(String str) {

        String[] eachWord = str.split("\\s+");
        str = "";
        for (int i = 0; i < eachWord.length; i++) {
            eachWord[i] = eachWord[i].substring(0, 1).toUpperCase() + eachWord[i].substring(1, eachWord[i].length()).toLowerCase();
            str += eachWord[i] + " ";
        }

        return str.trim();
    }

    public static String formatDouble(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    public static void MakeColoredText(Context context, int color, TextView lblInput, String heading, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString headSpannable = new SpannableString(heading);
        headSpannable.setSpan(new StyleSpan(Typeface.NORMAL), 0, heading.length(), 0);
        headSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)), 0, heading.length(), 0);
        builder.append(headSpannable);

        SpannableString valueSpannable = new SpannableString(value);
        valueSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, color)), 0, value.length(), 0);
        builder.append(valueSpannable);

        lblInput.setText(builder, TextView.BufferType.SPANNABLE);
    }
}
