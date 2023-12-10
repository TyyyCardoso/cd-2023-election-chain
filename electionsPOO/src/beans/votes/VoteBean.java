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
package beans.votes;

import utils.SecurityUtils;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import utils.Serializer;

/**
 *
 * @author manso
 */
public class VoteBean implements Serializable {

    private String from;
    private String to; 
    
    String signature;
    
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

    public VoteBean(String from, String to) {
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
         return     "From : "+from
                + "\nTo   : " + to
                + "\nSignature : " + signature;
    }

    public String toText() {
        return Serializer.objectToBase64(this);
    }

    public static VoteBean fromText(String obj) {
        return (VoteBean)Serializer.base64ToObject(obj);
    }

    @Override
    public int hashCode() {
        return toText().hashCode();
    }

    @Override
    public boolean equals(Object t) {
        if (t instanceof VoteBean) {
            return this.toText().equals(((VoteBean) t).toText());
        }
        return false;
    }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202312050910L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2023  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
