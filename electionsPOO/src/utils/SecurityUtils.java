///****************************************************************************/
///****************************************************************************/
///****     Copyright (C) 2012                                             ****/
///****     Antonio Manuel Rodrigues Manso                                 ****/
///****     e-mail: manso@ipt.pt                                           ****/
///****     url   : http://orion.ipt.pt/~manso                             ****/
///****     Instituto Politecnico de Tomar                                 ****/
///****     Escola Superior de Tecnologia de Tomar                         ****/
///****************************************************************************/
///****************************************************************************/
///****     This software was built with the purpose of investigating      ****/
///****     and learning. Its use is free and is not provided any          ****/
///****     guarantee or support.                                          ****/
///****     If you met bugs, please, report them to the author             ****/
///****                                                                    ****/
///****************************************************************************/
///****************************************************************************/
package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Algoritmos de segurança
 *
 * @author ZULU
 */
public class SecurityUtils {

    /**
     * encrypt data with a key
     *
     * @param data data to encrypt
     * @param key key of encriptation
     * @return encripted data
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {

        // chiper object with algorithm of the key
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        //configure to encrypt
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //encrypt data
        return cipher.doFinal(data);
    }

    /**
     * decrypt data
     *
     * @param data encrypted data
     * @param key key of encryptation
     * @return decrypted data
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        //cipher object
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        //configure to decrypt
        cipher.init(Cipher.DECRYPT_MODE, key);
        //decrypt message
        return cipher.doFinal(data);
    }

    /**
     * Assina os dados passados com a chave privada passada
     *
     * @param data dados a serem utilizados para a assinatura
     * @param key chave que irá assinar os dados
     * @return A assinatura em um array de bytes
     * @throws Exception Caso ocorra algum erro, como por exemplo o algoritmo
     * não existir
     */
    public static byte[] sign(byte[] data, PrivateKey key) throws Exception {
        Signature shaWith = Signature.getInstance("SHA256withRSA");
        //inicializa a assinatura com a chave
        shaWith.initSign(key);
        //assina os dados
        shaWith.update(data);
        //devolve a assinatura
        return shaWith.sign();
    }

    /**
     * Verifica se assinatura é valida
     *
     * @param data dados assinados a serem validados com a assinatura
     * @param signature assinatura a ser validado
     * @param key cahve publica que faz par com a chave privada que foi
     * utilizada na assinatura
     * @return se a assinatura é valida
     * @throws Exception Caso ocorra algum erro, como por exemplo o algoritmo
     * não existir
     */
    public static boolean verifySign(byte[] data, byte[] signature, PublicKey key) throws Exception {
        Signature shaWith = Signature.getInstance("SHA256withRSA");
        //inicializa a validação da assinatura com a chave
        shaWith.initVerify(key);
        //verifica se assinatura é valida para os dados dados e para assinatura dada
        shaWith.update(data);
        return shaWith.verify(signature);
    }

    /**
     * Gera uma chave de criptogradia simetrica AES
     *
     * @param keySize tamanho da chave
     * @return chave cahve simétrica gerada
     * @throws Exception muito improvável de ocurrer
     */
    public static Key generateAESKey(int keySize) throws Exception {
        // gerador de chaves
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        //tamanho da chave
        keyGen.init(keySize);
        //gerar a chave
        Key key = keyGen.generateKey();
        return key;
    }

    /**
     * carrega uma chave AES
     *
     * @param key chave em array de bytes
     * @return chave chave carregada através da base64
     */
    public static Key getAESKey(byte[] key) {
        return new SecretKeySpec(key, "AES");
    }

