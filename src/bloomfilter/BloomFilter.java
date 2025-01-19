package bloomfilter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class BloomFilter<E> {
    protected static final int H_MUL = 31;

    private final byte[] bits;
    private final Function<Integer, Integer>[] hf;

    public BloomFilter(DataInput input) throws IOException {
        this(input.readInt());
        // Read the serialized bits
        for (int i = 0; i < bits.length; i++) {
            byte currentByte = input.readByte();
            bits[i] = currentByte;
        }
    }

    public BloomFilter(final int numBytes) {
        final int vecSize = numBytes * Byte.SIZE;
        this.bits = new byte[numBytes * Byte.SIZE];
        // False positive rate of 1%:
        
        this.hf = new Function[(int) -(Math.log(0.01) / Math.log(2))];
        for (int i = 0; i < this.hf.length; i++) {
            final int j = i;
            this.hf[i] = k -> (((j * H_MUL * k) % vecSize) + vecSize) % vecSize;
        }
    }

    public void close(DataOutput output) throws IOException {
        output.writeInt(bits.length / Byte.SIZE);
        // Write each byte directly
        for (byte bit : bits) {
            output.writeByte(bit);
        }
    }

    public void add(E element) {
        if (element == null) return;
        int hashCode = element.hashCode();
        for (Function<Integer, Integer> hashFunction : hf) {
            int index = hashFunction.apply(hashCode);
            bits[index] = 1;
        }
    }

    public boolean containsMaybe(E element) {
        if (element == null) return false;
        int hashCode = element.hashCode();
        for (Function<Integer, Integer> hashFunction : hf) {
            int index = hashFunction.apply(hashCode);
            if (bits[index] != 1) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        Arrays.fill(bits, (byte) 0);
    }
}