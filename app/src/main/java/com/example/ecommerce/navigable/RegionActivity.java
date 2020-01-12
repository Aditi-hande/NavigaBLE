package com.example.ecommerce.navigable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionActivity extends AppCompatActivity {

    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        cardView = (CardView) findViewById(R.id.card_view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("plans").document("PAirport")
                .collection("places").document("1ktTiRC3g5c8AVdJWTM5") //U5YtR9QHWDaCle11aDZ1
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        System.out.println(documentSnapshot.getId());
                        Map<String,Object> map = documentSnapshot.getData();
                        String listStr = "";
                        for(Map.Entry<String,Object> entry : map.entrySet()) {
                            listStr = listStr.concat("Region-"+entry.getKey()+"\n\t\t\t");
                            for(String elem : (List<String>)entry.getValue()) {
                                listStr = listStr.concat(elem+", ");
                                System.out.println(elem);
                            }
                            listStr = listStr.concat("\n\n");
                            System.out.println(listStr);
                        }

                    }
                });

    }


}
