/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans.election;

import beans.candidate.CandidateBean;
import beans.candidate.Candidates;
import beans.elector.ElectorBean;
import beans.elector.Electors;
import utils.MainUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class ElectionBean implements Serializable {
    
    private static final long serialVersionUID = -6930306948144226156L;
    
    private String name;
    private List<ElectorBean> electorList;
    private List<CandidateBean> candidateList;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean started;
    private boolean ended;
    
    public ElectionBean(Candidates candidates, Electors electors){
        this.name = "Eleicao Basica";
        this.electorList = electors.getList();
        this.candidateList = candidates.getList();
        this.startDate = LocalDate.parse("01/01/2022", MainUtils.formatter);
        this.endDate = LocalDate.parse("01/05/2022", MainUtils.formatter);
        this.started = false;
        this.ended = false;
    }
    
    public ElectionBean(String name, ArrayList<ElectorBean> eleitores, ArrayList<CandidateBean> candidatos, LocalDate startDate, LocalDate endDate){
        this.name = name;
        this.electorList = eleitores;
        this.candidateList = candidatos;
        this.startDate = startDate;
        this.endDate = endDate;
        this.started = false;
        this.ended = false;
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

    public List<ElectorBean> getElectorList() {
        return electorList;
    }

    public void setElectorList(List<ElectorBean> electorList) {
        this.electorList = electorList;
    }

    public List<CandidateBean> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(List<CandidateBean> candidateList) {
        this.candidateList = candidateList;
    }
    
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
    
    
}
