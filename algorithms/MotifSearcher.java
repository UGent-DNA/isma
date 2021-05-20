package algorithms;

import motif.*;
import network.*;
import datastructures.*;

import java.util.*;

public class MotifSearcher {
    
    private MotifSpecification spec;
    private Network network;
    private CheckList<Integer> indexList;
    private MotifIterator<Node> indexIterator;
    private MotifInstance constructor;
    private PriorityQueueMap PQmap;
    
    private SymmetrySets symmetrySets;
    
    public MotifSearcher(MotifSpecification spec, Network network){
        this.spec = spec;
        this.network = network;
        this.indexList = CheckList.newIndexList(spec.size());
        this.indexIterator = new MotifIterator<Node>(indexList);
        this.constructor = new MotifInstance(spec);
        this.PQmap = new PriorityQueueMap(spec.size());
        
        this.symmetrySets = new SymmetrySets(indexList);
        
        initialisation();
    }
    
    private void initialisation(){
        int minNumber = Integer.MAX_VALUE;
        //ArrayList<Integer> startIndex = new ArrayList<Integer>();
        int startIndex = 0;
        Link startLink = null;
        for(int i=0;i<spec.size();i++){
            for(int j=0;j<spec.size();j++){
                if(i==j)
                    continue;
                Link link = spec.getLink(i, j);
                if(link!=null){
                    int numberOfNodes = network.getNodes(link).size();
                    //System.out.println("test: "+link+" - "+numberOfNodes);
                    if(numberOfNodes<minNumber){
                        minNumber = numberOfNodes;
                        //startIndex.clear();
                        //startIndex.add(i);
                        startIndex = i;
                        startLink = link;
                    }/*else if(numberOfNodes == minNumber){
                        startIndex.add(i);
                    }*/
                }
            }
        }
        
        indexList.next(startIndex);
        Set<Node> startset = network.getNodes(startLink);
        
        //System.out.println("Startset: "+startset);

        if(spec.isSwitchSymmetric(startIndex)){
            int toPosition = spec.getSwitchSymmetricPosition(startIndex, indexList.getElements());
            if(toPosition != -1)
                symmetrySets.add(new HashSet<Node>(startset));
        }

        indexIterator.add(startset.iterator(), 0);
        
        /*if(startIndex.size()==1){
            indexList.next(startIndex.get(0));
            Set<Node> startset = network.getNodes(startLink);
             
            if(spec.isSwitchSymmetric(startIndex.get(0))){
                int toPosition = spec.getSwitchSymmetricPosition(startIndex.get(0), indexList.getElements());
                if(toPosition != -1)
                    symmetrySets.add(new HashSet<Node>(startset));
            }

            indexIterator.add(startset.iterator(), 0);
        }else{
            //calculate intersection for each start node
            int minN = Integer.MAX_VALUE;
            int startI = 0;
            Set<Node> startset = null;
            for(int i=0;i<startIndex.size();i++){
                List<Set<Node>> sets = new ArrayList<Set<Node>>();
                for(int j=0;j<spec.size();j++){
                    if(startIndex.get(i) ==j)
                        continue;
                    Set<Node> set = network.getNodes(spec.getLink(startIndex.get(i), j));
                    sets.add(set);
                }
                Set<Node> intersect = intersect(sets);
                if(intersect.size()<minN){
                    minN = intersect.size();
                    startI = i;
                    startset = intersect;
                }
            }
            indexList.next(startI);
            
            if(spec.isSwitchSymmetric(startI)){
                int toPosition = spec.getSwitchSymmetricPosition(startI, indexList.getElements());
                if(toPosition != -1)
                    symmetrySets.add(new HashSet<Node>(startset));
            }

            indexIterator.add(startset.iterator(), 0);
        }*/
        
    }
    
    public Set<Motif> searchMotifs(){
        int treeCounter = 0;
        Set<Motif> result = new HashSet<Motif>();

        while(indexIterator.hasNext()){
            Node n = indexIterator.next();
            treeCounter++;
            symmetrySets.update(n, indexList.currentIndex());
            if(!constructor.containsMinusLast(n, indexList.getOrder())){
                constructor.addNode(n, indexList.current());
                //for all other position, determine number of neighbours and add to PQM
                if(indexList.currentIndex()==0)
                    PQmap.clear();
                for(int i: indexList){
                    Link link = spec.getLink(indexList.current(), i);
                    if(link!=null)
                        PQmap.add(n, indexList.current(), i, n.getNeighbours(link).size());
                }
                
                //System.out.println(PQmap);
                PriorityObject best = PQmap.poll(indexList.getElements());

                if(best!=null){
                //Add new iterator to indexIterator
                Set<Node> set = calculateNewSet(best.getTo());
                indexList.next(best.getTo());
                
                if(spec.isSwitchSymmetric(best.getTo(), symmetrySets.getMarkedPositions())){
                    int toPosition = spec.getSwitchSymmetricPosition(best.getTo(), indexList.getElements());
                    if(toPosition!=-1)
                        symmetrySets.add(new HashSet<Node>(set));
                }
                
                indexIterator.add(set.iterator(), indexList.currentIndex());
                }

                //when last node, just add...
                while(indexIterator.hasNext() && indexList.currentIndex()==spec.size()-1){
                    Node node = indexIterator.next();
                    treeCounter++;
                    if(!constructor.containsMinusLast(node, indexList.getOrder())){
                        constructor.addNode(node, indexList.current());
                        result.add(constructor.getMotif());
                    }
                }
                
            }
        }
            
        System.out.println("Number of nodes: "+treeCounter);
        return result;
    }
    
    private Set<Node> calculateStartSet(int position){
        List<Set<Node>> setlist = new ArrayList<Set<Node>>();
        List<Integer> order = indexList.getOrder();
        for(int i: order){
            Link link = spec.getLink(position, i);
            if(link!=null){
                Set<Node> set = network.getNodes(link);
                if(setlist.isEmpty() || set.size()>setlist.get(0).size())
                    setlist.add(set);
                else
                    setlist.add(0, set);
            }
        }
        return intersect(setlist);
    }
    
    private Set<Node> calculateNewSet(int position){
        List<Set<Node>> setlist = new ArrayList<Set<Node>>();
        List<Integer> order = indexList.getOrder();
        for(int i: order){
            Link link = spec.getLink(i, position);
            if(link!=null){
                Set<Node> set = constructor.getNode(i).getNeighbours(link);
                if(setlist.isEmpty() || set.size()>setlist.get(0).size())
                    setlist.add(set);
                else
                    setlist.add(0, set);
                if(spec.isSwitchSymmetric(i, position)){
                    Set<Node> s = symmetrySets.get(indexList.getIndexOf(i));
                    if(s!=null)
                        setlist.add(s);
                }
            }
        }
        return intersect(setlist);
    }
    
    private Set<Node> intersect(List<Set<Node>> sets){
        Set<Node> result = new HashSet<Node>();
        
        if(!sets.isEmpty()){
            for(Node node: sets.get(0)){
                int i = 0;
                for(i=1;i<sets.size();i++)
                    if(!sets.get(i).contains(node))
                        break;
                if(i == sets.size())
                    result.add(node);
            }
        }
        
        return result;
    }
    
}
