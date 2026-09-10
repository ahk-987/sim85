package sim85.core;

public class Memory {
    final private int[] memory ; // 64KB of memory
    private int size;
    public Memory()
    {
        memory= new int[65536];
    }
    public Memory(int size)
    {
        this.size=size;
        memory = new int [size];
    }
    public int read(int address) {
        return memory[address & 0xFFFF];
    }

    public void write(int address, int value) {
        memory[address & 0xFFFF] = value & 0xFF; // Ensure value is 8-bit
    }

    public void reset() {
        java.util.Arrays.fill(memory, 0);
    }

    public int size() {
        return size;
    }
}
