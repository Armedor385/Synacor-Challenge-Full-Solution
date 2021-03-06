/***
 * @author Agnieszka Walczak
 */


import java.io.*;

enum INSTRUCTION {
    HALT, SET, PUSH, POP, EQ, GT, JMP, JT, JF, ADD, MULT, MOD, AND, OR, NOT, RMEM, WMEM, CALL, RET, OUT, IN, NOP, ERR;
    private static final INSTRUCTION[] allValues = values();

    public static INSTRUCTION fromInt(int n) {
        if (n >= allValues.length)
            return ERR;
        return allValues[n];
    }
}

public class VM {
    private int[] ram, stack;
    private int pc, sp;
    private boolean running;
    private boolean printing=false;
    private Reader input;
    private final File script;

    public VM(File script) throws IOException {
        this.script = script;
        reset();
    }

    public void reset() throws IOException {
        ram = new int[1 << 16];
        stack = new int[1 << 16];
        pc = 0;
        sp = -1;
        running = false;
        input = (script == null) ? new InputStreamReader(System.in)
                : new InputStreamReader(new FileInputStream(script));
    }

    public void loadProgram(File file) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        int pos = 0;
        while (input.available() > 0)
            ram[pos++] = input.readUnsignedByte() + (input.readUnsignedByte() << 8);
        input.close();
    }

    private boolean isReg(int pc) {
        return ram[pc] >= 0x8000;
    }

    private int getValue(int pc) {
        return isReg(pc) ? ram[ram[pc]] : ram[pc];
    }

    private void setValue(int pc, int value) {
        if (isReg(pc))
            ram[ram[pc]] = value;
        else
            ram[pc] = value;
    }

    public void run() throws IOException {
        running = true;
        while (running)

            doStep();

        System.out.println("VM stopped!");
        input.close();
    }

    public void doStep() throws IOException {

        int a, b, c;
        INSTRUCTION opCode = INSTRUCTION.fromInt(ram[pc]);
        a = getValue(pc + 1);
        b = getValue(pc + 2);
        c = getValue(pc + 3);
        switch (opCode) {
            case HALT: // halt: 0
                running = false;
                pc++;

                break;
            case SET: // set: 1 a b
                setValue(pc + 1, b);
                pc += 3;

                break;
            case PUSH: // push: 2 a
                stack[++sp] = getValue(pc + 1);
                pc += 2;

                break;
            case POP: // pop: 3 a
                if (sp == -1) {
                    System.err.println("Stack empty!");
                    running = false;

                } else
                    setValue(pc + 1, stack[sp--]);

                pc += 2;
                break;
            case EQ: // eq: 4 a b c
                setValue(pc + 1, b == c ? 1 : 0);

                pc += 4;

                break;
            case GT: // gt: 5 a b c
                setValue(pc + 1, b > c ? 1 : 0);
                pc += 4;

                break;
            case JMP: // jmp: 6 a
                pc = a;
                if(printing){
                    System.out.println (opCode+"<<<<opcode   pc>>>> "+ pc);
                }
                break;
            case JT: // jt: 7 a b
                pc = (a != 0) ? b : pc + 3;
                if(printing){
                    System.out.println (opCode+"<<<<opcode   pc>>>> "+ pc);

                }
                break;
            case JF: // jf: 8 a b
                pc = (a == 0) ? b : pc + 3;
                if(printing){
                    System.out.println (opCode+"<<<<opcode   pc>>>> "+ pc);
                }
                break;
            case ADD: // add: 9 a b c
                setValue(pc + 1, (b + c) % 0x8000);
                pc += 4;

                break;
            case MULT: // mult: 10 a b c
                setValue(pc + 1, (b * c) % 0x8000);
                pc += 4;

                break;
            case MOD: // mod: 11 a b c
                setValue(pc + 1, b % c);
                pc += 4;

                break;
            case AND: // and: 12 a b c
                setValue(pc + 1, b & c);
                pc += 4;

                break;
            case OR: // or: 13 a b c
                setValue(pc + 1, b | c);
                pc += 4;

                break;
            case NOT: // not: 14 a b
                setValue(pc + 1, ~b & 0x7FFF);
                pc += 3;

                break;
            case RMEM: // rmem: 15 a b
                b = getValue(getValue(pc + 2));
                setValue(pc + 1, b);
                pc += 3;

                break;
            case WMEM: // wmem: 16 a b
                setValue(getValue(pc + 1), b);
                pc += 3;

                break;
            case CALL: // call: 17 a
                stack[++sp] = pc + 2;
                pc = a;
                if(printing){
                    System.out.println (opCode+"<<<<opcode   pc>>>> "+ pc);
                }
                break;
            case RET: // ret: 18
                if (sp == -1) {
                    running = false;

                }else {

                    pc = stack[sp--];
                    break;
                }
            case OUT: // out: 19 a
                System.out.print((char) a);
                pc += 2;

                break;
            case IN: // in: 20 a
                int val = input.read();
                if (val == -1) {
                    input = new InputStreamReader(System.in);
                    val = input.read();
                }
                if(val=='&'){


                  //  printing=true;


                    ram[5485]=6;
                    ram[5489]=INSTRUCTION.NOP.ordinal();
                    ram[5490]=INSTRUCTION.NOP.ordinal();
                    ram[0x8000+1]=0;
                    ram[0x8000+7]=25734;
                    val=input.read ();


                }


                // Windows \r\n ==> \n
                if (val == '\r')
                    val = input.read();

                setValue(pc + 1, val);
                pc += 2;
                break;
            case NOP: // noop: 21
                pc += 1;
                break;
            default:
                System.err.println("Unknown Opcode : " + opCode);
                running = false;
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        PrintStream old = System.out;
       // System.setOut(new PrintStream(new File("./res/debug.txt")));
        VM vm = new VM(null); //new File("save.txt")  << use this to get to the teleporter checkpoint.
        vm.loadProgram(new File("challenge.bin"));
        vm.run();
      //  System.setOut(old);
        System.out.println("done");
    }
}