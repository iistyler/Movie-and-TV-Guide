package com.esoxjem.movieguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import java.nio.charset.Charset;


public class NFC extends Activity implements CreateNdefMessageCallback {
    NfcAdapter MyNfcAdapter;
    byte[] mimeBytes = "application/com.esoxjem.movieguide".getBytes(Charset.forName("US-ASCII"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout for the application.
        setContentView(R.layout.nfc);

        // This disables NFC sending (peer to peer). Forces device to act as a 'tag'.
        // (can be used on other activities to force this activity to be used.
        // MyNfcAdapter.getDefaultAdapter(this).enableReaderMode(this, null, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null);
        // This re-enableds NFC sending (peer to peer)
        // MyNfcAdapter.getDefaultAdapter(this).disableReaderMode(this);

        MyNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (MyNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else if (MyNfcAdapter.isEnabled() == false) {
            Toast.makeText(this, "NFC is not enabled", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else if (MyNfcAdapter.isNdefPushEnabled() == false) {
            Toast.makeText(this, "Android Beam is not enabled", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        MyNfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{
                        new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, null, android.os.Build.MODEL.getBytes())
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
        // Only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // Building the alert dialog and display it
        new AlertDialog.Builder(this)
                .setTitle("NFC Received")
                .setMessage(new String(msg.getRecords()[0].getPayload()))
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}