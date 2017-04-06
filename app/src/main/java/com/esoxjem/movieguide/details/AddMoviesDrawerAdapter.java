package com.esoxjem.movieguide.details;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.esoxjem.movieguide.R;

import java.util.HashMap;
import java.util.List;

/* Implement the ExpandableListAdapter interface */

public class AddMoviesDrawerAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> categoryStrings;
    private HashMap<String, List<String>> categoryItems;
    private ExpandableListView listView;

    public AddMoviesDrawerAdapter(Context context, List<String> categoryStrings, HashMap<String, List<String>> categoryItems, ExpandableListView listView) {
        this.context = context;
        this.categoryStrings = categoryStrings;
        this.categoryItems = categoryItems;
        this.listView = listView;
    }

    /* Get number of groups in list */
    @Override
    public int getGroupCount() {
        try {
            return this.categoryStrings.size();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /* Get number of children in list */
    @Override
    public int getChildrenCount(int i) {
        try {
            return this.categoryItems.get(this.categoryStrings.get(i)).size();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return this.categoryStrings.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.categoryItems.get(this.categoryStrings.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /* Creates the group/top level items */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);

        if (getChildrenCount(i) == 0)
            listView.setGroupIndicator(null);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_group_add, null);
        }

        TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return view;
    }

    /* Creates the second level of items */
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i, i1);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item_add, null);
        }

        TextView txtListChild = (TextView) view.findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
