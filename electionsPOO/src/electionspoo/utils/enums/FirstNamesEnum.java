/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package electionspoo.utils.enums;

/**
 *
 * @author User
 */
public enum FirstNamesEnum {
    //MALE NAMES
    Tiago('M'),
    Luis('M'),
    Ambrósio('M'),
    Tomás('M'),
    Cristiano('M'),
    Hélder('M'),
    Simão('M'),
    Marco('M'),
    Manuel('M'),
    Guilherme('M'),
    Leonardo('M'),
    Frederico('M'),
    Jorge('M'),
    Álvaro('M'),
    William('M'),
    Dinis('M'),
    Denis('M'),
    Afonso('M'),
    Carlos('M'),
    Diogo('M'),
    Vasco('M'),
    Bernardo('M'),
    Gabriel('M'),
    Samuel('M'),
    Bruno('M'),
    //FEMALE NAMES
    Mara('F'),
    Neuza('F'),
    Patrícia('F'),
    Íris('F'),
    Liliana('F'),
    Viviane('F'),
    Adriana('F'),
    Ana('F'),
    Catarina('F'),
    Eduarda('F'),
    Sofia('F'),
    Ariana('F'),
    Madalena('F'),
    Leticia('F'),
    Tatiana('F'),
    Bárbara('F'),
    Iara('F'),
    Marta('F'),
    Jéssica('F'),
    Carolina('F'),
    Michele('F'),
    Alice('F'),
    Elisabete('F'),
    Renata('F'),
    Gabriela('F');
     
    private final char gender;
    
    private FirstNamesEnum(char gender){
        this.gender = gender;
    }
    
    public char getGender(){
        return this.gender;
    }
}
