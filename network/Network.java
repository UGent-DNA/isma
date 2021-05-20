package network;

import java.util.*;

public class Network {
    
    private List<Node> nodes;               //to add neighbours to correct node
    private Map<Link, Set<Node>> nodeMap;   //all nodes from which this kind of link leaves
    
    public Network(){
        this.nodes = new ArrayList<Node>();
        this.nodeMap = new HashMap<Link, Set<Node>>();
    }
    
    public void addLink(Node from, Node to, Link link){
        
        Node origin = from, destination = to;
        int f = nodes.indexOf(from), t = nodes.indexOf(to);
        if(f==-1)
            nodes.add(from);
        else
            origin = nodes.get(f);
        if(t==-1)
            nodes.add(to);
        else
            destination = nodes.get(t);
        
        if(!nodeMap.containsKey(link))
            nodeMap.put(link, new HashSet<Node>());
        nodeMap.get(link).add(origin);
        origin.addNeighbour(destination, link);
        Link inverse = link.invert();
        if(!nodeMap.containsKey(inverse))
            nodeMap.put(inverse, new HashSet<Node>());
        nodeMap.get(inverse).add(destination);
        destination.addNeighbour(origin, inverse);
    }
    
    public Set<Node> getNodes(Link link){
        if(!nodeMap.containsKey(link))
            return new HashSet<Node>();
        return nodeMap.get(link);
    }
    
    /*public List<Node> getAllNodes(){
        return nodes;
    }*/
    
    public Set<Node> getAllNodes(){
        return new HashSet<Node>(nodes);
    }
    
    public double calculateConnectivity(Link link1, Link link2){
        if(link1==null || link2==null)
            return Double.MAX_VALUE;
        double result = 0.;
        Set<Node> node = nodeMap.get(link1);
        for(Node n: node)
            result += n.getNeighbours(link2).size();
        result /= node.size();
        return result;
    }
    
    public String toString(){
        String s = "Network with "+nodeMap.keySet().size()+" types of links: ";
        for(Link link: nodeMap.keySet())
            s += ("\n\t"+link+" has "+nodeMap.get(link).size()+" nodes");
        s+= "\n"+nodes.size()+" nodes in total";
        return s;
    }
}
