import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BruteForcingTeleportation {

    int[] ram = new int[1<<16];

    int[][] memo = new int[5][1<<16];


    public  void bruteForce() throws IOException {
        File file = new File("challenge.bin");
        loadProgram (new File ("challenge.bin"));
        //what happens with 6027 that takes 1 bilion years
       /*for(int i=6027;i<=6127;i++){
           System.out.println (ram[i] +  " PC " + i +  "  <>   <>   <>  <> ");
       }*/

       //how VM validates.
       /*for(int i=5470;i<=5510;i++){
           System.out.println (ram[i] + " <> <> <>  pc " + i);
       }


        *//*
        for(int x=0;x<1<<16;x++){
            for(int y=0;y<1<<16;y++){
                memo[x][y]=-1;
            }
        }*/
        for (int r7 = 0; r7 < 0x8000; r7++) {
            if (calculations (4,1,r7) == 6) {
                System.out.println(r7);
            }
        }


    }
    public void loadProgram(File file) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream (file));
        int pos = 0;
        while (input.available() > 0)
            ram[pos++] = input.readUnsignedByte() + (input.readUnsignedByte() << 8);
        input.close();
    }

    //reversed function that takes 1 bilion years to calculate in VM.
    public int calculations(int r0_max,int r1_max,int r3) {
        for( int r0=0;r0 < memo.length;r0++) {
            for (int r1 = 0; r1 < memo[r0].length; r1++) {
                if (r0 == 0) {
                    memo[0][r1] = (r1 + 1) % 32768;
                } else if (r1 == 0) {
                    memo[r0][0]=memo[r0-1][r3];
                } else {
                    int r1_res = memo[r0][r1 - 1];
                    memo[r0][r1] = memo[r0 - 1][r1_res];


                }
            }
        }
        return memo[r0_max][r1_max];
    }
    public void memoization(int r0,int r1,int r7){

    }

    public void loopThrough(){
        int pc=0;
        while(true){
            if(ram[pc]==6027){
                System.out.println (ram[pc] + " " + pc);
                break;
            }
            pc++;
        }
    }


    public void add(int variable,int r1,int r2){
        variable=r1+r2;

    }


    public static void main(String[] args) throws IOException {
        BruteForcingTeleportation x = new BruteForcingTeleportation ();
        x.bruteForce ();
        //x.calculations (1,4,21);
       // x.loopThrough ();
    }
}
