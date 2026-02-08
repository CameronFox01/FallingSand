import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

/*
 * Display for sand lab from http://nifty.stanford.edu/2017/feinberg-falling-sand/
 */
public class SandDisplay {

    private final Image image;
    private final int cellSize;
    private int tool;
    private final int numRows;
    private final int numCols;
    private int[] mouseLoc;
    private final JRadioButton[] buttons;
    private int speed;
    private final JPanel display;

    /**
     * Create SandDisplay window of given size and tools
     *
     * @param title     The window title
     * @param toolNames Names of the tools for user to choose from.
     * @param numRows   Number of rows
     * @param numCols   Number of columns
     */
    public SandDisplay(String title, String[] toolNames, int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        tool = 1;
        mouseLoc = null;
        speed = computeSpeed(50);

        //determine cell size
        cellSize = Math.max(1, 600 / Math.max(numRows, numCols));
        image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout()); // Changed from BoxLayout
        frame.getContentPane().add(topPanel);

        display = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Scale image to fit current panel size
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(numCols * cellSize, numRows * cellSize);
            }
        };
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseLoc = toLocation(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseLoc = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseLoc = toLocation(e);
            }
        };
        display.addMouseListener(mouseHandler);
        display.addMouseMotionListener(mouseHandler);
        topPanel.add(display, BorderLayout.CENTER); // Changed to CENTER

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        topPanel.add(buttonPanel, BorderLayout.EAST); // Changed to EAST

        buttons = new JRadioButton[toolNames.length];
        ButtonGroup buttonGroup = new ButtonGroup();

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JRadioButton(toolNames[i]);
            buttonGroup.add(buttons[i]);
            final int finalI = i;
            buttons[i].addActionListener(e -> {
                tool = finalI;
            });
            buttonPanel.add(buttons[i]);
        }

        buttons[tool].setSelected(true);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.addChangeListener(e -> speed = computeSpeed(slider.getValue()));
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(0, new JLabel("Slow"));
        labelTable.put(100, new JLabel("Fast"));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

        frame.getContentPane().add(slider);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Repaint the display and pause to allow for mouse input.
     * @param milliseconds How many milliseconds to pause?
     */
    public void repaintAndPause(int milliseconds) {
        display.repaint();
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get last mouse click location
     * @return Array containing row and column of mouse click.
     */
    public int[] getMouseLocation() {
        return mouseLoc;
    }

    /**
     * Get name of currently selected tool
     * @return Tool String
     */
    public String getToolString() {
        return buttons[tool].getText();
    }

    /**
     * Set color for given cell location.
     * @param row The cell row
     * @param col The cell column
     * @param color The new color for the cell
     */
    public void setColor(int row, int col, Color color) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
    }

    /**
     * Find cell location of a mouse event.
     * @param e The mouse event
     * @return Array of (row, col)
     */
    private int[] toLocation(MouseEvent e) {
        // Calculate scaling factors
        double scaleX = (double) display.getWidth() / (numCols * cellSize);
        double scaleY = (double) display.getHeight() / (numRows * cellSize);

        int row = (int) (e.getY() / scaleY / cellSize);
        int col = (int) (e.getX() / scaleX / cellSize);

        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return null;
        }
        int[] loc = new int[2];
        loc[0] = row;
        loc[1] = col;
        return loc;
    }

    /**
     * Get simulation speed.
     * @return number of times to step between repainting and processing mouse input
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Returns speed based on sliderValue
     *     speed of 0 returns 10^3
     *     speed of 100 returns 10^6
     * @param sliderValue Value from slider
     * @return Speed value
     */
    private static int computeSpeed(int sliderValue) {
        return (int) Math.pow(10, 0.03 * sliderValue + 3);
    }
}