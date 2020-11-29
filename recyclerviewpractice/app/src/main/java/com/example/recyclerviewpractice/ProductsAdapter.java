package com.example.recyclerviewpractice;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.recyclerviewpractice.databinding.WeightBasedLayoutBinding;
import com.example.recyclerviewpractice.databinding.VariantBasedLayoutBinding;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public int lastSelectedItemPosition;
    Context context;
   public ArrayList<Product> strings,visibleProducts,allProducts;



    public ProductsAdapter(Context context, ArrayList<Product> strings) {
        this.context = context;
        allProducts = strings;
        this.visibleProducts = new ArrayList<>(strings);
    }


    @Override
    public int getItemViewType(int position) {

         return visibleProducts.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case Product.WEIGHT_BASED:
                WeightBasedLayoutBinding tb = WeightBasedLayoutBinding.inflate(LayoutInflater.from(context),
                        parent,
                        false);

                return new WeightBasedProduct(tb);

            case Product.VARIANTS_BASED:
                VariantBasedLayoutBinding tb2 = VariantBasedLayoutBinding.inflate(LayoutInflater.from(context),
                        parent,
                        false);

                return new VariantsBasedBinding(tb2);
        }


        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         Product product = visibleProducts.get(position);
   switch (holder.getItemViewType()) {
       case Product.WEIGHT_BASED:
       WeightBasedProduct wbp = (WeightBasedProduct) holder;
       WeightBasedLayoutBinding b = wbp.b;

           b.name.setText(product.name);
           b.pricePerKg.setText("Rs. " + product.pricePerKg);

           b.minQty.setText("Min " + product.minQty + "kg");
//inflation
           setupContextMenu(b.getRoot());

           break;

       case Product.VARIANTS_BASED:

       VariantsBasedBinding variantsBasedBinding = (VariantsBasedBinding) holder;
      VariantBasedLayoutBinding x ;
      x = variantsBasedBinding.x;

           x.name.setText(product.name);
           x.variants.setText(product.variantsString());
           //inflation
           setupContextMenu(x.getRoot());


           break;



   }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lastSelectedItemPosition = holder.getAdapterPosition();
                return false;
            }
        });
    }

    private void setupContextMenu(ConstraintLayout root) {
        root.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                if(! (context instanceof MainActivity))
                    return;

                ((MainActivity) context)
                        .getMenuInflater().inflate(R.menu.contextual_menu, contextMenu);
            }
        });



    }

    @Override
    public int getItemCount() {
        return visibleProducts.size();
    }


        public void filter(String query){
            query = query.toLowerCase();
            visibleProducts = new ArrayList<>();

            for(Product product : allProducts){
                if(product.name.toLowerCase().contains(query))
                    visibleProducts.add(product);
            }

            notifyDataSetChanged();
        }



    public static class WeightBasedProduct extends RecyclerView.ViewHolder{

        WeightBasedLayoutBinding b;

        public WeightBasedProduct(@NonNull WeightBasedLayoutBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
    public static class VariantsBasedBinding extends RecyclerView.ViewHolder{

        public VariantBasedLayoutBinding x;


        public VariantsBasedBinding(@NonNull VariantBasedLayoutBinding x) {
            super(x.getRoot());
            this.x = x;
        }
    }
}
