package motif;

import java.util.*;
import network.*;

public class MotifSpecification {
    
    private int size;           //number of nodes
    private Link[][] links;     //linkmap
    private List<Set<Integer>> symmetry;
    private List<Set<Integer>> connectedTo; //defines the parts which are symmetric to each other
    
    public MotifSpecification(Link[] spec){
        this.size = (int)Math.round((1+Math.sqrt(1+8*spec.length))/2);
        this.links = new Link[size][size];
        this.symmetry = new ArrayList<Set<Integer>>();
        this.connectedTo = new ArrayList<Set<Integer>>();
        int m = 0;
        this.symmetry.add(new HashSet<Integer>());
        this.connectedTo.add(new HashSet<Integer>());
        for(int i=1;i<size;i++){
            this.symmetry.add(new HashSet<Integer>());
            this.connectedTo.add(new HashSet<Integer>());
            for(int j=0;j<i;j++){
                Link l = spec[m++];
                if(l!=null){
                    links[i][j] = l;
                    links[j][i] = l.invert();
                }
            }
        }
        checkSwitchSymmetry();
    }
    
    public int size(){
        return size;
    }
    
    public Link getLink(int from, int to){
        return links[to][from];
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof MotifSpecification){
            MotifSpecification spec = (MotifSpecification) o;
            if(this.size != spec.size)
                return false;
            for(int i=0;i<size;i++)
                for(int j=0;j<size;j++){
                    if(i==j)
                        continue;
                    if((this.links[i][j]==null && this.links[j][i]!=null) ||
                         (this.links[j][i]==null && this.links[i][j]!=null) ||  
                         (this.links[i][j]!=null && this.links[j][i]!=null &&!this.links[i][j].equals(spec.links[i][j])))
                        return false;
                    //if(!this.links[i][j].equals(spec.links[i][j]))
                    //    return false;
                }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return size+links.hashCode();
    }
    
    @Override
    public String toString(){
        /*String s = "MotifSpecification: ";
        for(int i=0;i<size;i++){
            s += "\n\t";
            for(int j=0;j<size;j++)
                s += ("\t"+links[i][j]);
        }*/
        String s = "";
        for(int i=1;i<size;i++)
            for(int j=0;j<i;j++)
                s += links[i][j]!=null?links[i][j].getType():'0';
                
        return s;
    }
    
    private void checkSwitchSymmetry(){        
        for(int i=0;i<size;i++){
            loop: for(int j=0;j<i;j++){
                if(links[i][j]!=null){
                    if(!links[i][j].equals(links[j][i]))
                        continue;
                    for(int k=0;k<size;k++){
                        if(k==j||k==i)
                            continue;
                        if(links[j][k]==null || links[i][k]==null || !links[j][k].equals(links[i][k]))
                            continue loop;
                    }
                    symmetry.get(i).add(j);
                    symmetry.get(j).add(i);
                }
            }
        }
    }
    
    public boolean isSwitchSymmetric(int position){
        return !symmetry.get(position).isEmpty();
    }
    
    public boolean isSwitchSymmetric(int position, List<Integer> positions){
        if(!symmetry.get(position).isEmpty()){
            for(int i: positions)
                if(connectedTo.get(position).contains(i))
                    return false;
            return true;
        }
        return false;
    }
    
    public boolean isSwitchSymmetric(int i, int position){
        return symmetry.get(position).contains(i);
    }
    
    public int getSwitchSymmetricPosition(int position, Set<Integer> possibilities){
        for(int i: symmetry.get(position))
            if(possibilities.contains(i))
                return i;
        return -1;
    }
    
    public void addSymmetry(int[] from, int[] to){
        for(int i=0;i<from.length;i++)
            symmetry.get(from[i]).add(to[i]);
    }
    
    public void addSymmetry(int[] from, int[] to, List<int[]> subsets){
        for(int i=0;i<from.length;i++)
            symmetry.get(from[i]).add(to[i]);
        for(int[] subset: subsets){
            for(int i=0;i<subset.length;i++)
                for(int j=i+1;j<subset.length;j++){
                        connectedTo.get(subset[i]).add(subset[j]);
                        connectedTo.get(subset[j]).add(subset[i]);
                }
        }
    }
    
    public Link[] getLinksFrom(int i){
        Link[] result = new Link[links.length];
        for(int j=0;j<links[i].length;j++)
            result[j] = links[j][i];
        return result;
    }
    
}
