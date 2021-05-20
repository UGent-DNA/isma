package network;

import java.util.*;

public class Node {
    
    private String name;
    private String organism;
    private Map<Link, Set<Node>> neighbours;
    
    public Node(String name){
        this.name = name;
        this.organism = "";
        this.neighbours = new HashMap<Link, Set<Node>>();
    }
    public Node(String name, String organism){
        this.name = name;
        this.organism = organism;
        this.neighbours = new HashMap<Link, Set<Node>>();
    }
    
    public void addNeighbour(Node node, Link link){
        if(!neighbours.containsKey(link))
            neighbours.put(link, new HashSet<Node>());
        neighbours.get(link).add(node);
    }
    
    public Set<Node> getNeighbours(Link link){
        if(!neighbours.containsKey(link))
            return new HashSet<Node>();
        return neighbours.get(link);
    }
    
    public Map<Link, Set<Node>> getNBs(){
        return neighbours;
    }
    
    public String getName(){
        return name;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Node)
            return this.name.equals(((Node)o).name) && this.organism.equals(((Node)o).organism);
        return false;
    }
    
    @Override
    public int hashCode(){
        return (name+""+organism).hashCode();
    }
    
    public String toString(){
        return this.name+"_"+this.organism;
    }
}
