import Laberinto.Laberinto;

import java.io.IOException;

import Laberinto.LaberintoVisualizer;

import javax.swing.*;

public class MainLaberinto {
    public static void main(String [] args){
        /*Laberinto lab= new Laberinto();
        lab.escribir();
        lab.Aest();
        System.out.println("---------------------------------------------------------------------------------------------");
        lab.escribir();
        lab.gEncerrado();
        try {
            lab.imprimir();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        LaberintoVisualizer visualizer = new LaberintoVisualizer(null); // Inicializamos sin laberinto al principio
        Laberinto laberinto = new Laberinto(visualizer); // Ahora pasamos el visualizador al laberinto
        visualizer.laberinto = laberinto; // Actualizamos la referencia dentro del visualizador

        // Ejecutar el algoritmo A* en un nuevo hilo para no bloquear la interfaz gráfica
        new Thread(() -> laberinto.Aest()).start();

        // Crear la ventana
        JFrame frame = new JFrame("A* Visualization");
        frame.add(visualizer);
        frame.setSize(800, 600); // Tamaño adaptable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
