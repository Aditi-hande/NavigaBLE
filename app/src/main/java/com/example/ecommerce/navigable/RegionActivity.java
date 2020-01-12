package com.example.ecommerce.navigable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionActivity extends AppCompatActivity {

    List<String> regions;
    HashMap<String, List<String>> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.exp_list_view);

        regions = new ArrayList<>();
        places = new HashMap<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("plans").document("PAirport")
                .collection("places").document("1ktTiRC3g5c8AVdJWTM5") //U5YtR9QHWDaCle11aDZ1
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        System.out.println(documentSnapshot.getId());
                        Map<String,Object> map = documentSnapshot.getData();
                        for(Map.Entry<String,Object> entry : map.entrySet()) {
                            regions.add(entry.getKey());
                            places.put(entry.getKey(), (List<String>) entry.getValue());
                        }
                        expandableListView.setAdapter(new ExpandableListAdapter(getApplicationContext(), regions, places, RegionActivity.this));
                    }
                });

    }


}
