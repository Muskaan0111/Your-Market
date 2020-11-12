package com.example.recyclerviewpractice;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.recyclerviewpractice.databinding.DialogEditorBinding;
import com.example.recyclerviewpractice.databinding.WeightpickerDialogBinding;
import com.google.android.material.textfield.TextInputEditText;

public class WeightPicker {
DialogEditorBinding db;


    public void show(Context context, String minQty ,final OnWeightPickedListener listener){
       WeightpickerDialogBinding b = WeightpickerDialogBinding.inflate(
                LayoutInflater.from(context)
        );

        new AlertDialog.Builder(context)
                .setTitle("Select Weight")
                .setView(b.getRoot())
                .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO 3 : Replace 0s & assign kg & g values from respective NumberPickers

                        int kg = b.numberPickerKg.getValue();
                        int g = b.numberPickerG.getValue();


                        //TODO 4 : Add GuardCode to prevent user from selecting 0kg 0g. If so, then return

                        if(kg==0&&g==0){
                            return ;
                        }

                        listener.onWeightPicked(kg, g);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onWeightPickerCancelled();
                    }
                })
                .show();

        setupNumberPickers(b.numberPickerKg,b.numberPickerG,minQty);

        //TODO 5 : Call new WeightPicker().show() in MainActivity and pass (this, new OnWeight...)

        //TODO 6 : Show toast of selected weight in onWeightPicked() method

        //TODO 7 : Find appropriate solution for : NumberPicker not formatting the first item

        //TODO 8 : Test your code :)

        //TODO 9 : Try to understand the flow as to how our Listener interface is working
    }

    private void setupNumberPickers(NumberPicker numberPicker_kg , NumberPicker numberPicker_g,String minQ) {
        //TODO 2 : Define this method to setup kg & g NumberPickers as per the given ranges
        //kg Range - 0kg to 10kg


           numberPicker_kg.setMinValue((int)((Float.parseFloat(minQ)*1000)/1000));
numberPicker_kg.setMaxValue(10);

        //g Range - 0g to 950g

        numberPicker_g.setMinValue((int)((Float.parseFloat(minQ)*1000))/50);
         numberPicker_g.setMaxValue(19);


        numberPicker_kg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + " " + "kg";
            }
        });

        numberPicker_g.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return (value * 50) + " " + "g";
            }
        });

        View firstItemKg = numberPicker_kg.getChildAt(0);
        if (firstItemKg != null) {
            firstItemKg.setVisibility(View.INVISIBLE);
        }

        View firstItemG = numberPicker_g.getChildAt(0);
        if (firstItemG != null) {
            firstItemG.setVisibility(View.INVISIBLE);
        }

    }





    interface OnWeightPickedListener{
        void onWeightPicked(int kg, int g);
        void onWeightPickerCancelled();
    }

}
