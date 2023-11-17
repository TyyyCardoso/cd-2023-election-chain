/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.beans.elector;

import electionspoo.beans.candidate.CandidateBean;
import electionspoo.beans.transaction.UserCredentials;
import electionspoo.utils.Constants;
import electionspoo.utils.MainUtils;
import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class ElectorBean implements Serializable, Comparable {

    private static final long serialVersionUID = 7920815667225358507L; 
    
    private String name;
    private int CC;
    private char gender;
    private LocalDate birthDate;
    private String password;
    private boolean voted;
    private CandidateBean votedCandidate;
    private byte[] photo;

    
    public CandidateBean getVotedCandidate() {
        return votedCandidate;
    }
    

    public void setVotedCandidate(CandidateBean votedCandidate) {
        this.votedCandidate = votedCandidate;
    }

    public ElectorBean() {
        try {
            this.name = "Nome";
            this.CC = 123456789;
            this.gender = 'M';
            this.birthDate = LocalDate.parse("01/01/1990", MainUtils.formatter);
            this.password = "123";
            this.voted = false;
            this.photo = MainUtils.iconToByteArray(new ImageIcon(getClass().getResource(Constants.personResource)));
        } catch (IOException ex) {
            Logger.getLogger(ElectorBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public ElectorBean(String name, int cc, char gender, LocalDate birthDate, String password, byte[] photo) {
        this.name = name;
            this.CC = cc;
            this.gender = gender;
            this.birthDate = birthDate;
            this.password = password;
            this.photo =  photo;
            this.voted = false;
    }
    

    public ElectorBean(String name, int cc, char gender, LocalDate birthDate, String password, ImageIcon photo) {
        try {
            this.name = name;
            this.CC = cc;
            this.gender = gender;
            this.birthDate = birthDate;
            this.password = password;
            this.photo =  MainUtils.iconToByteArray(photo);
            this.voted = false;
        } catch (IOException ex) {
            Logger.getLogger(ElectorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCC() {
        return CC;
    }

    public void setCC(int CC) {
        this.CC = CC;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return this.birthDate.format(MainUtils.formatter);
    }
    
    public LocalDate getBirthDateLikeADate(){
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ImageIcon getPhoto() {
        try {
            if(null!=photo)
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

    @Override
    public int compareTo(Object elector) {
        int compareCC = ((ElectorBean)elector).getCC();
        /* For Ascending order*/
        return this.CC-compareCC;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
