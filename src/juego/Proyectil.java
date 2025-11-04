/*package juego;

import java.awt.Image;

import entorno.Entorno;

public class Proyectil {
	
	private int ancho;
	private int alto;
	private int daño;
	private double velocidad;
	private double coordenadaX;
	private double coordenadaY;
	private boolean proyectilActivo;
	//private Image imagenProyectil;

	public Proyectil(double d, double e, double mouseX, double mouseY) {
		this.coordenadaX = d;
		this.coordenadaY = e;
		this.ancho=35;
		this.alto=35;
		this.velocidad=5;
		this.daño=50;
		this.proyectilActivo = false;
		//this.imagenProyectil = Herramientas.cargarImagen("proyectil.gif");

	}
	
	public void actualizar (Entorno entorno) {
		if (entorno.estaPresionado(entorno.TECLA_ESPACIO)) {
			proyectilActivo = true;
		}
			
	}
	
	public void proyectilInactivo() {
		this.proyectilActivo = false;
	}
	
	public boolean estaActivada() {
		return proyectilActivo;
	}

	public int getX() {
		return (int) this.coordenadaX;
	}

	public int getY() {
		return (int) this.coordenadaY;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void dibujar(Entorno e, Image proyectil) {
		if (proyectil != null) {
			e.dibujarImagen(proyectil, getX(), getY(), 0, 1);
		}
	}

	public void avanzar() {
		this.coordenadaX += velocidad;
	}

	public boolean desaparecer() {
		return this.coordenadaX < 0 || this.coordenadaX > 645 || this.coordenadaY < 0 || this.coordenadaY > 600;
	}
	
	public boolean colisionaConZombie(Zombie z) {
	    if (z == null) return false;

	    double dx = this.coordenadaX - z.getX();
	    double dy = this.coordenadaY - z.getY();
	    double distancia = Math.sqrt(dx * dx + dy * dy);

	    return distancia < 30; // Ajusta el radio si querés mayor o menor precisión
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}*/
