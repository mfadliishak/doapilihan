package com.mfadli.utils;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
