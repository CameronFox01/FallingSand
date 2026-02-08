import java.awt.*;

/**
 * Sand lab adapted from http://nifty.stanford.edu/2017/feinberg-falling-sand/
 * <p>
 * Student name: Cameron Fox
 * <p>
 * Empty, Will delete everything except from the inside of the Mountain
 *        To empty a mountain must start from the top and work your way down.
 * Metal, creates floating metal pieces.
 * Water, Will interact and flow in the space that is given
 * Fire,  Must be placed on the top layer of what you want to burn.
 *        Once Placed it will burn forever. And will interact with Water to
 *        create Steam or if something placed on top will go out.
 * Steam, Will rise to as high as it can and will go around other
 *        objects to reach the top. Is created by Fire and Water interacting
 *        Steam when trapped will turn back into water and rain down.
 * Sand,  Will fall into nice piles and go down hill. Will sink through water.
 * Mountain, Will create a mountain that can be on top of everything but Water.
 *        Will put out Fire.
 * Bomb,  Will totally destroy everything! That it is touching. Except for Water
 *        It will turn the water it touches into Steam.
 * Ice,   Will fall similar to Sand but when reacting with water will turn to
 *        the water to ice and when reacting with fire will turn both ice and
 *        fire to water... Which usually means it all becomes ice.
 *Sun,    There can only be one. If the sun is out Ice will melt and turn into
 *        Water.
 *Clear,  Everything on the screen gets set to Empty, so you can restart.
 */
public class SandLab {

    /**
     * Enum for material types of the particles
     * In Alphabetical Order
     */
    public enum Material {
        BOMB, CLEAR, EMPTY, FIRE, ICE, METAL, MOUNTAIN, SAND, STEAM, SUN, WATER
    }

    /**
     * grid of particles of various materials
     */
    private Material[][] grid;

    /**
     * The display window
     */
    private SandDisplay display;

