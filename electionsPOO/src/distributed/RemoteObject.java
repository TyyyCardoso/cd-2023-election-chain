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
import beans.blockchain.BlockchainException;
import beans.candidate.Candidates;
import beans.blockchain.consensus.LastBlock;
import beans.votes.Votes;
import beans.candidate.CandidateBean;
import beans.election.ElectionBean;
import beans.election.ElectionManager;
import beans.elector.ElectorBean;
import beans.elector.Electors;
import beans.votes.VoteBean;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import utils.Serializer;

/**
 *
 * @author manso
 */
public class RemoteObject extends UnicastRemoteObject implements RemoteInterface {

    MiningListener listener;
    MinerP2P myMiner;
    
    Votes votes;
    Candidates candidates;
    Electors electors;
    ElectionManager election;
    
    boolean hasElectionEnded;
    String electionResult;
    
    public Block miningBlock; // block in mining process
    public BlockChain blockchain;

    private String address; // nome do servidor
    private List<RemoteInterface> network; // network of miners

    
    
    /**
     * creates a object listening the port
     *
     * @param port port to listen
     * @param listener listener do objeto
     * @throws RemoteException
     */
    public RemoteObject(int port, MiningListener listener) throws RemoteException {
        super(port);
        try {
            this.listener = listener;
            this.myMiner = new MinerP2P(listener);
            //atualizar o endereço do objeto remoto
            address = "//" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/" + RemoteInterface.OBJECT_NAME;
            //inicializar nova rede
            network = new CopyOnWriteArrayList<>();
            //inicializar novas transações
            votes = new Votes();
            this.miningBlock = new Block("dummy", "dummy", 1);
            //inicializar blockchain
            blockchain = new BlockChain();
            //inicializar lista de candidatos
            candidates = new Candidates();
            //inicializar lista de eleitores
            electors = new Electors();
            //inicializar eleição
            election = new ElectionManager(candidates, electors);
            
            hasElectionEnded = false;
            electionResult = "";
            
            listener.onStartServer(utils.RMI.getRemoteName(port, RemoteInterface.OBJECT_NAME));
        } catch (Exception e) {
            address = "unknow" + ":" + port;

        }
    }

    @Override
    public String getAdress() throws RemoteException {
        return address;
    }

