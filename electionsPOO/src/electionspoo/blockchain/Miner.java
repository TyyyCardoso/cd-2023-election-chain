package electionspoo.blockchain;

import electionspoo.utils.blockchain.Hash;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Miner implements Runnable {
    public static int MAX_NONCE = (int)1E9;

    private String data;
    private int difficulty;
    private int nonceResult = -1;
    private final Consumer<Integer> callback;
    private static final AtomicBoolean nonceFound = new AtomicBoolean(false);
    private int startNonce;
    private int endNonce;

    public Miner(String data, int difficulty, int startNonce, int endNonce, Consumer<Integer> callback) {
        this.data = data;
        this.difficulty = difficulty;
        this.callback = callback;
        this.startNonce = startNonce;
        this.endNonce = endNonce;
    }

    @Override
    public void run() {
        if (!nonceFound.get()) {
            nonceResult = getNonce(data, difficulty, startNonce, endNonce);
            if (nonceResult != -1) {
                nonceFound.set(true);
                if (callback != null) {
                    SwingUtilities.invokeLater(() -> callback.accept(nonceResult));
                }
            }
        }
    }

    private int getNonce(String data, int difficulty, int start, int end) {
        String zeros = String.format("%0" + difficulty + "d", 0);
        for (int nonce = start; nonce <= end && nonce < MAX_NONCE; nonce++) {
            System.out.println(Thread.currentThread().getName() + " checking nonce: " + nonce);
            if (nonceFound.get()) return -1;  // Stop processing if nonce is found by another thread
            String hash = Hash.getHash(nonce + data);
            if (hash.startsWith(zeros)) {
                return nonce;
            }
        }
        return -1;
    }

    // This method will start multiple threads for mining based on the number of cores
    public static void startMining(String data, int difficulty, int numberOfCores, Consumer<Integer> callback) {
        nonceFound.set(false); // Reset the flag for a new mining process
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCores);
        int range = MAX_NONCE / numberOfCores;
        for (int i = 0; i < numberOfCores; i++) {
            int start = i * range;
            int end = (i == numberOfCores - 1) ? MAX_NONCE : start + range - 1;
            executor.execute(new Miner(data, difficulty, start, end, callback));
        }
        executor.shutdown();  // The executor will stop accepting new tasks but will finish the current ones
    }
}
