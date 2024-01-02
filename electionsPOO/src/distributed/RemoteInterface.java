package distributed;


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


import beans.blockchain.Block;
import beans.blockchain.BlockChain;
import beans.candidate.Candidates;
import beans.candidate.CandidateBean;
import beans.election.ElectionBean;
import beans.election.ElectionManager;
import beans.elector.ElectorBean;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author manso
 */
public interface RemoteInterface extends Remote {

    public static String OBJECT_NAME = "RemoteMiner";
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               M I N E I R O 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void startMining(String msg, int dificulty) throws RemoteException;

    public void stopMining(int nonce) throws RemoteException;

    public int getNonce() throws RemoteException;

    public int getTicket() throws RemoteException;

    public boolean isMining() throws RemoteException;

    public int mine(String msg, int dificulty) throws RemoteException;

    public String getHash(int nonce, String msg) throws RemoteException;

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                R E D E   M I N E I R A 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public String getAdress() throws RemoteException;

    public void addNode(RemoteInterface node) throws RemoteException;

    public List<RemoteInterface> getNetwork() throws RemoteException;

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               V O T E S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void addVote(String vote) throws RemoteException;

    public void synchonizeVotes(List<String> list) throws RemoteException;

    public List<String> getVotesList() throws RemoteException;
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               E L E C T I O N S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void addElection(ElectionManager election) throws RemoteException;

    public void synchonizeElection(ElectionManager election) throws RemoteException;

    public ElectionManager getElection() throws RemoteException;
    
    public void endElection() throws RemoteException;
    
    public boolean getElectionState() throws RemoteException;
    
    public void synchonizeElectionState() throws RemoteException;
    
    public void setElectionResult(String electionResult) throws RemoteException;
    
    public String getElectionResult() throws RemoteException;
    
    public void synchonizeElectionResult(String electionResult) throws RemoteException;
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                    C A N D I D A T E S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    public void addCandidates(List<CandidateBean> list) throws RemoteException;
    
    public void synchonizeCandidates(List<CandidateBean> list) throws RemoteException;
    
    public List<CandidateBean> getCandidateList() throws RemoteException;
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                    E L E C T O R S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    public void addElectors(List<ElectorBean> list) throws RemoteException;
    
    public void synchonizeElectors(List<ElectorBean> list) throws RemoteException;
    
    public List<ElectorBean> getElectorsList() throws RemoteException;
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void startMiningBlock(Block bl) throws RemoteException;

    public void updateMiningBlock(Block bl) throws RemoteException;

    public void buildNewBlock() throws RemoteException;
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K C H A I N
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void synchonizeBlockchain(RemoteInterface syncNode) throws RemoteException;

    public int getBlockchainSize() throws RemoteException;

    public BlockChain getBlockchain() throws RemoteException;

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               C O N S E N S U S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public List getLastBlock(long timeStamp, int dept, int maxDep) throws RemoteException;


   
}
