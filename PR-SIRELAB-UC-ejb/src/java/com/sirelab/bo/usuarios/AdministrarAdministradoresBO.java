package com.sirelab.bo.usuarios;

import com.sirelab.bo.interfacebo.AdministrarAdministradoresBOInterface;
import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.dao.interfacedao.TipoUsuarioDAOInterface;
import com.sirelab.dao.interfacedao.UsuarioDAOInterface;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.TipoUsuario;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class AdministrarAdministradoresBO implements AdministrarAdministradoresBOInterface {

    @EJB
    TipoUsuarioDAOInterface tipoUsuarioDAO;
    @EJB
    UsuarioDAOInterface usuarioDAO;
    @EJB
    PersonaDAOInterface personaDAO;

    @Override
    public List<Persona> consultarAdministradoresPorParametro(Map<String, String> filtros) {
        try {
            List<Persona> lista = personaDAO.buscarAdministradoresPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarAdministradoresBO consultarAdministradoresPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona obtenerAdministradorPorIDPersona(BigInteger idPersona) {
        try {
            Persona registro = personaDAO.buscarPersonaPorID(idPersona);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarAdministradoresBO obtenerAdministradorPorIDPersona : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona obtenerAdministradorPorCorreoNumDocumento(String correo, String documento) {
        try {
            Persona registro = personaDAO.buscarPersonaPorCorreoYNumeroIdentificacion(correo, documento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarAdministradoresBO obtenerAdministradorPorCorreoNumDocumento : " + e.toString());
            return null;
        }
    }

    @Override
    public void actualizarInformacionAdministrador(Persona administrador) {
        try {
            personaDAO.editarPersona(administrador);
        } catch (Exception e) {
            System.out.println("Error AdministrarAdministradoresBO almacenarNuevaPersonaEnSistema : " + e.toString());
        }
    }

    //@Override
    public void actualizarInformacionUsuario(Usuario usuario) {
        try {
            usuarioDAO.editarUsuario(usuario);
        } catch (Exception e) {
            System.out.println("Error AdministrarAdministradoresBO almacenarNuevaPersonaEnSistema : " + e.toString());
        }
    }

    @Override
    public void almacenarNuevaPersonaEnSistema(Usuario usuarioNuevo, Persona personaNuevo) {
        try {
            //usuarioNuevo.setIdusuario(idUsuario);
            TipoUsuario tipoUsuario = tipoUsuarioDAO.buscarTipoUsuarioPorNombre("ADMINISTRADOR");
            usuarioNuevo.setTipousuario(tipoUsuario);
            usuarioDAO.crearUsuario(usuarioNuevo);
            Usuario usuarioRegistrado = usuarioDAO.obtenerUltimoUsuarioRegistrado();
            //personaNuevo.setIdpersona(idPersona);
            personaNuevo.setUsuario(usuarioRegistrado);
            personaDAO.crearPersona(personaNuevo);
        } catch (Exception e) {
            System.out.println("Error AdministrarAdministradoresBO almacenarNuevaPersonaEnSistema : " + e.toString());
        }
    }
}
