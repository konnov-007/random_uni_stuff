package konnov.commr.vk.fordbellman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {
    EditText inputEditText;
    TextView outputTextView;
    int numberOfEdges;
    int numberOfVertexes;
    int trudoemkost;
    ArrayList<Integer> p;
    int indices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEditText = findViewById(R.id.inputEditText);
        outputTextView = findViewById(R.id.outputTextView);
        //inputEditText.setText("1 2 25\n1 3 15\n1 4 7\n2 3 6\n3 4 4\n4 5 3\n1 4 2");
    }

    public void buttonClicked(View view) {
        if (inputEditText.getText().toString().isEmpty())
            return;
        trudoemkost = 0;
        outputTextView.setText("");
        p = new ArrayList<>();
        findShortestWay();
    }

    private void findShortestWay() {
        Edge[] edge = getGraph();
        int[] lengthPath;
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = 0; i < numberOfEdges; i++) {
            edge[i].x--;
            edge[i].y--;
        }

        lengthPath = fordBellman(edge);
        outputTextView.append("\n");
        for (int i = 1; i < numberOfVertexes; i++) {
            outputTextView.append(String.format("Минимальное расстояние пути (1, %d): %d\n", (int)(i + 1), lengthPath[i]));
            for (int k = i; ; k = p.get(k)) {
                path.add(k);
                if (k == 0) break;
            }
            Collections.reverse(path);
            for (int k = 0; k < path.size(); k++) {
                outputTextView.append("" + (int) (path.get(k) + 1));
                if (k != path.size() - 1) outputTextView.append(" -> ");
            }
            outputTextView.append("\n\n");
            path.clear();
        }
        outputTextView.append("Трудоемкость: " + trudoemkost + "\n\n");
    }

    private int[] fordBellman(Edge[] edge) {
        p.add(0);
        int[] DPrev = new int[numberOfVertexes];
        int[] D = new int[numberOfVertexes];
        int[] sums = new int[numberOfVertexes];

        DPrev[0] = 0;
        for (int i = 1; i < numberOfVertexes; i++) {
            DPrev[i] = (int) pow(10, 5); // ~бесконченость
        }
        printGraph(DPrev);
        while (true) {
            trudoemkost++; //до стабилизации
            for (int i = 1; i < numberOfVertexes; i++) {
                trudoemkost++; // по вершинам графа
                for (int j = 0; j < numberOfVertexes; j++) {
                    trudoemkost++; // нахождение минимума по переменной j
                    sums[j] = DPrev[j] + cost_of_transit(edge, j, i);
                }
                int minimum = search_min(i, sums);
                D[i] = minimum;
                p.add(indices);
            }
            printGraph(D);
            if (!stability(DPrev, D)) {
                p.clear();
                p.add(0);
                for (int i = 0; i < numberOfVertexes; i++) {
                    DPrev[i] = D[i];
                }
            } else break;
        }
        return D;
    }

    private boolean stability(int[] DPrev, int[] D) {
        int count = 0;
        for (int i = 0; i < numberOfVertexes; i++) {
            if (DPrev[i] == D[i]) count++;
        }
        if (count == numberOfVertexes) return true;
        else return false;
    }

    private int search_min(int iter, int[] m) {
        indices = 0;
        int min = m[0];
        for (int i = 1; i < m.length; i++) {
            if (m[i] < min) {
                min = m[i];
                indices = i;
            }
            if (indices == iter) indices = i + 1;
        }
        return min;
    }

    private int cost_of_transit(Edge[] edge, int x, int y) {
        if (x == y) return 0;
        for (int i = 0; i < numberOfEdges; i++) {
            if ((x == edge[i].x && y == edge[i].y) || (x == edge[i].y && y == edge[i].x)) {
                return edge[i].w;
            }
        }
        return (int) pow(10, 5);
    }


    Edge[] getGraph() {
        outputTextView.setText("");
        String[] graphesString = inputEditText.getText().toString().split("\n");
        numberOfEdges = graphesString.length;
        Edge[] edge = new Edge[numberOfEdges];
        for (int i = 0; i < numberOfEdges; i++)
            edge[i] = new Edge();
        for (int i = 0; i < numberOfEdges; i++) {
            String[] edgeString = graphesString[i].split(" ");
            edge[i].x = Integer.parseInt(edgeString[0]);
            edge[i].y = Integer.parseInt(edgeString[1]);
            edge[i].w = Integer.parseInt(edgeString[2]);
        }
        numberOfVertexes = getNumberOfVertexes(edge);
        return edge;
    }


    int getNumberOfVertexes(Edge[] edges) {
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < edges.length; i++) {
            set.add(edges[i].x);
            set.add(edges[i].y);
        }
        return set.size();
    }


    void printGraph(int [] D){
        outputTextView.append("D = (");
        outputTextView.append("" + D[0] + ", ");
        for(int i = 1; i < D.length; i++) {
            if(D[i] == pow(10, 5) || D[i] == 0)
                outputTextView.append("∞");
            else
                outputTextView.append("" + D[i]);
            if(i!= D.length-1)
                outputTextView.append(", ");
        }
        outputTextView.append(")");
        outputTextView.append("\n");
    }


    class Edge {
        int x;
        int y;
        int w;
    }
}











