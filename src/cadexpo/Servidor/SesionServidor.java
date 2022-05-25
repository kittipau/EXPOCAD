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
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.ExcepcionExpo;
import pojos.Participante;
import pojos.Usuario;

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
            Usuario usuario = new Usuario();
            Participante participante = new Participante();
            //Recibo la opción para saber qué método invocar  

            DataInputStream dis = new DataInputStream(clienteConectado.getInputStream());
            // abro el objeto que recibiré
            String opcion = dis.readUTF();
            System.out.println(opcion);

            ObjectInputStream ois = new ObjectInputStream(clienteConectado.getInputStream());
            //abro el cad

            OutputStream os = clienteConectado.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            //preparo el objeto que devolveré
            if (opcion.equalsIgnoreCase("1")) {
                //leo el objeto            
                usuario = (Usuario) ois.readObject();
                //lo inserto en la BD
                cad.insertarUsuario(usuario);
            } else if (opcion.equalsIgnoreCase("2")) {
                //leo el objeto 
                usuario = (Usuario) ois.readObject();
                //elimino el usuario
                cad.eliminarUsuario(usuario.getUser());
            } else if (opcion.equalsIgnoreCase("3")) {

                DataInputStream dis2 = new DataInputStream(clienteConectado.getInputStream());

                System.out.println("paso por aqui1");
                String nombre = dis2.readUTF();

                Participante p = new Participante();
               // p = cad.buscarDisenador(nombre);

                oos.writeObject(p);
                System.out.println(p.toString());

            }

            //cierro

            ois.close();
            os.close();
            oos.close();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ExcepcionExpo ex) {
            Logger.getLogger(SesionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
