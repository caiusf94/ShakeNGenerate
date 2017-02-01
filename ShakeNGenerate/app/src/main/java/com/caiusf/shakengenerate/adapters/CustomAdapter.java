package com.caiusf.shakengenerate.adapters;

/**
 * Created by caius.florea on 10-Jan-17.
 */

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.caiusf.shakengenerate.R;
import com.caiusf.shakengenerate.activities.ListOfNumbersActivity;

import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<String> index;
    private Context context;
    private ArrayList<String> numbers;
    private int lower;
    private int upper;
    private boolean isChecked;
    private static LayoutInflater inflater = null;

    public CustomAdapter(ListOfNumbersActivity listOfNumbersActivity, ArrayList<String> indexArrayList, ArrayList<String> numbersArrayList, int lower, int upper, boolean isChecked) {

        this.index = new ArrayList<>(indexArrayList);
        this.context = listOfNumbersActivity;
        this.numbers = new ArrayList<>(numbersArrayList);
        this.lower = lower;
        this.upper = upper;
        this.isChecked = isChecked;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return index.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public class Holder {
        TextView tv1;
        TextView tv2;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item, null);
        holder.tv1 = (TextView) rowView.findViewById(R.id.itemIndex);
        holder.tv2 = (TextView) rowView.findViewById(R.id.item);
        holder.tv1.setText(index.get(position));
        holder.tv2.setText(numbers.get(position));
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!isChecked) {

                    Random random = new Random();
                    numbers.set(position, Integer.toString(random.nextInt(upper - lower + 1) + lower));
                    holder.tv2.setText(numbers.get(position));
                } else {
                    Random random = new Random();
                    boolean replaced = false;

                    int maxPossible = Math.abs(upper - lower) + 1;

                    if (numbers.size() < maxPossible) {

                        do {
                            String newNumber = Integer.toString(random.nextInt(upper - lower + 1) + lower);
                            if (!numbers.contains(newNumber)) {
                                numbers.set(position, newNumber);
                                holder.tv2.setText(numbers.get(position));
                                replaced = true;
                            }
                        } while (!replaced);
                    }
                    else{
                        displayShortToast(v, parent.getContext().getResources().getString(R.string.fullList));
                    }
                }
                return true;
            }
        });
        return rowView;
    }

    Toast toast;

    public void displayShortToast(View view, String message) {


        Spannable centeredText = new SpannableString(message);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, message.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(view.getContext(), centeredText, Toast.LENGTH_SHORT);
        toast.show();
    }

}