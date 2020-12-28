import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Petridish extends JPanel {

    BufferedImage img;
    int rgbLebend = Color.BLACK.getRGB();
    int rgbCellBoarder = Color.lightGray.getRGB();
    int rgbTot = 0;
    int rowCount;
    int columnCount;
    int petriCellSize;
    int[][] firstGeneration;
    int[][] secondGeneration;

    public Petridish(int petriWidth, int petriHeight, int petriCellSize) {
        this.petriCellSize = petriCellSize;
        this.columnCount = petriWidth / this.petriCellSize;
        this.rowCount = petriHeight / this.petriCellSize;
        this.firstGeneration = new int[this.columnCount][this.rowCount];
        this.secondGeneration = new int[this.columnCount][this.rowCount];

        this.setPreferredSize(new Dimension(petriWidth, petriHeight));

        this.img = new BufferedImage(petriWidth, petriHeight, BufferedImage.TYPE_INT_ARGB);
        drawImageFromData();
    }

    private void drawImageFromData() {
        for (int xKoordinate = 0; xKoordinate < this.img.getWidth(); xKoordinate++) {
            for (int yKoordinate = 0; yKoordinate < this.img.getHeight(); yKoordinate++) {
                if ((xKoordinate + 1) % this.petriCellSize == 0 || (yKoordinate + 1) % this.petriCellSize == 0
                        || xKoordinate == 0 || yKoordinate == 0) {
                    this.img.setRGB(xKoordinate, yKoordinate, rgbCellBoarder);
                } else if (this.firstGeneration[xKoordinate / this.petriCellSize][yKoordinate
                        / this.petriCellSize] == 0) {
                    this.img.setRGB(xKoordinate, yKoordinate, rgbTot);
                } else {
                    this.img.setRGB(xKoordinate, yKoordinate, rgbLebend);
                }
            }
        }
    }

    public void clearAll() {
        for (int x = 0; x < this.columnCount; x++) {
            for (int y = 0; y < this.rowCount; y++) {
                firstGeneration[x][y] = 0;
                secondGeneration[x][y] = 0;
            }
        }
        drawImageFromData();
        repaint();
    }

    public void update() {
        copyGeneration(firstGeneration, secondGeneration);
        calculateSecondGeneration();
        copyGeneration(secondGeneration, firstGeneration);
        drawImageFromData();
        repaint();
    }

    private void calculateSecondGeneration() {
        int neighborCount = 0;
        for (int x = 1; x < this.columnCount - 1; x++) {
            for (int y = 1; y < this.rowCount - 1; y++) {
                neighborCount = countNeighbors(x, y);

                if (firstGeneration[x][y] == 0) {
                    if (neighborCount == 3) {
                        secondGeneration[x][y] = 1;
                    }
                } else {
                    if (neighborCount < 2 || neighborCount > 3) {
                        secondGeneration[x][y] = 0;
                    } else {
                        secondGeneration[x][y] = 1;
                    }
                }
            }
        }
    }

    private void copyGeneration(int[][] von, int[][] zu) {
        for (int x = 0; x < this.columnCount; x++) {
            for (int y = 0; y < this.rowCount; y++) {
                zu[x][y] = von[x][y];
            }
        }
    }

    private int countNeighbors(int x, int y) {
        return firstGeneration[x - 1][y - 1] + firstGeneration[x][y - 1] + firstGeneration[x + 1][y - 1]
                + firstGeneration[x - 1][y] + firstGeneration[x + 1][y] + firstGeneration[x - 1][y + 1]
                + firstGeneration[x][y + 1] + firstGeneration[x + 1][y + 1];
    }

    public void toggleState(int xPos, int yPos) {
        if (firstGeneration[xPos / petriCellSize][yPos / petriCellSize] != 0) {
            firstGeneration[xPos / petriCellSize][yPos / petriCellSize] = 0;
        } else {
            firstGeneration[xPos / petriCellSize][yPos / petriCellSize] = 1;
        }
        drawImageFromData();
        repaint();
    }

    public void setAlive(int xPos, int yPos) {
        firstGeneration[xPos / petriCellSize][yPos / petriCellSize] = 1;
        drawImageFromData();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
}
