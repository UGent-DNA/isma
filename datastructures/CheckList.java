package datastructures;

import java.util.*;

public class CheckList<E> implements Iterable<E> {
    
    private Set<E> elements;
    private List<E> order;
    
    public CheckList(Set<E> elements){
        this.elements = elements;
        this.order = new ArrayList<E>();
    }
    
    public static CheckList newIndexList(int n){
        Set<Integer> set = new HashSet<Integer>();
        for(int i=0;i<n;i++)
            set.add(i);
        return new CheckList(set);
    }
    
    @Override
    public Iterator<E> iterator(){
        return elements.iterator();
    }
    
    public void next(E element){
        if(elements.remove(element))
            order.add(element);
    }
    
    public void undo(){
        elements.add(order.remove(currentIndex()));
    }
    
    public Set<E> getElements(){
        return elements;
    }
    
    public int currentIndex(){
        return order.size()-1;
    }
    
    public E current(){
        return order.get(currentIndex());
    }
    
    public boolean isFixed(E element){
        return order.contains(element);
    }
    
    public int size(){
        return elements.size()+order.size();
    }
    
    public List<E> getOrder(){
        return order;
    }
    
    public int getIndexOf(int i){
        return order.indexOf(i);
    }
    
    public String toString(){
        return order+" + "+elements;
    }
    
}
