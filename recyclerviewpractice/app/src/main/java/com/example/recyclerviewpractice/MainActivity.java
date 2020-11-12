package com.example.recyclerviewpractice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.recyclerviewpractice.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding b;

    private ArrayList<Product> strings;
    private ProductsAdapter productsAdapter;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b =  ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setUpItems();



    }

    private void setUpItems() {
        strings= new ArrayList<>(Arrays.asList(


        ));

        productsAdapter = new ProductsAdapter(this,strings);

        b.recyclerView.setAdapter(productsAdapter);

        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    //inflating options menu

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {
                productsAdapter.filter(query);
                return true;

            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }








    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_item :
                showProductEditorDialog();
                return true;

                case R.id.sort_list :
                sortList();
                return true;




        }

        return super.onOptionsItemSelected(item);
    }

    private void sortList() {
        Collections.sort(productsAdapter.visibleProducts, new Comparator<Product>(){
            @Override
            public int compare(Product a, Product b) {
                return a.name.compareTo(b.name);
            }
        });
       productsAdapter.notifyDataSetChanged();
        Snackbar.make(b.recyclerView, " List Sorted", Snackbar.LENGTH_LONG)

    .show();
    }










    private void showProductEditorDialog() {
            new EditorDialog()
                    .show(this, new Product(), new EditorDialog.OnProductEditedListener() {
                        @Override
                        public void onProductEdited(Product product) {
                            productsAdapter.allProducts.add(product);

                            if(isNameInQuery(product.name)){
                                productsAdapter.visibleProducts.add(product);
                                productsAdapter.notifyItemInserted(productsAdapter.visibleProducts.size() - 1);
                            }
//                        if(isNameInQuery(product.name)){
//                            adapter.visibleProducts.add(product);
//                            adapter.notifyItemInserted(adapter.visibleProducts.size() - 1);
//                        }



                        }

                        @Override
                        public void onCancelled() {
                            Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    private boolean isNameInQuery(String name) {

        String query = searchView.getQuery().toString().toLowerCase();
        return name.toLowerCase().contains(query);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.product_edit :
                editLastSelectedItem();

                return true;

            case R.id.product_remove :
                removeLastSelectedItem();

                return true;



        }

        return super.onContextItemSelected(item);
    }

    private void removeLastSelectedItem() {

        new AlertDialog.Builder(this)
                .setTitle("Remove this product?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Product productToBeRemoved = productsAdapter.visibleProducts.get(productsAdapter.lastSelectedItemPosition);

                        productsAdapter.visibleProducts.remove(productToBeRemoved);
                        productsAdapter.allProducts.remove(productToBeRemoved);


                        productsAdapter.notifyItemRemoved(productsAdapter.lastSelectedItemPosition);

                        Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();

    }

    private void editLastSelectedItem() {

        Product lastProduct = productsAdapter.visibleProducts.get(productsAdapter.lastSelectedItemPosition);


        new EditorDialog()
                .show(this, lastProduct, new EditorDialog.OnProductEditedListener() {
                    @Override
                    public void onProductEdited(Product product) {

                        productsAdapter.notifyItemChanged(productsAdapter.lastSelectedItemPosition);
                    }

                    @Override
                    public void onCancelled() {
                        Toast.makeText(MainActivity.this, "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });




    }
    }