    /**
     * gera um par de chave RSA
     *
     * @param size tamanho da chave tamanho que as chaves irão ter
     * @return par de chaves o par de chaves gerado
     * @throws Exception caso ocorra algum tipo de erro na geração das chaves
     */
    public static KeyPair generateKeyPair(int size) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        //tamanho da chave
        keyGen.initialize(size);
        //devolve o par de chaves gerado
        return keyGen.generateKeyPair();
    }

    /**
     * Transforma um array de bytes representante de uma chave publica em chave
     * publica
     *
     * @param pubData array de bytes representante da chave
     * @return a chave publica em forma de chave
     * @throws Exception Caso ocorra algum erro:
     * <code>NoSuchAlgorithmException</code>,<code>InvalidKeySpecException</code>,<code>NullPointerException</code>
     * e <code>NoSuchProviderException</code>
     */
    public static PublicKey getPublicKey(byte[] pubData) throws Exception {
        //especifacção do encoding da chave publica X509
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //Gerar a chave pública
        return keyFactory.generatePublic(pubSpec);
    }

    /**
     * Transforma um array de bytes representante de uma chave privada em chave
     * privada
     *
     * @param privData array de bytes representante da chave
     * @return a chave privada em forma de chave
     * @throws Exception Caso ocorra algum erro:
     * <code>NoSuchAlgorithmException</code>,<code>InvalidKeySpecException</code>,<code>NullPointerException</code>
     * e <code>NoSuchProviderException</code>
     */
    public static PrivateKey getPrivateKey(byte[] privData) throws Exception {
        //especificações da chave privada PKCS8
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //Gerar a chave privada
        return keyFactory.generatePrivate(privSpec);
    }

    /**
     * Transforma uma string em base64 representante de uma chave publica em
     * chave publica
     *
     * @param b64 string em base64 representante da chave
     * @return a chave publica em forma de chave
     * @throws Exception Caso ocorra algum erro:
     * <code>NoSuchAlgorithmException</code>,<code>InvalidKeySpecException</code>,<code>NullPointerException</code>
     * e <code>NoSuchProviderException</code>
     */
    public static PublicKey getPublicKey(String b64) throws Exception {
        return getPublicKey(Base64.getDecoder().decode(b64));
    }

    /**
     * Transforma uma string em base64 representante de uma chave privada em
     * chave privada
     *
     * @param b64 string em base64 representante da chave
     * @return a chave privada em forma de chave
     * @throws Exception Caso ocorra algum erro:
     * <code>NoSuchAlgorithmException</code>,<code>InvalidKeySpecException</code>,<code>NullPointerException</code>
     * e <code>NoSuchProviderException</code>
     */
    public static PrivateKey getPrivateKey(String b64) throws Exception {
        return getPrivateKey(Base64.getDecoder().decode(b64));
    }

    /**
     * Guarda uma chave num ficheiro
     *
     * @param key chave a ser armazenada
     * @param file nome do ficheiro
     * @throws IOException caso não haja permissão para aceder ou escrever ao
     * ficheiro indicado
     */
    public static void saveKey(KeyPair key, String file) throws IOException {
        saveKey(key.getPublic(), file + ".pub");
        saveKey(key.getPrivate(), file + ".priv");
    }

    /**
     * Guarda uma chave num ficheiro
     *
     * @param key chave a ser armazenada
     * @param file nome do ficheiro
     * @throws IOException caso não haja permissão para aceder ou escrever ao
     * ficheiro indicado
     */
    public static void saveKey(Key key, String file) throws IOException {
        Files.write(Paths.get(file), key.getEncoded());
    }

    /**
     * Carrega uma chave de um ficheiro
     *
     * @param file nome do ficheiro
     * @return a chave que estava armazenada no ficheiro
     * @throws IOException caso não haja permissão para aceder ou ler ao
     * ficheiro indicado
     */
    public static Key loadKey(String file) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(file));
        try {
            return getPublicKey(encoded);
        } catch (Exception e) {
            try {
                return getPrivateKey(encoded);
            } catch (Exception ex) {
                return getAESKey(encoded);
            }
        }
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::        PASSWORD BASED ENCRYPTATION                 :::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Cria um objecto de cifragem com PBE SHA1 e triple DES
     *
     * @param mode Cipher.DECRYPT_MODE ou Cipher.ENCRYPT_MODE
     * @param password password de da cifra
     * @return Objecto de cifragem
     * @throws Exception
     */
    public static Cipher createCipherPBE(int mode, String password) throws Exception {
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::   1 - gerar uma chave secreta  :::::::::::::::::::::::::::::
        //transformar a password nos parametros na chave
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        //algoritmo de cifragem
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede");
        //gerar a chave
        SecretKey key = keyFactory.generateSecret(keySpec);
        //::::::::: 2 -  adicionar sal á chave  :::::::::::::::::::::::::::::::
        // usar a password para inicializar o secure
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //::::::::: 2 -  adicionar sal á chave  :::::::::::::::::::::::::::::::
        // usar o SHA1 para gerar um conjunto de  bytes a partir da password
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(password.getBytes());
        //usar os primeiros 8 bytes
        byte[] digest = Arrays.copyOf(md.digest(), 8);
        //fazer 1000 iterações com o sal
        PBEParameterSpec paramSpec = new PBEParameterSpec(digest, 1000);

        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::::   3 - Gerar o objecto de cifragem      :::::::::::::::::::
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //:::::::::   4 - iniciar a cifra ::::::::: ::::::::: ::::::::: ::::::::: 
        // iniciar o objeto de cifragem com os parâmetros
        cipher.init(mode, key, paramSpec);
        return cipher;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::::               ENCRYPT /  DECRYPT                   :::::::::::
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    /**
     * encripta dados usando uma password de texto
     *
     * @param data dados para encriptar
     * @param password password de encriptação
     * @return dados encriptados
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String password) throws Exception {
        //criar um objecto de cifragem da chave
        Cipher cipher = createCipherPBE(Cipher.ENCRYPT_MODE, password);
        //cifrar os dados
        return cipher.doFinal(data);
    }

    /**
     * desencripta dados usando uma password de texto
     *
     * @param data dados para desencriptar
     * @param password password de desencriptação
     * @return dados desencriptados
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String password) throws Exception {
        //criar um objecto de cifragem da chave
        Cipher cipher = createCipherPBE(Cipher.DECRYPT_MODE, password);
        //cifrar os dados
        return cipher.doFinal(data);
    }
   //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::      I N T E G R I T Y         :::::::::::::::::::::::::::::::::    
    ///////////////////////////////////////////////////////////////////////////
   /**
     * calcula o hash de uma mensagem
     *
     * @param data dados da mensagem
     * @param algorithm algorithm
     * @return hash
     * @throws Exception
     */
    public static byte[] getHash(byte[] data, String algorithm)
            throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(data);
        return md.digest();
    }
    /**
     * calcula o hash de uma mensagem com o algoritm SHA256
     *
     * @param data dados da mensagem
     * @return hash
     * @throws Exception
     */
    public static byte[] getHash(byte[] data) throws Exception {
        return getHash(data, "SHA-256");
    }
     /**
     * calcula o hash de uma mensagem com o algoritm SHA256
     *
     * @param data dados da mensagem
     * @param algorithm algoritmo de hash
     * @return hash
     * @throws Exception
     */
    public static String getHash(String data, String algorithm) throws Exception {
        return Base64.getEncoder().encodeToString(
                getHash(data.getBytes(), algorithm));
    }
    
     /**
     * calcula o hash de uma mensagem com o algoritm SHA256
     *
     * @param data dados da mensagem
     * @param algorithm algoritmo de hash
     * @return hash
     * @throws Exception
     */
    public static String getHash(String data) throws Exception {
        return getHash(data, "SHA-256");
    }   

    /**
     * verifica o hash de uma mensagem
     *
     * @param data dados da mensagem
     * @param hash hash de verificação
     * @param algorithm algoritmo
     * @return
     * @throws Exception
     */
    public static boolean verifyHash(byte[] data, byte[] hash, String algorithm)
            throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(data);
        byte[] trueHash = md.digest();
        return Arrays.equals(trueHash, hash);
    }

    /**
     * verifica o hash de uma mensagem com o algoritm SHA256
     *
     * @param data dados da mensagem
     * @param hash hash dos dados
     * @return hash
     * @throws Exception
     */
    public static boolean verifyHash(byte[] data, byte[] hash) throws Exception {
        return verifyHash(data, hash, "SHA-256");
    }
    
    /**
     * verifica o hash de uma mensagem com o algoritm SHA256
     *
     * @param data dados da mensagem
     * @param hash hash dos dados
     * @return hash
     * @throws Exception
     */
    public static boolean verifyHash(String data, String hash) throws Exception {
        return verifyHash(
                Base64.getDecoder().decode(data),
                Base64.getDecoder().decode(hash),
                "SHA-256");
    }
    
    
    
      //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    //:::::::::      I N T E G R I T Y         :::::::::::::::::::::::::::::::::    
    ///////////////////////////////////////////////////////////////////////////
   

}
