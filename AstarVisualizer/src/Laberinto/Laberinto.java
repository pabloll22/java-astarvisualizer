package Laberinto;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Laberinto{
    private Nodo matriz [][] = new Nodo[60][80];
    private LaberintoVisualizer visualizer;
    private boolean opt = false;
    public Laberinto(LaberintoVisualizer visualizer){
        this.visualizer = visualizer;

        for(int i=0;i<60;++i){
            for (int j=0;j<80;++j){
                matriz[i][j]=new Nodo(i,j, Nodo.tipo.NORM);
            }
        }
        Random rd=new Random();
       int anch= rd.nextInt(80) ;
       int alt= rd.nextInt(60) ;
       matriz[alt][anch]=new Nodo(alt,anch, Nodo.tipo.I);
       anch= rd.nextInt(80) ;
       alt= rd.nextInt(60) ;
       matriz[alt][anch]= new Nodo(alt,anch, Nodo.tipo.G);
       for (int i=0; i<1440; i++){
           anch= rd.nextInt(80);
           alt= rd.nextInt(60);
          if(matriz[alt][anch].getT() == Nodo.tipo.OBS){
               i--;
            }else if(matriz[alt][anch].getT() == Nodo.tipo.I||matriz[alt][anch].getT() == Nodo.tipo.G){
               throw new RuntimeException("Error, el estado inicial o final ha coincidido con un obstaculo");
           }
            else{
               matriz[alt][anch] = new Nodo(alt,anch, Nodo.tipo.OBS);
           }
       }
    }

    public void escribir(){
        for (int x=0; x < matriz.length; x++) {
            for (int y=0; y < matriz[x].length; y++) {
                    if (matriz[x][y].getT()== Nodo.tipo.NORM){
                        System.out.print (" ");
                }else if (matriz[x][y].getT()== Nodo.tipo.OPT){
                        System.out.print("+");
                }else if (matriz[x][y].getT()== Nodo.tipo.OBS){
                        System.out.print("*");
                    }
                    else {
                    System.out.print(matriz[x][y].getT());
                }
          }

            System.out.println("|\n");
        }

    }
    public Nodo buscarIni(){
        boolean esINI =false;
        int x=0;
        int y=0;
        for(int i=0;i<matriz.length&&!esINI;i++){
            for (int j=0;j<matriz[i].length&&!esINI;j++){
                if(matriz[i][j].getT()== Nodo.tipo.I){
                    x=i;
                    y=j;
                    esINI=true;
                }

            }
        }
        return matriz[x][y];

    }
    public Nodo buscarG(){
        boolean esG =false;
        int x=0;
        int y=0;
        for(int i=0;i<matriz.length&&!esG;i++){
            for (int j=0;j<matriz[i].length&&!esG;j++){
                if(matriz[i][j].getT()== Nodo.tipo.G){
                    x=i;
                    y=j;
                    esG=true;
                }

            }
        }
        return matriz[x][y];

    }
    public void buscarVecino(Nodo n){

        if(n.getX()!=0){
            n.setVecinos(matriz[n.getX()-1][n.getY()]);
        }
        if(n.getX()!= matriz.length-1){
            n.setVecinos(matriz[n.getX()+1][n.getY()]);
        }
        if(n.getY()!=0){
            n.setVecinos(matriz[n.getX()][n.getY()-1]);
        }
        if(n.getY()!=matriz[0].length-1){
            n.setVecinos(matriz[n.getX()][n.getY()+1]);
        }


    }
    public void Aest(){
        List<Nodo> cerrados = new ArrayList<Nodo>();
        List<Nodo> abiertos = new ArrayList<Nodo>();

        Nodo aux=buscarIni();
        Nodo fin=buscarG();

        aux.setG(0);
        aux.setH(distM(aux,fin));
        aux.setF(aux.getH()+aux.getG());

        abiertos.add(aux);

        while(!vacio(abiertos)){
            Nodo current = menorF(abiertos);

            if(current!=aux && current!=fin){
                current.setT(Nodo.tipo.EXPLORADO); // Color de exploración
                visualizer.repaint(); // Actualizar la visualización
            }

            if(current.getX() == fin.getX() && current.getY() == fin.getY()){
                opt = true;
                construirCamino(current);
                visualizer.repaint();
                break;
                //System.out.println("Encontrado");
            }
            abiertos.remove(current);
            cerrados.add(current);

            buscarVecino(current);
            for(Nodo m: current.getVecinos()){
                if(!estaCerrado(m, cerrados)){
                    int tentative = current.getG() + 1;//distM(current, m);
                    if(noEstaEnAbierto(m,abiertos) || tentative<m.getG()){
                        m.setBestPrev(current);
                        m.setG(tentative);
                        m.setH(distM(m,fin));
                        m.setF(m.getG() + m.getH());
                        if(noEstaEnAbierto(m,abiertos)){
                            abiertos.add(m);}
                    }
                }
            }

            try {
                Thread.sleep(50); // Añadir un pequeño retraso para ver la animación
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void gEncerrado(){
        if(opt == false){
            System.out.println("El algoritmo no devuelve un camino porque el punto final G o I esta encerrado por obstaculos");
        }
    }

    private void construirCamino(Nodo current){
        Nodo aux = current;
        Nodo ini =buscarIni();
        Nodo fin = buscarG();
        while(!(aux.getX() == ini.getX() && aux.getY() == ini.getY())){
            if(aux.getT() == Nodo.tipo.G){
                aux = aux.getBestPrev();
            }else{
                aux.setT(Nodo.tipo.OPT);
                aux = aux.getBestPrev();
            }

        }
        visualizer.repaint();
    }

    private boolean estaCerrado(Nodo aux, List<Nodo> cerrados){
        boolean esta = false;
        for(Nodo m: cerrados){
            if(aux.getY() == m.getY() && aux.getX() == m.getX()){
                esta = true;
            }
        }
        return esta;
    }

    private boolean noEstaEnAbierto(Nodo aux, List<Nodo> v){
        boolean noEsta = true;
        for(Nodo m: v){
            if(aux.getY() == m.getY() && aux.getX() == m.getX()){
                noEsta = false;
            }
        }
        return noEsta;
    }

    private boolean vacio(List<Nodo> abiertos){
        boolean ok = true;
        if(abiertos.size()>0){
            ok = false;
        }
        return ok;
    }

    public int distM(Nodo n,Nodo g){
        return Math.abs(n.getX()-g.getX())+Math.abs(n.getY()-g.getY());

    }
    public Nodo menorF(List<Nodo> v){
        int i=0;
        Nodo aux= v.get(0);
        for (i=0;i<v.size();i++) {
            if(v.get(i).getF()<aux.getF()){
                aux=v.get(i);
            }

        }
       return aux;
    }

    public void imprimir() throws IOException {
        FileWriter archivo = new FileWriter("salida.txt");
        BufferedWriter bw = new BufferedWriter(archivo);
        bw.write("Camino optimo.");
        for (int x=0; x < matriz.length; x++) {
            for (int y=0; y < matriz[x].length; y++) {
                if (matriz[x][y].getT()== Nodo.tipo.NORM){
                    bw.write (" ");
                }else if (matriz[x][y].getT()== Nodo.tipo.OPT){
                    bw.write("+");
                }else if (matriz[x][y].getT()== Nodo.tipo.OBS){
                    bw.write("*");
                }else if(matriz[x][y].getT() == Nodo.tipo.I){
                    bw.write("I");
                }else{
                    bw.write("G");
                }
            }
            bw.write("\n");
        }
        bw.close();
    }

    public Nodo[][] getMatriz() {
        return matriz;
    }

}
