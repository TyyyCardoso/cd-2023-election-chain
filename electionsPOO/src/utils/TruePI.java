/** ************************************************************************** */
/** **     Copyright (C) 2011                                              *** */
/** **     António Manuel Rodrigues Manso                                  *** */
/** **     e-mail: manso@ipt.pt                                            *** */
/** **     url   : http://orion.ipt.pt/~manso    manso@ipt.pt              *** */
/** **     Instituto Politécnico de Tomar                                  *** */
/** **     Escola Superior de Tecnologia de Tomar                          *** */
/** **                                                                     *** */
/** ************************************************************************** */
/** **     This software was build with the purpose of learning.           *** */
/** **     Its use is free and is not provided any guarantee               *** */
/** **     or support.                                                     *** */
/** **     If you met bugs, please, report them to the author              *** */
/** **                                                                     *** */
/** ************************************************************************** */
/** ************************************************************************** */
package utils;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author work02
 */
public class TruePI {

    public static void main(String[] args) {
        System.out.println("PI " + PI);
    }

    public static String PI;

    static {
        try {
            byte[] data = Files.readAllBytes(Paths.get(TruePI.class.getClassLoader().getResource("utils/million_pi.txt").toURI()));
            PI = new String(data).replaceAll("\\s+", "");
        } catch (Exception ex) {
            PI = Math.PI + "";
        }
    }

    public static int getLastDigitCorrect(String myPi) {
        for (int i = 0; i < myPi.length(); i++) {
            if (myPi.charAt(i) != PI.charAt(i)) {
                return i;
            }
        }
        return PI.length() < myPi.length() ? PI.length() : myPi.length(); 

    }

    public static String getCorrectDigits(String myPi) {
        StringBuilder strPi = new StringBuilder();
        for (int i = 0; i < myPi.length(); i++) {
            if (myPi.charAt(i) != PI.charAt(i)) {
                break;
            }
            strPi.append(myPi.charAt(i));
        }
        return strPi.toString();

    }
}
