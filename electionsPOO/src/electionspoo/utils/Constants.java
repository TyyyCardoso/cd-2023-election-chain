/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.utils;

/**
 *
 * @author User
 */
public class Constants {
    
    public static final int maxCCNumber = 99999999;
    public static final int minCCNumber = 1000000;
    
    //Voto
    public static final String voteCompleted = "Obrigado! O seu voto foi submetido com sucesso!";
    
    //Eleitor
    public static final String gerarEleitorPopUpText = "Quantos eleitores pretende gerar?";
    public static final String gerarEleitorPopUpTitle = "Gerar eleitores";
    public static final String eliminarEleitorPopUpText = "Tem a certeza que pretende eliminar este eleitor?";
    public static final String eliminarEleitorPopUpTitle = "Eliminar eleitor";
    
    //Candidato
    public static final String eliminarCandidatoPopUpText = "Tem a certeza que pretende eliminar este candidato?";
    public static final String eliminarCandidatoPopUpTitle = "Eliminar candidato";
    
    //Erro dialog
    public static final String exceptionDialogPopUpTitle = "Erro!";
    public static final String infoDialogPopUpTitle = "Info!";
    
    //Sizes
    public static final int maxSizeForTextBox = 50;
    public static final int maxSizeForSigla=6;
    public static final int maxSizeForCC = 8;
    
    //Files
    public static final String electorFilePath = "electors.txt";
    public static final String candidateFilePath = "candidate.txt";
    public static final String electionFilePath = "election.txt";
    public static final String blankCandidateName = "Voto em Branco";
    
    //user directory
    public static final String userSystemDir = "user.dir";
    
    //Resources
    public static final String personResource = "/electionspoo/multimedia/person.png";
    public static final String blankResource = "/electionspoo/multimedia/blank.png";
    public static final String manuCandidatosResource = "/electionspoo/multimedia/menu_candidates.png";
    
    public static final int transactionListSize = 2;
    public static final int minerDifficulty = 1;
}
