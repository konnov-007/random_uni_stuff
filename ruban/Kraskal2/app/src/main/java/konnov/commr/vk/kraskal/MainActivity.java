package konnov.commr.vk.kraskal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    EditText inputEditText;
    TextView outputTextView;
    int numberOfEdges;
    int numberOfVertexes;
    int trudoemkost;
    ArrayList<Integer> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEditText = findViewById(R.id.inputEditText);
        outputTextView = findViewById(R.id.outputTextView);
    }

    public void buttonClicked(View view) {
        if(inputEditText.getText().toString().isEmpty())
            return;
        trudoemkost = 0;
        links = new ArrayList<>();
        kaskal();
    }


    Edge[] getGraph(){
        outputTextView.setText("");
        String[] graphesString = inputEditText.getText().toString().split("\n");
        numberOfEdges = graphesString.length;
        Edge[] edge = new Edge[numberOfEdges];
        for(int i = 0; i < numberOfEdges; i++)
            edge[i] = new Edge();
        for(int i = 0; i < numberOfEdges; i++){
            String[] edgeString = graphesString[i].split(" ");
            edge[i].x = Integer.parseInt(edgeString[0]);
            edge[i].y = Integer.parseInt(edgeString[1]);
            edge[i].w = Integer.parseInt(edgeString[2]);
        }
        numberOfVertexes = getNumberOfVertexes(edge);
        return edge;
    }

    void kaskal(){
        Edge [] edge = getGraph();
        for (int i = 0; i < numberOfEdges; i++) {
            edge[i].x--;
            edge[i].y--;
        }

        edge = sortEdgeByW(edge);
        outputTextView.append("Граф после сортировки по весу:\nx \t y \t w");
        for(int i = 0; i < numberOfEdges; i++) {
            outputTextView.append("\n" + (int) (edge[i].x + 1));
            outputTextView.append(" \t " + (int) (edge[i].y + 1));
            outputTextView.append(" \t " + edge[i].w);
        }
        for (int i = 0; i < numberOfVertexes; i++) {
            links.add(i);
        }
        ArrayList<Edge> result = new ArrayList<>();
        outputTextView.append("\n");
        for (int i = 0; i < numberOfEdges; i++) {
            int x = edge[i].x, y = edge[i].y;
            outputTextView.append("\n");
            if (union_vertex(x, y) == 1) {
                outputTextView.append("Берем ребро (" + (int) (x+1) + ", " + (int) (y+1) + ") с ценой " + edge[i].w);
                trudoemkost++;
                result.add(edge[i]);
            } else{
                if(i < numberOfEdges - 3)
                    outputTextView.append("Не берем ребро (" + (int) (x+1) + ", " + (int) (y+1) + ") с ценой " + edge[i].w);
            }
        }

        int sum_of_weights = 0;
        for (int i = 0; i < result.size(); i++) {
            sum_of_weights += result.get(i).w;
        }

        outputTextView.append("Оставное дерево:\n\n");
        for (int i = 0; i < result.size(); i++) {
            outputTextView.append(String.format("V1: %d |    V2: %d |    Вес: %d\n", (int) (result.get(i).x + 1), (int) (result.get(i).y + 1), result.get(i).w));
            outputTextView.append("----------------------------------------\n");
        }
        outputTextView.append(String.format("\nСумма весов оставного дерева: %d\n\n", sum_of_weights));
        outputTextView.append("Трудоемкость: " + trudoemkost + "\n");

    }

    private int union_vertex(int vertex_1, int vertex_2) {
        vertex_1 = get_links(vertex_1);
        vertex_2 = get_links(vertex_2);
        trudoemkost++;
        if (vertex_1 == vertex_2)
            return 0;
        if (vertex_2 > vertex_1) {
            int tempvalue = vertex_1;
            vertex_1 = vertex_2;
            vertex_2 = tempvalue;
        }
        trudoemkost++;
        links.set(vertex_1, vertex_2);
        return 1;
    }

    int get_links(int vertex) {
        trudoemkost++;
        if (vertex == links.get(vertex)) return vertex;
        return get_links(links.get(vertex));
    }


    int getNumberOfVertexes(Edge[]edges){
        Set<Integer> set = new HashSet<Integer>();
        for(int i = 0; i < edges.length; i++){
            set.add(edges[i].x);
            set.add(edges[i].y);
        }
        return set.size();
    }



    Edge[] sortEdgeByW(Edge[] edge){
        Edge [] sortedEdge = new Edge[numberOfEdges];
        for(int i = 0; i < numberOfEdges; i++)
            sortedEdge[i] = new Edge();
        int [] weightArray = new int[numberOfEdges];
        for(int i = 0; i < numberOfEdges; i++)
            weightArray[i] = edge[i].w;
        Arrays.sort(weightArray);

        for(int i = 0; i < numberOfEdges; i++){
            for(int j = 0; j < numberOfEdges; j++){
                if(weightArray[i] == edge[j].w){
                    sortedEdge[i].w = edge[j].w;
                    sortedEdge[i].x = edge[j].x;
                    sortedEdge[i].y = edge[j].y;
                }
            }
        }

        return sortedEdge;
    }


    class Edge{
        int x;
        int y;
        int w;
    }
}


