package datastructures;

import network.*;

public class PriorityObject implements Comparable{
    
    private Node start;
    private int fromPosition;
    private int toPosition;
    private int numberOfNeighbours;
    
    public PriorityObject(Node start, int fromPosition, int toPosition, int numberOfNeighbours){
        this.start = start;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.numberOfNeighbours = numberOfNeighbours;
    }
    
    public Node getNode(){
        return start;
    }
    public int getFrom(){
        return fromPosition;
    }
    public int getTo(){
        return toPosition;
    }
    public int getScore(){
        return numberOfNeighbours;
    }
    
    @Override
    public int compareTo(Object o){
        if(o instanceof PriorityObject){
            return this.numberOfNeighbours-((PriorityObject)o).numberOfNeighbours;
        }
        return -1;
    } 
    
    public String toString(){
        return "<"+start+","+fromPosition+","+toPosition+","+numberOfNeighbours+">";
    }
    
}

