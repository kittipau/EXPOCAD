/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadexpo.Servidor;

import cadexpo.CADexpo;
import cadexpo.ExcepcionExpo;
import cadexpo.Usuario;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            //Recibo la opción para saber qué método invocar  

            InputStream is = clienteConectado.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            // abro el objeto que recibiré
            String opcion = dis.readUTF();

            ObjectInputStream ois = new ObjectInputStream(is);
            //abro el cad
            
            OutputStream os = clienteConectado.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            //preparo el objeto que devolveré
            if (opcion.equalsIgnoreCase("1")) {
                //leo el objeto            
                usuario = (Usuario) ois.readObject();
                //lo inserto en la BD
                cad.insertarUsuario(usuario);
            } else if (opcion.equalsIgnoreCase("2")){
                //leo el objeto 
                 usuario = (Usuario) ois.readObject();
                 //elimino el usuario
                cad.eliminarUsuario(usuario.getUser());
            } else if (opcion.equalsIgnoreCase("3")){
                 usuario = (Usuario) ois.readObject();
            } 

            //cierro
            is.close();
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
