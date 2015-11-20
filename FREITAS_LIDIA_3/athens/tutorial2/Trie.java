package athens.tutorial2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * It defines the methods that define a Trie data structure.
 * @author Luis GalÃ¡rraga.
 *
 */
public class Trie {
        
    private Node _root;
	/**
	 * Adds a string to the trie.
         * @param str
	 * @return true if the trie changed as a result of this operation, that is if
	 * the new string was not in the dictionary.
	 */
	public boolean add(String str) {
            // get root children information
            HashMap<Character, Node> child = _root._children;
            boolean changed = false;
            
            Node successor;
            for(int i=0; i < str.length(); i++){
                char c = str.charAt(i);
                
                //if a path already exists with this sentence so far
                if (child.containsKey(c)){
                    // move down in the path
                    successor = child.get(c);
                } else {
                    // create a new path/branch and move down
                    changed = true;
                    successor = new Node(c);
                    child.put(c, successor);
                }
                // get current node info
                child = successor._children;
 
                if(i==str.length()-1){
                    if (!successor._final){
                        successor._final = true;
                        changed = true;
                    }
                }
            }  
            return changed;
	}

	/**
	 * Checks whether a string exists in the trie.
	 * @param str
	 * @return true if the string is in the trie, false otherwise.
	 */
	public boolean contains(String str) {
            // get root children information
            HashMap<Character, Node> child = _root._children;
            Node successor;
            
            for(int i=0; i < str.length(); i++){
                char c = str.charAt(i);
                
                if (child.containsKey(c)){
                    // move down in the path
                    successor = child.get(c);
                } else {
                    return false;
                }
                // get current node info
                child = successor._children;
 
                if(i==str.length()-1){
                    if (successor._final)
                        return true;
                    else
                        return false;
                }
            }  
            return true;
	}

	/**
	 * Given a string and a starting position (<var>startPos</var>), it returns the length
	 * of the longest word in the trie that starts in the string at the given position.
	 * For example,
	 * if the trie contains words "New York", and "New York City", containedLength("I live in New York City!", 10)
	 * returns 13, that is the length of the longest word ("New York City") registered in the 
         * trie that starts at position 10.
	 * Hint: proceed as in the lecture
	 * @param s
	 * @param startPos
	 * @return int
	 */
	public int containedLength(String s, int startPos) {
            // get root children information
            HashMap<Character, Node> child = _root._children;
            int longestLength = 0;
            int currentLength = 0;
            String str = s.substring(startPos);
            Node successor;
            
            for(int i=0; i < str.length(); i++){
                char c = str.charAt(i);
                
                if (child.containsKey(c)){
                    // move down in the path
                    successor = child.get(c);
                    currentLength++;
                } else {
                    return longestLength;
                }
                // get current node info
                child = successor._children;
                
                if (successor._final){
                    longestLength = currentLength;
                }
            }  
            return longestLength;
	}

	/** Constructs a Trie from the lines of a file
        * @param file
        * @throws java.io.IOException
        */
	public Trie(File file) throws IOException {
            this._root = new Node();
		try(BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"))) {
			String line;
			while((line=in.readLine())!=null) {
				add(line);
			}
		}
	}

	/** Constructs an empty Trie*/
	public Trie() {
            this._root = new Node();
	}

	/** Constructs a Trie from a collection*/
	public Trie(Iterable<String> collection) {
		for(String s : collection) add(s);
	}

	/** Use this to test your implementation. Provide the file with list of Wikipedia titles as argument to this program.*/
	public static void main(String[] args) throws IOException {
		//Trie trie = new Trie(new File(args[0]));
                
                Trie trie = new Trie();
                trie.add("New York");
                trie.add("New York City");
                
                System.out.println(trie.containedLength("I live in New York City!", 10));
                
		for(String test : Arrays.asList("Brooklyn","Dudweiler","Elvis Presley","Juan Pihuave")) {
			System.out.println(test + " is in trie: " + trie.contains(test));
		}

	}
}