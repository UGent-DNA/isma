package datastructures;

import java.util.*;
import network.*;

public class SymmetrySets {
    
    private List<Set<Node>> sets;
    private CheckList<Integer> indexList;
    private List<Integer> markedPositions;
    
    public SymmetrySets(CheckList indexList){
        this.indexList = indexList;
        this.sets = new ArrayList<Set<Node>>(indexList.size());
        this.markedPositions = new ArrayList<Integer>(indexList.size());
    }
    
    public void add(Set<Node> set){
        while(sets.size()<indexList.currentIndex())
            sets.add(sets.size(), null);
        sets.add(indexList.currentIndex(), set);
        markedPositions.add(indexList.current());
    }
    
    public void update(Node n, int position){
        if(position<sets.size() && sets.get(position)!=null)
            sets.get(position).remove(n);
        while(!sets.isEmpty() && indexList.currentIndex()<sets.size()-1){
            if(sets.get(sets.size()-1)!=null)
                markedPositions.remove(markedPositions.size()-1);
            sets.remove(sets.size()-1);
        }
    }
    
    public Set<Node> get(int i){
        if(i<sets.size())
            return sets.get(i);
        return null;
    }
    
    public List<Integer> getMarkedPositions(){
        return markedPositions;
    }
    
    public String toString(){
        return sets.toString();
    }
}
