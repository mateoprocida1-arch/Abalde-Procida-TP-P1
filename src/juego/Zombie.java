package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Zombie {
    double x, y, velocidad, ancho, escala;
    boolean estaActivo;
    int vida;
    Image imagenZombie;
    Entorno e;
    
    public Zombie(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.velocidad = 0.5;
        this.ancho = 50;
        this.escala = 0.15;
        this.vida = 100;
        this.estaActivo = true;
        this.imagenZombie = Herramientas.cargarImagen("imagenZombie.png");
    }
    
    public void dibujar() {
        if (e != null && estaActivo) {
            e.dibujarImagen(imagenZombie, x, y, 0, escala);
        }
    }

    public void mover() {
        if (estaActivo) {
            x -= velocidad;
        }
    }

    public boolean llegoARegalo() {
        return x < 100;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
