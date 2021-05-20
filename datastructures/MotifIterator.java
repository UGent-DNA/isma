package datastructures;

import java.util.*;

public class MotifIterator<E> {
    
    private Iterator<E>[] iterators;
    private CheckList<Integer> indexList;
    
    public MotifIterator(CheckList indexList){
        this.indexList = indexList;
        iterators = new Iterator[indexList.size()];
    }
    
    public void add(Iterator<E> iterator, int index){
        iterators[index] = iterator;
    }
    
    public boolean hasNext(){
        //if set empty, check previous set..
        while(indexList.currentIndex()>0){
            if(iterators[indexList.currentIndex()].hasNext())
                return true;
            indexList.undo();
        }
        return iterators[indexList.currentIndex()].hasNext();
    }
    
    public E next(){
        return iterators[indexList.currentIndex()].next();
    }
    
    public void remove(){
        iterators[indexList.currentIndex()].remove();
    }
    
    public String toString(){
        return Arrays.toString(iterators);
    }
    
}