    public String getClientName() {
        //informação do cliente
        try {
            return RemoteServer.getClientHost();
        } catch (ServerNotActiveException ex) {
        }
        return "Anonimous";
    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               M I N E I R O 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public String getHash(int nonce, String msg) throws RemoteException {
        //informação do cliente       
        System.out.println("Hashing to " + getClientName());
        //calcular hash      
        return MinerP2P.getHash(nonce + msg);
    }

    @Override
    public void startMining(String msg, int dificulty) throws RemoteException {
        //se estivar a minar não faz nada
        if (myMiner.isMining()) {
            return;
        }
        int numCores = Runtime.getRuntime().availableProcessors();    
        //colocar o mineiro a minar
        myMiner.startMining(msg, dificulty, numCores);
        listener.onStartMining(msg, dificulty);
        //mandar a rede minar
        for (RemoteInterface node : network) {
            node.startMining(msg, dificulty);
        }
    }

    @Override
    public void stopMining(int nonce) throws RemoteException {
        //Se estiver parado
        if (!myMiner.isMining()) {
            return; // sair
        }
        //parar o mineiro  
        myMiner.stopMining(nonce);
        //atualizar o bloco com o nonce
        this.miningBlock.setNonce(nonce);
        //mandar parar a rede
        for (RemoteInterface node : network) {
            listener.onMessage("Stop Miner", node.getAdress());
            node.stopMining(nonce);
        }
    }

    @Override
    public int getNonce() throws RemoteException {
        return myMiner.getNonce();
    }

    @Override
    public int getTicket() throws RemoteException {
        return myMiner.getTicket();
    }

    @Override
    public boolean isMining() throws RemoteException {
        return myMiner.isMining();
    }

    @Override
    public int mine(String msg, int dificulty) throws RemoteException {
        try {
            //informação do cliente
            System.out.println("Mining to " + getClientName());
            int numCores = Runtime.getRuntime().availableProcessors();    
            //começar minar o bloco
            myMiner.startMining(msg, dificulty, numCores);
            //esperar que termine
            return myMiner.waitToNonce();
        } catch (Exception ex) {
            throw new RemoteException(ex.getMessage(), ex.getCause());
        }

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                R E D E   M I N E I R A 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public void addNode(RemoteInterface node) throws RemoteException {
        //se a rede não tiver o no
        if (!network.contains(node)) {
            listener.onAddNode(node);
            //adicionar o mineiro à rede
            network.add(node);
            //adicionar o nosso no a rede do remoto remoto
            node.addNode(this);
            //espalhar o mineiro pela rede            
            for (RemoteInterface remote : network) {
                //adicionar o novo no ao remoto ao nos da rede
                remote.addNode(node); // pedir para adiconar o novo nó
            }
            //notificar o listener
            listener.onAddNode(node);
        }
    }

    @Override
    public List<RemoteInterface> getNetwork() throws RemoteException {
        //transformar a network num arraylist
        return new ArrayList<>(network);
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               TRANSACTIONS
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public void addVote(String vote) throws RemoteException {
        //se já tiver a transação não faz nada
        if (this.votes.contains(vote)) {
            listener.onMessage("Duplicated Vote", vote);
            return;
        }

        votes.addVote(vote);
        listener.onUpdateVotes(vote);
        listener.onMessage("addTransaction", getClientName());
        //se tiver mais de 2 transacoes e não estiver a minar
        if (votes.getList().size() >= 2 && !myMiner.isMining()) {
            buildNewBlock();
        } else {
            //sincronizar a transacao
            for (RemoteInterface node : network) {
                node.synchonizeVotes(votes.getList());
            }
        }

    }

    @Override
    public List<String> getVotesList() throws RemoteException {
        return votes.getList();
    }

    @Override
    public void synchonizeVotes(List<String> list) throws RemoteException {
        if (list.equals(votes.getList())) {
            return;
        }
        for (String string : list) {
            addVote(string);
        }
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeVotes(votes.getList());
        }
        listener.onMessage("synchonizeVotes", getClientName());

    }
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K 
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public void startMiningBlock(Block newBlock) throws RemoteException {
        //se já tiver o bloco
        if (miningBlock.equals(newBlock)) {
            return;
        }
        listener.onMessage("New Mining Block", newBlock.getHash() + "");
        this.miningBlock = newBlock;
        //Remover as transacoes
        List<String> lst = (List<String>) Serializer.base64ToObject(newBlock.getData());
        this.votes.removeVotes(lst);
        listener.onUpdateVotes(null);
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            node.startMiningBlock(miningBlock);
        }
        //minar o bloco
        startMining(newBlock.getMiningData(), newBlock.getNumberOfZeros());
    }

    @Override
    public void buildNewBlock() throws RemoteException {
        if (votes.getList().size() < Votes.MAXVOTES) {
            return;
        }
        listener.onUpdateBlockchain();
        //espalhar o bloco pela rede
        for (RemoteInterface node : network) {
            listener.onMessage("Synchronize blockchain", node.getAdress());
            node.synchonizeBlockchain(this);
        }
        
        //String lastHash = blockchain.getLast().getHash();
        String lastHash = new LastBlock(this).getLastBlock().getHash();

        //dados do bloco são as lista de transaçoes 
        String data = Serializer.objectToBase64(votes.getList());

        //Construir um novo bloco logado ao último
        Block newBlock = new Block(data, lastHash, Block.DIFICULTY); 
        
        
        //Começar a minar o bloco
        startMiningBlock(newBlock);
    }

    @Override
    public void updateMiningBlock(Block newBlock) throws RemoteException {
        // se o novo bloco for válido
        // e encaixar na minha blochain      
        if (newBlock.isValid()
                && newBlock.getPrevious().equals(blockchain.getLast().getHash())) {
            try {

                blockchain.addBlock(newBlock);
                this.miningBlock = newBlock;

                listener.onUpdateBlockchain();
                //espalhar o bloco pela rede
                for (RemoteInterface node : network) {
                    node.updateMiningBlock(newBlock);
                }
            } catch (BlockchainException ex) {
                throw new RemoteException("Update mining Block", ex);
            }
        }
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::               B L O C K C H A I N
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public int getBlockchainSize() throws RemoteException {
        return blockchain.getChain().size();
    }

    @Override
    public BlockChain getBlockchain() throws RemoteException {
        return blockchain;

    }

    @Override
    public void synchonizeBlockchain(RemoteInterface syncNode) throws RemoteException {
        //se os tamanhos forem diferentes
        if (syncNode.getBlockchainSize() != this.getBlockchainSize()) {
            //se o meu tamnho for menor
            if (syncNode.getBlockchainSize() > this.getBlockchainSize()) {
                //atualizar aminha blockchain
                listener.onUpdateBlockchain();
                this.blockchain = syncNode.getBlockchain();
            } else // Se a do no for menor
            if (syncNode.getBlockchainSize() < this.getBlockchainSize()) {
                //sincronizar com a minha
                syncNode.synchonizeBlockchain(this);
            }
            //sincronizar a blockchain pela rede
            for (RemoteInterface node : network) {
                node.synchonizeBlockchain(this);
            }
        }

    }
    
    Map<Long, Boolean> flagLastBlock = new ConcurrentHashMap<>();

    @Override
    public List getLastBlock(long timeStamp, int dept, int maxDep) throws RemoteException {
       //codigo com acesso exclusivo  para a thread
        synchronized (this) {
            //verificar se ja respondi
            if (flagLastBlock.get(timeStamp) != null) {
                return null;
            }
            //verificar se cheguei ao limite de profundidade
            if (dept > maxDep) {
                return null;
            }

            //responder
            flagLastBlock.put(timeStamp, Boolean.TRUE);
        }
        listener.onConsensus("Last Block", address);
        //calcular o ultimo bloco
        List myList = new CopyOnWriteArrayList();
        myList.add(blockchain.getLast());

        // Usar parallelStream para processar as solicitações de rede de forma paralela
        List<List> responses = network.parallelStream().map(node -> {
            try {
                listener.onConsensus("Get Last Block List", node.getAdress());
                return node.getLastBlock(timeStamp, dept + 1, maxDep);
            } catch (RemoteException e) {
                // Lidar com a exceção aqui
                return null;
            }
        }).filter(resp -> resp != null).collect(Collectors.toList());

        // Adicionar todas as respostas à lista principal
        for (List resp : responses) {
            myList.addAll(resp);
        }

        return myList;

    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                 C A N D I D A T E S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    
    @Override
    public void addCandidates(List<CandidateBean> list) throws RemoteException {
        //se já tiver a transação não faz nada
        if(list.size()==this.candidates.getList().size()){
            listener.onMessage("Finished Added Candidates", "");
            return;
        }
        
        for(CandidateBean bean : list){
            candidates.addCandidate(bean);
        }
        listener.onUpdateCandidates();
        listener.onMessage("addCandidates", getClientName());
       
        for (RemoteInterface node : network) {
            node.synchonizeCandidates(candidates.getList());
        }
        
    }

    @Override
    public void synchonizeCandidates(List<CandidateBean> list) throws RemoteException {
        if (list.size()==candidates.getList().size()) {
            return;
        }
           
        addCandidates(list);
  
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeCandidates(candidates.getList());
        }
        
        listener.onMessage("synchonizeCandidates", getClientName());

    }

    @Override
    public List<CandidateBean> getCandidateList() throws RemoteException {
        return candidates.getList();
    }
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                 E L E C T O R S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    

    @Override
    public void addElectors(List<ElectorBean> list) throws RemoteException {
        //se já tiver a transação não faz nada
        if(list.size()==this.electors.getList().size()){
            listener.onMessage("Finished Added Electors", "");
            return;
        }
        
        for(ElectorBean bean : list){
            electors.addElector(bean);
        }
        listener.onUpdateElectors();
        listener.onMessage("addElectors", getClientName());
       
        for (RemoteInterface node : network) {
            node.synchonizeElectors(electors.getList());
        }
    }

    @Override
    public void synchonizeElectors(List<ElectorBean> list) throws RemoteException {
        if (list.size()==electors.getList().size()) {
            return;
        }
           
        addElectors(list);
  
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeElectors(electors.getList());
        }
        
        listener.onMessage("synchonizeElectors", getClientName());
    }
    
    @Override
    public List<ElectorBean> getElectorsList() throws RemoteException {
        return electors.getList();
    }
    
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::                                                         :::::::::::::
    //:::::                 E L E C T I O N S
    //:::::                                                         :::::::::::::
    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @Override
    public void addElection(ElectionManager newElection) throws RemoteException {
        //se já tiver a eleição não faz nada
        if(newElection.equals(election)){
            listener.onMessage("Finished Added Election", "");
            return;
        }
        
        election.setElection(newElection.getElection());
        
        listener.onUpdateElection();
        listener.onMessage("addElection", getClientName());
       
        for (RemoteInterface node : network) {
            node.synchonizeElection(election);
        }
    }

    @Override
    public void synchonizeElection(ElectionManager newElection) throws RemoteException {
        if(newElection.equals(election)){
            return;
        }
           
        addElection(newElection);
  
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeElection(election);
        }
        
        listener.onMessage("synchonizeElection", getClientName());
    }

