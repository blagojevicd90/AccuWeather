package com.example.blago.accuweather.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.blago.accuweather.Common.Common;
import com.example.blago.accuweather.R;
import com.example.blago.accuweather.db.DBProvider;

import java.util.ArrayList;

public class Dialog extends AppCompatDialogFragment {

    private TextView txt_imperial, txt_metric;
    private RadioButton btn_imperial, btn_metric;
    private DialogListener listener;
    private RelativeLayout layout_metric, layout_imperial;
    private DBProvider dbProvider;
    private ArrayList <Common> mCommon;
    private Button btn_cancel;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view)
                .setTitle("Temperature unit");
        initComponents(view);
        addListener();
        return builder.create();
    }

    private void initComponents(View view) {
        mCommon = new ArrayList<>();
        dbProvider = DBProvider.getInstance(getContext());
        mCommon.addAll(dbProvider.getmDb().weatherDao().getCommons());
        txt_metric = (TextView) view.findViewById(R.id.metric);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        txt_imperial = (TextView) view.findViewById(R.id.imperial);
        btn_imperial = (RadioButton) view.findViewById(R.id.btn_imperial);
        btn_metric = (RadioButton) view.findViewById(R.id.btn_metric);
        layout_imperial = (RelativeLayout) view.findViewById(R.id.layout_imperial);
        layout_metric = (RelativeLayout) view.findViewById(R.id.layout_metric);
        if(mCommon.get(0).getDialogPosition()==1){
            btn_metric.setChecked(true);
        }else if(mCommon.get(0).getDialogPosition()==2){
            btn_imperial.setChecked(true);
        }
    }

    private void addListener() {
        layout_metric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common common = mCommon.get(0);
                dbProvider.getmDb().weatherDao().deleteCommon(mCommon.get(0));
                common.setDialogPosition(1);
                dbProvider.getmDb().weatherDao().insertCommon(common);
                listener.applyText(txt_metric.getText().toString());
                dismiss();
            }
        });

        layout_imperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common common = mCommon.get(0);
                dbProvider.getmDb().weatherDao().deleteCommon(mCommon.get(0));
                common.setDialogPosition(2);
                dbProvider.getmDb().weatherDao().insertCommon(common);
                listener.applyText(txt_imperial.getText().toString());
                dismiss();
            }
        });

        btn_metric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common common = mCommon.get(0);
                dbProvider.getmDb().weatherDao().deleteCommon(mCommon.get(0));
                common.setDialogPosition(1);
                dbProvider.getmDb().weatherDao().insertCommon(common);
                listener.applyText(txt_metric.getText().toString());
                dismiss();
            }
        });

        btn_imperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common common = mCommon.get(0);
                dbProvider.getmDb().weatherDao().deleteCommon(mCommon.get(0));
                common.setDialogPosition(2);
                dbProvider.getmDb().weatherDao().insertCommon(common);
                listener.applyText(txt_imperial.getText().toString());
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface DialogListener {
        void applyText(String tempUnit);
    }

}
