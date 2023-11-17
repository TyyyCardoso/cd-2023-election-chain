/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.beans.candidate;

import electionspoo.beans.elector.ElectorBean;
import electionspoo.utils.Constants;
import electionspoo.utils.MainUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class CandidateBean implements Serializable{
    private String name;
    private String initials;
    private int votes;
    private byte[] photo;
    
    public CandidateBean(){
        try {
            //TODO: Generate?
            this.name = "Exemplo";
            this.initials = "EXEM";
            this.photo = MainUtils.iconToByteArray(new ImageIcon(getClass().getResource(Constants.manuCandidatosResource)));
            this.votes = 0;
        } catch (IOException ex) {
            Logger.getLogger(CandidateBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CandidateBean(String name, String initials, ImageIcon photo){
        try {
            this.name = name;
            this.initials = initials;
            this.photo = MainUtils.iconToByteArray(photo);
            this.votes = 0;
        } catch (IOException ex) {
            Logger.getLogger(CandidateBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     public CandidateBean(String name, String initials){
        try {
            this.name = name;
            this.initials = initials;
            this.photo = MainUtils.iconToByteArray(new ImageIcon(getClass().getResource(Constants.blankResource)));
            this.votes = 0;
        } catch (IOException ex) {
            Logger.getLogger(CandidateBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public ImageIcon getPhoto() {
        try {
            return MainUtils.byteArrayToIcon(photo);
        } catch (IOException ex) {
            Logger.getLogger(ElectorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ImageIcon(getClass().getResource(Constants.personResource));
    }

    public void setPhoto(ImageIcon photo) {
        try {
            this.photo =  MainUtils.iconToByteArray(photo);
        } catch (IOException ex) {
            Logger.getLogger(ElectorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getVotes() {
        return votes;
    }
    
    public void setVotesFixed(int votes){
        this.votes = votes;
    }

    public void setVotes(int votes) {
        this.votes += votes;
    }
}
