/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utils.interfaces;

import beans.candidate.Candidates;
import beans.elector.Electors;

/**
 *
 * @author User
 */
public interface FileManager{
    
    //guarda a arraylist num ficheiro
    public void save(String nomeFicheiro) throws Exception;

    //le um ficheiro e passa os dados para a arraylist
    public void load(String nomeFicheiro) throws Exception;
    
     //le um ficheiro e passa os dados para a arraylist
    public void load(String nomeFicheiro, Candidates candidates, Electors electors) throws Exception;
}
