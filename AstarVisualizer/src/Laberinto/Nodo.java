package Laberinto;

import java.util.ArrayList;
import java.util.List;


public class Nodo {
    private int x,y;
    public enum tipo{I,G,NORM,OBS,OPT,EXPLORADO};
    private tipo t;

    private List<Nodo> vecinos;
    private Nodo bestPrev;
    private boolean optPath;
    private int g,f,h;
    public Nodo(int x1,int y1,tipo tip ){
        x=x1;
        y=y1;
        f=-1;
        g=-1;
        h=-1;
        bestPrev=null;
        optPath=false;
        t=tip;
        vecinos= new ArrayList<Nodo>();

    }

    public Nodo getBestPrev() {
        return bestPrev;
    }

    public void setBestPrev(Nodo bestPrev) {
        this.bestPrev = bestPrev;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public int getF() {
        return f;
    }

    public void setH(int h) {
        this.h = h;
    }


    public List<Nodo> getVecinos() {
        return vecinos;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setVecinos(Nodo n) {
        if (n.getT() != tipo.OBS) {
            vecinos.add(n);
        }
    }

    public int getH() {
        return h;
    }

    public void setT(tipo t) {
        this.t = t;
    }

    public tipo getT() {
        return t;
    }

    @Override
    public String toString() {
        return ""+t;
    }
}