    /**
     * Create a new SandLab of given size.
     *
     * @param numRows number of rows
     * @param numCols number of columns
     */
    public SandLab(int numRows, int numCols) {
        //This automatically adds the ENUM to the screen
        String[] names = new String[Material.values().length];
        Material[] MaterialValue = Material.values();
        for (int i = 0; i < names.length; i++) {
            names[i] = MaterialValue[i].toString();
        }

        display = new SandDisplay("Falling Sand", names, numRows, numCols);

        // Initialize grid with empty cells
        grid = new Material[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid[row][col] = Material.EMPTY;
            }
        }

    }

    /**
     * Called after the user clicks on a location using the given tool
     *
     * @param row  Row of location
     * @param col  Column of location
     * @param tool Name of selected tool
     */
    public void updateFromUser(int row, int col, String tool) {
        grid[row][col] = Material.valueOf(tool);
    }

    /**
     * copies each element of grid into the display
     */
    public void refreshDisplay() {
        // Update display with colors based on grid contents
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == Material.EMPTY) {
                    display.setColor(i, j, Color.BLACK);
                } else if (grid[i][j] == Material.METAL) {
                    display.setColor(i, j, Color.GRAY);
                } else if (grid[i][j] == Material.SAND) {
                    display.setColor(i, j, Color.ORANGE);
                } else if (grid[i][j] == Material.WATER) {
                    display.setColor(i, j, Color.BLUE);
                } else if (grid[i][j] == Material.MOUNTAIN) {
                    display.setColor(i, j, Color.GREEN);
                } else if (grid[i][j] == Material.FIRE) {
                    display.setColor(i, j, Color.RED);
                } else if (grid[i][j] == Material.STEAM) {
                    display.setColor(i, j, Color.WHITE);
                }else if(grid[i][j] == Material.BOMB){
                    display.setColor(i, j, Color.RED);
                } else if (grid[i][j] == Material.ICE) {
                    display.setColor(i,j, Color.WHITE);
                }else if(grid[i][j] == Material.SUN){
                    display.setColor(i, j, Color.YELLOW);
                }
            }
        }
    }

    /**
     * Update the simulation by one step.
     * Called repeatedly.
     * Causes one random particle to maybe do something
     */
    public void updateRandomLocation() {
        // TODO: select random location and update the particle if relevant
        int randRow = (int) (Math.random() * (grid.length - 1));
        int randCol = (int) (Math.random() * (grid[0].length - 1));
        //Physics rule for Sand Falling
        if (grid[randRow][randCol] == Material.SAND
                && randRow <= grid.length - 1 &&
                grid[randRow + 1][randCol] == Material.EMPTY) {
            grid[randRow + 1][randCol] = Material.SAND;
            grid[randRow][randCol] = Material.EMPTY;
        } else if (grid[randRow][randCol] == Material.SAND
                && randRow <= grid.length - 1
                && grid[randRow + 1][randCol] != Material.EMPTY
                && grid[randRow+1][randCol] != Material.WATER
                && randCol <= grid[0].length - 1 && randCol > 0) {
            if (grid[randRow + 1][randCol + 1] == Material.EMPTY) {
                grid[randRow + 1][randCol + 1] = Material.SAND;
                grid[randRow][randCol] = Material.EMPTY;
            } else if (grid[randRow + 1][randCol - 1] == Material.EMPTY) {
                grid[randRow + 1][randCol - 1] = Material.SAND;
                grid[randRow][randCol] = Material.EMPTY;
            }
        }
        //Sand rule for interacting with Water
        else if(grid[randRow][randCol] == Material.SAND
                && randRow <= grid.length - 1
                && grid[randRow+1][randCol] == Material.WATER
                && randCol <= grid[0].length - 1 && randCol > 0){
            grid[randRow][randCol] = Material.WATER;
            grid[randRow+1][randCol] = Material.SAND;
        }
        //This is the Rules for the Mountain
        else if(grid[randRow][randCol] == Material.MOUNTAIN
                && randCol < 3 && grid[randRow][3] == Material.EMPTY){
            grid[randRow][randCol] = Material.EMPTY;
        } else if (grid[randRow][randCol] == Material.MOUNTAIN
                && randRow <= grid.length - 1
                && grid[randRow + 1][randCol] == Material.EMPTY) {
            if (grid[randRow + 1][randCol] == Material.EMPTY) {
                grid[randRow + 1][randCol] = Material.MOUNTAIN;
                grid[randRow][randCol] = Material.EMPTY;
            }
        } else if (grid[randRow][randCol] == Material.MOUNTAIN
                && randRow <= grid.length - 1
                && grid[randRow + 1][randCol] == Material.MOUNTAIN) {
            if (grid[randRow + 1][randCol] == Material.MOUNTAIN
                    && randRow <= grid.length - 1 && randRow > 0
                    && grid[randRow + 1][randCol + 1] == Material.EMPTY) {
                grid[randRow + 1][randCol + 1] = Material.MOUNTAIN;
            }
            if ((randCol - 1) <= grid[0].length && randCol - 1 >= 0
                    && grid[randRow + 1][randCol - 1] == Material.EMPTY) {
                grid[randRow + 1][randCol - 1] = Material.MOUNTAIN;
            }
        }
        //This makes sure water can rise to the top!
        //TODO: Should I keep this or change it? SO the water won't rise
        else if(grid[randRow][randCol] == Material.MOUNTAIN
                && randRow <= grid.length - 1
                && grid[randRow+1][randCol] == Material.WATER
                && randCol <= grid[0].length - 1 && randCol > 0){
            grid[randRow][randCol] = Material.WATER;
            grid[randRow+1][randCol] = Material.MOUNTAIN;
        }

        //This is the rules for Fire and Steam
        else if(grid[randRow][randCol] == Material.FIRE
                && grid[randRow-1][randCol] != Material.EMPTY
                && randRow-1 > 0){
            grid[randRow][randCol] = grid[randRow-1][randCol];
        }else if (grid[randRow][randCol] == Material.FIRE
                && grid[randRow + 1][randCol] == Material.WATER
                && randRow + 1 <= grid.length) {
            grid[randRow + 1][randCol] = Material.STEAM;
            grid[randRow][randCol] = Material.STEAM;
        } else if (grid[randRow][randCol] == Material.FIRE
                && grid[randRow + 1][randCol] != Material.EMPTY
                && grid[randRow + 1][randCol] != Material.WATER
                && grid[randRow + 1][randCol] != Material.STEAM
                && randRow + 1 <= grid.length && randRow > 0
                && randCol + 1 <= grid[0].length && randCol > 0) {
            if (grid[randRow + 2][randCol] == Material.FIRE
                    && randRow + 2 <= grid.length) {
                grid[randRow][randCol] = Material.EMPTY;
                grid[randRow + 1][randCol] = Material.EMPTY;
                grid[randRow - 1][randCol] = Material.EMPTY;
            } else if (grid[randRow - 1][randCol] == Material.EMPTY
                    && randRow -1 > 0) {
                grid[randRow - 1][randCol] = Material.FIRE;
                if (grid[randRow][randCol - 1] == Material.EMPTY
                        && randCol-1 > 0) {
                    grid[randRow][randCol - 1] = Material.FIRE;
                }
                if (grid[randRow][randCol + 1] == Material.EMPTY
                        && randCol+1 < grid[0].length) {
                    grid[randRow][randCol + 1] = Material.FIRE;
                }

            }
        } else if (grid[randRow][randCol] == Material.FIRE
                && (grid[randRow][randCol - 1] == Material.WATER
                || grid[randRow][randCol + 1] == Material.WATER)
                && randCol + 1 <= grid[0].length && randCol - 1 > 0) {
            grid[randRow][randCol] = Material.EMPTY;
        } else if (grid[randRow][randCol] == Material.STEAM
                && grid[randRow - 1][randCol] == Material.FIRE
                && randRow + 1 <= grid.length) {
            grid[randRow][randCol] = Material.EMPTY;
            grid[randRow - 1][randCol] = Material.STEAM;
        } else if (grid[randRow][randCol] == Material.STEAM
                && randRow - 1 > 0
                && randCol - 1 > 0
                && grid[randRow - 1][randCol] == Material.EMPTY) {
            grid[randRow][randCol] = Material.EMPTY;
            grid[randRow - 1][randCol] = Material.STEAM;
        } else if (grid[randRow][randCol] == Material.FIRE
                && randRow <= grid.length - 1 &&
                grid[randRow + 1][randCol] == Material.EMPTY) {
            grid[randRow][randCol] = Material.EMPTY;
        }
        if(grid[randRow][randCol] == Material.STEAM
                && randRow-1 == 0){
            grid[randRow][randCol] = Material.EMPTY;
        }
        if(grid[randRow][randCol] == Material.STEAM
            && grid[randRow-1][randCol] != Material.EMPTY
            && grid[randRow-1][randCol+1] == Material.EMPTY
            && randRow-1 > 0 && randCol + 1 < grid[0].length){
            grid[randRow-1][randCol+1] = Material.STEAM;
            grid[randRow][randCol] = Material.EMPTY;
        }else if(grid[randRow][randCol] == Material.STEAM
                && grid[randRow-1][randCol] != Material.EMPTY
                && grid[randRow-1][randCol-1] == Material.EMPTY
                && randRow-1 > 0 && randCol - 1 > 0){
            grid[randRow-1][randCol-1] = Material.STEAM;
            grid[randRow][randCol] = Material.EMPTY;
        } else if (grid[randRow][randCol] == Material.STEAM
                && grid[randRow-1][randCol] != Material.EMPTY
                && grid[randRow-1][randCol-1] != Material.EMPTY
                && grid[randRow-1][randCol] == Material.EMPTY
                && randRow-1 > 0 && randCol - 1 > 0) {
            grid[randRow][randCol-1] = Material.STEAM;
            grid[randRow][randCol] = Material.EMPTY;
        }else if (grid[randRow][randCol] == Material.STEAM
                && grid[randRow-1][randCol] != Material.EMPTY
                && grid[randRow-1][randCol+1] != Material.EMPTY
                && grid[randRow-1][randCol] == Material.EMPTY
                && randRow-1 > 0 && randCol - 1 > 0) {
            grid[randRow][randCol+1] = Material.STEAM;
            grid[randRow][randCol] = Material.EMPTY;
        }else if(grid[randRow][randCol] == Material.STEAM
                && grid[randRow-1][randCol] != Material.EMPTY
                && grid[randRow-1][randCol+1] != Material.EMPTY
                && grid[randRow-1][randCol-1] != Material.EMPTY
                && grid[randRow][randCol+1] == Material.EMPTY){
            grid[randRow][randCol+1] = Material.STEAM;
            grid[randRow][randCol] = Material.EMPTY;
        }else if(grid[randRow][randCol] == Material.STEAM
                && grid[randRow-1][randCol] != Material.EMPTY
                && grid[randRow-1][randCol+1] != Material.EMPTY
                && grid[randRow-1][randCol-1] != Material.EMPTY
                && grid[randRow][randCol-1] == Material.EMPTY){
            grid[randRow][randCol-1] = Material.STEAM;
            grid[randRow][randCol] = Material.EMPTY;
        }else if(grid[randRow][randCol] == Material.STEAM
                && grid[randRow-1][randCol] != Material.EMPTY
                && grid[randRow-1][randCol+1] != Material.EMPTY
                && grid[randRow-1][randCol-1] != Material.EMPTY
                && grid[randRow][randCol-1] != Material.EMPTY){
            grid[randRow][randCol] = Material.WATER;
        }

        //This is the rules for Water
        else if (randRow < grid.length - 1 && randCol > 1
                && randCol < grid[0].length - 1
                && grid[randRow][randCol] == Material.WATER) {
            int direction = (int) (Math.random() * 3);
            switch (direction) {
                case 0:
                    if (grid[randRow][randCol - 1] == Material.EMPTY) {
                        grid[randRow][randCol - 1] = Material.WATER;
                        grid[randRow][randCol] = Material.EMPTY;
                    }
                    break;
                case 1:
                    if (grid[randRow + 1][randCol] == Material.EMPTY) {
                        grid[randRow + 1][randCol] = Material.WATER;
                        grid[randRow][randCol] = Material.EMPTY;
                    }
                    break;
                case 2:
                    if (grid[randRow][randCol + 1] == Material.EMPTY) {
                        grid[randRow][randCol + 1] = Material.WATER;
                        grid[randRow][randCol] = Material.EMPTY;
                    }
                    break;
                default:
                    System.out.println("HOW?");
            }
        }
        else if(grid[randRow][randCol] == Material.WATER && randRow <= 62){
            if(grid[randRow+1][randCol] == Material.EMPTY){
                grid[randRow+1][randCol] = Material.WATER;
                grid[randRow][randCol] = Material.EMPTY;
            }else {
                System.out.println(grid[randRow][randCol] + " " + randRow + " " + randCol);
                System.out.println(grid[randRow+1][randCol] + " " + randRow+1 + " " + randCol);
            }
        }
        //This is for BOMB rules
       else if(grid[randRow][randCol] == Material.BOMB
                && randRow+1 < grid.length && randCol+1 < grid[0].length){
            if(grid[randRow+1][randCol] != Material.EMPTY
                && randRow+1 < grid.length
                && grid[randRow+1][randCol] != Material.WATER){
                grid[randRow+1][randCol] = Material.BOMB;
            }
            if(grid[randRow-1][randCol] != Material.EMPTY
                    && randRow-1 > 0
                    && grid[randRow-1][randCol] != Material.WATER){
                grid[randRow-1][randCol] = Material.BOMB;
            }
            if(grid[randRow][randCol+1] != Material.EMPTY
                    && randCol+1 < grid[0].length
                    && grid[randRow][randCol+1] != Material.WATER){
                grid[randRow][randCol+1] = Material.BOMB;
            }
            if(grid[randRow][randCol-1] != Material.EMPTY
                    && randCol-1 > 0
                    && grid[randRow][randCol-1] != Material.WATER){
                grid[randRow][randCol-1] = Material.BOMB;
            }
            //Steam being made by a BOMB
            if(grid[randRow][randCol-1] == Material.WATER
                    && randCol-1 > 0){
                grid[randRow][randCol-1] = Material.STEAM;
            }
            if(grid[randRow][randCol+1] == Material.WATER
                    && randCol+1 > grid[0].length){
                grid[randRow][randCol+1] = Material.STEAM;
            }
            if(grid[randRow-1][randCol] == Material.WATER
                    && randRow -1 > 0){
                grid[randRow-1][randCol] = Material.STEAM;
            }
            if(grid[randRow+1][randCol] == Material.WATER
                    && randRow +1 < grid.length){
                grid[randRow+1][randCol] = Material.STEAM;
            }
            grid[randRow][randCol] = Material.EMPTY;
            for(int i = 0; i < grid[0].length; i++){
                if(grid[grid.length-1][i] == Material.BOMB) {
                    grid[grid.length-1][i] = Material.EMPTY;
                }
            }
            for(int i = 0; i < grid.length; i++){
                if(grid[i][grid[0].length-1] == Material.BOMB) {
                    grid[i][grid[0].length-1] = Material.EMPTY;
                }
            }
        }
       //Here is the rules for ICE
       else if (grid[randRow][randCol] == Material.ICE) {
           //Water turned to ICE
           if(grid[randRow+1][randCol] == Material.WATER
                   && randRow +1 < grid.length){
               grid[randRow+1][randCol] = Material.ICE;
           }
           if(grid[randRow-1][randCol] == Material.WATER
                   && randRow-1 > 0){
               grid[randRow-1][randCol] = Material.ICE;
           }
           if(grid[randRow][randCol+1] == Material.WATER
                   && randCol+1 < grid[0].length){
               grid[randRow][randCol+1] = Material.ICE;
           }
           if(grid[randRow][randCol-1] == Material.WATER
                   && randCol-1 > 0){
               grid[randRow][randCol-1] = Material.ICE;
           }
           //FIRE AND ICE COLLIDE
            if(grid[randRow+1][randCol] == Material.FIRE
                    && randRow +1 < grid.length){
                grid[randRow+1][randCol] = Material.FIRE;
                grid[randRow][randCol] = Material.WATER;
            }
            if(grid[randRow-1][randCol] == Material.FIRE
                    && randRow-1 > 0){
                grid[randRow-1][randCol] = Material.FIRE;
                grid[randRow][randCol] = Material.WATER;
            }
            if(grid[randRow][randCol+1] == Material.FIRE
                    && randCol+1 < grid[0].length){
                grid[randRow][randCol+1] = Material.FIRE;
                grid[randRow][randCol] = Material.WATER;
            }
            if(grid[randRow][randCol-1] == Material.FIRE
                    && randCol-1 > 0){
                grid[randRow][randCol-1] = Material.FIRE;
                grid[randRow][randCol] = Material.WATER;
            }
            //Falling Physics for ICE
            //  This is the same as SANDS
            if(grid[randRow+1][randCol] == Material.EMPTY
                    && randRow+1 < grid.length){
                grid[randRow][randCol] = Material.EMPTY;
                grid[randRow+1][randCol] = Material.ICE;
            }else if (grid[randRow+1][randCol] != Material.EMPTY
                    && grid[randRow+1][randCol] != Material.WATER
                    && randRow+1 < grid.length){
                if(grid[randRow+1][randCol+1] == Material.EMPTY
                        && randCol+1 < grid[0].length){
                    grid[randRow+1][randCol+1] = Material.ICE;
                    grid[randRow][randCol] = Material.EMPTY;
                }else if(grid[randRow+1][randCol-1] == Material.EMPTY
                        && randCol-1 > 0){
                    grid[randRow+1][randCol-1] = Material.ICE;
                    grid[randRow][randCol] = Material.EMPTY;
                }
            }
        }
       //Rules for the Sun
        else if(grid[randRow][randCol] == Material.SUN){
            //Checking for only one Sun and Checking for Ice
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(grid[i][j] == Material.SUN
                            && (i != randRow || j !=randCol)){
                        grid[randRow][randCol] = Material.EMPTY;
                    }
                    if(grid[i][j] == Material.ICE){
                        grid[i][j] = Material.WATER;
                    }
                }
            }
        }
        //Rules for Clear
        else if(grid[randRow][randCol] == Material.CLEAR){
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length;j++){
//                    if(grid[i][j] == Material.WATER){
//                        System.out.println(i + " " + j);
//                    }
                    grid[i][j] = Material.EMPTY;
                }
            }
        }

    }

    /**
     * Run the SandLab particle simulation.
     * <p>
     * DO NOT MODIFY THIS METHOD!
     */
    public void run() {
        // keep updating as long as the program is running
        while (true) {
            // update some number of particles, as determined by the speed slider
            for (int i = 0; i < display.getSpeed(); i++) {
                updateRandomLocation();
            }
            // Update the display object's colors
            refreshDisplay();
            // wait for redrawing and for mouse events
            display.repaintAndPause(1);

            int[] mouseLoc = display.getMouseLocation();
            //test if mouse clicked
            if (mouseLoc != null) {
                updateFromUser(mouseLoc[0], mouseLoc[1], display.getToolString());
            }
        }
    }

    /**
     * Creates a new SandLab and sets it running
     */
    public static void main(String[] args) {
        SandLab lab = new SandLab(120, 80);
        lab.run();
    }
}