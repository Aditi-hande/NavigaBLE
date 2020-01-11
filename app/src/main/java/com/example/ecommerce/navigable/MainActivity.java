package com.example.ecommerce.navigable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ecommerce.navigable.dijkstra.DijkstraAlgorithm;
import com.example.ecommerce.navigable.dijkstra.exception.PathNotFoundException;
import com.example.ecommerce.navigable.dijkstra.model.Edge;
import com.example.ecommerce.navigable.dijkstra.model.Graph;
import com.example.ecommerce.navigable.dijkstra.model.Vertex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab, fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen = false;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    ArrayList<Vertex<VertexData>> vertices;
    ArrayList<Edge> edges;
    GraphView graphView;

    private int source = -1, dest = -1;
    private boolean isSourceSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphView = findViewById(R.id.graph_view);
        //setContentView(view);



        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);
        fabLayout3 = (LinearLayout) findViewById(R.id.fabLayout3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabBGLayout = findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegionActivity.class));
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });


        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance("gs://navigable-25e2d.appspot.com");
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if(!task.isSuccessful())
                {
                    Log.i("Token of Notif","Task Failed");
                    return;
                }
                Log.i("Token of Notif","The result: "+  task.getResult().getToken());

            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("AirIndia");

        vertices = new ArrayList<>();
        edges = new ArrayList<>();


        /*vertices.add(new Vertex<VertexData>(new VertexData(15,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(37,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(49,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(72,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(104,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(117,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(130,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(150,12)));
        vertices.add(new Vertex<VertexData>(new VertexData(180,12)));


        vertices.add(new Vertex<VertexData>(new VertexData(10,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(33,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(49,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(72,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(95,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(106,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(117,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(130,51)));
        vertices.add(new Vertex<VertexData>(new VertexData(159,51)));

        vertices.add(new Vertex<VertexData>(new VertexData(49,36)));
        vertices.add(new Vertex<VertexData>(new VertexData(117,36)));

        vertices.add(new Vertex<VertexData>(new VertexData(180,40)));
        vertices.add(new Vertex<VertexData>(new VertexData(95,65)));
        vertices.add(new Vertex<VertexData>(new VertexData(180,51)));*/

    }


    private void showFABMenu() {
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);
        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotation(0);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }
