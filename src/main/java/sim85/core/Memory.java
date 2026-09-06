package sim85.core;

public class Memory {
    private int[] memory = new int[65536]; // 64KB of memory

    public int read(int address) {
        return memory[address & 0xFFFF];
    }

    public void write(int address, int value) {
        memory[address & 0xFFFF] = value & 0xFF; // Ensure value is 8-bit
    }

    public void reset() {
        java.util.Arrays.fill(memory, 0);
    }
}
