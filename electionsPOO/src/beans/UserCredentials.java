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
//::                                                               (c)2022   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package beans;

import utils.SecurityUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created on 28/10/2022, 15:37:44
 *
 * @author aulas - computer
 */
public class UserCredentials{

    public static String USER_PATH = "users/";

    String name;
    PrivateKey privKey;
    PublicKey pubKey;
    Key key;

    /**
     * construtor privado não é possivel fazer users fora desta classe
     */
    private UserCredentials(String name) {
        this.name = name;
        this.privKey = null;
        this.pubKey = null;
        this.key = null;
    }

    public String getName() {
        return name;
    }

    public PrivateKey getPrivKey() {
        return privKey;
    }

    public PublicKey getPubKey() {
        return pubKey;
    }

    public Key getKey() {
        return key;
    }

    /**
     * Registar um utilizador
     *
     * @param name nome do utilizador
     * @param password password para segurança das chaves
     * @throws Exception
     */
    public static UserCredentials registar(String name, String password) throws Exception {
        //criar as pastas se necessário
        new File(USER_PATH).mkdirs();
        //criar um utilizador
        UserCredentials user = new UserCredentials(name);
        //gerar as chaves
        KeyPair kp = SecurityUtils.generateKeyPair(SIZE_RSA_KEY);
        user.privKey = kp.getPrivate();
        user.pubKey = kp.getPublic();
        user.key = SecurityUtils.generateAESKey(SIZE_AES_KEY);
        //:::::::::::::::::::::::::::::::
        // guardar as chaves
        Files.write(Paths.get(USER_PATH + name + ".pub"), user.pubKey.getEncoded());
        //encriptar a chave privada com a password
        byte[] dataPriv = user.privKey.getEncoded();
        dataPriv = SecurityUtils.encrypt(dataPriv, password);
        Files.write(Paths.get(USER_PATH + name + ".priv"), dataPriv);
        //encriptar a chave simetrica coma chave pública
        byte[] dataKey = SecurityUtils.encrypt(user.key.getEncoded(), user.pubKey);
        Files.write(Paths.get(USER_PATH + name + ".sim"), dataKey);
        return user;
    }

    /**
     * Ler a chave pública de um utilizador
     *
     * @param name nome do utilizador
     * @return Utilizador com a chave pública
     * @throws Exception
     */
    public static UserCredentials load(String name) throws Exception {
        UserCredentials user = new UserCredentials(name);
        //ler os dados da chave pública
        byte[] pubData = Files.readAllBytes(Paths.get(USER_PATH + name + ".pub"));
        //chave publica
        user.pubKey = SecurityUtils.getPublicKey(pubData);
        return user;
    }

    /**
     * L~es as credenciais de um utilizador
     *
     * @param name nome do utilizador
     * @param password passwords para desencriptar as chaves
     * @return Utilizador com condenciais
     * @throws Exception
     */
    public static UserCredentials autenticar(String name, String password) throws Exception {
        try {
            UserCredentials user = new UserCredentials(name);
            //ler os dados das chaves
            byte[] pubData = Files.readAllBytes(Paths.get(USER_PATH + name + ".pub"));
            byte[] privData = Files.readAllBytes(Paths.get(USER_PATH + name + ".priv"));
            byte[] simData = Files.readAllBytes(Paths.get(USER_PATH + name + ".sim"));
            //Construir as chaves com os dados
            try {
                //chave publica
                user.pubKey = SecurityUtils.getPublicKey(pubData);
                //desencriptar chave privada com a password
                privData = SecurityUtils.decrypt(privData, password);
                user.privKey = SecurityUtils.getPrivateKey(privData);
                //desencriptar chave simetrica com a chave privada
                simData = SecurityUtils.decrypt(simData, user.privKey);
                user.key = SecurityUtils.getAESKey(simData);
                return user;
            } catch (Exception e) {
                throw new Exception("Wrong password");
            }

        } catch (Exception e) {
            throw new Exception("user name not registred :" + e.getMessage());
        }
    }

    /**
     * lê a lista de utilizadores registados
     *
     * @return
     */
    public static List<UserCredentials> getUserList() {
        List<UserCredentials> lst = new ArrayList<>();
        //Ler os ficheiros da path dos utilizadores
        File[] files = new File(USER_PATH).listFiles();
        if (files == null) {
            return lst;
        }
        //contruir um user com cada ficheiros
        for (File file : files) {
            //se for uma chave publica
            if (file.getName().endsWith(".pub")) {
                //nome do utilizador
                String userName = file.getName().substring(0, file.getName().lastIndexOf("."));
                try {
                    lst.add(load(userName));
                } catch (Exception e) {
                }
            }
        }
        return lst;

    }

    @Override
    public String toString() {
        return name;
    }

    public String getInfo() {
        StringBuilder txt = new StringBuilder();
        txt.append("Name : " + name);
        txt.append("\nSimetric Key Algorithm   : " + pubKey.getAlgorithm());
        txt.append("\nSimetric Key Size        : " + SIZE_RSA_KEY);
        txt.append("\nAssimetric Key Algorithm : " + key.getAlgorithm());
        txt.append("\nAssimetric Key Size      : " + SIZE_AES_KEY);
        txt.append("\n\nFiles :");
        txt.append("\n" + USER_PATH + name + ".pub");
        txt.append("\n" + USER_PATH + name + ".priv");
        txt.append("\n" + USER_PATH + name + ".sim");
        return txt.toString();
    }

    public byte[] sign(byte[] data) throws Exception {
        return SecurityUtils.sign(data, privKey);
    }

    public boolean verifySignature(byte[] data, byte[] sign) throws Exception {
        return SecurityUtils.verifySign(data, sign, pubKey);
    }
    
    public String getBase64PublicKey(){
        return Base64.getEncoder().encodeToString(pubKey.getEncoded());
    }
    
    public static UserCredentials getFromPublicKey(String publicKey) throws Exception{
        List<UserCredentials> lst = getUserList();
        PublicKey pk = SecurityUtils.getPublicKey(
        Base64.getDecoder().decode(publicKey)
        );
        
        for (UserCredentials user : lst) {
           if( user.getPubKey().equals(pk))
               return user;
        }
        return null;
    }

    public static int SIZE_RSA_KEY = 2048;
    public static int SIZE_AES_KEY = 256;

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202210281537L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
