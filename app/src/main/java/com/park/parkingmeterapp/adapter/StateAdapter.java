package com.park.parkingmeterapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.park.parkingmeterapp.model.State;

import java.util.List;

/**
 * Created by Jitesh Dalsaniya on 24-Nov-16.
 */

public class StateAdapter extends ArrayAdapter<State> {

    private List<State> items;
    public StateAdapter(Context context, int resource, List<State> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public State getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(18);
        view.setText(items.get(position).getName());
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(items.get(position).getName());
        return view;
    }
}
