package com.esoxjem.movieguide;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esoxjem.movieguide.listing.lists.DBClass;
import com.esoxjem.movieguide.listing.lists.GroupInteractor;
import com.esoxjem.movieguide.listing.lists.GroupInteractorImpl;
import com.esoxjem.movieguide.listing.lists.ListInteractorImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;


public class NFC extends Activity implements CreateNdefMessageCallback {
    final private byte[] mimeBytes = "application/com.esoxjem.movieguide".getBytes(Charset.forName("US-ASCII"));
    private Boolean allChecked;
    private ListView checkList;
    private ArrayList<Integer> toExport = new ArrayList<Integer>();
    private String currentExport;

    private boolean updateExport(Integer[] values) {
        boolean allSelected = true;
        SparseBooleanArray checked = this.checkList.getCheckedItemPositions();

        // Clear out the export list.
        while(toExport.size() > 0) {
            toExport.remove(0);
        }

        // Add the selected to the export list.
        for (int i=0; i<checkList.getAdapter().getCount(); i++) {
            if (checked.valueAt(i)) {
                toExport.add(values[i]);
            } else {
                allSelected = false;
            }
        }

        // Ensure no false outputs are sent out.
        if (toExport.size() == 0) {
            allSelected = false;
        }

        // Modify the export list if all are to be exported.
        if (allSelected) {
            while(toExport.size() > 0) {
                toExport.remove(0);
            }
            toExport.add(-1);
        }

        // Re-export
        try {
            if (toExport.size() > 0) {
                NFC.this.setCurrentExport(GroupInteractorImpl.getInstance().exportGroups(toExport));
            } else {
                NFC.this.setCurrentExport("");
            }
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Error")
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

        // Ensure the select all input is updated.
        if (getAllChecked() != allSelected) {
            toggleAllChecked();
        }

        return allSelected;
    }

    private void setCurrentExport(String exportString) {
        this.currentExport = exportString;
    }

    private String getCurrentExport() {
        return this.currentExport;
    }

    // Returns the select all variable.
    private Boolean getAllChecked(){
        return this.allChecked;
    }

    // Maintain boolean variable indicating if select all is 'active' or not.
    private void toggleAllChecked() {
        this.allChecked = !this.allChecked;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout for the application.
        setContentView(R.layout.nfc);

        // This disables NFC sending (peer to peer). Forces device to act as a 'tag'.
        // (can be used on other activities to force this activity to be used.
        // MyNfcAdapter.getDefaultAdapter(this).enableReaderMode(this, null, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null);
        // This re-enables NFC sending (peer to peer)
        // MyNfcAdapter.getDefaultAdapter(this).disableReaderMode(this);

        NfcAdapter myNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (myNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else if (!myNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is not enabled", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else if (!myNfcAdapter.isNdefPushEnabled()) {
            Toast.makeText(this, "Android Beam is not enabled", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Register callback
        myNfcAdapter.setNdefPushMessageCallback(this, this);

        // Get all groups in the database.
        List<String> theList = new ArrayList<String>();
        Map<Integer, String> groupsList = GroupInteractorImpl.getInstance().getAllGroups();
        final Integer[] keyList = new Integer[groupsList.size()];
        final String[] items = new String[groupsList.size()];
        boolean[] checkedItems = null;

        this.allChecked = false;

        this.checkList = (ListView)this.findViewById(R.id.allGroups);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.nfc_item, theList);
        checkList.setAdapter(arrayAdapter);

        // Organize the groups returned.
        int i = 0;
        for (Map.Entry<Integer, String> entry : groupsList.entrySet()) {
            keyList[i] = entry.getKey();
            items[i] = entry.getValue();
            i++;
        }

        // Add to the Listview.
        for (i=0; i< keyList.length; i++) {
            theList.add(items[i]);
        }

        // Select all groups in the list.
        final Button selectAll = (Button) this.findViewById(R.id.selectAll);
        selectAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleAllChecked();
                if (NFC.this.getAllChecked()) {
                    selectAll.setText(R.string.nfc_unselect_all);
                } else {
                    selectAll.setText(R.string.nfc_select_all);
                }

                for (int i=0; i<keyList.length; i++) {
                    if (NFC.this.getAllChecked()) {
                        checkList.setItemChecked(i, true);
                    } else {
                        checkList.setItemChecked(i, false);
                    }
                }

                // Update the export list as per the selections.
                updateExport(keyList);
            }
        });

        // Test buttons - add static content including mixed movie/tv list, and 2 groups with content.
        Button addB = (Button) this.findViewById(R.id.addB_testButton);
        addB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBClass.setupInitialNFC();

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Added to Database")
                        .setMessage(GroupInteractorImpl.getInstance().getAllGroups().toString())
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        // Add favourites group with movies and tv show lists.
        Button add = (Button) this.findViewById(R.id.add_testButton);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBClass.setupInitialDB();
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Added to Database")
                        .setMessage(GroupInteractorImpl.getInstance().getAllGroups().toString())
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        // Clear database entirely.
        Button clearAll = (Button) this.findViewById(R.id.clearAll);
        clearAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBClass.resetDB();
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Database Reset")
                        .setMessage("Database was reset.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        checkList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Update export list AND update button as need be (select all)
                if (updateExport(keyList)) {
                    selectAll.setText(R.string.nfc_unselect_all);
                } else {
                    selectAll.setText(R.string.nfc_select_all);
                }
            }
        });
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{
                        new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, null, this.getCurrentExport().getBytes())
                }
        );
        return msg;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        String message = "Lists were received!";

        // Only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        if (new String(msg.getRecords()[0].getPayload()).length() != 0) {
            // Building message of list added.
            try {
                JSONArray jsonGroups = new JSONArray(new String(msg.getRecords()[0].getPayload()));

                message = "";
                // Go through groups and edit message variable.
                for (int i = 0; i < jsonGroups.length(); i++) {
                    message += jsonGroups.getJSONObject(i).getString("name");
                    if (i == jsonGroups.length() - 1) {
                        message += ".";
                    } else if(i == jsonGroups.length() - 2) {
                        message += ", & ";
                    } else {
                        message += ", ";
                    }
                }
            } catch (Exception e) {
                message = "An error has occurred.";
            }

            new AlertDialog.Builder(this)
                    .setTitle("List(s) Received")
                    .setMessage(message)
                    .setCancelable(false)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();

            try {
                GroupInteractorImpl.getInstance().importGroups(new String(msg.getRecords()[0].getPayload()));
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}