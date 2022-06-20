package com.example.shoppingcart.ui;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import com.example.shoppingcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class AddItemInShopTable {

    public static void addRow(final Context context, final TableLayout addda1TableLayout, final DatabaseReference databaseReference)
    {
        final TableRow tr=new TableRow(context);
        TableLayout.LayoutParams paramsForRow=new TableLayout.LayoutParams(0,TableLayout.LayoutParams.WRAP_CONTENT,1f);
        paramsForRow.setMargins(10, 10, 0,  20);
        tr.setLayoutParams(paramsForRow);

        final TableRow.LayoutParams ParamsForCategoryAndProduct =new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.4f);
        final TableRow.LayoutParams ParamsForUnit =new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 9f);
        final TableRow.LayoutParams ParamsForQuantity =new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.4f);

        ViewGroup.LayoutParams paramsForCategoryAndProduct = null;

        final Spinner catSpinner=new Spinner(context);
        catSpinner.setLayoutParams(paramsForCategoryAndProduct);

        final Spinner productSpinner= new Spinner(context);
        catSpinner.setLayoutParams(paramsForCategoryAndProduct);

        ViewGroup.LayoutParams paramsForQuantity = null;

        final EditText quantity= new EditText(context);
        quantity.setLayoutParams(paramsForQuantity);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        ViewGroup.LayoutParams paramsForUnit = null;

        final Spinner unitSpinner= new Spinner(context);
        unitSpinner.setLayoutParams(paramsForUnit);

        final EditText etNewCategory= new EditText(context);
        etNewCategory.setLayoutParams(paramsForCategoryAndProduct);
        etNewCategory.setHint("New Category");

        final EditText etNewProduct= new EditText(context);
        etNewCategory.setLayoutParams(paramsForCategoryAndProduct);
        etNewCategory.setHint("New Product");

        final TableRow.LayoutParams rowParamasMinus=new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, .5f);
        ImageButton minusButton= new ImageButton(context);
        minusButton.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
        minusButton.setLayoutParams(rowParamasMinus);

        fetchUnitSpinner(databaseReference,context,unitSpinner);
        fetchCategoryandProduct(databaseReference,context,catSpinner,productSpinner,tr,etNewCategory,etNewProduct);
        addOrRemoveProductSpinner(productSpinner,tr,etNewProduct);

        tr.addView(catSpinner);
        tr.addView(quantity);
        tr.addView(unitSpinner);
        tr.addView(minusButton);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addda1TableLayout.removeView((ViewGroup)view.getParent());
            }
        });
        addda1TableLayout.addView(tr, addda1TableLayout.getChildCount() - 2,
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));




    }

    private static void addOrRemoveProductSpinner(Spinner productSpinner,final TableRow tr,final EditText etNewProduct) {
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (productSpinner.getSelectedItem().equals("Others"))
                {
                    tr.addView(etNewProduct, 2);
                }
                else
                {
                    tr.removeView(etNewProduct);
                }


                }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private static void fetchCategoryandProduct(DatabaseReference databaseReference, final Context context,final Spinner catSpinner,final Spinner productSpinner,final TableRow tr,final EditText etNewCategory,final EditText etNewProduct) {
        fetchCategoriesInSpinner(databaseReference,context,catSpinner);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) catSpinner.getSelectedItem();
                if (selectedItem.equals("Others")) {
                    tr.removeView(productSpinner);
                    tr.addView(etNewCategory, 1);
                    tr.addView(etNewProduct, 2);
                } else {
                    tr.removeView(etNewCategory);
                    if (!tr.getChildAt(1).equals(productSpinner)) ;
                    {
                        tr.addView(productSpinner, 1);
                    }
                    databaseReference.child("categories").child(selectedItem)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final ArrayList<String> productlist = new ArrayList<>();
                                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) ;
                                    {
                                        productlist.add(dataSnapshot.getValue(String.class));
                                    }
                                    Collections.sort(productlist);
                                    productlist.add("Others");

                                    final ArrayAdapter<String> productAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, productlist);
                                    productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    productSpinner.setAdapter(productAdapter);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private static void fetchCategoriesInSpinner(DatabaseReference databaseReference,final Context context,final Spinner catSpinner) {

        databaseReference.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> catlist = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) ;
                {
                    catlist.add(dataSnapshot.getKey());
                }
                Collections.sort(catlist);
                catlist.add("Others");

                final ArrayAdapter<String> catAdapter=new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, catlist);
                catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                catSpinner.setAdapter(catAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }



    private static void fetchUnitSpinner(DatabaseReference databaseReference, final Context context, final Spinner unitSpinner) {

        databaseReference.child("Unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> unitlist = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) ;
                {
                    unitlist.add(dataSnapshot.getValue(String.class));
                }
                Collections.sort(unitlist);
                final ArrayAdapter<String> unitAdapter=new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, unitlist);
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                unitSpinner.setAdapter((SpinnerAdapter) unitSpinner);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


}
