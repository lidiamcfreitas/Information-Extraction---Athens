package athens.tutorial2;

import athens.Page;
import athens.Parser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class implements a entity recognizer based on dictionary of 
 * entities.
 */
class EntityRecognizer {
	

	/**
	 * Given as arguments (1) a Wikipedia file and (2) a file with a list of entities, 
	 * this program reports mentions of entities in the content of articles. 
	 * An output record consists of:
	 * <ul>
	 * <li>The entity mentioned</li>
	 * <li>TAB (\t)
	 * <li>The title of the article where the mention occurs.</li>
	 * <li>NEWLINE (\n)
	 * </ul>
	 * The method should print one record per line.
	 */
	public static void main(String args[]) throws IOException {
            
        String filename = args[0];
        try {      
            File f = new File(filename);
            FileWriter fileWriter= new FileWriter("matches.text");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            Parser parser = new Parser(f);
            String title;
            String content;
            String strToAnalize;
            Page page;
            
            Trie trie = new Trie(new File("entities.txt"));
            
            while (parser.hasNext()){
                page = parser.next();
                title = page.title;
                content = page.content;
                //splitContent = page.content.split(" ");
                
                //for (int i = 0; i < content.length(); i++) {
                for (int i = content.length(); i >= 0; i--) {
                    int length = trie.containedLength(content, i);
                    if (length > 0 ){
                        bufferedWriter.write(content.substring(i, length + i) + '\t' + title +'\n');
                        //i += length;
                        i -= length;
                    }
                }
            }
        
            bufferedWriter.close();
        } catch(IOException e){
            System.out.println("An error has occurred");
        }
    }

}