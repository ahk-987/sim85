package sim85.assembler;

public class Assembler {

    /**
     * Assembles ONE source line into machine bytes.
     * Step 1 scope: no labels, no comments, no directives.
     * Supported: NOP, HLT, MOV dst,src, MVI reg,value
     */
    public int[] assembleLine(String line) {
        // 1. Clean the line: trim whitespace, convert to uppercase.
        // 2. Split into mnemonic and operand text (e.g. "MVI" and "A,05H").
        // 3. Split operand text on ',' and trim each piece.
        // 4. Switch on mnemonic. For each one:
        //      - check the operand count is what that instruction needs
        //      - build the opcode (and operand bytes, if any)
        //      - return them as int[]
        // 5. Unknown mnemonic -> throw IllegalArgumentException with a clear message.
        return null; // TODO
    }

    /** Register name -> 3-bit code used inside opcodes. */
    private int registerCode(String name) {
        // TODO: B, C, D, E, H, L, M, A -> 0..7
        // Unknown name -> throw IllegalArgumentException.
        return -1;
    }

    /** "05H" -> 5, "10" -> 10. Result must fit in 8 bits for now. */
    private int parseNumber(String text) {
        // TODO: trailing 'H' means hex, otherwise decimal.
        // Out of range (not 0..255) -> throw IllegalArgumentException.
        return -1;
    }
}