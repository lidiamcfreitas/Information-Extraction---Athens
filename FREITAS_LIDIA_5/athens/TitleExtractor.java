package athens;

import java.io.*;

public class TitleExtractor {
    public static void main(String[] args) throws IOException {
        String filename = args[0];
        try {      
        File f = new File(filename);
        FileWriter fileWriter= new FileWriter("titles.text");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        Parser parser = new Parser(f);
        
        Page page = null;

        	while (parser.hasNext()){
        		page = parser.next();
        		bufferedWriter.write(page.title+'\n');
        	}
        
        	bufferedWriter.close();
        } catch(IOException e){
        	System.out.println("An error has occurred");
        }
    }	

}
