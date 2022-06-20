package com.example.shoppingcart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.example.shoppingcart.Model.Firebase.AddShoppingModel;
import com.example.shoppingcart.Model.ShopItem;
import com.example.shoppingcart.Model.ui.ExpandshoppingGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ListShoppingList extends AppCompatActivity {

    ImageButton addShoppingButton;
    private CreateAndFillValuesInListShoppingRows createAndFillValuesInListShoppingRows;
    public ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shopping_list);
        addShoppingButton=findViewById(R.id.addButton);
        expandableListView=findViewById(R.id.ExpList);
        fetchAndPopulateEntriesInExpandableListView();
        addShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ListShoppingList.this,CreateShoppingList.class);
                startActivity(intent); // This helps moves from one activity to another.
            }
        });
    }

    private void fetchAndPopulateEntriesInExpandableListView() {
        FirebaseDatabase.getInstance().getReference().child("shopping")
                .child(MainActivity.getSubscriberId(ListShoppingList.this))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ArrayList<ExpandshoppingGroup> groupList=new ArrayList<>();
                        for (DataSnapshot areaSnapshot:dataSnapshot.getChildren())
                        {
                            AddShoppingModel value=areaSnapshot.getValue(AddShoppingModel.class);
                            String orderId=areaSnapshot.getKey();
                            ExpandshoppingGroup grul=new ExpandshoppingGroup();
                            grul.setShoppingId(orderId);
                            grul.setDate(value.getDate());
                            ArrayList<ShopItem> childList=new ArrayList<>();

                            for (ShopItem shopItem:value.getSales().values())
                            {
                                ShopItem rowChild=new ShopItem();
                                rowChild.setCategory(shopItem.getCategory());
                                rowChild.setProduct(shopItem.getProduct());
                                rowChild.setQuantity(shopItem.getQuantity());
                                rowChild.setUnit(shopItem.getUnit());
                                childList.add(rowChild);
                            }
                            grul.setItems(childList);
                            groupList.add(grul);
                        }
                        Collections.sort(groupList);
                        for (int k=1;k<=groupList.size();k++)
                        {
                            groupList.get(k-1).setName("Shopping List "+k);
                        }
                        createAndFillValuesInListShoppingRows=new CreateAndFillValuesInListShoppingRows(ListShoppingList.this,groupList);
                        expandableListView.setAdapter(createAndFillValuesInListShoppingRows);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setChildIndicatorBounds(width - GetPixelfromDips(80), width - GetPixelfromDips(55));
        }
        else
        {
            expandableListView.setChildIndicatorBounds(width - GetPixelfromDips(85), width - GetPixelfromDips(55));
        }




    }
    private int GetPixelfromDips(float pixels)
    {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels* scale + 0.5f);
    }
}