/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.beans.transaction;

import electionspoo.beans.elector.*;
import electionspoo.blockchain.MerkleTree;
import electionspoo.utils.Constants;
import electionspoo.utils.interfaces.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author User
 */
public class TransactionList{

    //arraylist com os eleitores lidos do ficheiro + os que estão em memória
    private static ArrayList<Transaction> transactionList;
    
    //retorna a arraylist
    public static ArrayList<Transaction> getList(){
        return transactionList;
    }
    
    public static void resetTransactionList(){
       transactionList = new ArrayList<Transaction>();
    }
    
    //retorna a arraylist
    public static void setList(ArrayList<Transaction> newList){
        transactionList = newList;
    }

    //apaga um eleitor da arraylist dado o seu index
    public static void deleteTransactionFromList(int id) throws IOException, FileNotFoundException, ClassNotFoundException, ParseException {
        transactionList.remove(id);
    }

    public static MerkleTree addTransaction(Transaction transacao) throws Exception{
        if(!(null!=transactionList)){
            resetTransactionList();
        }
        transactionList.add(transacao);
        
        if(transactionList.size()==Constants.transactionListSize){
            ArrayList<String> transactionsStrings = new ArrayList<String>();
            
            for(Transaction trans : transactionList){
                if(transacao.isValid()){
                    transactionsStrings.add(transacao.toString());
                }else{
                    transactionList.remove(transacao);
                }
            }
            
            MerkleTree merkle = new MerkleTree(transactionsStrings);
            resetTransactionList();
            return merkle;
            
        }
        
        return null;
    }
    

}
