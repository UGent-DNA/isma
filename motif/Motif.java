package motif;

import java.util.Arrays;
import network.*;

public class Motif {
    
    private Node[] nodes;
    private MotifSpecification spec;
    
    public Motif(MotifSpecification spec){
        this.spec = spec;
        this.nodes = new Node[spec.size()];
    }
    public Motif(MotifSpecification spec, Node[] nodes){
        this.spec = spec;
        this.nodes = nodes;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Motif){
            Motif m = (Motif) o;
            if(spec.equals(m.spec)){
                for(int i=0;i<nodes.length;i++)
                    if(!nodes[i].equals(m.nodes[i]))
                        return false;
                return false;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return this.nodes.hashCode()+spec.hashCode();
    }
    
    public String toString(){
        return "Motif ["+spec+"]: "+Arrays.toString(nodes);
    }
    
}
