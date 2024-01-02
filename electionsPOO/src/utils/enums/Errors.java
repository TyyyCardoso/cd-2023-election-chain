/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package utils.enums;

/**
 *
 * @author User
 */
public enum Errors {
    
    ElectionNotStarted("Esta eleição já acabou, não pode votar mais!"),
    EmptyLists("As listas de eleitores ou candidatos não podem estar vazias!"),
    AlreadyVoted("Você já votou!"),
    CheckVote("Quer validar em quem votou?"),
    MoreThan8Chars("Número de cartão de cidadão não pode ter menos que 8 caracteres!"),
    NumberFormatIncorret("Por favor insira um número válido!"),
    DateFormatIncorret("Ocorreu um erro na validação das datas, por favor, insira um formato válido. Exemplo: dd/mm/yyyy"),
    OpeningNewPanelError("Ocorreu um erro ao tentar abrir a próxima página, por favor, tente novamente"),
    MoreThan6Chars("Sigla não pode ter mais que 6 caracteres!"),
    ErrorExecutingAction("Ocorreu um erro ao tentar executar a ação pretendida, por favor, tente novamente"),
    MoreThan50Chars("Nome não pode ter mais que 50 caracteres!"),
    NoRecordsFound("Não foram encontrados registos com esse nome!"),
    UnavailableFunctionality("Funcionalidade indisponivel!"),
    FileManipulation("Ocorreu um erro ao tentar abrir/fechar os ficheiros, por favor, tente novamente."),
    ElectionDatesWrong("Não podes iniciar este eleição porque ainda não está no dia correto!");
    
    private final String erro;

    private Errors(String erro){
        this.erro = erro;
    }
    
    public String getErro(){
        return this.erro;
    }
    
}
