/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import utils.enums.FirstNamesEnum;
import utils.enums.LastNamesEnum;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class GenerateUtils {
       
    
    

    public static FirstNamesEnum getRandomFirstName(Random rd){
        return FirstNamesEnum.values()[rd.nextInt(50)];
    }
    
     public static LastNamesEnum getRandomLastName(Random rd){
        return LastNamesEnum.values()[rd.nextInt(25)];
    }
     
    public static int getRandom8DigitNumber(Random rd){  
        return randBetween(Constants.minCCNumber, Constants.maxCCNumber);
    }
    
    public static LocalDate getRandomBirthDate() throws ParseException{
 
       int year = randBetween(1980, 2005);
       int month = randBetween(1,12);
       int dayOfMonth = randBetween(1,31);
       
       String dateString = String.format("%02d/%02d/%04d", dayOfMonth, month, year);

       return LocalDate.parse(dateString, MainUtils.formatter);

    }
    
    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
    
    //funcao que obtem uma foto de 1 pessoa random de acordo com o genero e intervalo de idade
    public static byte[] getRandomPhoto(char gender, int idade) throws IOException {
        //verifica genero
        String genderFinal = "male";
        if (gender == ('M') || gender == ('m')) {
            genderFinal = "male";
        } else {
            genderFinal = "female";
        }

        //abrir stream para o url da API
        InputStream in = URI.create("https://fakeface.rest/face/json?gender=" + genderFinal + "&minimum_age=" + (idade - 5) + "&maximum_age=" + (idade + 5)).toURL().openStream();
        
        //Ler o JSON da API
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        String json = new String(bytes);
        
        //extrair o url da imagem: Começa por "https://" e acaba em .jpg
        String url = json.substring(json.indexOf("https://"), json.lastIndexOf(".jpg") + 4);
        
        //converte o url em ImageIcon 
        //ImageIcon imageIcon = new ImageIcon(new URL(url));
        return MainUtils.imageFromURLToByteArray(new URL(url));
    }
}
