/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.beans.elector;

import electionspoo.utils.interfaces.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author User
 */
public class ElectorList implements FileManager, Serializable{

    //arraylist com os eleitores lidos do ficheiro + os que estão em memória
    private static ArrayList<ElectorBean> electorList;
    
    //retorna a arraylist
    public static ArrayList<ElectorBean> getList(){
        return electorList;
    }
    
    public static void resetElectorsVoted(){
        for(ElectorBean elector: electorList){
            elector.setVoted(false);
            elector.setVotedCandidate(null);
        }
    }
    
    //retorna a arraylist
    public static void setList(ArrayList<ElectorBean> newList){
        electorList = newList;
    }
    
    //Ordena a arraylist de eleitores por CC
    public static void orderArrayListByCC() {
        Collections.sort(electorList);
    }

    //formata eleitor para mostrar JList
    public static String getGUIListLine(ElectorBean electorBean) {
        
        String hasVoted = electorBean.isVoted()?"Já votou":"";
        
        return String.format("%08d | %c | %s | %-20s | %-5s", electorBean.getCC(), electorBean.getGender(), electorBean.getBirthDate(), electorBean.getName(), hasVoted);
    }

    //apaga um eleitor da arraylist dado o seu index
    public static void deleteElectorFromList(int id) throws IOException, FileNotFoundException, ClassNotFoundException, ParseException {
        electorList.remove(id);
    }

    //procura um eleitor pelo nome na arraylist
    public static int searchElectorByName(String text) {

        for (ElectorBean elector : electorList) {
            if (elector.getName().contains(text)) {
                return electorList.indexOf(elector);
            }
        }
        return 0;
    }

    //procura um eleitor pelo CC na arraylist
    public static int searchElectorByCC(String text) {

        for (ElectorBean elector : electorList) {
            if (String.valueOf(elector.getCC()).equals(text)) {
                return electorList.indexOf(elector);
            }
        }
        return 0;
    }


    /**
     *
     * @param nomeFicheiro
     * @throws Exception
     * 
     * Guarda a arraylist num ficheiro
     */
    @Override
    public void save(String nomeFicheiro) throws Exception {
        try (ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(nomeFicheiro))) {
            file.writeObject(electorList);
        }
    }

    /**
     *
     * @param nomeFicheiro
     * @throws Exception
     * 
     * Lê um ficheiro e passa os dados para a arraylist
     */
    @Override
    public void load(String nomeFicheiro) throws Exception {
        if (new File(nomeFicheiro).exists()) {
            try (ObjectInputStream file = new ObjectInputStream(new FileInputStream(nomeFicheiro))) {
                electorList = (ArrayList<ElectorBean>) file.readObject();
            }
        } else {
            electorList = new ArrayList();
            electorList.add(new ElectorBean());
            save(nomeFicheiro);
        }
    }
    
    

}
