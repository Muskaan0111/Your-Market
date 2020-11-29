package com.example.recyclerviewpractice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.recyclerviewpractice.databinding.DialogEditorBinding;

import java.util.regex.Pattern;

public class EditorDialog {

    interface OnProductEditedListener{
        void onProductEdited(Product product);
        void onCancelled();
    }

    DialogEditorBinding b;
    private Product product;
   public int productType;
    public static final byte PRODUCT_ADD = 0, PRODUCT_EDIT = 1;

    public EditorDialog(int type) {
        productType = type;
    }


    void show(final Context context, final Product product, final OnProductEditedListener listener) {

        this.product = product;

        b = DialogEditorBinding.inflate(
                LayoutInflater.from(context)
        );


        new AlertDialog.Builder(context).setTitle("Your Space")
                .setView(b.getRoot())
                .setPositiveButton(productType == PRODUCT_ADD ? "ADD" : "EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(areProductDetailsValid(productType))
                            listener.onProductEdited(EditorDialog.this.product);
                        else
                            Toast.makeText(context, "Check Details Again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onCancelled();
                    }
                })
                .show();

        setupRadioGroup();

        if (productType == PRODUCT_EDIT) {
            preFillPreviousDetails();
        }




    }

    private void setupRadioGroup() {

        b.type.clearCheck();

        b.type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.weight_based){
                    b.weightbased1.setVisibility(View.VISIBLE);
                    b.weightbased2.setVisibility(View.VISIBLE);
                    b.variantsbased.setVisibility(View.GONE);
                } else {
                    b.variantsbased.setVisibility(View.VISIBLE);
                    b.weightbased1.setVisibility(View.GONE);
                    b.weightbased2.setVisibility(View.GONE);
                }
            }
        });

    }

    private void preFillPreviousDetails() {
        //Set name
        b.itemName.setText(product.name);

        //Change RadioGroup Selected
        b.type.check(product.type == Product.WEIGHT_BASED
                ? R.id.weight_based : R.id.variant_based);


        //Setup views according to type
        if(product.type == Product.WEIGHT_BASED){
            b.price.setText(product.pricePerKg + "");
            b.minQty.setText(product.minQtyToString());
        } else {
            b.variant.setText(product.variantsString());
        }
    }

    private boolean areProductDetailsValid(int type) {


        String name = b.itemName.getText().toString().trim();
        if(name.isEmpty()){
            return false;
        }

        product.name=name;




        switch(b.type.getCheckedRadioButtonId()){

            case R.id.weight_based:
                String pricePerKg = b.price.getText().toString().trim() , minQty = b.minQty.getText().toString().trim();


                if( minQty.isEmpty() || !minQty.matches("\\d+(kg|g)") || pricePerKg.isEmpty())
                    return false;

                product.startWeightBasedProduct(name,Integer.parseInt(pricePerKg), extractQty(minQty));
                if (type == PRODUCT_ADD) {
                    product = new Product(name, Integer.parseInt(pricePerKg), extractQty(minQty));
                } else {
                    product.startWeightBasedProduct(name, Integer.parseInt(pricePerKg), extractQty(minQty));
                }
                return true;




            case R.id.variant_based:
                String variants = b.variant.getText().toString().trim();
                if (type == PRODUCT_ADD) {
                    product = new Product(name);
                } else {
                    product.startVariantsBasedProduct(name);
                }



                return areVariantsValid(variants);


        }


        return false;



    }

    private float extractQty(String minQty) {
        if(minQty.contains("kg"))
            return Integer.parseInt(minQty.replace("kg",""));
        else

            return Integer.parseInt(minQty.replace("g",""))/1000f;


    }

    private boolean areVariantsValid(String variants) {
        if(variants.length() == 0)
            return true;


        String[] vs = variants.split("\n");


        Pattern pattern = Pattern.compile("^\\w+(\\s|\\w)+,\\d+$");
        for (String variant : vs)
            if (!pattern.matcher(variant).matches())
                return false;

        product.fromVariantStrings(vs);

        return true;
    }


}
