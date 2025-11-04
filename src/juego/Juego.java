package juego;


import java.awt.Color;
import java.awt.Point;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	Cuadricula cua;
	Regalo[] regalos;
	Planta[]plantas;
	Zombie[]zombies;
	int contadorZombies = 0;          // cuántos zombies se crearon
	int zombiesEliminados = 0;        // cuántos murieron (lo usaremos después)
	int totalZombies = 10;            // cantidad máxima a crear
	int tiempoEntreZombies = 120;     // 120 ticks ≈ 2 segundos si el juego va a ~60fps
	int temporizadorZombie = 0;       // cuenta ticks hasta la próxima aparición
	boolean juegoTerminado = false;

	// Variables y métodos propios de cada grupo
	// ...

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		cua=new Cuadricula(50,150,entorno);
		regalos=new Regalo[5];
		
		for(int i=0; i < 5; i++) {
			regalos[i]=new Regalo(50, 150+i*100,entorno);
		}
		
		plantas=new Planta[15];
		plantas[0]=new Planta(50,50,entorno);		
		
		zombies=new Zombie[15];
		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick() {
		
		if (juegoTerminado) {
	        entorno.cambiarFont("Arial", 40, Color.RED);
	        entorno.escribirTexto("¡PERDISTE!", 270, 300);
	        return; // No sigue ejecutando el resto de la lógica
	    }
	    // --- DIBUJAR ESCENARIO ---
	    cua.dibujar();

	    // Dibujar regalos
	    for (Regalo r : this.regalos) {
	        if (r != null) {
	            r.dibujar();
	        }
	    }

	    // Dibujar plantas
	    for (int i = 0; i < plantas.length; i++) {
	        if (plantas[i] != null) {
	            plantas[i].dibujar();
	        }
	    }

	    // --- SELECCIONAR PLANTA CON CLICK IZQUIERDO ---
	    if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
	        for (int i = 0; i < plantas.length; i++) {
	            if (plantas[i] != null) {
	                if (plantas[i].encima(entorno.mouseX(), entorno.mouseY())) {
	                    plantas[i].seleccionada = true;
	                } else {
	                    plantas[i].seleccionada = false;
	                }
	            }
	        }
	    }

	    // --- ARRASTRAR PLANTA MIENTRAS SE MANTIENE EL CLICK ---
	    if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
	        for (int i = 0; i < plantas.length; i++) {
	            if (plantas[i] != null && plantas[i].seleccionada) {
	                plantas[i].arrastrar(entorno.mouseX(), entorno.mouseY());
	            }
	        }
	    }

	    // --- SOLTAR PLANTA ---
	    if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
	        for (int i = 0; i < plantas.length; i++) {
	            if (plantas[i] != null && plantas[i].seleccionada) {
	                // Si la suelta en la parte superior, vuelve a su posición inicial
	                if (entorno.mouseY() < 70 && !plantas[i].plantada) {
	                    plantas[i].arrastrar(50, 50);
	                } else {
	                    // Buscar la celda más cercana y colocarla si está libre
	                    Point celda = cua.cercanoL(entorno.mouseX(), entorno.mouseY());
	                    int col = celda.x;
	                    int fila = celda.y;

	                    if (!cua.ocupado[col][fila]) {
	                        plantas[i].arrastrar(cua.corX[col], cua.corY[fila]);
	                        cua.ocupado[col][fila] = true;
	                        plantas[i].plantada = true;
	                    }
	                }
	                plantas[i].seleccionada = false;
	            }
	        }
	    }

	    // --- MOVER PLANTAS CON TECLAS ---
	    for (int i = 0; i < plantas.length; i++) {
	        if (plantas[i] != null && plantas[i].plantada && plantas[i].seleccionada) {
	            int indiceX = cua.cercano(plantas[i].x, plantas[i].y).x;
	            int indiceY = cua.cercano(plantas[i].x, plantas[i].y).y;

	            if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
	                if (indiceY > 0 && !cua.ocupado[indiceX][indiceY - 1]) {
	                    cua.ocupado[indiceX][indiceY] = false;
	                    cua.ocupado[indiceX][indiceY - 1] = true;
	                    plantas[i].y -= 100;
	                }
	            }

	            if (entorno.sePresiono(entorno.TECLA_ABAJO)) {
	                if (indiceY < 4 && !cua.ocupado[indiceX][indiceY + 1]) {
	                    cua.ocupado[indiceX][indiceY] = false;
	                    cua.ocupado[indiceX][indiceY + 1] = true;
	                    plantas[i].y += 100;
	                }
	            }

	            if (entorno.sePresiono(entorno.TECLA_DERECHA)) {
	                if (indiceX < 7 && !cua.ocupado[indiceX + 1][indiceY]) {
	                    cua.ocupado[indiceX][indiceY] = false;
	                    cua.ocupado[indiceX + 1][indiceY] = true;
	                    plantas[i].x += 100;
	                }
	            }

	            if (entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
	                if (indiceX > 0 && !cua.ocupado[indiceX - 1][indiceY]) {
	                    cua.ocupado[indiceX][indiceY] = false;
	                    cua.ocupado[indiceX - 1][indiceY] = true;
	                    plantas[i].x -= 100;
	                }
	            }
	        }
	    }

	    // --- CREACIÓN GRADUAL DE ZOMBIES ---
	    temporizadorZombie++;

	    if (contadorZombies < totalZombies && temporizadorZombie >= tiempoEntreZombies) {
	        for (int i = 0; i < zombies.length; i++) {
	            if (zombies[i] == null) {
	                int filaAleatoria = (int) (Math.random() * 5);
	                zombies[i] = new Zombie(
	                    850,                     // x inicial fuera de la pantalla
	                    cua.corY[filaAleatoria], // y según la fila del pasto
	                    entorno
	                );
	                contadorZombies++;
	                temporizadorZombie = 0; // reinicia el intervalo
	                break; // solo crea uno por vez
	            }
	        }
	    }

	    // --- MOVER Y DIBUJAR ZOMBIES ---
	    for (int i = 0; i < zombies.length; i++) {
	        if (zombies[i] != null) {
	            zombies[i].mover();
	            zombies[i].dibujar();

	            // Si llega al borde izquierdo (regalos)
	            if (zombies[i].llegoARegalo()) {
	                juegoTerminado = true;
	            }

	        }
	    }

	    // --- CREAR NUEVAS PLANTAS SI NO HAY DISPONIBLES ---
	    if (!plantasNoPlantadas(this.plantas)) {
	        crearPlanta(this.plantas);
	    }
	}

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean plantasNoPlantadas(Planta[] pl) {
		for(Planta p:pl) {
			if(p != null && !p.plantada) {
				return true;
			}
		}
		return false;
	}

	private void crearPlanta(Planta[] pl) {

		for(int x=0; x < pl.length;x++) {
			if(pl[x] == null) {
				pl[x] = new Planta(50,50,entorno);
				return;
			}
		}
	}


	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
