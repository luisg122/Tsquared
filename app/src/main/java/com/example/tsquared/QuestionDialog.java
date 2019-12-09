package com.example.tsquared;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

public class QuestionDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder   = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(this.getActivity());
        View view = layoutInflater.inflate(R.layout.floating_dialog_post, null);


        builder.setView(view);
        builder.setCancelable(true);


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1000);

        return super.onCreateDialog(savedInstanceState);
    }
}
