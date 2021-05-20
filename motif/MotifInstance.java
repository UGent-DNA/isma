package motif;

import java.util.*;
import network.*;

public class MotifInstance {
    
    private Node[] nodes;
    private MotifSpecification spec;
    
    public MotifInstance(MotifSpecification spec){
        this.spec = spec;
        this.nodes = new Node[spec.size()];
    }
    
    public void addNode(Node node, int position){
        nodes[position] = node;
    }
    
    public Node getNode(int i){
        return nodes[i];
    }
    
    public boolean contains(Node node, List<Integer> indices){
        for(int i: indices)
            if(node.equals(nodes[i]))
                return true;
        return false;
    }
    public boolean contains(Node node){
        for(int i=0;i<nodes.length;i++)
            if(node.equals(nodes[i]))
                return true;
        return false;
    }
    public boolean containsMinusLast(Node node, List<Integer> indices){
        for(int i=0;i<indices.size()-1;i++)
            if(node.equals(nodes[indices.get(i)]))
                return true;
        return false;
    }
    
    public boolean containsMinusLast(Node node, int index){
        for(int i=0;i<index;i++)
            if(node.equals(nodes[i]))
                return true;
        return false;
    }
    
    public Motif getMotif(){
        return new Motif(spec, nodes.clone());
    }
    
    public String toString(){
        return "Constructor: "+Arrays.toString(nodes);
    }
    
}
