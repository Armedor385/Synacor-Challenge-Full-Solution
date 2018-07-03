import java.awt.*;
import java.util.ArrayList;

/***
 * extension of the orbMaze riddle which serves as a Path which is being deep cloned after every new direction visited.
 */
public class Path {
    private final int[][] vault_layout = {{'*', 8, '-', 1},
            {4, '*', 11, '*'},
            {'+', 4, '-', 18},
            {22, '-', 9, '*'}};

    // Position
    java.awt.Point pos;
    // List<Direction>
    ArrayList<direction> moves = new ArrayList<>();
    int orbValue=22;
    char lastSeenOp = 0;

    // Path constructor
    public Path(){
        this.pos=new Point (0,3);
    }

    public Path(Path p) {
        this.pos = new Point(p.pos);
        this.moves =  new ArrayList<>(p.moves);
        this.orbValue = p.orbValue;
        this.lastSeenOp = p.lastSeenOp;
    }


    public boolean isAtVault(){
        return pos.x == 3 && pos.y == 0;
    }

    public void alterOrbValue(){
        char currentRoomValue = (char) vault_layout[pos.y][pos.x];

        // orbValue         22 -> 22 -> 88
        // currentRoomValue 22 -> '*' -> 4
        // lastSeenOp        0 -> '*' -> 0

        switch(lastSeenOp){
            case '*':
                orbValue *= currentRoomValue;
                lastSeenOp = 0;
                break;
            case '+':
                orbValue += currentRoomValue;
                lastSeenOp = 0;
                break;
            case '-':
                orbValue -= currentRoomValue;
                lastSeenOp = 0;
                break;
            default:
                lastSeenOp  = currentRoomValue;
                break;

        }
    }

    public boolean canMove(direction direction) {
        switch (direction) {
            case NORTH:
                return pos.y - 1 >= 0;

            case SOUTH:
                return pos.y + 1 <= 3 && pos.y!=2 && pos.x != 0;

            case EAST:
                return pos.x + 1 <= 3;

            case WEST:
                return pos.x - 1 >= 0 && pos.x!=1 && pos.y != 3;
        }
        return false;
    }



    public void move(direction direction) {
        switch (direction) {
            case NORTH:
                pos.y--;
                break;
            case SOUTH:
                pos.y++;
                break;
            case WEST:
                pos.x--;
                break;
            case EAST:
                pos.x++;
                break;
        }

        // Add the move to the move list
        moves.add(direction);
        alterOrbValue();

        //visited[pos.x][pos.y] = true;
        //orbValue = vault_layout[pos.x][pos.y];
    }
}
