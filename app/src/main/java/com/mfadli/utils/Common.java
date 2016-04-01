package com.mfadli.utils;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.mfadli.doapilihan.DoaPilihanApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by mfad on 22/03/2016.
 */
public class Common {

    private static String LOG_TAG = Common.class.getSimpleName();

    /**
     * Given array resource (e.g. {@link android.R.array#phoneTypes}),
     * will return ArrayList<String>.
     *
     * @param context android.content.Context
     * @param id      int array resource reference (e.g. {@link android.R.array#phoneTypes})
     * @return ArrayList<String>
     */
    public static ArrayList<String> getArrayListFromResource(@NonNull Context context, @ArrayRes int id) {
        List<String> stringList = Arrays
                .asList(context.getResources().getStringArray(id));

        return new ArrayList<>(stringList);
    }

    /**
     * Removing extra back slash symbol in breakline. That Extra symbol was added
     * when reading from SQLite.
     *
     * @param toTrim String which to be trimmed
     * @return String trimmed
     */
    public static String trimBreakLine(String toTrim) {
        return toTrim.replace("\\n", "\n");
    }

    /**
     * Change the font type of the strings.
     *
     * @param string String that need to be changed.
     * @param font   String Font name. e.g. "fonts/BArabics.ttf"
     * @return SpannableStringBuilder
     */
    public static SpannableStringBuilder convertStringToFont(String string, String font) {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(string);

        if (font.length() > 0) {
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(
                    TypefaceUtils.load(DoaPilihanApp.getContext().getAssets(), font));

            sBuilder.setSpan(typefaceSpan, 0, sBuilder.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        return sBuilder;
    }
}
