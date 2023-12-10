/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beanbuilder;

import beans.elector.ElectorBean;
import utils.GenerateUtils;
import utils.MainUtils;
import utils.enums.FirstNamesEnum;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class ElectorBeanBuilder {
    
      
    public static ElectorBean buildRandomElectorBean() throws ParseException, IOException{
       
        Random rd = new Random();
        StringBuilder nome = new StringBuilder();
  
        //Generate first name
        FirstNamesEnum fne = GenerateUtils.getRandomFirstName(rd);
        nome.append(fne.toString()).append(" ").append(GenerateUtils.getRandomLastName(rd).toString());
        
        int cc = GenerateUtils.getRandom8DigitNumber(rd); //Generate CC
        
        char gender = fne.getGender(); //Get name's gender

        LocalDate date = GenerateUtils.getRandomBirthDate(); //Generate random birth date

        //int password = GenerateUtils.getRandom8DigitNumber(rd); //Generate random password(8 digits)
                
        byte[] photo = null;
        try{
            photo = GenerateUtils.getRandomPhoto(gender, MainUtils.getPersonAge(date.format(MainUtils.formatter)));
        }catch(Exception e){
        }
        
        int password = 123;
        
        //int age = MainUtils.getPersonsAge(sdf.format(date));
        
        ElectorBean electorBean = new ElectorBean(nome.toString(), cc, gender, date, String.valueOf(password), photo);
        
        return electorBean;
    }
}
