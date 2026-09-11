package sim85.core;

public class Flags {
    private boolean Z, S, P, CY, AC; // Zero, Sign, Parity, Carry, Auxiliary Carry flags

    public boolean isZero() { return Z; }
    public void setZero(boolean value) { Z = value; }

    public boolean isSign() { return S; }
    public void setSign(boolean value) { S = value; }

    public boolean isParity() { return P; }
    public void setParity(boolean value) { P = value; }

    public boolean isCarry() { return CY; }
    public void setCarry(boolean value) { CY = value; }

    public boolean isAuxiliaryCarry() { return AC; }
    public void setAuxiliaryCarry(boolean value) { AC = value; }

    public void updateZSP(int result) {
        int masked = result & 0xFF;
        setZero(masked == 0);
        setSign((masked & 0x80) != 0);           // bit 7 set → negative in two's complement
        setParity(Integer.bitCount(masked) % 2 == 0);  // 8085 parity is "even parity": set if even number of 1-bits
    }
    public void updateAllFlags(int result)
    {
        int masked = result & 0x1FF;
        updateZSP(result);
        //setAuxiliaryCarry((masked & 0x10)!=0); //This is wrong
        // Skipping it as it is only used for DAA 
        setCarry((masked & 0x100)!=0);
    }

    public void reset(){
        Z = S = P = CY = AC = false;
    }
}
