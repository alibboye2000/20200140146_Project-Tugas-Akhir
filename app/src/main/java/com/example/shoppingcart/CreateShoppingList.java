package com.example.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.style.DynamicDrawableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.shoppingcart.Model.Firebase.AddShoppingModel;
import com.example.shoppingcart.Model.ShopItem;
import com.example.shoppingcart.ui.AddItemInShopTable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CreateShoppingList extends AppCompatActivity {
    TableLayout tableLayoutforCreateShoppingList;
    DatabaseReference databaseReference;
    Button submitDB;
    Button addMoreItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);

        tableLayoutforCreateShoppingList = findViewById(R.id.addOrderinTable);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addMoreItem = findViewById(R.id.addMoreItem);
        addMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItemInShopTable.addRow(view.getContext(), tableLayoutforCreateShoppingList, databaseReference); //this will add a row in the app item if you click it

            }
        });

        submitDB = findViewById(R.id.submit);
        submitDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, ShopItem> sales = new HashMap<>();
                Map<String, String> newCatandProd = new HashMap<>();
                boolean error = false; //create entry to save row by row in firebase
                for (int i = 1, j = tableLayoutforCreateShoppingList.getChildCount() - 2; i < j; i++) {
                    View rowView = tableLayoutforCreateShoppingList.getChildAt(i);
                    if (rowView instanceof TableRow) {
                        TableRow row = (TableRow) rowView;
                        String category, product;
                        int itemDetailChildCount = row.getChildCount();
                        String newCategory = null;
                        String newProduct = null;
                        Spinner categorySpinner = (Spinner) row.getChildAt(0);
                        int quantityIndex;
                        int unitIndex;
                        category = (String) categorySpinner.getSelectedItem();
                        if (itemDetailChildCount == 5) {
                            Spinner productSpinner = (Spinner) row.getChildAt(1);
                            product = (String) productSpinner.getSelectedItem();
                            quantityIndex = 2;
                            unitIndex = 3;
                        } else {
                            if (category.equals("Others")) {
                                EditText newCategoryEditText = (EditText) row.getChildAt(1);
                                category = newCategoryEditText.getText().toString();
                                newCategory = category;

                                EditText newProductEditText = (EditText) row.getChildAt(1);
                                product = newProductEditText.getText().toString();
                                newProduct = product;
                            } else {
                                EditText newProductEditText = (EditText) row.getChildAt(1);
                                product = newProductEditText.getText().toString();
                                newProduct = product;
                            }
                            quantityIndex = 3;
                            unitIndex = 4;

                        }

                        EditText quantityEditText = (EditText) row.getChildAt(quantityIndex);
                        Spinner unitSpinner = (Spinner) row.getChildAt(unitIndex);
                        if (quantityEditText.getText().length() == 0 || Integer.valueOf(quantityEditText.getText().toString()) < 1) {
                            quantityEditText.setError("Quantity must be provided and greather tan 0");
                            error = false;
                            break;
                        }

                        ShopItem shopItem = new ShopItem(category, product, Integer.valueOf(quantityEditText.getText().toString()), (String) unitSpinner.getSelectedItem());
                        sales.put(UUID.randomUUID().toString(),shopItem); //Crete a unitID to store item in database
                        if (newCategory!=null) {
                            newCatandProd.put(newCategory, newProduct);
                        }
                        else if (newProduct !=null)
                        {
                            newCatandProd.put(category,newProduct);
                        }

                    }

                }
                if (sales.size()==0) {
                    DynamicToast.makeError(getApplicationContext(), "provide sale", Toast.LENGTH_LONG).show();
                }
                else if (!error)
                {
                    String saleId="startup_splash_cart "+ UUID.randomUUID().toString();
                    Date c= Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat( "dd-MMM-yyyy 'T' HH:mm:ssZ");
                    String formattedDate=df.format(c);
                    AddShoppingModel shoppingModel=new AddShoppingModel(sales,formattedDate);
                    databaseReference.child("shopping").child(MainActivity.getSubscriberId(getApplicationContext()))
                            .child(saleId).setValue(shoppingModel); // This add shopping list inside file list.

                    DynamicToast.makeSuccess(getApplicationContext(),"Shopping List Added Successfully",Toast.LENGTH_LONG).show();
                    int count=tableLayoutforCreateShoppingList.getChildCount();
                    for (int i=1;i<count -2 ;i++)
                    {
                        tableLayoutforCreateShoppingList.removeView(tableLayoutforCreateShoppingList.getChildAt(1));
                    }
                    if (newCatandProd.size()>0)
                    {
                        for (String newCat:newCatandProd.keySet())
                        {
                            databaseReference.child("categories").child(toCamelCase(newCat)).child(UUID.randomUUID().toString())
                                    .setValue(toCamelCase(newCatandProd.get(newCat)));
                        }
                    }


                }
            }

        });
    }
    static String toCamelCase(String s) {
        String [] parts=s.split("_");
        String camelCaseString="";
        for (String part:parts)
        {
            camelCaseString=camelCaseString + toProperCase(part);
        }
        return  camelCaseString;
        }

        static String toProperCase(String s)
        {
            return  s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();

        }

    }

