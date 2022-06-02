/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadexpo;

import cadexpo.Servidor.Servidor;
import java.io.IOException;
import java.sql.SQLException;
import pojos.ExcepcionExpo;
import pojos.Participante;
import pojos.Usuario;
import static sun.net.www.http.HttpClient.New;

/**
 *
 * @author Paula
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ExcepcionExpo, IOException, SQLException {

        CADexpo cad = new CADexpo();
        Servidor servidor = new Servidor();
        
        servidor.start();

       
        
        Usuario u = new Usuario();
        u.setUser("kitiperrima3");
        u.setMail("kiti@kitiwifi.com3");
        u.setContra("kk");
        
        cad.insertarUsuario(u);
        
        
        //cad.buscarDisenador("kitti");
        
        //Participante p = new Participante();
        //p.setNombreDisenador("kitti");
        
        //cad.eliminarUsuario("ASas");
        
//        u = cad.iniciarSesion("sfsd", "123");
//        System.out.println(u.toString());
//        
   
//        p = cad.buscarDisenador("kitti");
//        System.out.println(p.toString());
       
        
        //cad.leerDisenadores();
        //System.out.println(p.toString());        
        
       // cad.eliminarUsuario(u.getUser());
        
        //cad.insertarUsuario(u);
        //cad.actualizarUsuario(14, u);
        //cad.eliminarUsuario(14);
        //System.out.println(cad.buscarUsuario(13));
        //System.out.println(cad.leerUsuarios());
        
        //Configuracion c  = new Configuracion();
        


        
        
        
        
    }

}
