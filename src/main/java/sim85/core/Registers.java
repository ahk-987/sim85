package sim85.core;

public class Registers {
    private int A, B, C, D, E, H, L; // 8-bit registers
    private int PC,SP;
    public enum Reg {
        A, B, C, D, E, H, L, PC, SP
    }
    public int get(Reg register) {
        switch (register) {
            case A: return A;
            case B: return B;
            case C: return C;
            case D: return D;
            case E: return E;
            case H: return H;
            case L: return L;
            case PC: return PC;
            case SP: return SP;
            default: throw new IllegalArgumentException("Invalid register: " + register);
        }
    }

    public void set(Reg register, int value) {
        switch (register) {
            case A: A = value & 0xFF; break;
            case B: B = value & 0xFF; break;
            case C: C = value & 0xFF; break;
            case D: D = value & 0xFF; break;
            case E: E = value & 0xFF; break;
            case H: H = value & 0xFF; break;
            case L: L = value & 0xFF; break;
            case PC: PC = value & 0xFFFF; break;
            case SP: SP = value & 0xFFFF; break;
            default: throw new IllegalArgumentException("Invalid register: " + register);
        }
    }
    
    public int getPair(Reg high, Reg low) {
    return (get(high) << 8) | get(low);
    }

    public void setPair(Reg high, Reg low, int value) {
        set(high, (value >> 8) & 0xFF);
        set(low, value & 0xFF);
    }

}
