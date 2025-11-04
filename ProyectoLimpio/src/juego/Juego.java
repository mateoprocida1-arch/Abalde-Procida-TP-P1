package juego;

import java.awt.Color;
import java.awt.Point;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    Cuadricula cua;
    Regalo[] regalos;
    Planta[] plantas;
    ZombieGrinch[] zombis;
    int totalZombis = 50;
    int zombisEliminados = 0;
    int zombisActivos = 0;
    boolean juegoTerminado = false;
    boolean gano = false;

    Juego() {
        this.entorno = new Entorno(this, "La invasión del Grinch Zombie", 800, 600);
        cua = new Cuadricula(50, 150, entorno);
        regalos = new Regalo[5];
        for (int i = 0; i < 5; i++) {
            regalos[i] = new Regalo(50, 150 + i * 100, entorno);
        }
        plantas = new Planta[15];
        plantas[0] = new Planta(50, 50, entorno);

        zombis = new ZombieGrinch[15];
        this.entorno.iniciar();
    }

    public void tick() {
        if (juegoTerminado) {
            entorno.cambiarFont("Arial", 40, Color.WHITE);
            if (gano)
                entorno.escribirTexto("¡GANASTE!", 300, 300);
            else
                entorno.escribirTexto("¡LOS ZOMBIES GANARON!", 220, 300);
            return;
        }

        // --- DIBUJAR TABLERO ---
        cua.dibujar();
        for (Regalo r : regalos) {
            r.dibujar();
        }

        // --- DIBUJAR PLANTAS ---
        for (int i = 0; i < plantas.length; i++) {
            if (plantas[i] != null) {
                plantas[i].dibujar();
                plantas[i].actualizar();
            }
        }

        // --- CONTROL DE PLANTAS ---
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            for (Planta p : plantas) {
                if (p != null) {
                    if (p.encima(entorno.mouseX(), entorno.mouseY()))
                        p.seleccionada = true;
                    else
                        p.seleccionada = false;
                }
            }
        }

        if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
            for (Planta p : plantas) {
                if (p != null && p.seleccionada) {
                    int indiceX = cua.cercano(entorno.mouseX(), entorno.mouseY()).x;
                    int indiceY = cua.cercano(entorno.mouseX(), entorno.mouseY()).y;
                    p.arrastrar(entorno.mouseX(), entorno.mouseY());
                    cua.ocupado[indiceX][indiceY] = false;
                }
            }
        }

        if (entorno.seLevantoBoton(entorno.BOTON_IZQUIERDO)) {
            for (Planta p : plantas) {
                if (p != null && p.seleccionada) {
                    if (entorno.mouseY() < 70 && !p.plantada) {
                        p.arrastrar(50, 50);
                    } else {
                        int indiceX = cua.cercanoL(entorno.mouseX(), entorno.mouseY()).x;
                        int indiceY = cua.cercanoL(entorno.mouseX(), entorno.mouseY()).y;
                        if (cua.ocupado[indiceX][indiceY])
                            return;
                        p.arrastrar(cua.corX[indiceX], cua.corY[indiceY]);
                        cua.ocupado[indiceX][indiceY] = true;
                        p.plantada = true;
                    }
                }
            }
        }

        // --- Movimiento con teclas ---
        for (Planta p : plantas) {
            if (p != null) {
                if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
                    if (p.seleccionada && p.plantada) {
                        int indiceX = cua.cercano(p.x, p.y).x;
                        int indiceY = cua.cercano(p.x, p.y).y;
                        if (indiceY >= 1 && !cua.ocupado[indiceX][indiceY - 1]) {
                            cua.ocupado[indiceX][indiceY] = false;
                            cua.ocupado[indiceX][indiceY - 1] = true;
                            p.y -= 100;
                        }
                    }
                }

                if (entorno.sePresiono(entorno.TECLA_ABAJO)) {
                    if (p.seleccionada && p.plantada) {
                        int indiceX = cua.cercano(p.x, p.y).x;
                        int indiceY = cua.cercano(p.x, p.y).y;
                        if (indiceY <= 3 && !cua.ocupado[indiceX][indiceY + 1]) {
                            cua.ocupado[indiceX][indiceY] = false;
                            cua.ocupado[indiceX][indiceY + 1] = true;
                            p.y += 100;
                        }
                    }
                }

                if (entorno.sePresiono(entorno.TECLA_DERECHA)) {
                    if (p.seleccionada && p.plantada) {
                        int indiceX = cua.cercano(p.x, p.y).x;
                        int indiceY = cua.cercano(p.x, p.y).y;
                        if (indiceX <= 6 && !cua.ocupado[indiceX + 1][indiceY]) {
                            cua.ocupado[indiceX][indiceY] = false;
                            cua.ocupado[indiceX + 1][indiceY] = true;
                            p.x += 100;
                        }
                    }
                }

                if (entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
                    if (p.seleccionada && p.plantada) {
                        int indiceX = cua.cercano(p.x, p.y).x;
                        int indiceY = cua.cercano(p.x, p.y).y;
                        if (indiceX > 1 && !cua.ocupado[indiceX - 1][indiceY]) {
                            cua.ocupado[indiceX][indiceY] = false;
                            cua.ocupado[indiceX - 1][indiceY] = true;
                            p.x -= 100;
                        }
                    }
                }
            }
        }

        // --- Crear nuevas plantas ---
        if (!plantasNoPlantadas(plantas)) {
            crearPlanta(plantas);
        }

        // --- Crear zombies ---
        if (zombisActivos < 15 && zombisEliminados + zombisActivos < totalZombis) {
            if (Math.random() < 0.02) {
                for (int i = 0; i < zombis.length; i++) {
                    if (zombis[i] == null) {
                        double y = 150 + (int) (Math.random() * 5) * 100;
                        zombis[i] = new ZombieGrinch(850, y, entorno);
                        zombisActivos++;
                        break;
                    }
                }
            }
        }

        // --- Actualizar y dibujar zombies ---
        for (int i = 0; i < zombis.length; i++) {
            ZombieGrinch z = zombis[i];
            if (z != null) {
                z.mover();
                z.dibujar();

                // Colisión con proyectiles
                for (Planta p : plantas) {
                    if (p != null && p.proyectiles != null) {
                        for (int j = 0; j < p.proyectiles.length; j++) {
                            Proyectil pr = p.proyectiles[j];
                            if (pr != null && pr.colisionaCon(z)) {
                                z.recibirGolpe();
                                p.proyectiles[j] = null;
                                if (!z.estaVivo()) {
                                    zombis[i] = null;
                                    zombisEliminados++;
                                    zombisActivos--;
                                    break;
                                }
                            }
                        }
                    }
                }

                // --- Colisión ZOMBIE vs PLANTA ---
                for (int j = 0; j < plantas.length; j++) {
                    if (plantas[j] != null && colisionZombiePlanta(z, plantas[j])) {
                        Point celda = cua.cercano(plantas[j].x, plantas[j].y);
                        cua.ocupado[celda.x][celda.y] = false;
                        plantas[j] = null;
                        break; // el zombie sigue, la planta muere
                    }
                }

                // Zombie llega a los regalos
                if (z != null && z.llegoARegalo()) {
                    juegoTerminado = true;
                    gano = false;
                    return;
                }
            }
        }

        // --- Condición de victoria ---
        if (zombisEliminados >= totalZombis) {
            juegoTerminado = true;
            gano = true;
        }

        // --- HUD ---
        entorno.cambiarFont("Arial", 18, Color.WHITE);
        entorno.escribirTexto("Zombies eliminados: " + zombisEliminados, 550, 30);
        entorno.escribirTexto("Zombies restantes: " + (totalZombis - zombisEliminados), 550, 50);
    }

    // --- AUXILIARES ---
    public boolean plantasNoPlantadas(Planta[] pl) {
        for (Planta p : pl) {
            if (p != null && !p.plantada)
                return true;
        }
        return false;
    }

    private void crearPlanta(Planta[] pl) {
        for (int x = 0; x < pl.length; x++) {
            if (pl[x] == null) {
                pl[x] = new Planta(50, 50, entorno);
                return;
            }
        }
    }

    // --- COLISIÓN ZOMBIE - PLANTA ---
    private boolean colisionZombiePlanta(ZombieGrinch z, Planta p) {
        if (z == null || p == null)
            return false;
        double dx = z.x - p.x;
        double dy = z.y - p.y;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        return distancia < 30;
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}