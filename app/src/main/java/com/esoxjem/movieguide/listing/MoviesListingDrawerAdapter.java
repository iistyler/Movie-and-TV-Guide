package com.esoxjem.movieguide.listing;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.listing.lists.GroupInteractor;
import com.esoxjem.movieguide.listing.lists.GroupInteractorImpl;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by tylermclean on 2017-02-15.
 */

/* Implement the ExpandableListAdapter interface */

public class MoviesListingDrawerAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> categoryStrings;
    private HashMap<String, List<String>> categoryItems;
    private ExpandableListView listView;

    public MoviesListingDrawerAdapter(Context context, List<String> categoryStrings, HashMap<String, List<String>> categoryItems, ExpandableListView listView) {
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

    private void showRadioButtonDialog() {

        // custom dialog
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.groups_radio_list, null);
        ButterKnife.bind(this, dialogView);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(dialogView);

        List<String> stringList=new ArrayList<>();  // here is list
        for(int i=0;i<5;i++) {
            stringList.add("RadioButton " + (i + 1));
        }

        RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(context); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        dialog.setTitle("Delete Group");

        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            } });

        dialog.show();

    }

    /* Creates the group/top level items */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);
        final int index = i;

        if (getChildrenCount(i) == 0)
            listView.setGroupIndicator(null);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (i > 0)
                view = infalInflater.inflate(R.layout.list_group, null);
            else
                view = infalInflater.inflate(R.layout.list_group_special, null);
        }

        if (i > 0) {
            TextView lblListHeader = (TextView) view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            Button addButton = (Button) view.findViewById(R.id.add_btn);
            Button removeButton = (Button) view.findViewById(R.id.delete_btn);

            addButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            final EditText txtUrl = new EditText(context);
                            txtUrl.setSingleLine(true);

                            new AlertDialog.Builder(context)
                                    .setTitle("Add List")
                                    .setView(txtUrl)
                                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            ListInteractorImpl listInterator = ListInteractorImpl.getInstance();
                                            GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
                                            Map<Integer, String> groups = groupInteractor.getAllGroups();
                                            Boolean exists = false;

                                            String groupName = categoryStrings.get(index);
                                            int listId = -1;
                                            for (int group : groups.keySet()) {
                                                if (groups.get(group).equals(groupName))
                                                    listId = group;
                                            }

                                            Map<Integer, String> listList = listInterator.getLists(listId);

                                            for (Integer e : listList.keySet()) {
                                                if (listList.get(e).toLowerCase().equals(txtUrl.getText().toString().toLowerCase()))
                                                    exists = true;
                                            }

                                            if (!exists) {
                                                if (txtUrl.getText().toString().isEmpty()) {
                                                    Toast.makeText(context, "List name is empty", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    listInterator.createList(txtUrl.getText().toString(), listId);
                                                    Map<Integer, String> map = listInterator.getLists(listId);
                                                    for (Integer e : map.keySet()) {
                                                        System.out.println(map.get(e));
                                                    }
                                                    List<String> list = categoryItems.get(categoryStrings.get(index));
                                                    if (list == null)
                                                        list = new ArrayList<>();
                                                    list.add(txtUrl.getText().toString());
                                                    categoryItems.put(categoryStrings.get(index), list);
                                                    notifyDataSetChanged();
                                                }
                                            } else {
                                                Toast.makeText(context, "List already exists", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                            // Your action here on button click
                        case MotionEvent.ACTION_CANCEL: {
                            break;
                        }
                    }
                    return true;
                }
            });

            removeButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            // custom dialog
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.groups_radio_list, null);
                            ButterKnife.bind(this, dialogView);
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setView(dialogView);

                            GroupInteractorImpl groupImpl = GroupInteractorImpl.getInstance();
                            Map <Integer, String> groupList = groupImpl.getAllGroups();
                            ArrayList<Integer> groupInts = new ArrayList<Integer>(groupList.keySet());
                            Collections.sort(groupInts);
                            Map<Integer, String> stringList= ListInteractorImpl.getInstance().getLists(groupInts.get(index - 1));  // here is list

                            final RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.radio_group);
                            final ArrayList<Integer> intList = new ArrayList<Integer>(stringList.keySet());
                            Collections.sort(intList);

                            for(int i = 0; i < intList.size(); i++){
                                RadioButton rb=new RadioButton(context); // dynamically creating RadioButton and adding to RadioGroup.
                                rb.setId(i);
                                rb.setText(stringList.get(intList.get(i)));
                                //intList.add(i);
                                rg.addView(rb);
                            }

                            dialog.setTitle("Delete List");

                            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if (rg.getCheckedRadioButtonId() == -1) {
                                        rg.removeAllViews();
                                        dialog.dismiss();
                                    }
                                    else {
                                        ListInteractorImpl listInteractor = ListInteractorImpl.getInstance();
                                        listInteractor.removeList(intList.get(rg.getCheckedRadioButtonId()));

                                        List<String> newList = categoryItems.get(categoryStrings.get(index));
                                        newList.remove(rg.getCheckedRadioButtonId());
                                        categoryItems.put(categoryStrings.get(index), newList);
                                        rg.removeAllViews();
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                } });

                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                } });

                            dialog.show();
                        case MotionEvent.ACTION_CANCEL: {
                            break;
                        }
                    }
                    return true;
                }
            });
        }
        else {
            Button addButton = (Button) view.findViewById(R.id.add_btn);
            Button removeButton = (Button) view.findViewById(R.id.delete_btn);

            addButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            final EditText txtUrl = new EditText(context);
                            txtUrl.setSingleLine(true);

                            new AlertDialog.Builder(context)
                                    .setTitle("Add Group")
                                    .setView(txtUrl)
                                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            GroupInteractorImpl groupInterator = GroupInteractorImpl.getInstance();
                                            Boolean exists = false;
                                            Map<Integer, String> list = groupInterator.getAllGroups();

                                            for (Integer e : list.keySet()) {
                                                if (list.get(e).toLowerCase().equals(txtUrl.getText().toString().toLowerCase()))
                                                    exists = true;
                                            }

                                            if (!exists) {
                                                if (txtUrl.getText().toString().isEmpty()) {
                                                    Toast.makeText(context, "Group name is empty", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    groupInterator.createGroup(txtUrl.getText().toString());
                                                    categoryStrings.add(txtUrl.getText().toString());
                                                    notifyDataSetChanged();
                                                }
                                            } else {
                                                Toast.makeText(context, "Group already exists", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        case MotionEvent.ACTION_CANCEL: {
                            break;
                        }
                    }
                    return true;
                }
            });

            removeButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                            // custom dialog
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.groups_radio_list, null);
                            ButterKnife.bind(this, dialogView);
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setView(dialogView);

                            Map<Integer, String> stringList = GroupInteractorImpl.getInstance().getAllGroups();  // here is list

                            final RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.radio_group);
                            final ArrayList<Integer> intList = new ArrayList<Integer>(stringList.keySet());
                            Collections.sort(intList);

                            for(int i = 0; i < intList.size(); i++){
                                RadioButton rb=new RadioButton(context); // dynamically creating RadioButton and adding to RadioGroup.
                                rb.setId(i);
                                rb.setText(stringList.get(intList.get(i)));
                                //intList.add(i);
                                rg.addView(rb);
                            }

                            dialog.setTitle("Delete Group");

                            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (rg.getCheckedRadioButtonId() == -1) {
                                        rg.removeAllViews();
                                        dialog.dismiss();
                                    }
                                    else {
                                        GroupInteractorImpl groupInteractor = GroupInteractorImpl.getInstance();
                                        groupInteractor.removeGroup(intList.get(rg.getCheckedRadioButtonId()));

                                        categoryItems.remove(categoryStrings.get(rg.getCheckedRadioButtonId() + 1));
                                        categoryStrings.remove(rg.getCheckedRadioButtonId() + 1);

                                        rg.removeAllViews();
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                } });

                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    rg.removeAllViews();
                                    dialog.dismiss();
                                } });

                            dialog.show();
                        case MotionEvent.ACTION_CANCEL: {
                            break;
                        }
                    }
                    return true;
                }
            });
        }

        return view;
    }

    /* Creates the second level of items */
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i, i1);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_item, null);
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
