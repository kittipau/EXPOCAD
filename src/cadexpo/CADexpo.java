/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadexpo;

import cadexpo.Servidor.Servidor;
import cadexpo.Servidor.SesionServidor;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.UIManager;
import org.postgresql.largeobject.LargeObjectManager;
import pojos.ExcepcionExpo;
import pojos.Usuario;
import pojos.Configuracion;
import pojos.Participante;
import pojos.Votante;

/**
 *
 * @author Paula Unibaso
 */
public class CADexpo {

    Connection conexion;
    SesionServidor sesion;

    /**
     * ConstructoR Utiliza la librería de Oracle
     *
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de MuseoException
     */
    public CADexpo() throws ExcepcionExpo {
        try {
            Class.forName("org.postgresql.Driver");
            // Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(null);
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(null);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    }

    /**
     * Método para crear la conexión a la Base de Datos
     *
     * @throws ExcepcionMUSEO
     */
    private void conectarExpo() throws ExcepcionExpo {
        try {
            conexion = DriverManager.getConnection("jdbc:postgresql://192.168.0.27:5432/BIAAF", "odoo", "123abc.");
            //conexion = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.69:1521:test", "EXPO", "kk");
            //conexion = DriverManager.getConnection("jdbc:oracle:thin:@172.16.200.69:1521:test", "EXPO", "kk");

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(null);
            e.setMensajeErrorUsuario("Error general del sistema. Consule con el administrador ");
            throw e;
        }
    }

    //-----------------------------------------------------------------------------------------------
    //   USUARIOS
    //-----------------------------------------------------------------------------------------------
    /**
     * Método para insertar un nuevo usuario en la Base de datos
     *
     * @param usuario Objeto Usuario con todos los datos que se quieren insertar
     * @return Cantidad de registros insertados (1 si se ha insertado, 0 si no.)
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     */
    
    public int insertarVotante (Votante votante) throws ExcepcionExpo {
        conectarExpo();
        int registrosafectados = 0;
        String dml = "insert into BIAAF_VOTANTE (NOMBRE, APELLIDO, EMAIL)"
                + "values ( ?, ?, ?)";
        try {

            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setObject(1, votante.getNombre());
            sentenciaPreparada.setObject(2, votante.getApellido());
            sentenciaPreparada.setObject(3, votante.getMail());

            registrosafectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            switch (ex.getErrorCode()) {
                case 1400:
                    e.setMensajeErrorUsuario("El nombre de la obra y el identificador del artista son obligatorios.");
                    break;
                case 1403:
                    e.setMensajeErrorUsuario("No existe el artista seleccionado.");
                case 2291:
                    e.setMensajeErrorUsuario("No existe el artista seleccionado.");
                    break;
                case 20002:
                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener el alias del artista.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;

        }

        return registrosafectados;
    }
    public int insertarUsuario(Usuario usuario) throws ExcepcionExpo {
        conectarExpo();
        int registrosafectados = 0;
        String dml = "insert into biaaf_usuario(USERNAME, EMAIL, CONTRASENA)"
                + "values ( ?, ?, ?)";
        try {

            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setObject(1, usuario.getUser());
            sentenciaPreparada.setObject(2, usuario.getMail());
            sentenciaPreparada.setObject(3, usuario.getContra());
            registrosafectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            switch (ex.getErrorCode()) {
                case 23505:
                    e.setMensajeErrorUsuario("Ya existe un usuario con ese nombre con ese mail");
                    break;
                case 22001:
                    e.setMensajeErrorUsuario("El usuario tiene un máximo de 20 caracteres, el mail de 35 y la contraseña de 10."); 
                case 23502:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios.");
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;

        }

        return registrosafectados;
    }
    

    /**
     * Método para eliminar un disenador de la Base de datos
     *
     * @param nombre id del disenador que se quiere eliminar
     * @return Cantidad de registros insertados (1 si se ha eliminado, 0 si no)
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     */
    public int eliminarUsuario(String username) throws ExcepcionExpo {
        conectarExpo();

        String dml = "delete from biaaf_usuario where USERNAME= " + "'" + username + "'";
        int registrosAfectados = 0;

        try {
            Statement sentencia = conexion.createStatement();
            registrosAfectados = sentencia.executeUpdate(dml);
            sentencia.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);
            switch (ex.getErrorCode()) {
                case 904:
                    e.setMensajeErrorUsuario("No existe un usuario con el identificador indicado.");
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosAfectados;
    }

    /**
     * Método para actualizar la información de un Diseñador
     *
     * @param usuarioID id del usuario que se quiere actualizar
     * @param u objeto de tipo Usuario con los datos nuevos
     * @return Cantidad de registros modificados (1 si se ha modificado, 0 si
     * no)
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     */
    public int actualizarUsuario(String username, Usuario u) throws ExcepcionExpo {

        conectarExpo();
        int registrosafectados = 0;
        String dml = "update biaaf_usuario set USERNAME = ?, MAIL = ?, CONTRA = ?, DISENO_ID = ? where    USERNAME = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setObject(1, u.getUser());
            sentenciaPreparada.setObject(2, u.getMail());
            sentenciaPreparada.setObject(3, u.getContra());
            //  sentenciaPreparada.setObject(4, u.getDiseño().getDiseñoID());
            sentenciaPreparada.setString(5, username);

            registrosafectados = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            switch (ex.getErrorCode()) {
                case 1:
                    e.setMensajeErrorUsuario("El alias y el email deben ser únicos.");
                    break;
                case 1407:
                    e.setMensajeErrorUsuario("El nombre, el apellido, el DNI, y el teléfono son obligatorios");
                    break;
                // case 2292:
                //    e.setMensajeErrorUsuario("No se puede moficiar el id del artista");
                case 2290:
                    e.setMensajeErrorUsuario("Error en el formato: El email debe llevar el caracter \"@\", el DNI 8 dígitos y una letra, y el teléfono 9 dígitos.");
                    break;
                case 12899:
                    e.setMensajeErrorUsuario("EL DNI y el teléfono deben tener 9 caracteres exactos");
                    break;
                case 20004:
                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener al alias del artista.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
            }
            throw e;
        }
        return registrosafectados;
    }

    /**
     * Método para buscar la información de un diseñador de la Base de Datos
     *
     * @param usuarioID El id del usuario que se quiere buscar
     * @return a Devuelve un usuario con la informacion encontrada en la BD
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     *
     */
    public Usuario iniciarSesion(String username, String contraseña) throws ExcepcionExpo {
        Usuario u = new Usuario();
        conectarExpo();
        String dml = "select * from biaaf_usuario where USERNAME = ? and CONTRASENA = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, username);
            sentenciaPreparada.setString(2, contraseña);
            ResultSet resultado = sentenciaPreparada.executeQuery();

            resultado.next();
            u.setUsuarioID(resultado.getInt("ID"));
            u.setContra(resultado.getString("CONTRASENA"));
            u.setUser(resultado.getString("USERNAME"));
            u.setMail(resultado.getString("EMAIL"));
            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);
            switch (ex.getErrorCode()) {

                case 17011:
                    e.setMensajeErrorUsuario("No existe ningún artista con ese ID.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            }
            throw e;
        }
        return u;
    }

    public Usuario buscarUsuario(String username) throws ExcepcionExpo {
        Usuario u = new Usuario();
        conectarExpo();
        int registrosafectados = 0;

        String dml = "select * from biaaf_usuario where USERNAME = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, username);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            resultado.next();
            u.setUsuarioID(resultado.getInt("ID"));
            u.setContra(resultado.getString("CONTRASENA"));
            u.setUser(resultado.getString("USERNAME"));
            u.setMail(resultado.getString("EMAIL"));
            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);
            switch (ex.getErrorCode()) {

                case 17011:
                    e.setMensajeErrorUsuario("No existe ningún artista con ese ID.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            }
            throw e;
        }
        return u;
    }

    /**
     * Método para buscar la información de un diseñador de la Base de Datos
     *
     * @param disenadorID El id del diseñador que se quiere buscar
     * @return a Devuelve un diseñador con la informacion encontrada en la BD
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     *
     */
    public Participante buscarDisenador(String nombre) throws ExcepcionExpo, IOException {
        Participante p = new Participante();
        conectarExpo();
        String dml = "select * from biaaf_participante where aliasdisenador =?";

        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, nombre);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            resultado.next();
            p.setDescripcionDiseno(resultado.getString("descripciondiseno"));
            p.setPaisDisenador(resultado.getString("PAISDISENADOR"));
            p.setNombreDisenador(resultado.getString("ALIASDISENADOR"));
            p.setNombreDiseno(resultado.getString("NOMBREDISENO"));
            p.setParticipanteID(resultado.getInt("ID"));
            p.setImagenDisenador(resultado.getBytes("IMAGENDISENADOR"));
            p.setImagenDiseno(resultado.getBytes("IMAGENDISENO"));

            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);
            switch (ex.getErrorCode()) {

                case 17011:
                    e.setMensajeErrorUsuario("No existe ningún artista con ese ID.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            }
            throw e;
        }
        return p;
    }

    /**
     * Método para ver todos los diseñadores de la Base de datos
     *
     * @return listaArtistas. Devuelve un ArrayList con la lista de artistas
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     */
    public ArrayList<Participante> leerDisenadores() throws ExcepcionExpo, IOException {
        ArrayList<Participante> listaDisenadores = new ArrayList<Participante>();
        conectarExpo();
        String dql1 = "select * from BIAAF_PARTICIPANTE";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql1);

            while (resultado.next()) {
                Participante p = new Participante();
                p.setDescripcionDiseno(resultado.getString("descripciondiseno"));
                p.setPaisDisenador(resultado.getString("PAISDISENADOR"));
                p.setNombreDisenador(resultado.getString("ALIASDISENADOR"));
                p.setNombreDiseno(resultado.getString("NOMBREDISENO"));
                p.setParticipanteID(resultado.getInt("ID"));
                p.setImagenDisenador(resultado.getBytes("IMAGENDISENADOR"));
                p.setImagenDiseno(resultado.getBytes("IMAGENDISENO"));

                System.out.println(p.toString());
                listaDisenadores.add(p);
            }
            resultado.close();

            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dql1);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            throw e;
        }
        return listaDisenadores;
    }

    public ArrayList<Participante> listarGanadores() throws ExcepcionExpo, IOException {
        ArrayList<Participante> listaDisenadores = new ArrayList<Participante>();
        conectarExpo();
        String dql1 = "select diseno_id, count(*) as votos FROM biaaf_usuario GROUP BY diseno_id";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql1);

            while (resultado.next()) {
                Participante p = new Participante();
                p.setDescripcionDiseno(resultado.getString("descripciondiseno"));
                p.setPaisDisenador(resultado.getString("PAISDISENADOR"));
                p.setNombreDisenador(resultado.getString("ALIASDISENADOR"));
                p.setNombreDiseno(resultado.getString("NOMBREDISENO"));
                p.setParticipanteID(resultado.getInt("ID"));
                p.setImagenDisenador(resultado.getBytes("IMAGENDISENADOR"));
                p.setImagenDiseno(resultado.getBytes("IMAGENDISENO"));

                System.out.println(p.toString());
                listaDisenadores.add(p);
            }
            resultado.close();

            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dql1);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            throw e;
        }
        return listaDisenadores;
    }

    public Configuracion horaFin() throws ExcepcionExpo, IOException {
        Configuracion c = new Configuracion();
        conectarExpo();
        String dql1 = "select * from BIAAF_CONFIGURAION";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql1);

            while (resultado.next()) {

                c.setFechaLimite(resultado.getDate("fechalimte"));
                System.out.println(c.toString());
            }
            resultado.close();

            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dql1);
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            throw e;
        }
        return c;
    }
    
    public Votante comprobarEmail (String email) throws ExcepcionExpo, IOException {
        Votante v = new Votante();
        conectarExpo();
        String dml = "select * from BIAAF_VOTANTE where EMAIL =?";

        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, email);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            resultado.next();
            v.setNombre(resultado.getString("nombre"));
            v.setApellido(resultado.getString("apellido"));
            v.setMail(resultado.getString("email"));


            sentenciaPreparada.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionExpo e = new ExcepcionExpo();
            e.setCodigoError(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);
            switch (ex.getErrorCode()) {

                case 17011:
                    e.setMensajeErrorUsuario("No existe ningún artista con ese ID.");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");

            }
            throw e;
        }
        return v;
    } 


}
