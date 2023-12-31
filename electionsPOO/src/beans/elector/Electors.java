/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans.elector;

import beans.candidate.Candidates;
import utils.interfaces.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author tiago
 */
public class Electors implements FileManager {
 
    //Attributes
    private List<ElectorBean> list;
    
    public Electors() {
        list = new CopyOnWriteArrayList<>();
    }
    
    //Getters and Setters
    public List<ElectorBean> getList(){
        return list;
    }
    
    public void setList(List<ElectorBean> listP){
        list = listP;
    }
    
    public boolean contains(ElectorBean elec) {
        return list.contains(elec);
    }

    public void addElector(ElectorBean newElec) {
        if (!list.contains(newElec)) {
            list.add(newElec);
        }
    }

    public void removeElectors(List<ElectorBean> lst) {
        list.removeAll(lst);
    }

    public void synchronize(List<ElectorBean> electors) {
        for (ElectorBean elec : electors) {
            addElector(elec);
        }
    }

    public int size(){
        return this.list.size();
    }
    
    public void clear(){
        this.list.clear();
    }
    
    public void resetElectorsVoted(){
        for(ElectorBean elector: this.list){
            elector.setVoted(false);
            elector.setVotedCandidate(null);
        }
    }
    
    //Ordena a arraylist de eleitores por CC
    public void orderArrayListByCC() {
        Collections.sort(list);
    }

    //formata eleitor para mostrar JList
    public String getGUIListLine(ElectorBean electorBean) {
        return String.format("%08d | %c | %s | %-20s", electorBean.getCC(), electorBean.getGender(), electorBean.getBirthDate(), electorBean.getName());
    }

    //apaga um eleitor da arraylist dado o seu index
    public void deleteElectorFromList(int id) throws IOException, FileNotFoundException, ClassNotFoundException, ParseException {
        this.list.remove(id);
    }

    //procura um eleitor pelo nome na arraylist
    public int searchElectorByName(String text) {

        for (ElectorBean elector : this.list) {
            if (elector.getName().contains(text)) {
                return this.list.indexOf(elector);
            }
        }
        return 0;
    }

    //procura um eleitor pelo CC na arraylist
    public int searchElectorByCC(String text) {

        for (ElectorBean elector : this.list) {
            if (String.valueOf(elector.getCC()).contains(text)) {
                return this.list.indexOf(elector);
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
            file.writeObject(this.list);
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
                List<ElectorBean> electors = (ArrayList<ElectorBean>) file.readObject();
                for(ElectorBean bean : electors){
                    addElector(bean);
                }
            }
        } else {
            this.list.clear();
            this.list.add(new ElectorBean());
            save(nomeFicheiro);
        }
    }

    @Override
    public void load(String nomeFicheiro, Candidates candidates, Electors electors) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
