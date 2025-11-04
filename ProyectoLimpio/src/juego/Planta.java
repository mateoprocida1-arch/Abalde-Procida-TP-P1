package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Planta {
    double x, y, escala;
    Image imagen, imagenSeleccionada;
    Entorno e;
    boolean seleccionada;
    boolean plantada;

    // Control de disparos
    Proyectil[] proyectiles;
    int tiempoEntreDisparos = 80; // frames entre disparos
    int contadorDisparo = 0;

    public Planta(double x, double y, Entorno e) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.escala = 0.35;
        this.imagen = Herramientas.cargarImagen("planta.png");
        this.imagenSeleccionada = Herramientas.cargarImagen("plantaSeleccionada.png");
        this.seleccionada = false;
        this.plantada = false;

        // Hasta 10 proyectiles activos por planta
        this.proyectiles = new Proyectil[10];
    }

    public void dibujar() {
        if (seleccionada) {
            e.dibujarImagen(imagenSeleccionada, x, y, 0, escala);
        }
        e.dibujarImagen(imagen, x, y, 0, escala);

        // Dibujar proyectiles activos
        for (Proyectil p : proyectiles) {
            if (p != null) {
                p.dibujar();
            }
        }
    }

    public void actualizar() {
        // Solo dispara si está plantada
        if (plantada) {
            contadorDisparo++;
            if (contadorDisparo >= tiempoEntreDisparos) {
                disparar();
                contadorDisparo = 0;
            }
        }

        // Actualizar proyectiles
        for (int i = 0; i < proyectiles.length; i++) {
            if (proyectiles[i] != null) {
                proyectiles[i].mover();
                // Eliminar si sale de pantalla
                if (proyectiles[i].x > 800) {
                    proyectiles[i] = null;
                }
            }
        }
    }

    private void disparar() {
        // Crea un nuevo proyectil desde la posición de la planta
        for (int i = 0; i < proyectiles.length; i++) {
            if (proyectiles[i] == null) {
                proyectiles[i] = new Proyectil(this.x + 30, this.y, this.e);
                break;
            }
        }
    }

    // --- Herramientas auxiliares ---
    public double distancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean encima(double xM, double yM) {
        return distancia(xM, yM, this.x, this.y) < 20;
    }

    public void arrastrar(double xM, double yM) {
        this.x = xM;
        this.y = yM;
    }

    // Detectar colisión con un zombie
    public boolean colisionaCon(ZombieGrinch z) {
        return distancia(this.x, this.y, z.x, z.y) < 40;
    }

	public Proyectil getProyectil() {
		// TODO Auto-generated method stub
		return null;
	}
}
