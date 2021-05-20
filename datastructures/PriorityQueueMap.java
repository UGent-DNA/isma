package datastructures;

import java.util.*;
import network.*;

public class PriorityQueueMap {
    
    private Map<Integer, PriorityQueue<PriorityObject>> map;
    private final Comparator<PriorityObject> comparator = new Comparator<PriorityObject>(){
        @Override
            public int compare(PriorityObject left, PriorityObject right){
                return left.compareTo(right);
            }
    };
    
    public PriorityQueueMap(int n){
        map = new HashMap<Integer, PriorityQueue<PriorityObject>>();
        for(int i=0;i<n;i++)
            map.put(i, new PriorityQueue<PriorityObject>(10,comparator));
    }
    
    public void add(Node node, int from, int to, int nbs){
        map.get(to).add(new PriorityObject(node, from, to, nbs));
    }
    
    public void add(PriorityObject ro){
        map.get(ro.getTo()).add(ro);
    }
    
    public PriorityObject poll(Set<Integer> indices){
        PriorityObject result = null;
        int minScore = Integer.MAX_VALUE, index = -1;
        for(int i: indices){
            if(!map.get(i).isEmpty()){
                int score = map.get(i).peek().getScore();
                if(score<minScore){
                    minScore = score;
                    index = i;
                }
            }
        }
        if(map.get(index)==null)
            return null;
        return map.get(index).poll();
    }
    
    public String toString(){
        return ""+map;
    }
    
    public void clear(){
        for(int i: map.keySet())
            map.get(i).clear();
    }
    
    public void remove(PriorityObject ro){
        map.get(ro.getTo()).remove(ro);
    }
    
}

