/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Step45;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nathan Vaty
 */
public class ListeBlocs {
    List<Integer> listeB;
    
    public ListeBlocs() {
        listeB = new ArrayList<>();
    }
    
    public List<Integer> getListe(){
        
        return listeB;
    }
    
    public void addNbBlocs(int nb){
        listeB.add(nb);
    }
    
    public void setNbBlocsIndice(int indice, int nb){
        listeB.set(indice, nb);
    }
    
}





