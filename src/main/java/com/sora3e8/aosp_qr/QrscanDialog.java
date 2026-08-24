package com.sora3e8.aosp_qr;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class QrscanDialog extends DialogFragment implements View.OnClickListener, qrscanner.on_qrdata{

    public QrscanDialog(FragmentManager manager) { }

    private String data="";
    private boolean success=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        this.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.AdaptiveTheme_dialog_style);
        this.setCancelable(true);
        View view = inflater.inflate(R.layout.qrscan_dialog, container, false);
        Button btn = view.findViewById(R.id.button_negative);
        btn.setOnClickListener(this::onClick);
        qrscanner scanner = view.findViewById(R.id.qr_scanner);
        scanner.setOnQRdataListener(this::qrdata_received);
        return view;
    }

    public interface onDialogResult
    {
        void QRdialogResult(boolean success,String data);
    }
    public static onDialogResult dialogResultInterface;
    public void setOnDialogResult(onDialogResult listener){ dialogResultInterface = listener; }


    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.button_negative)
        {
            ((qrscanner) getView().findViewById(R.id.qr_scanner)).stopScanner();
            this.success = false;
            this.data = "";
            this.dismiss();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if(dialogResultInterface!=null) {
            dialogResultInterface.QRdialogResult(this.success, this.data);
        }
        dialog.dismiss();
    }

    @Override
    public void qrdata_received(String data)
    {
        this.success = true;
        this.data = data;
        this.dismiss();
    }
}