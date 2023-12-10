//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::     http://www.fc.ul.pt/en/unidade/bioisi                               ::
//::                                                                         ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2021   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package distributedMiner.blockchain;

import distributedMiner.mining.MinerP2P;
import java.io.Serializable;

/*
 *
 * @author manso
 */
public class Block implements Serializable {

    public static int DIFICULTY = 4;

    String previous;        // hash do no anterior
    String data;            // dados 
    int numberOfZeros = DIFICULTY;  // número de zeros do hash
    String hash = "000";     // hash do bloco actual
    int nonce = 0;          // numero de validação
    
    /**
     * cria um bloco
     *
     * @param message dados do bloco
     * @param previous hash do bloco anterior
     * @param zeros numero de zeros
     */
    public Block(String message, String previous, int zeros) {
        try {
            this.data = message;
            this.previous = previous;
            this.numberOfZeros = zeros;
            this.nonce = 0;
            this.hash = MinerP2P.getHash(getMiningData());
        } catch (Exception ex) {
        }
    }

    /**
     * para mineração
     *
     * @return previous + data + nonce
     */
    public final String getMiningData() {
        return previous + data + numberOfZeros;
    }

    public String getPrevious() {
        return previous;
    }

    public int getNonce() {
        return nonce;
    }

    public int getNumberOfZeros() {
        return numberOfZeros;
    }

    public String getHash() {
        return hash;
    }

    /**
     * mensagem do no
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * actualiza o nonce do no
     *
     * @param nonce novo nonce
     *
     */
    public void setNonce(int nonce) {
        this.nonce = nonce;
        //calcular o hash
        this.hash = MinerP2P.getHash(nonce + getMiningData());
    }

    /**
     * verify id the block is valid
     *
     * @return
     */
    public boolean isValid() {
        try {
            //zeros do prefix
            String prefix = String.format("%0" + numberOfZeros + "d", 0);
            if (!hash.startsWith(prefix)) {
                throw new BlockchainException("Wrong prefix Hash" + hash);
            }
            //comparar o hash da mensagem com o hash actual
            String realHash = MinerP2P.getHash(nonce + getMiningData());
            if (!realHash.equals(hash)) {
                throw new BlockchainException("Corrupted data : " + data);
            }
            //OK
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
      public String getInfo() {
        return "Previous:" + previous
                + "\nData    :" + data
                + "\nNonce   :" + nonce
                + "\nHash    :" + hash
                + "\nNº Zeros:" + numberOfZeros
                + "\nValid   :" + isValid();
    }
      

    @Override
    public String toString() {
        return data;
    }

  

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Block) {
            return hash.equals(((Block) obj).hash);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.hash.hashCode();
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202112061136L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2021  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
