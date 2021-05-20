/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package motiffinder;

import java.io.*;
import java.util.*;
import org.apache.commons.cli.*;
import network.*;
import motif.*;
import algorithms.*;

public class CommandLineInterface {
    
    public static void main(String[] args) throws IOException{
		
        String folder = null, files = null, motif = null, output = null;
        
        Options opts = new Options();
        opts.addOption("folder", true, "Folder name");
        opts.addOption("linkfiles", true, "Link files seperated by spaces (format: linktype[char] directed[d/u] filename)");
        opts.addOption("motif", true, "Motif description by two strings (format: linktypes)");
        opts.addOption("output", true, "Output file name");

        CommandLineParser parser = new PosixParser();
        try{
            CommandLine cmd = parser.parse(opts, args);
            if(cmd.hasOption("folder"))
                folder = cmd.getOptionValue("folder");
            if(cmd.hasOption("linkfiles"))
                files = cmd.getOptionValue("linkfiles");
            if(cmd.hasOption("motif"))
                motif = cmd.getOptionValue("motif");
            if(cmd.hasOption("output"))
                output = cmd.getOptionValue("output");
        }catch(ParseException e){
            Die("Error: Parsing error");
        }

        printBanner();
        System.out.println("--------------------------------------------------------------\n"
                + "folder\t\tFolder name\n"
                + "linkfiles\tLink files seperated by spaces \n\t\t(format: linktype[char] directed[d/u] filename)\n"
                + "motif\t\tMotif description by two strings \n\t\t(format: linktypes directed)\n"
                + "output\t\tOutput file name\n"
                + "--------------------------------------------------------------\n"
                + "folder\t\t"+folder+"\n"
                + "linkfiles\t"+files+"\n"
                + "motif\t\t"+motif+"\n"
                + "output\t\t"+output+"\n");
        
        if(folder==null || files==null || motif==null || output==null){
            Die("Error: not all options are provided");
        }else{
            ArrayList<String> linkfiles = new ArrayList<String>();
            ArrayList<Character> linkTypes = new ArrayList<Character>();
            ArrayList<Boolean> directed = new ArrayList<Boolean>();
            StringTokenizer st = new StringTokenizer(files, " ");
            while(st.hasMoreTokens()){
                linkTypes.add(st.nextToken().charAt(0));
                directed.add(st.nextToken().equals("d"));
                linkfiles.add(folder+st.nextToken());
            }
            System.out.println("Reading network..");
            Network network = readFromFiles(linkfiles, linkTypes, directed);
            System.out.println("Network: "+network);
            
            st = new StringTokenizer(motif, " ");
            String linktypes = st.nextToken();
            String ldirected = st.nextToken();
            Link[] motifspec = new Link[linktypes.length()];
            for(int i=0;i<linktypes.length();i++)
                motifspec[i] =  linktypes.charAt(i)=='0'?null:new Link(linktypes.charAt(i), directed.get(linkTypes.indexOf(linktypes.charAt(i))));
            MotifSpecification mspec = new MotifSpecification(motifspec);
             
            System.out.println("Starting the search..");
            long start = System.nanoTime();
            Set<Motif> motifs = null;
            motifs = new MotifSearcher(mspec, network).searchMotifs();

            System.out.println("Time: "+(System.nanoTime()-start)/1000000);
            
            printMotifs(motifs, output);
        }  

}
	
	public static void Die (String msg) {
		System.out.println(msg);
		System.exit(1);
	}
	
	public static void printBanner () {
		System.out.println("");
		System.out.println("Sub Graph Matching Algorithm for Motifs in Biological Networks");
		System.out.println("--------------------------------------------------------------");
		System.out.println("Version 1.0");
		System.out.println("Copyright (c) 2011-2012 Sofie Demeyer");
		System.out.println("");
	}
   
        private static Network readFromFiles(ArrayList<String> files, ArrayList<Character> linkTypes, ArrayList<Boolean> directed) throws IOException{
        Network network = new Network();
        for(int i=0;i<files.size();i++){
            System.out.println("Reading: "+files.get(i)+"\t"+linkTypes.get(i)+"\t"+directed.get(i));
            BufferedReader in = new BufferedReader(new FileReader(files.get(i)));
            
            String line = in.readLine();
            while(line!=null){
                int t = line.indexOf('\t');
                if(t<=0){
                    line = in.readLine();
                    continue;
                }
                String n1 = line.substring(0, t);
                String n2 = line.substring(t+1);
                network.addLink(new Node(n1, "1"), new Node(n2, "1"), new Link(linkTypes.get(i), directed.get(i)));
                line = in.readLine();
            }
        }
        return network;
    }

        private static void printMotifs(Set<Motif> motifs, String output) throws IOException{
            PrintWriter out = new PrintWriter(new File(output));
            out.println(motifs.size()+" motifs found");
            for(Motif m: motifs)
                out.println(m);
            out.close();
        }

}

