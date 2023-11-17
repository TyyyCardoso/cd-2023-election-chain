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
//::                                                               (c)2022   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package electionspoo.beans.transaction;

import electionspoo.utils.blockchain.SecurityUtils;
import electionspoo.utils.blockchain.Serializer;
import java.io.IOException;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author manso
 */
public class Transaction implements Serializable {

    private String from;  //chave publica
    private String to;    //chave publica
    
    String signature;     //assinatura do FROM
    
    public void sign(PrivateKey priv) throws Exception{
        byte[]data = (from+to).getBytes();
        byte[] s = SecurityUtils.sign(data, priv);
        signature = Base64.getEncoder().encodeToString(s);
    }
    
    public boolean validateSignature() throws Exception{
        //dados da transacao
        byte[]data = (from+to).getBytes();
        //dados da assinatura
        byte [] sign = Base64.getDecoder().decode(signature);
        //chave publica do from
        byte [] pk = Base64.getDecoder().decode(from);
        PublicKey pubKey = SecurityUtils. getPublicKey(pk);
        return SecurityUtils.verifySign(data, sign, pubKey);
    }

    public Transaction(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        try {
            UserCredentials uFrom = UserCredentials.getFromPublicKey(from);
            //UserCredentials uTo = UserCredentials.getFromPublicKey(to);
            String txtFrom = uFrom!= null ? uFrom.name : "Unknown";
            //String txtTo = uTo!= null ? uTo.name : "Unknown";
            return String.format("%-10s -> %s", txtFrom,  to);
        } catch (Exception ex) {
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ERROR TOSTRING";
    }
    /**
     * converte o this para base64
     * @return 
     */
    public String toBase64() throws IOException{
        byte[] data = Serializer.objectToByteArray(this);
        return Base64.getEncoder().encodeToString(data);  
    }
    /**
     * constroi umobjecto com base64
     * @param b64
     * @return 
     */
    public static Transaction fromBase64(String b64) throws Exception{
        
        byte [] data = Base64.getDecoder().decode(b64);
        return(Transaction) Serializer.byteArrayToObject(data);
    }

    public String getSignature() {
        return signature;
    }
    
     public boolean isValid() throws Exception {
        if (this.getFrom().equals(this.getTo())) {
            throw new Exception("The users are equal ");
        }
        if (this.getFrom().trim().isEmpty()) {
            throw new Exception("From user is empty");
        }
        if (this.getTo().trim().isEmpty()) {
            throw new Exception("To user is empty");
        }
        return true;
    }
   

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202211011644L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
