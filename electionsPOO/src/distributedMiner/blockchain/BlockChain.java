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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author manso
 */
public class BlockChain implements Serializable {

    public static String DEFAULT_FILENAME = "blockchain.blc";

    List<Block> chain;

    public BlockChain() {
        try {
            //tentar ler a blockchain guardada no ficheiro
            load(DEFAULT_FILENAME);
        } catch (Exception e) {
            //criar uma nova blockchain
            chain = new CopyOnWriteArrayList<>();
            //introduzir um bloco vazio
            Block b = new Block("First Block", "Empty", 1);
            chain.add(b);
        }

    }

    public List<Block> getChain() {
        return chain;
    }

    public Block getLast() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) throws BlockchainException {
        //se bão for válido
        if (!newBlock.isValid()) {
            throw new BlockchainException("Block corrupted");
        }
        //se não encaixar no último
        if (!newBlock.getPrevious().equals(getLast().getHash())) {
            throw new BlockchainException("Block does not fit in the last");
        }
        //adicionar o bloco
        this.chain.add(newBlock);
    }

    public boolean isValid() {
        //começar no primeiro
        for (int i = 1; i < chain.size(); i++) {
            //não for válido
            if (!chain.get(i).isValid() 
                    //não encaixar no anterior
                    || !chain.get(i).previous.equals(chain.get(i - 1).hash)) {
                //blockchain invalida
                return false;
            }
        }
        return true;
    }

    public boolean contains(Block b) {
        //iterar os blocos
        for (Block block : chain) {
            //comparar com o hash
            if (block.getHash().equals(b.getHash())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        for (Block node : chain) {
            txt.append(node.toString()).append("\n");
        }

        return txt.toString();
    }

    public void save(String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName));
        out.writeObject(chain);
        out.close();
    }

    public void load(String fileName) throws BlockchainException, Exception {
        //ler os blocos
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fileName))) {
            //ler os blocos
            List<Block> list = (List<Block>) in.readObject();
            //Verificar os blocos - comecar no 1
            for (int i = 1; i < list.size(); i++) {
                Block node = list.get(i);
                if (!node.isValid()) {
                    throw new BlockchainException("Corrupted data");
                }
            }
            this.chain = list;
        }
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202112061136L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2021  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
