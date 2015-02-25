package com.sirelab.bo.usuarios;

import com.sirelab.bo.interfacebo.AdministrarDocentesBOInterface;
import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.dao.interfacedao.DocenteDAOInterface;
import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.dao.interfacedao.TipoUsuarioDAOInterface;
import com.sirelab.dao.interfacedao.UsuarioDAOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Docente;
import com.sirelab.entidades.Facultad;
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
public class AdministrarDocentesBO implements AdministrarDocentesBOInterface {

    @EJB
    UsuarioDAOInterface usuarioDAO;
    @EJB
    FacultadDAOInterface facultadDAO;
    @EJB
    DepartamentoDAOInterface departamentoDAO;
    @EJB
    PersonaDAOInterface personaDAO;
    @EJB
    DocenteDAOInterface docenteDAO;
    @EJB
    TipoUsuarioDAOInterface tipoUsuarioDAO;

    @Override
    public List<Docente> consultarDocentesPorParametro(Map<String, String> filtros) {
        try {
            List<Docente> lista = docenteDAO.buscarDocentesPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO consultarDocentesPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public Docente obtenerDocentePorIDDocente(BigInteger idDocente) {
        try {
            Docente registro = docenteDAO.buscarDocentePorID(idDocente);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO obtenerDocentePorIDDocente : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Facultad> obtenerListaFacultades() {
        try {
            List<Facultad> lista = facultadDAO.consultarFacultades();
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO obtenerListaFacultades : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad) {
        try {
            List<Departamento> lista = departamentoDAO.buscarDepartamentosPorIDFacultad(idFacultad);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO obtenerDepartamentosPorIDFacultad : " + e.toString());
            return null;
        }
    }

    @Override
    public Docente obtenerDocentePorCorreoNumDocumento(String correo, String documento) {
        try {
            Docente registro = docenteDAO.obtenerDocentePorCorreoDocumento(correo, documento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO obtenerDocentePorCorreoNumDocumento : " + e.toString());
            return null;
        }
    }

    @Override
    public void actualizarInformacionDocente(Docente docente) {
        try {
            docenteDAO.editarDocente(docente);
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO actualizarInformacionDocente : " + e.toString());
        }
    }

   //@Override 
    public void actualizarInformacionPersona(Persona persona) {
        try {
            personaDAO.editarPersona(persona);
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO actualizarInformacionPersona : " + e.toString());
        }
    }

    //@Override 
    public void actualizarInformacionUsuario(Usuario usuario) {
        try {
            usuarioDAO.editarUsuario(usuario);
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO actualizarInformacionUsuario : " + e.toString());

        }
    }

    @Override
    public void almacenarNuevoDocenteEnSistema(Usuario usuarioNuevo, Persona personaNuevo, Docente docenteNuevo) {
        try {
            int sec = 1;
            //usuarioNuevo.setIdusuario(idUsuario);
            TipoUsuario tipoUsuario = tipoUsuarioDAO.buscarTipoUsuarioPorNombre("DOCENTE");
            usuarioNuevo.setTipousuario(tipoUsuario);
            usuarioDAO.crearUsuario(usuarioNuevo);
            Usuario usuarioRegistrado = usuarioDAO.obtenerUltimoUsuarioRegistrado();
            //personaNuevo.setIdpersona(idPersona);
            personaNuevo.setUsuario(usuarioRegistrado);
            personaDAO.crearPersona(personaNuevo);
            Persona personaRegistrada = personaDAO.obtenerUltimaPersonaRegistrada();
            //estudianteNuevo.setIdestudiante(idEstudiante);
            docenteNuevo.setPersona(personaRegistrada);
            docenteDAO.crearDocente(docenteNuevo);
        } catch (Exception e) {
            System.out.println("Error AdministrarDocentesBO almacenarNuevoDocenteEnSistema : " + e.toString());
        }
    }

}
