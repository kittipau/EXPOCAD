/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadexpo;

import cadexpo.Servidor.Servidor;
import pojos.ExcepcionExpo;
import pojos.Usuario;

/**
 *
 * @author Paula
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ExcepcionExpo {

        CADexpo cad = new CADexpo();
        Servidor servidor = new Servidor();
        
        servidor.start();

       
        
        Usuario u = new Usuario();
        u.setUser("kitiperrima3");
        u.setMail("kiti@kitiwifi.com3");
        u.setContra("kk");

        //cad.insertarUsuario(u);
        
       // cad.eliminarUsuario(u.getUser());
        
        //cad.insertarUsuario(u);
        //cad.actualizarUsuario(14, u);
        //cad.eliminarUsuario(14);
        //System.out.println(cad.buscarUsuario(13));
        //System.out.println(cad.leerUsuarios());
        
        //Configuracion c  = new Configuracion();
        


        
        
        
        
    }

}
