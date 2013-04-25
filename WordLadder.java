import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Iterator;

public class WordLadder{
	
	private int V;
	private int E;
	
	private HashMap<String, Integer> integerMap;
	
	private HashMap<String, Bag<Integer>> adjacentMap;
	private String[] strings;
	
	private Digraph digraph;
	
	public WordLadder(In in)
	{
		strings = in.readAll().split("\n");							//We accumulate a string array containing each seperate line in the file.
		
		V = strings.length;
	
		digraph = new Digraph(V);
		
		integerMap = new HashMap<String, Integer>();
		adjacentMap = new HashMap<String, Bag<Integer>>();
			
		//We iterate through our array of strings
		for (int v = 0; v < V; v++) 
		{
			integerMap.put(strings[v], v);							//We assign an index to each of our strings
			
			String subString = sort(strings[v].substring(1,5));		//We make a substring of the last 4 letters of the string, and sort them
			
			if(adjacentMap.get(subString) == null)					//If the key of the substring doesnt exist we initialize it
				adjacentMap.put(subString, new Bag<Integer>());
			
			adjacentMap.get(subString).add( v );					//We add the current index to our bag of indexes associated with this substring
		}
		
		for(int i=0; i<V; i++)
		{
			computeConnections(strings[i]);							//We iterate through our strings and make the connections they require
		}
	}
	
	//Method used to compute all the connections of a given string
	public void computeConnections(String string)
	{
		int thisIndex = integerMap.get(string);						//We get the index that is associated with the current string
		
		//We iterate through the strings letters
		for(int i=0; i<string.length(); i++)
		{
			String subString = sort( string.substring( 0, i ) + string.substring( i + 1, string.length() ) );		//We find every possible combination of sorted letters in the word
			
			if(adjacentMap.containsKey(subString))																	//If the substring exists in our map of substring keys..
			{
				Iterator<Integer> iterator = adjacentMap.get(subString).iterator();									//We iterate through it's bag..
				
				while(iterator.hasNext())
					digraph.addEdge(iterator.next(), thisIndex);													//And connect all of the vertices
			}
		}
	}

	public HashMap<String, Integer> getIntegerMap()
	{
		return integerMap;
	}
	
	public Digraph getDigraph()
	{
		return digraph;
	}
	
	//Method used to sort a string
	private static String sort( String str )
	{
		char[] stringArray = str.toCharArray();			//We convert the string to an array of chars
		Arrays.sort( stringArray );						//We sort the array of chars
		return( new String(stringArray) );				//We return the sorted char array as a string array
	}
	
	public static void main(String[] args)
	{
		Stopwatch timer = new Stopwatch();											//We begin our stopwatch
	
		In in = new In(args[0]);													
        
		WordLadder wordLadder = new WordLadder(in);									//We initialize our WordLadder with the first input.
		
		StdOut.println("Creating graph took = " + timer.elapsedTime());					//We print out the total amount of time the processing took
		timer = new Stopwatch();														//We restart our stopwatch
	
		in = new In(args[1]);
		
		String[] lineStrings = in.readAll().split("\n");							//We accumulate a string array containing each seperate line in the file.
		
		//We iterate through each of the pair of words that needs comparing
		for(int i=0; i<lineStrings.length; i++)
		{
			//We split the line into 2 string for processing
			String string1 = lineStrings[i].split(" ")[0];
			String string2 = lineStrings[i].split(" ")[1];
		
			//We initialize our BFS with our wordladder's digraph aswell as the string we need to find
			BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(wordLadder.getDigraph(), (int)wordLadder.getIntegerMap().get(string1));
			
			//We output the distance to the other word
			StdOut.println(bfs.distTo(wordLadder.getIntegerMap().get(string2)));
		}
		
		StdOut.println("BSF took = " + timer.elapsedTime());					//We output the time it took four our BFS to process our data
		
	}
}