/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.beans.candidate;

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

/**
 *
 * @author User
 */
public class CandidateList implements FileManager, Serializable{

    //Attributes
    private static ArrayList<CandidateBean> candidateList;
    
    //Getters and Setters
    public static ArrayList<CandidateBean> getList(){
        return candidateList;
    }
    
    public static int getCandidateVotes(CandidateBean candidate){
        return candidate.getVotes();
    }
    
    public static void resetAllCandidateVotes(){
        for(CandidateBean candidate: candidateList){
            candidate.setVotesFixed(0);
        }
    }
    
    public static String getResultsGUILine(CandidateBean candidate){
         return String.format("%-15s %-50s %d Votos", candidate.getInitials(), candidate.getName(), getCandidateVotes(candidate));
    }
    
    //retorna a arraylist
    public static void setList(ArrayList<CandidateBean> newList){
        candidateList = newList;
    }
    
    //Object Operations
    public static String getGUIListLine(CandidateBean candidateBean) {
        return String.format("%-20s | %-10s ", candidateBean.getName(), candidateBean.getInitials());
    }

    public static void deleteCandidateFromList(int id) throws IOException, FileNotFoundException, ClassNotFoundException, ParseException {
        candidateList.remove(id);
    }

    public static int searchCandidateByName(String text) {

        for (CandidateBean candidate : candidateList) {
            if (candidate.getName().contains(text)) {
                return candidateList.indexOf(candidate);
            }
        }
        return 0;
    }

    public static int searchCandidateByInitials(String text) {

        for (CandidateBean candidate : candidateList) {
            if (candidate.getInitials().contains(text)) {
                return candidateList.indexOf(candidate);
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
            file.writeObject(candidateList);
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
                candidateList = (ArrayList<CandidateBean>) file.readObject();
            }
        } else {
            candidateList = new ArrayList();
            candidateList.add(new CandidateBean());
            save(nomeFicheiro);
        }
    }
}
