package com.example.ecommerce.navigable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ecommerce.navigable.dijkstra.model.Edge;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {

    private Paint p;
    private Path path;

    private int scale = 10;

    private Point point1;
    private Point point2;
    List<MainActivity.VertexData> vertices;
    List<Edge> edges;
    List<MainActivity.VertexData> navPath;

    public GraphView(Context context) {
        super(context);
        initialize();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        this.p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(getResources().getColor(R.color.colorPath, null));
        this.path = new Path();
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.navPath = new ArrayList<>();

        point1 = new Point(200, 300);
        point2 = new Point(700, 800);
    }

    protected void onDraw(Canvas canvas) {

        if(vertices == null || edges == null) {
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point1.x, point1.y, 18, p);
        }
        else {
            p.setColor(getResources().getColor(R.color.colorPoint, null));
            p.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate(90);
            canvas.translate(50, -800);
            for(MainActivity.VertexData vertex : vertices) {
                canvas.drawCircle(vertex.x * scale, vertex.y * scale, 10, p);
            }

            //Draw Edges
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(15);
            p.setColor(getResources().getColor(R.color.colorEdge, null));
            path.reset();
            for(Edge edge : edges) {
                String msg = "src: " + ((MainActivity.VertexData)edge.getSource().getPayload()).x + "," + ((MainActivity.VertexData)edge.getSource().getPayload()).y +
                        " ; " + "dst: " + ((MainActivity.VertexData)edge.getDestination().getPayload()).x + "," + ((MainActivity.VertexData)edge.getDestination().getPayload()).y;
                Log.d("EDGES", msg);
                path.moveTo(((MainActivity.VertexData)edge.getSource().getPayload()).x * scale, ((MainActivity.VertexData)edge.getSource().getPayload()).y * scale);
                path.lineTo(((MainActivity.VertexData)edge.getDestination().getPayload()).x * scale, ((MainActivity.VertexData)edge.getDestination().getPayload()).y * scale);

            }
            canvas.drawPath(path, p);

            //Draw Navigation Path
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(8);
            p.setColor(getResources().getColor(R.color.colorPath, null));
            path.reset();
            path.moveTo(navPath.get(0).x * scale, navPath.get(0).y * scale);
            for(MainActivity.VertexData vertex : navPath) {
                path.lineTo(vertex.x * scale, vertex.y * scale);
            }
            canvas.drawPath(path, p);
            canvas.restore();
        }
        /*// draw the edge
        path.reset();
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.CYAN);
        canvas.drawPath(path, p);

        // draw second vertex
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLUE);
        canvas.drawCircle(point2.x, point2.y, 15, p);
        */
    }

    public void updateVertices(List<MainActivity.VertexData> vertices) {
        this.vertices = vertices;

        Log.d("VERTICES", "size: " + this.vertices.size());

        invalidate();
    }

    public void updateEdges(List<Edge> edges) {
        this.edges = edges;

        Log.d("EDGES", "size: " + this.edges.size());

        invalidate();
    }

    public void updateNavPath(List<MainActivity.VertexData> navPath) {
        this.navPath = navPath;

        Log.d("EDGES", "size: " + this.navPath.size());

        invalidate();
    }
}
