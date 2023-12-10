//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2023   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 29/11/2023, 07:09:06
 *
 * @author IPT - computer
 * @version 1.0
 */
public class MinerTHR extends Thread {

    AtomicInteger ticket;
    AtomicInteger nonce;
    int dificulty;
    String data;

    public MinerTHR(AtomicInteger ticket, AtomicInteger nonce, int dificulty, String data) {
        this.ticket = ticket;
        this.nonce = nonce;
        this.dificulty = dificulty;
        this.data = data;
    }

    public void stop(int number) {
        nonce.set(number);
        interrupt();
    }

    public void run() {
        //String of zeros
        String zeros = String.format("%0" + dificulty + "d", 0);
        int number;
        while (nonce.get() <= 0) {
            //calculate hash of block   
            number = ticket.getAndIncrement();
            String hash = getHash(number + data);
            //Nounce found
            if (hash.startsWith(zeros)) {
                nonce.set(number);
            }
        }
    }

    public static String getHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
            return data;
        }
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202209281113L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
