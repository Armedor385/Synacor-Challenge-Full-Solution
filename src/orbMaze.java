import java.awt.*;
import java.util.ArrayList;

/***
 * BFS to find the shortest path for the last riddle.
 */

enum direction {
    NORTH, EAST, WEST, SOUTH
}


public class orbMaze {

    final int goalValue = 30;

    public static void  BFS() {
        ArrayList<Path> queue = new ArrayList<> ();

        queue.add(new Path());

        while (!queue.isEmpty ()) {

            Path current =  queue.remove(0);
            if(current.isAtVault()) {
                if(current.orbValue==30) {
                    current.moves.forEach (System.out::println);
                    break;
                }
                continue;
            }
            if(current.moves.size()>=12) continue;
            // Check each Direction if we can move there
            for (direction dir: direction.values()) {
                if(current.canMove(dir)) { //current.move(dir);
                    Path expanded = new Path(current);
                    expanded.move(dir);
                    queue.add(expanded);

                }
            }



        }

    }

    public static void main(String[] args) {

        BFS ();
    }


}