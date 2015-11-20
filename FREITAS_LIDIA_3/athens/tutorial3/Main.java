/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package athens.tutorial3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Turing
 */
public class Main {
    public static void main(String args[]) throws IOException { 
        
        //new DateExtractor().run(new File(args[0]));
        new TypeExtractor().run(new File(args[0]));
    }
}
