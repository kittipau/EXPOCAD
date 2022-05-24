/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadexpo;

import cadexpo.Servidor.Servidor;

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

        Disenador dis = new Disenador();
        dis.setAlias("kitti");
        dis.setDescripcion("uh llalal");
        dis.setDiseñadorID(11);

        //cad.eliminarDisenador(10);
        //cad.insertarDisenador(dis);
        //cad.actualizarDisenador(11,dis);
        //System.out.println(cad.buscarDisenadores(11));
        //System.out.println(cad.leerDisenadores());
        Diseno di = new Diseno();
        di.setNombre("kitidiseño");
        di.setDescripcion("este es mi kittidiseño modificado");
        di.setDiseñoID(11);
        di.setDiseñador(dis);

        //cad.insertarDiseno(di);
        //cad.actualizarDiseno(11, di);
        //cad.eliminarDiseno(11);
        //System.out.println(cad.buscarDiseno(11));
        //System.out.println(cad.leerDisenos());
        
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
