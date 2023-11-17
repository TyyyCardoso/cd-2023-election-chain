/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.beans.election;

import electionspoo.beans.candidate.CandidateBean;
import electionspoo.beans.candidate.CandidateList;
import electionspoo.beans.elector.ElectorBean;
import electionspoo.beans.elector.ElectorList;
import electionspoo.beans.transaction.Transaction;
import electionspoo.utils.MainUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class ElectionBean implements Serializable {
    
    private static final long serialVersionUID = 7753064471717099599L; 
    
    private String name;
    private ArrayList<ElectorBean> electorList;
    private ArrayList<CandidateBean> candidateList;
    private ArrayList<Transaction> transactionList;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean started;

    
    public ElectionBean(){
        this.name = "Eleicao Basica";
        this.electorList = ElectorList.getList();
        this.candidateList = CandidateList.getList();
        this.startDate = LocalDate.parse("01/01/2022", MainUtils.formatter);
        this.endDate = LocalDate.parse("01/05/2022", MainUtils.formatter);
        this.started = false;
    }
    
    public ElectionBean(String name, ArrayList<ElectorBean> eleitores, ArrayList<CandidateBean> candidatos, LocalDate startDate, LocalDate endDate){
        this.name = name;
        this.electorList = eleitores;
        this.candidateList = candidatos;
        this.startDate = startDate;
        this.endDate = endDate;
        this.started = false;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate.format(MainUtils.formatter);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate.format(MainUtils.formatter);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ArrayList<ElectorBean> getElectorList() {
        return electorList;
    }

    public void setElectorList(ArrayList<ElectorBean> electorList) {
        this.electorList = electorList;
    }

    public ArrayList<CandidateBean> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(ArrayList<CandidateBean> candidateList) {
        this.candidateList = candidateList;
    }
    
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
    
    
}
