package com.example.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    private static String uniqueID = null;
    private static final String PREE_UNIQUE_ID = "PREFER_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ListShoppingList.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

    }

    public synchronized static String getSubscriberId(Context context) {
        if (uniqueID == null)
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREE_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID=sharedPreferences.getString(PREE_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = "subscriber" + UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREE_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
}



