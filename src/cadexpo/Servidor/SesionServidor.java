/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadexpo.Servidor;

import cadexpo.CADexpo;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Configuracion;
import pojos.ExcepcionExpo;
import pojos.Participante;

import pojos.Votante;

/**
 *
 * @author pulpracticas.VSTI
 */
public class SesionServidor extends Thread {

    Socket clienteConectado;

    public SesionServidor(Socket clienteConectado) {
        this.clienteConectado = clienteConectado;
    }

    @Override
    public void run() {
        try {
            CADexpo cad = new CADexpo();
            Votante votante = new Votante();
            Participante participante = new Participante();
            ArrayList<Participante> listaParticipantes = new ArrayList<Participante>();
            Configuracion config = new Configuracion();
            //Recibo la opción para saber qué método invocar  

            DataInputStream dis = new DataInputStream(clienteConectado.getInputStream());
            System.out.println("Nueva sesión");
            // abro el objeto que recibiré y leo la opción
            String opcion = dis.readUTF();
            System.out.println(opcion);
            //abro el cad                

            //preparo el objeto que devolveré
            if (opcion.equalsIgnoreCase("1")) {
                //leo el objeto  
                ObjectInputStream ois = new ObjectInputStream(clienteConectado.getInputStream());
                votante = (Votante) ois.readObject();
                //lo inserto en la BD
                cad.insertarVotante(votante);

            } else if (opcion.equalsIgnoreCase("2")) {
                //leo el objeto  
                ObjectInputStream ois = new ObjectInputStream(clienteConectado.getInputStream());
                votante = (Votante) ois.readObject();

                votante = cad.comprobarEmail(votante.getMail());

                ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
                oos.writeObject(votante);

            } else if (opcion.equalsIgnoreCase("3")) {
                ObjectInputStream ois = new ObjectInputStream(clienteConectado.getInputStream());
                participante = (Participante) ois.readObject();

                System.out.println("Leo el objeto" + participante.toString());

                participante = cad.buscarDisenador(participante.getNombreDisenador());
                ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
                oos.writeObject(participante);
                System.out.println(participante.toString());
                oos.close();

            } else if (opcion.equalsIgnoreCase("4")) {
                ObjectInputStream ois = new ObjectInputStream(clienteConectado.getInputStream());
                votante = (Votante) ois.readObject();
                cad.comprobarEmail(votante.getMail());

                ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
                oos.writeObject(votante);

            } else if (opcion.equalsIgnoreCase("6")) {
                listaParticipantes = cad.leerDisenadores();
                ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
                oos.writeObject(listaParticipantes);
                System.out.println(listaParticipantes.toString());

            } else if (opcion.equalsIgnoreCase("7")) {
                config = cad.horaFin();
                ObjectOutputStream oos = new ObjectOutputStream(clienteConectado.getOutputStream());
                oos.writeObject(config);
                System.out.println(config.toString());
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionExpo ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
