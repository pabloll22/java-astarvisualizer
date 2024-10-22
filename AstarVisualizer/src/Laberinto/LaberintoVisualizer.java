package Laberinto;

import javax.swing.*;
import java.awt.*;

public class LaberintoVisualizer extends JPanel {
    public Laberinto laberinto; // Agregado este atributo
    private final int CELL_SIZE = 10; // Tamaño de las celdas

    public LaberintoVisualizer(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (laberinto != null) { // Asegúrate de que laberinto no sea nulo
            Nodo[][] grid = laberinto.getMatriz();
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    Nodo nodo = grid[row][col];
                    switch (nodo.getT()) {
                        case NORM -> g.setColor(Color.WHITE); // Espacio normal
                        case OBS -> g.setColor(Color.BLACK); // Obstáculo
                        case I -> g.setColor(Color.GREEN); // Inicio
                        case G -> g.setColor(Color.RED); // Meta
                        case OPT -> g.setColor(Color.MAGENTA); // Camino óptimo
                        case EXPLORADO -> g.setColor(Color.BLUE); // Camino explorado
                    }
                    g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.GRAY); // Borde de la celda
                    g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}