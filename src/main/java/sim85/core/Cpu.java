package sim85.core;
import sim85.core.Registers.Reg;
public class Cpu{
    private boolean running=true; 
    final private Memory ram ;
    final private Flags flags ;
    final private Registers registers ;
    
    private void loadImmediate() //Loads Immediate data to Reg I 
    {
        registers.incrementRegister(Reg.PC);
        int immediateData=ram.read(registers.get(Reg.PC));
        registers.set(Reg.I,immediateData);
    }
    private void inx(Reg regPair ,boolean increase)
    {
        int valueOfPair;
        switch (regPair) {
            case B -> {
                valueOfPair =registers.getPair(Reg.B, Reg.C)+(increase?1:-1);
                registers.setPair(Reg.B,Reg.C,valueOfPair);
            }
            case D -> {valueOfPair =registers.getPair(Reg.D, Reg.E)+(increase?1:-1);
                registers.setPair(Reg.D,Reg.E,valueOfPair);
            }
            case H -> {valueOfPair =registers.getPair(Reg.H, Reg.L)+(increase?1:-1);
                registers.setPair(Reg.H,Reg.L,valueOfPair);
            }
            case SP -> {
                valueOfPair = registers.get(Reg.SP)+(increase?1:-1);
                registers.set(Reg.SP,valueOfPair);
            }
            default -> throw new IllegalArgumentException("Invalid Register Pair");
        }
    }

    private void inr(Reg register,boolean increase){
        if(register == Reg.M)
        {
            int address=registers.getPair(Reg.H,Reg.L);
            int valueOfAddress=ram.read(address)+(increase?1:-1);
            ram.write(address,valueOfAddress);
        }
        else{
            int value=registers.get(register)+(increase?1:-1);
            registers.set(register,value);
            flags.updateZSP(value);
        }
    }

    private void mov(Reg source,Reg destination){
        int sourceValue;
        if(source==Reg.M)
        {
            sourceValue=ram.read(registers.getPair(Reg.H,Reg.L));
        }
        else{
            sourceValue=registers.get(source);
        }
        if(destination==Reg.M)
        {
            ram.write(registers.getPair(Reg.H,Reg.L),sourceValue);
        }
    }

    private void add(Reg register,boolean useCarry)
    {
        int regValue;
        if(register==Reg.M)
        {
            regValue=ram.read(registers.getPair(Reg.H,Reg.L));
        }
        else{
            regValue=registers.get(register);
        }
        int aReg=registers.get(Reg.A);
        int result=aReg+regValue + (int)(useCarry?flags.isCarry():0);
        registers.set(register, result);
        flags.updateAllFlags(result);
    }

