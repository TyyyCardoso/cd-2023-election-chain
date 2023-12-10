/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans.election;

import beans.candidate.CandidateBean;
import beans.candidate.Candidates;
import beans.elector.ElectorBean;
import beans.elector.Electors;
import utils.Constants;
import utils.interfaces.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author User
 */
public class ElectionManager implements FileManager, Serializable {

    private ElectionBean election;

    public ElectionManager(Candidates candidates, Electors electors) {
        election = new ElectionBean(candidates, electors);
    }
    
    public ElectionBean getElection() {
        return election;
    }
    
     
    public void setElection(ElectionBean election) {
        this.election = election;
    }
    
    
    public boolean electionStarted(){
        return election.isStarted();
    }
    
    public void addBlankCandidate(){
        for(CandidateBean candidate : election.getCandidateList())
            if(candidate.getName().equals(Constants.blankCandidateName)){
                election.getCandidateList().remove(candidate);
                break;
            }          
        election.getCandidateList().add(new CandidateBean(Constants.blankCandidateName, Constants.blankCandidateName));  
    }

    public void newElection(Candidates candidates, Electors electors) {
        candidates.resetAllCandidateVotes();
        electors.resetElectorsVoted();
        candidates = new Candidates();
        electors = new Electors();
        election = new ElectionBean(candidates, electors);
    }

    public void updateBeanLists(Candidates candidates, Electors electors) {
        election.setCandidateList(candidates.getList());
        election.setElectorList(electors.getList());
    }

    @Override
    public void save(String nomeFicheiro) throws Exception {
        try ( ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(nomeFicheiro))) {
            file.writeObject(election);
        }
    }

    @Override
    public boolean equals(Object obj) {
        ElectionManager newElection = (ElectionManager) obj;
        
        if(newElection.getElection().getName().equals(election.getName())){
            if(newElection.getElection().getStartDate().equals(election.getStartDate())){
                if(newElection.getElection().getEndDate().equals(election.getEndDate())){
                    if(newElection.getElection().getCandidateList().size()==election.getCandidateList().size()){
                        if(newElection.getElection().getElectorList().size()==election.getElectorList().size()){
                            return true;
                        }
                    }
                }
            }
        }  
        return false;
    }
    
    @Override
    public void load(String nomeFicheiro, Candidates candidates, Electors electors) throws Exception {
        if (new File(nomeFicheiro).exists()) {
            try ( ObjectInputStream file = new ObjectInputStream(new FileInputStream(nomeFicheiro))) {
               
                election = (ElectionBean) file.readObject();
               
                List<CandidateBean> candidatesList = election.getCandidateList();
                for(CandidateBean bean : candidatesList){
                    candidates.addCandidate(bean);
                }
                
                List<ElectorBean> electorList = election.getElectorList();
                for(ElectorBean bean : electorList){
                    electors.addElector(bean);
                }
            }
        } else {
            election = new ElectionBean(candidates, electors);
            save(nomeFicheiro);
        }
    }

    @Override
    public void load(String nomeFicheiro) throws Exception {
        
    }

}
