package com.example.recyclerviewpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.recyclerviewpractice.databinding.ActivityNumberSelectorBinding;

public class NumberSelector extends AppCompatActivity {

    ActivityNumberSelectorBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityNumberSelectorBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
    }


    public void showNumberDialog(View view) {

            new WeightPicker().show(NumberSelector.this, b.minQty.getText().toString().trim(), new WeightPicker.OnWeightPickedListener() {
                @Override
                public void onWeightPicked(int kg, int g) {
                    Toast.makeText(NumberSelector.this, kg + "kg" + " " + (g * 50) + "g", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onWeightPickerCancelled() {
                    Toast.makeText(NumberSelector.this, "Clicked!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
