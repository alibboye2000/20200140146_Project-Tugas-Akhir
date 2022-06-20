package com.example.shoppingcart.Model.ui;

import com.example.shoppingcart.Model.ShopItem;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExpandshoppingGroup implements Comparable<ExpandshoppingGroup>, Serializable {
    private String shoppingId;
    private String date;
    private String name;
    private ArrayList<ShopItem> Items;

    @Override
    public int compareTo(ExpandshoppingGroup expandshoppingGroup) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aaa z");
        try
        {
            return sdf.parse(getDate()).compareTo(sdf.parse(ExpandshoppingGroup.getDate()));
        }
        catch (ParseException e)
        {
            return 1;
        }
    }

    public ExpandshoppingGroup() {
    }

    public ExpandshoppingGroup(String shoppingId, String date, String name, ArrayList<ShopItem> items) {
        this.shoppingId = shoppingId;
        this.date = date;
        this.name = name;
        Items = items;
    }

    public String getShoppingId() {
        return shoppingId;
    }

    public void setShoppingId(String shoppingId) {
        this.shoppingId = shoppingId;
    }

    public static String getDate() {
        return getDate();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ShopItem> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ShopItem> items) {
        Items = items;
    }
}
