/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package athens.tutorial2;

import java.util.HashMap;

/**
 *
 * @author Turing
 */
public class Node {
    private char _c;
    protected boolean _final;
    protected HashMap<Character, Node> _children = new HashMap<Character, Node>();
    
    public Node(){
        this._final = false;
    }
    
    public Node(char c){
        this._final = false;
        this._c = c;
    }
}