    @Override
    public ElectionManager getElection() throws RemoteException {
        return election;
    }

    @Override
    public void endElection() throws RemoteException {
        if(this.hasElectionEnded){
            listener.onMessage("Election has finished", "");
            return;
        }
        
        this.hasElectionEnded = true;

        listener.onMessage("endingElection", getClientName());
        
        for(RemoteInterface node : network){
            node.synchonizeElectionState();
        }
    }

    @Override
    public boolean getElectionState() throws RemoteException {
        return this.hasElectionEnded;
    }

    @Override
    public void synchonizeElectionState() throws RemoteException {
        if(this.hasElectionEnded){
            listener.onMessage("Election has finished", "");
            return;
        }
           
        endElection();
  
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeElectionState();
        }
        
        listener.onMessage("synchonizeElectionState", getClientName());
    }

    @Override
    public void setElectionResult(String electionResult) throws RemoteException {
            if(this.electionResult.equals(electionResult)){
                listener.onMessage("Result updated", "");
                return;
            }

            this.electionResult = electionResult;
            
            listener.onUpdateElection();
            listener.onMessage("updatingResults", getClientName());
        
            for(RemoteInterface node : network){
                node.synchonizeElectionResult(electionResult);
            }
    }

    @Override
    public String getElectionResult() throws RemoteException {
        return this.electionResult;
    }

    @Override
    public void synchonizeElectionResult(String electionResult) throws RemoteException {
        if(this.electionResult.equals(electionResult)){
            listener.onMessage("Result are ok", "");
            return;
        }
           
        setElectionResult(electionResult);
  
        //mandar sincronizar a rede
        for (RemoteInterface node : network) {
            node.synchonizeElectionResult(electionResult);
        }
        
        listener.onMessage("synchonizeElectionResult", getClientName());
    }




}
