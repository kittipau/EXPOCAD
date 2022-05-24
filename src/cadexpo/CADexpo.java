/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadexpo;


import cadexpo.Servidor.Servidor;
import cadexpo.Servidor.SesionServidor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.UIManager;

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
    public int insertarUsuario(Usuario usuario) throws ExcepcionExpo {
        conectarExpo();
        int registrosafectados = 0;
        String dml = "insert into PAULA_USUARIOS(USERNAME, EMAIL, CONTRASENA)"
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

        String dml = "delete from PAULA_USUARIOS where USERNAME= " +"'"+ username +"'";
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
        String dml = "update PAULA_USUARIOS set USERNAME = ?, MAIL = ?, CONTRA = ?, DISENO_ID = ? where    USERNAME = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setObject(1, u.getUser());
            sentenciaPreparada.setObject(2, u.getMail());
            sentenciaPreparada.setObject(3, u.getContra());
            sentenciaPreparada.setObject(4, u.getDiseño().getDiseñoID());
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
        Diseno d = new Diseno();
        conectarExpo();
        String dml = "select * from PAULA_USUARIOS where USERNAME = ? and CONTRASENA = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, username);
            sentenciaPreparada.setString(1, contraseña);
            ResultSet resultado = sentenciaPreparada.executeQuery();

            resultado.next();
            u.setUsuarioID(resultado.getInt("USUARIO_ID"));
            u.setContra(resultado.getString("CONTRA"));
            u.setUser(resultado.getString("USERNAME"));
            u.setMail(resultado.getString("MAIL"));
            u.setDiseño(buscarDiseno(resultado.getInt("DISENO_ID")));

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

        public int buscarUsuario(String username) throws ExcepcionExpo {
        Usuario u = new Usuario();
        Diseno d = new Diseno();
        conectarExpo();
        int registrosafectados =0;
        
        String dml = "select * from PAULA_USUARIOS where USERNAME = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setString(1, username);
            ResultSet resultado = sentenciaPreparada.executeQuery();
            resultado.next();
            registrosafectados = sentenciaPreparada.executeUpdate();
            
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
        return registrosafectados;
    }
//
// 
    /**
     * Método para buscar la información de un diseñador de la Base de Datos
     *
     * @param disenadorID El id del diseñador que se quiere buscar
     * @return a Devuelve un diseñador con la informacion encontrada en la BD
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     *
     */
    public Disenador buscarDisenador(Integer disenadorID) throws ExcepcionExpo {
        Disenador d = new Disenador();
        conectarExpo();
        String dml = "select * from DISENADOR where DISENADOR_ID = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setInt(1, disenadorID);
            ResultSet resultado = sentenciaPreparada.executeQuery();

            resultado.next();
            d.setDiseñadorID(resultado.getInt("DISENADOR_ID"));
            d.setAlias(resultado.getString("ALIAS"));
            d.setDescripcion(resultado.getString("DESCRIPCION"));
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
        return d;
    }

    /**
     * Método para ver todos los diseñadores de la Base de datos
     *
     * @return listaArtistas. Devuelve un ArrayList con la lista de artistas
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     */
    public ArrayList<Disenador> leerDisenadores() throws ExcepcionExpo {
        ArrayList<Disenador> listaDisenadores = new ArrayList<Disenador>();
        conectarExpo();
        String dql1 = "select * from DISENADOR";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql1);

            while (resultado.next()) {
                Disenador d = new Disenador();
                d.setDiseñadorID(resultado.getInt("DISENADOR_ID"));
                d.setAlias(resultado.getString("ALIAS"));
                d.setDescripcion(resultado.getString("DESCRIPCION"));

                listaDisenadores.add(d);
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

//   
// 
    /**
     * Método para buscar la información de un diseño de la Base de Datos
     *
     * @param disenoID El id del diseño que se quiere buscar
     * @return a Devuelve un diseñador con la informacion encontrada en la BD
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     *
     */
    public Diseno buscarDiseno(Integer disenoID) throws ExcepcionExpo {
        Diseno d = new Diseno();
        Disenador di = new Disenador();
        conectarExpo();
        String dml = "select * from DISENO where DISENO_ID = ?";
        try {
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            sentenciaPreparada.setInt(1, disenoID);
            ResultSet resultado = sentenciaPreparada.executeQuery();

            resultado.next();
            d.setDiseñoID(resultado.getInt("DISENO_ID"));
            d.setNombre(resultado.getString("NOMBRE"));
            d.setDescripcion(resultado.getString("DESCRIPCION"));
            di = buscarDisenador(resultado.getInt("DISENADOR_ID"));
            d.setDiseñador(di);
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
        return d;
    }

    /**
     * Método para ver todos los diseños de la Base de datos
     *
     * @return listadisenos. Devuelve un ArrayList con la lista de diseños
     * @throws ExcepcionExpo En caso de algún error se produce la excepción
     * personalizada a través de ExcepcionExpo
     */
    public ArrayList<Diseno> leerDisenos() throws ExcepcionExpo {
        ArrayList<Diseno> listadisenos = new ArrayList<Diseno>();
        conectarExpo();
        Diseno d = new Diseno();
        Disenador di = new Disenador();
        String dql1 = "select * from DISENO";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(dql1);

            while (resultado.next()) {
                d.setDiseñoID(resultado.getInt("DISENO_ID"));
                d.setNombre(resultado.getString("NOMBRE"));
                d.setDescripcion(resultado.getString("DESCRIPCION"));
                di = buscarDisenador(resultado.getInt("DISENADOR_ID"));
                d.setDiseñador(di);
                listadisenos.add(d);
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
        return listadisenos;
    }

    
     /**
//     * Método para ver todos los usuarios de la Base de datos
//     *
//     * @return listaUsuarios. Devuelve un ArrayList con la lista de usuarios
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public ArrayList<Usuario> leerUsuarios() throws ExcepcionExpo {
//        ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
//        conectarExpo();
//        Usuario u = new Usuario();
//        Diseno d = new Diseno();
//        String dql1 = "select * from USUARIO";
//
//        try {
//            Statement sentencia = conexion.createStatement();
//            ResultSet resultado = sentencia.executeQuery(dql1);
//
//            while (resultado.next()) {
//                resultado.next();
//                u.setUsuarioID(resultado.getInt("USUARIO_ID"));
//                u.setContra(resultado.getString("CONTRA"));
//                u.setUser(resultado.getString("USER_NAME"));
//                u.setMail(resultado.getString("MAIL"));
//                u.setDiseño(buscarDiseno(resultado.getInt("DISENO_ID")));
//                listaUsuarios.add(u);
//            }
//            resultado.close();
//
//            sentencia.close();
//            conexion.close();
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dql1);
//            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//
//            throw e;
//        }
//        return listaUsuarios;
//    }

    
       /**
//     * Método para insertar un nuevo diseñador en la Base de datos
//     *
//     * @param disenador Objeto Disenador con todos los datos que se quieren
//     * insertar
//     * @return Cantidad de registros insertados (1 si se ha insertado, 0 si no.)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int insertarDisenador(Disenador disenador) throws ExcepcionExpo {
//        conectarExpo();
//        int registrosafectados = 0;
//        String dml = "insert into DISENADOR(DISENADOR_ID, ALIAS, DESCRIPCION)"
//                + "values (DISENADOR_SEQ.nextval, ?, ?)";
//        try {
//            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//            sentenciaPreparada.setObject(1, disenador.getAlias());
//            sentenciaPreparada.setObject(2, disenador.getDescripcion());
//
//            registrosafectados = sentenciaPreparada.executeUpdate();
//            sentenciaPreparada.close();
//            conexion.close();
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//
//            switch (ex.getErrorCode()) {
//                case 1400:
//                    e.setMensajeErrorUsuario("El nombre de la obra y el identificador del artista son obligatorios.");
//                    break;
//                case 1403:
//                    e.setMensajeErrorUsuario("No existe el artista seleccionado.");
//                case 2291:
//                    e.setMensajeErrorUsuario("No existe el artista seleccionado.");
//                    break;
//                case 20002:
//                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener el alias del artista.");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//
//        }
//
//        return registrosafectados;
//    }
//
//    /**
//     * Método para eliminar un disenador de la Base de datos
//     *
//     * @param disenadorID id del disenador que se quiere eliminar
//     * @return Cantidad de registros insertados (1 si se ha eliminado, 0 si no)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int eliminarDisenador(Integer disenadorID) throws ExcepcionExpo {
//        conectarExpo();
//
//        String dml = "delete from DISENADOR where DISENADOR_ID=" + disenadorID;
//        int registrosAfectados = 0;
//
//        try {
//            Statement sentencia = conexion.createStatement();
//            registrosAfectados = sentencia.executeUpdate(dml);
//            sentencia.close();
//            conexion.close();
//
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//            switch (ex.getErrorCode()) {
//                case 904:
//                    e.setMensajeErrorUsuario("No existe un disenador con el identificador indicado.");
//                case 2292:
//                    e.setMensajeErrorUsuario("No se puede eliminar el disenador porque tiene diseños asociadas");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//        }
//        return registrosAfectados;
//    }
//
//    /**
//     * Método para actualizar la información de un Diseñador
//     *
//     * @param disenadorID id del disenador que se quiere actualizar
//     * @param d objeto de tipo Disenador con los datos nuevos
//     * @return Cantidad de registros modificados (1 si se ha modificado, 0 si
//     * no)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int actualizarDisenador(Integer disenadorID, Disenador d) throws ExcepcionExpo {
//
//        conectarExpo();
//        int registrosafectados = 0;
//        String dml = "update DISENADOR set ALIAS = ?, DESCRIPCION = ? where DISENADOR_ID = ?";
//        try {
//            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//            sentenciaPreparada.setObject(1, d.getAlias());
//            sentenciaPreparada.setObject(2, d.getDescripcion());
//            sentenciaPreparada.setInt(3, disenadorID);
//            registrosafectados = sentenciaPreparada.executeUpdate();
//            sentenciaPreparada.close();
//            conexion.close();
//
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//
//            switch (ex.getErrorCode()) {
//                case 1:
//                    e.setMensajeErrorUsuario("El alias y el email deben ser únicos.");
//                    break;
//                case 1407:
//                    e.setMensajeErrorUsuario("El nombre, el apellido, el DNI, y el teléfono son obligatorios");
//                    break;
//                // case 2292:
//                //    e.setMensajeErrorUsuario("No se puede moficiar el id del artista");
//                case 2290:
//                    e.setMensajeErrorUsuario("Error en el formato: El email debe llevar el caracter \"@\", el DNI 8 dígitos y una letra, y el teléfono 9 dígitos.");
//                    break;
//                case 12899:
//                    e.setMensajeErrorUsuario("EL DNI y el teléfono deben tener 9 caracteres exactos");
//                    break;
//                case 20004:
//                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener al alias del artista.");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//        }
//        return registrosafectados;
//    }
    
     //-----------------------------------------------------------------------------------------------
//    //   DISEÑOS
//    //-----------------------------------------------------------------------------------------------
//    /**
//     * Método para insertar un nuevo Diseño en la Base de datos
//     *
//     * @param diseno Objeto Diseno con todos los datos que se quieren insertar
//     * @return Cantidad de registros insertados (1 si se ha insertado, 0 si no.)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int insertarDiseno(Diseno diseno) throws ExcepcionExpo {
//        conectarExpo();
//        int registrosafectados = 0;
//        String dml = "insert into DISENO(DISENO_ID, NOMBRE, DESCRIPCION, DISENADOR_ID)"
//                + "values (DISENO_SEQ.nextval, ?, ?, ?)";
//        try {
//            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//            sentenciaPreparada.setObject(1, diseno.getNombre());
//            sentenciaPreparada.setObject(2, diseno.getDescripcion());
//            sentenciaPreparada.setObject(3, diseno.getDiseñador().getDiseñadorID());
//            registrosafectados = sentenciaPreparada.executeUpdate();
//            sentenciaPreparada.close();
//            conexion.close();
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//
//            switch (ex.getErrorCode()) {
//                case 1400:
//                    e.setMensajeErrorUsuario("El nombre de la obra y el identificador del artista son obligatorios.");
//                    break;
//                case 1403:
//                    e.setMensajeErrorUsuario("No existe el artista seleccionado.");
//                case 2291:
//                    e.setMensajeErrorUsuario("No existe el artista seleccionado.");
//                    break;
//                case 20002:
//                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener el alias del artista.");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//
//        }
//
//        return registrosafectados;
//    }

    
       /**
//     * Método para eliminar un diseno de la Base de datos
//     *
//     * @param disenoID id del disenador que se quiere eliminar
//     * @return Cantidad de registros insertados (1 si se ha eliminado, 0 si no)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int eliminarDiseno(Integer disenoID) throws ExcepcionExpo {
//        conectarExpo();
//
//        String dml = "delete from DISENO where DISENO_ID=" + disenoID;
//        int registrosAfectados = 0;
//
//        try {
//            Statement sentencia = conexion.createStatement();
//            registrosAfectados = sentencia.executeUpdate(dml);
//            sentencia.close();
//            conexion.close();
//
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//            switch (ex.getErrorCode()) {
//                case 904:
//                    e.setMensajeErrorUsuario("No existe un disenador con el identificador indicado.");
//                case 2292:
//                    e.setMensajeErrorUsuario("No se puede eliminar el disenador porque tiene diseños asociadas");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//        }
//        return registrosAfectados;
//    }

    
//    /**
//     * Método para actualizar la información de un Diseñador
//     *
//     * @param disenoID id del disenador que se quiere actualizar
//     * @param d objeto de tipo Diseno con los datos nuevos
//     * @return Cantidad de registros modificados (1 si se ha modificado, 0 si
//     * no)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int actualizarDiseno(Integer disenoID, Diseno d) throws ExcepcionExpo {
//
//        conectarExpo();
//        int registrosafectados = 0;
//        String dml = "update DISENO set NOMBRE = ?, DESCRIPCION = ?, DISENADOR_ID = ? where DISENO_ID = ?";
//        try {
//            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//            sentenciaPreparada.setObject(1, d.getNombre());
//            sentenciaPreparada.setObject(2, d.getDescripcion());
//            sentenciaPreparada.setObject(3, d.getDiseñador().getDiseñadorID());
//            sentenciaPreparada.setInt(4, disenoID);
//            registrosafectados = sentenciaPreparada.executeUpdate();
//            sentenciaPreparada.close();
//            conexion.close();
//
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//
//            switch (ex.getErrorCode()) {
//                case 1:
//                    e.setMensajeErrorUsuario("El alias y el email deben ser únicos.");
//                    break;
//                case 1407:
//                    e.setMensajeErrorUsuario("El nombre, el apellido, el DNI, y el teléfono son obligatorios");
//                    break;
//                // case 2292:
//                //    e.setMensajeErrorUsuario("No se puede moficiar el id del artista");
//                case 2290:
//                    e.setMensajeErrorUsuario("Error en el formato: El email debe llevar el caracter \"@\", el DNI 8 dígitos y una letra, y el teléfono 9 dígitos.");
//                    break;
//                case 12899:
//                    e.setMensajeErrorUsuario("EL DNI y el teléfono deben tener 9 caracteres exactos");
//                    break;
//                case 20004:
//                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener al alias del artista.");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//        }
//        return registrosafectados;
//    }


   
    
    //   
//    //-----------------------------------------------------------------------------------------------
//    //   CONFIGURACIÓN
//    //-----------------------------------------------------------------------------------------------
//    /**
//     * Método para actualizar la información de la Configuración
//     *
//     * @param usuarioID id del usuario que se quiere actualizar
//     * @param u objeto de tipo Usuario con los datos nuevos
//     * @return Cantidad de registros modificados (1 si se ha modificado, 0 si
//     * no)
//     * @throws ExcepcionExpo En caso de algún error se produce la excepción
//     * personalizada a través de ExcepcionExpo
//     */
//    public int actulizarConfig(Integer usuarioID, Usuario u) throws ExcepcionExpo {
//
//        conectarExpo();
//        int registrosafectados = 0;
//        String dml = "update USUARIO set USER = ?, MAIL = ?, CONTRA = ?, DISEÑO_ID = ?, where DISENO_ID = ?";
//        try {
//            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//            sentenciaPreparada.setObject(1, u.getUser());
//            sentenciaPreparada.setObject(2, u.getMail());
//            sentenciaPreparada.setObject(3, u.getContra());
//            sentenciaPreparada.setObject(4, u.getDiseño().getDiseñoID());
//            sentenciaPreparada.setInt(5, usuarioID);
//
//            registrosafectados = sentenciaPreparada.executeUpdate();
//            sentenciaPreparada.close();
//            conexion.close();
//
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//
//            switch (ex.getErrorCode()) {
//                case 1:
//                    e.setMensajeErrorUsuario("El alias y el email deben ser únicos.");
//                    break;
//                case 1407:
//                    e.setMensajeErrorUsuario("El nombre, el apellido, el DNI, y el teléfono son obligatorios");
//                    break;
//                // case 2292:
//                //    e.setMensajeErrorUsuario("No se puede moficiar el id del artista");
//                case 2290:
//                    e.setMensajeErrorUsuario("Error en el formato: El email debe llevar el caracter \"@\", el DNI 8 dígitos y una letra, y el teléfono 9 dígitos.");
//                    break;
//                case 12899:
//                    e.setMensajeErrorUsuario("EL DNI y el teléfono deben tener 9 caracteres exactos");
//                    break;
//                case 20004:
//                    e.setMensajeErrorUsuario(" El nombre de la obra no puede contener al alias del artista.");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//            }
//            throw e;
//        }
//        return registrosafectados;
//    }
//    
    
//    public Configuracion verConfig() throws ExcepcionExpo {
//        Configuracion c = new Configuracion();
//        conectarExpo();
//        String dml = "select * from CONFIGURACION";
//        try {
//            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
//
//            ResultSet resultado = sentenciaPreparada.executeQuery();
//
//            resultado.next();
//            c.setFechaLimita(resultado.getDate("FECHA_LIMITE"));
//            c.setAdminPass(resultado.getString("ADMIN_PASS"));
//            c.setAdminNombre(resultado.getString("ADMIN_USER"));
//
//            sentenciaPreparada.close();
//            conexion.close();
//
//        } catch (SQLException ex) {
//            ExcepcionExpo e = new ExcepcionExpo();
//            e.setCodigoError(ex.getErrorCode());
//            e.setMensajeErrorBD(ex.getMessage());
//            e.setSentenciaSQL(dml);
//            switch (ex.getErrorCode()) {
//
//                case 17011:
//                    e.setMensajeErrorUsuario("No existe ningún artista con ese ID.");
//                    break;
//                default:
//                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador.");
//
//            }
//            throw e;
//        }
//        return c;
//    }

}