/*                if (fab.getRotation() != -180) {
                    fab.setRotation(-180);
                }*/
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadDataFromFile();
        } else {
            mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    loadDataFromFile();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                            exception.printStackTrace();
                            Log.e("SIGN-IN", "Sign In failure");
                        }
                    });
        }
    }


    public void loadDataFromFile() {
        db.collection("plans").document("RahulRaj").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        try {
                            downloadFile(snapshot);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void downloadFile(DocumentSnapshot snapshot) throws IOException {
            final File temp = File.createTempFile("floor-plan", "txt");

            storage.getReference(snapshot.get("URL", String.class).replace("https://", "").replace("gs://", ""))
                    .getFile(temp)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            loadValues(temp);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
    }

    public void loadValues(File file) {
        String line="";
        //Input from database graph file in BufferedReader
        try{
            BufferedReader br=new BufferedReader(new FileReader(file));
            while((line=br.readLine())!=null) {
                if(line.equals("V")) {
                    continue;
                }else if(line.equals("E")){
                    break;
                }else {
                    String[] splitV=line.split("\\s");
                    int v1=Integer.parseInt(splitV[1]);
                    int v2=Integer.parseInt(splitV[2]);
                    //Uncomment line below in Android
                    vertices.add(new Vertex<VertexData>(new VertexData(v1,v2)));
                }
            }
            while((line=br.readLine())!=null) {
                String[] splitE=line.split("\\s");
                int e1=Integer.parseInt(splitE[1]);
                int e2=Integer.parseInt(splitE[2]);
                //Uncomment two lines below in Android
                edges.add(new EdgeData(vertices.get(e1),vertices.get(e2)));
                edges.add(new EdgeData(vertices.get(e2),vertices.get(e1)));
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        // Update Vertex List in View
        graphView.updateVertices(getVertexData(vertices));
        //

        /*edges.add(new EdgeData(vertices.get(0), vertices.get(1)));
        edges.add(new EdgeData(vertices.get(1), vertices.get(2)));
        edges.add(new EdgeData(vertices.get(2), vertices.get(3)));
        edges.add(new EdgeData(vertices.get(3), vertices.get(4)));
        edges.add(new EdgeData(vertices.get(4), vertices.get(5)));
        edges.add(new EdgeData(vertices.get(5), vertices.get(6)));
        edges.add(new EdgeData(vertices.get(6), vertices.get(7)));
        edges.add(new EdgeData(vertices.get(7), vertices.get(8)));

        edges.add(new EdgeData(vertices.get(8), vertices.get(20)));
        edges.add(new EdgeData(vertices.get(20), vertices.get(22)));

        edges.add(new EdgeData(vertices.get(9), vertices.get(10)));
        edges.add(new EdgeData(vertices.get(10), vertices.get(11)));
        edges.add(new EdgeData(vertices.get(11), vertices.get(12)));
        edges.add(new EdgeData(vertices.get(12), vertices.get(13)));
        edges.add(new EdgeData(vertices.get(13), vertices.get(14)));
        edges.add(new EdgeData(vertices.get(14), vertices.get(15)));
        edges.add(new EdgeData(vertices.get(15), vertices.get(16)));
        edges.add(new EdgeData(vertices.get(16), vertices.get(17)));

        edges.add(new EdgeData(vertices.get(2), vertices.get(18)));
        edges.add(new EdgeData(vertices.get(18), vertices.get(11)));
        edges.add(new EdgeData(vertices.get(5), vertices.get(19)));
        edges.add(new EdgeData(vertices.get(15), vertices.get(19)));
        edges.add(new EdgeData(vertices.get(13), vertices.get(21)));
        edges.add(new EdgeData(vertices.get(22), vertices.get(17)));

    // Reverse edges
        edges.add(new EdgeData(vertices.get(1), vertices.get(0)));
        edges.add(new EdgeData(vertices.get(2), vertices.get(1)));
        edges.add(new EdgeData(vertices.get(3), vertices.get(2)));
        edges.add(new EdgeData(vertices.get(4), vertices.get(3)));
        edges.add(new EdgeData(vertices.get(5), vertices.get(4)));
        edges.add(new EdgeData(vertices.get(6), vertices.get(5)));
        edges.add(new EdgeData(vertices.get(7), vertices.get(6)));
        edges.add(new EdgeData(vertices.get(8), vertices.get(7)));

        edges.add(new EdgeData(vertices.get(20), vertices.get(8)));
        edges.add(new EdgeData(vertices.get(22), vertices.get(20)));

        edges.add(new EdgeData(vertices.get(10), vertices.get(9)));
        edges.add(new EdgeData(vertices.get(11), vertices.get(10)));
        edges.add(new EdgeData(vertices.get(12), vertices.get(11)));
        edges.add(new EdgeData(vertices.get(13), vertices.get(12)));
        edges.add(new EdgeData(vertices.get(14), vertices.get(13)));
        edges.add(new EdgeData(vertices.get(15), vertices.get(14)));
        edges.add(new EdgeData(vertices.get(16), vertices.get(15)));
        edges.add(new EdgeData(vertices.get(17), vertices.get(16)));

        edges.add(new EdgeData(vertices.get(18), vertices.get(2)));
        edges.add(new EdgeData(vertices.get(11), vertices.get(18)));
        edges.add(new EdgeData(vertices.get(19), vertices.get(5)));
        edges.add(new EdgeData(vertices.get(19), vertices.get(15)));
        edges.add(new EdgeData(vertices.get(21), vertices.get(13)));
        edges.add(new EdgeData(vertices.get(17), vertices.get(22)));*/

        graphView.updateEdges(edges);

        final ArrayList<Integer> emergencyExits = new ArrayList<>();
        db.collection("plans").document("RahulRaj")
                .collection("places").document("U5YtR9QHWDaCle11aDZ1")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        System.out.println(documentSnapshot.getId());
                        Map<String,Object> map = documentSnapshot.getData();

                        for(Map.Entry<String,Object> entry : map.entrySet()) {
                            for(String elem : (List<String>)entry.getValue()) {
                                if(elem.equals("Emergency Exit")) {
                                    emergencyExits.add(Integer.parseInt(entry.getKey().replace("Region", "")));
                                    break;
                                }
                            }
                        }
                        graphView.updateHighlightedVertices(emergencyExits);
                    }
                });

/*
        ((Button)findViewById(R.id.graph_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int source = Integer.parseInt(((EditText)findViewById(R.id.source)).getText().toString());
                int dest = Integer.parseInt(((EditText)findViewById(R.id.dest)).getText().toString());
                if(source < vertices.size() && dest < vertices.size())
                    drawPath(graphView, source, dest);
                else
                    Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_LONG).show();

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/
        graphView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSourceSet) {
                    dest = graphView.getRegion();
                    drawPath(graphView, source, dest);
                    isSourceSet = false;
                    source = -1; dest = -1;
                } else {
                    drawPath(graphView, source, dest);
                    source = graphView.getRegion();
                    if(source > -1)
                        isSourceSet = true;
                    else
                        isSourceSet = false;
                }

            }
        });

    }

    public void drawPath(GraphView view, int source, int dest){
        if(source > -1 && dest > -1) {
            try {
                DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(new Graph(edges));
                dijkstraAlgorithm.execute(vertices.get(source));
                LinkedList<Vertex> path = dijkstraAlgorithm.getPath(vertices.get(dest));
                List<VertexData> navPath = new ArrayList<>(path.size());
                for (Vertex<VertexData> v : path) {
                    String msg = v.getPayload().x + " , " + v.getPayload().y;
                    Log.d("PATH", msg);
                    navPath.add(v.getPayload());
                }
                view.updateNavPath(navPath);
            } catch (PathNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            view.updateNavPath(new ArrayList<VertexData>());
        }
    }

    public void displayRegionData(View view) {
        Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
        startActivity(intent);
    }

    public void displayEmergencyExits(View view) {
        graphView.setHighlightVertices(!graphView.getHighlightVertices());
    }

    public ArrayList<VertexData> getVertexData(List<Vertex<VertexData>> list) {
        ArrayList<VertexData> vdList = new ArrayList<>();
        for(Vertex<VertexData> vertex : list) {
            vdList.add(vertex.getPayload());
        }
        return vdList;
    }

    public class VertexData {
        public int x;
        public int y;

        public VertexData(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return this.x == ((VertexData)obj).x && this.y == ((VertexData)obj).y;
        }

    }

    public class EdgeData extends Edge {

        public EdgeData(Vertex<VertexData> src, Vertex<VertexData> dest) {
            super(src, dest, (src.getPayload().x - dest.getPayload().x) * (src.getPayload().x - dest.getPayload().x) + (src.getPayload().y - dest.getPayload().y) * (src.getPayload().y - dest.getPayload().y));
        }

    }
}