    private void decode(int instruction)
    {
        switch (instruction) {
    case 0x00 -> {} // NOP
    case 0x01 -> {} // LXI B
    case 0x02 -> {} // STAX B
    case 0x03 -> inx(Reg.B,true); // INX B
    case 0x04 -> inr(Reg.B,true); // INR B
    case 0x05 -> inr(Reg.B,false); // DCR B
    case 0x06 -> {
        loadImmediate();
        mov(Reg.I,Reg.B);
    } // MVI B
    case 0x07 -> {} // RLC
    case 0x09 -> {} // DAD B
    case 0x0A -> {} // LDAX B
    case 0x0B -> inx(Reg.B,false); // DCX B
    case 0x0C -> inr(Reg.C,true); // INR C
    case 0x0D -> inr(Reg.C,false); // DCR C
    case 0x0E -> {
        loadImmediate();
        mov(Reg.I,Reg.C);
    } // MVI C
    case 0x0F -> {} // RRC
    case 0x11 -> {} // LXI D
    case 0x12 -> {} // STAX D
    case 0x13 -> inx(Reg.D,true); // INX D
    case 0x14 -> inr(Reg.D,true); // INR D
    case 0x15 -> inr(Reg.D,false); // DCR D
    case 0x16 -> {
        loadImmediate();
        mov(Reg.I,Reg.D);
    } // MVI D
    case 0x17 -> {} // RAL
    case 0x19 -> {} // DAD D
    case 0x1A -> {} // LDAX D
    case 0x1B -> inx(Reg.D,false); // DCX D
    case 0x1C -> inr(Reg.E,true); // INR E
    case 0x1D -> inr(Reg.E,false); // DCR E
    case 0x1E -> {
        loadImmediate();
        mov(Reg.I,Reg.E);
    } // MVI E
    case 0x1F -> {} // RAR
    case 0x20 -> {} // RIM
    case 0x21 -> {} // LXI H
    case 0x22 -> {} // SHLD
    case 0x23 -> inx(Reg.H,true); // INX H
    case 0x24 -> inr(Reg.H,true); // INR H
    case 0x25 -> inr(Reg.H,false); // DCR H
    case 0x26 -> {
        loadImmediate();
        mov(Reg.I,Reg.H);
    } // MVI H
    case 0x27 -> {} // DAA
    case 0x29 -> {} // DAD H
    case 0x2A -> {} // LHLD
    case 0x2B -> inx(Reg.H,false); // DCX H
    case 0x2C -> inr(Reg.L,true); // INR L
    case 0x2D -> inr(Reg.L,false); // DCR L
    case 0x2E -> {
        loadImmediate();
        mov(Reg.I,Reg.L);
    } // MVI L
    case 0x2F -> {} // CMA
    case 0x30 -> {} // SIM
    case 0x31 -> {} // LXI SP
    case 0x32 -> {} // STA
    case 0x33 -> inx(Reg.SP,true); // INX SP
    case 0x34 -> inr(Reg.M,true); // INR M
    case 0x35 -> inr(Reg.M,false); // DCR M
    case 0x36 -> {
        loadImmediate();
        mov(Reg.I,Reg.M);
    } // MVI M
    case 0x37 -> {} // STC
    case 0x39 -> {} // DAD SP
    case 0x3A -> {} // LDA
    case 0x3B -> inx(Reg.SP,false); // DCX SP
    case 0x3C -> inr(Reg.A,true); // INR A
    case 0x3D -> inr(Reg.A,false); // DCR A
    case 0x3E -> {
        loadImmediate();
        mov(Reg.I,Reg.A);    
    } // MVI A
    case 0x3F -> {} // CMC

    case 0x40 -> mov(Reg.B,Reg.B); // MOV B,B
    case 0x41 -> mov(Reg.B,Reg.C); // MOV B,C
    case 0x42 -> mov(Reg.B,Reg.D); // MOV B,D
    case 0x43 -> mov(Reg.B,Reg.E); // MOV B,E
    case 0x44 -> mov(Reg.B,Reg.H); // MOV B,H
    case 0x45 -> mov(Reg.B,Reg.L); // MOV B,L
    case 0x46 -> mov(Reg.B,Reg.M); // MOV B,M
    case 0x47 -> mov(Reg.B,Reg.A); // MOV B,A
    case 0x48 -> mov(Reg.C,Reg.B); // MOV C,B
    case 0x49 -> mov(Reg.C,Reg.C); // MOV C,C
    case 0x4A -> mov(Reg.C,Reg.D); // MOV C,D
    case 0x4B -> mov(Reg.C,Reg.E); // MOV C,E
    case 0x4C -> mov(Reg.C,Reg.H); // MOV C,H
    case 0x4D -> mov(Reg.C,Reg.L); // MOV C,L
    case 0x4E -> mov(Reg.C,Reg.M); // MOV C,M
    case 0x4F -> mov(Reg.C,Reg.A); // MOV C,A
    case 0x50 -> mov(Reg.D,Reg.B); // MOV D,B
    case 0x51 -> mov(Reg.D,Reg.C); // MOV D,C
    case 0x52 -> mov(Reg.D,Reg.D); // MOV D,D
    case 0x53 -> mov(Reg.D,Reg.E); // MOV D,E
    case 0x54 -> mov(Reg.D,Reg.H); // MOV D,H
    case 0x55 -> mov(Reg.D,Reg.L); // MOV D,L
    case 0x56 -> mov(Reg.D,Reg.M); // MOV D,M
    case 0x57 -> mov(Reg.D,Reg.A); // MOV D,A
    case 0x58 -> mov(Reg.E,Reg.B); // MOV E,B
    case 0x59 -> mov(Reg.E,Reg.C); // MOV E,C
    case 0x5A -> mov(Reg.E,Reg.D); // MOV E,D
    case 0x5B -> mov(Reg.E,Reg.E); // MOV E,E
    case 0x5C -> mov(Reg.E,Reg.H); // MOV E,H
    case 0x5D -> mov(Reg.E,Reg.L); // MOV E,L
    case 0x5E -> mov(Reg.E,Reg.M); // MOV E,M
    case 0x5F -> mov(Reg.E,Reg.A); // MOV E,A
    case 0x60 -> mov(Reg.H,Reg.B); // MOV H,B
    case 0x61 -> mov(Reg.H,Reg.C); // MOV H,C
    case 0x62 -> mov(Reg.H,Reg.D); // MOV H,D
    case 0x63 -> mov(Reg.H,Reg.E); // MOV H,E
    case 0x64 -> mov(Reg.H,Reg.H); // MOV H,H
    case 0x65 -> mov(Reg.H,Reg.L); // MOV H,L
    case 0x66 -> mov(Reg.H,Reg.M); // MOV H,M
    case 0x67 -> mov(Reg.H,Reg.A); // MOV H,A
    case 0x68 -> mov(Reg.L,Reg.B); // MOV L,B
    case 0x69 -> mov(Reg.L,Reg.C); // MOV L,C
    case 0x6A -> mov(Reg.L,Reg.D); // MOV L,D
    case 0x6B -> mov(Reg.L,Reg.E);// MOV L,E
    case 0x6C -> mov(Reg.L,Reg.H); // MOV L,H
    case 0x6D -> mov(Reg.L,Reg.L); // MOV L,L
    case 0x6E -> mov(Reg.L,Reg.M); // MOV L,M
    case 0x6F -> mov(Reg.L,Reg.A); // MOV L,A
    case 0x70 -> mov(Reg.M,Reg.B); // MOV M,B
    case 0x71 -> mov(Reg.M,Reg.C); // MOV M,C
    case 0x72 -> mov(Reg.M,Reg.D); // MOV M,D
    case 0x73 -> mov(Reg.M,Reg.E); // MOV M,E
    case 0x74 -> mov(Reg.M,Reg.H); // MOV M,H
    case 0x75 -> mov(Reg.M,Reg.L); // MOV M,L
    case 0x76 ->  {
        running=false;
    }// HLT
    case 0x77 -> mov(Reg.M,Reg.A); // MOV M,A
    case 0x78 -> mov(Reg.A,Reg.B); // MOV A,B
    case 0x79 -> mov(Reg.A,Reg.C); // MOV A,C
    case 0x7A -> mov(Reg.A,Reg.D); // MOV A,D
    case 0x7B -> mov(Reg.A,Reg.E); // MOV A,E
    case 0x7C -> mov(Reg.A,Reg.H); // MOV A,H
    case 0x7D -> mov(Reg.A,Reg.L); // MOV A,L
    case 0x7E -> mov(Reg.A,Reg.M); // MOV A,M
    case 0x7F -> mov(Reg.A,Reg.A); // MOV A,A

    case 0x80 -> add(Reg.B,false); // ADD B
    case 0x81 -> add(Reg.C,false); // ADD C
    case 0x82 -> add(Reg.D,false); // ADD D
    case 0x83 -> add(Reg.E,false); // ADD E
    case 0x84 -> add(Reg.H,false); // ADD H
    case 0x85 -> add(Reg.L,false); // ADD L
    case 0x86 -> add(Reg.M,false); // ADD M
    case 0x87 -> add(Reg.A,false); // ADD A
    case 0x88 -> add(Reg.B,true); // ADC B
    case 0x89 -> add(Reg.C,true); // ADC C
    case 0x8A -> add(Reg.D,true); // ADC D
    case 0x8B -> add(Reg.E,true); // ADC E
    case 0x8C -> add(Reg.H,true); // ADC H
    case 0x8D -> add(Reg.L,true); // ADC L
    case 0x8E -> add(Reg.M,true); // ADC M
    case 0x8F -> add(Reg.A,true); // ADC A
    case 0x90 -> {} // SUB B
    case 0x91 -> {} // SUB C
    case 0x92 -> {} // SUB D
    case 0x93 -> {} // SUB E
    case 0x94 -> {} // SUB H
    case 0x95 -> {} // SUB L
    case 0x96 -> {} // SUB M
    case 0x97 -> {} // SUB A
    case 0x98 -> {} // SBB B
    case 0x99 -> {} // SBB C
    case 0x9A -> {} // SBB D
    case 0x9B -> {} // SBB E
    case 0x9C -> {} // SBB H
    case 0x9D -> {} // SBB L
    case 0x9E -> {} // SBB M
    case 0x9F -> {} // SBB A

    case 0xA0 -> {} // ANA B
    case 0xA1 -> {} // ANA C
    case 0xA2 -> {} // ANA D
    case 0xA3 -> {} // ANA E
    case 0xA4 -> {} // ANA H
    case 0xA5 -> {} // ANA L
    case 0xA6 -> {} // ANA M
    case 0xA7 -> {} // ANA A
    case 0xA8 -> {} // XRA B
    case 0xA9 -> {} // XRA C
    case 0xAA -> {} // XRA D
    case 0xAB -> {} // XRA E
    case 0xAC -> {} // XRA H
    case 0xAD -> {} // XRA L
    case 0xAE -> {} // XRA M
    case 0xAF -> {} // XRA A
    case 0xB0 -> {} // ORA B
    case 0xB1 -> {} // ORA C
    case 0xB2 -> {} // ORA D
    case 0xB3 -> {} // ORA E
    case 0xB4 -> {} // ORA H
    case 0xB5 -> {} // ORA L
    case 0xB6 -> {} // ORA M
    case 0xB7 -> {} // ORA A
    case 0xB8 -> {} // CMP B
    case 0xB9 -> {} // CMP C
    case 0xBA -> {} // CMP D
    case 0xBB -> {} // CMP E
    case 0xBC -> {} // CMP H
    case 0xBD -> {} // CMP L
    case 0xBE -> {} // CMP M
    case 0xBF -> {} // CMP A

    case 0xC0 -> {} // RNZ

    default -> throw new IllegalArgumentException("Unknown opcode: " + Integer.toHexString(instruction));
}
    }
    public Cpu()
    {
        ram= new Memory();
        flags= new Flags();
        registers= new Registers();
    }
    public void run() // Still incomplete ig idk 
    {
        while(running){
        decode(ram.read(registers.get(Reg.PC)));
        registers.incrementRegister(Reg.PC); // increment PC 
        }
    }

    
}