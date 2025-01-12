package bloomfilter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        // Test with 10,000 elements as per requirements
        int numInserts = 10_000;
        Random rng = new Random();

        // Test with different bloom filter sizes
        int[] bloomSizes = {64, 256, 1024, 4096};

        for (int bloomSize : bloomSizes) {
            System.out.println("\nTesting with bloom filter size: " + bloomSize + " bytes");

            BloomFilter<Integer> filter = new BloomFilter<>(bloomSize);
            BloomList<Integer> bloomList = new BloomList<>(filter);
            List<Integer> list = new LinkedList<>();

            // Insert 10,000 random numbers
            for (int i = 0; i < numInserts; i++) {
                int n = rng.nextInt();
                bloomList.add(n);
                list.add(n);
            }

            // Test lookup times with 1000 random numbers (likely not in the list)
            int numLookups = 1000;

            long t0 = System.nanoTime();
            for (int i = 0; i < numLookups; i++) {
                list.contains(rng.nextInt());
            }
            long t1 = System.nanoTime();
            System.out.println("LinkedList lookup time: " + ((t1 - t0) / 1_000_000.0) + " ms");

            t0 = System.nanoTime();
            for (int i = 0; i < numLookups; i++) {
                bloomList.contains(rng.nextInt());
            }
            t1 = System.nanoTime();
            System.out.println("BloomList lookup time: " + ((t1 - t0) / 1_000_000.0) + " ms");
        }
    }
}