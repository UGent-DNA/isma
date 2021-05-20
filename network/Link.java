package network;

public class Link {
    
    private char type;
    private boolean directed;
    
    public Link(char type, boolean directed){
        this.type = type;
        this.directed = directed;
    }
    
    public char getType(){
        return type;
    }
    
    public Link invert(){
        if(!directed)
            return this;
        if(Character.isUpperCase(type))
            return new Link(Character.toLowerCase(type), directed);
        return new Link(Character.toUpperCase(type), directed);
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Link)
            return this.directed==((Link)o).directed && this.type==((Link)o).type;
        return false;
    }
    
    @Override
    public int hashCode(){
        return (int) type;
    }
    
    @Override
    public String toString(){
        return type+"_"+(directed?"D":"UD");
    }
    
}
