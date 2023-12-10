/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans.candidate;

import beans.candidate.CandidateBean;
import beans.elector.ElectorBean;
import beans.elector.Electors;
import utils.interfaces.FileManager;
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author User
 */
public class Candidates implements FileManager{

    //Attributes
    private final List<CandidateBean> list;
    
    public Candidates() {
        list = new CopyOnWriteArrayList<>();
    }
    
    //Getters and Setters
    public List<CandidateBean> getList(){
        return list;
    }
    
    
    public boolean contains(CandidateBean cand) {
        return list.contains(cand);
    }

    public void addCandidate(CandidateBean newCand) {
        if (!list.contains(newCand)) {
            list.add(newCand);
        }
    }

    public void removeCandidates(List<CandidateBean> lst) {
        list.removeAll(lst);
    }

    public void synchronize(List<CandidateBean> candidates) {
        for (CandidateBean cand : candidates) {
            addCandidate(cand);
        }
    }

    public int size(){
        return this.list.size();
    }
    
    public void clear(){
        this.list.clear();
    }
    
    
    
    //Object Operations
    public String getGUIListLine(CandidateBean candidateBean) {
        return String.format("%-20s | %-10s ", candidateBean.getName(), candidateBean.getInitials());
    }
    
    public static String getResultsGUILine(CandidateBean candidate){
        return String.format("%-15s %-50s %d Votos", candidate.getInitials(), candidate.getName(), getCandidateVotes(candidate));
    }
    
    public static int getCandidateVotes(CandidateBean candidate){
        return candidate.getVotes();
    }
    
    public void deleteCandidateFromList(int id) throws IOException, FileNotFoundException, ClassNotFoundException, ParseException {
        this.list.remove(id);
    }
    
    public int searchCandidateByName(String text) {
   
        for (CandidateBean candidate : this.list) {
            if (candidate.getName().contains(text)) {
                return this.list.indexOf(candidate);
            }
        }
        return 0;
    }
   
    public int searchCandidateByInitials(String text) {

        for (CandidateBean candidate : this.list) {
            if (candidate.getInitials().contains(text)) {
                return this.list.indexOf(candidate);
            }
        }
        return 0;
    }
    
    public void resetAllCandidateVotes(){
        for(CandidateBean candidate: this.list){
            candidate.setVotesFixed(0);
        }
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
            file.writeObject(getList());
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
                List<CandidateBean> list = (List<CandidateBean>) file.readObject();
                for(CandidateBean bean : list){
                    addCandidate(bean);
                }
            }
        } else {
            this.list.clear();
            this.list.add(new CandidateBean());
            save(nomeFicheiro);
        }
    }

    @Override
    public void load(String nomeFicheiro, Candidates candidates, Electors electors) throws Exception {
       
    }
}
