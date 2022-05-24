package cadexpo.Servidor;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase implementa un hilo que permite 2 conexiones de cliente. En cada
 * conexión, recibe un mensaje del cliente, lo muestra por consola y envía un
 * mensaje a dicho cliente
 *
 */
public class Servidor extends Thread {
    
    /**
     * Método que implementa el comportamiento del hilo
     */
    @Override
    public void run() {
        try {
               System.out.println("Servidor.Consola - Se abre un socket servidor en el puerto 30500 de la máquina local");
            int puertoServidor = 30500;
            ServerSocket socketServidor = new ServerSocket(puertoServidor);
            
            System.out.println("Servidor.Consola - Se crea un ArrayList para almacenar los manejadores de sockets de los clientes");
            ArrayList<SesionServidor> sesiones = new ArrayList();
            System.out.println("Servidor.Consola - El servidor queda a la espera indefinidamente de todas las conexiones de cleinte que se produzcan");
            Socket clienteConectado;
            SesionServidor sesion;
            while (true) {
                clienteConectado = socketServidor.accept();
                sesion = new SesionServidor(clienteConectado);
                sesiones.add(sesion);
                sesion.start();
            }
            }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        }

    }
