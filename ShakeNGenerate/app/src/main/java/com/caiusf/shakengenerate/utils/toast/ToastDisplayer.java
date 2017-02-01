package com.caiusf.shakengenerate.utils.toast;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.widget.Toast;

/**
 * Created by caius.florea on 17-Jan-17.
 */

public class ToastDisplayer {

    private static Toast toast;

    public static void displayShortToast(Context context, String message) {


        Spannable centeredText = new SpannableString(message);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, message.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, centeredText, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void displayLongToast(Context context, String message) {


        Spannable centeredText = new SpannableString(message);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, message.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, centeredText, Toast.LENGTH_LONG);
        toast.show();
    }

    public static Toast getToast(){
        return toast;
    }
}
