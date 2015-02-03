package com.sirelab.bo;

import com.sirelab.bo.interfacebo.AdministrarEncargadosLaboratoriosBOInterface;
import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.dao.interfacedao.EncargadoLaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.dao.interfacedao.LaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.dao.interfacedao.TipoUsuarioDAOInterface;
import com.sirelab.dao.interfacedao.UsuarioDAOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.EncargadoLaboratorio;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Laboratorio;
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
public class AdministrarEncargadosLaboratoriosBO implements AdministrarEncargadosLaboratoriosBOInterface {

    @EJB
    EncargadoLaboratorioDAOInterface encargadoLaboratorioDAO;
    @EJB
    FacultadDAOInterface facultadDAO;
    @EJB
    DepartamentoDAOInterface departamentoDAO;
    @EJB
    UsuarioDAOInterface usuarioDAO;
    @EJB
    PersonaDAOInterface personaDAO;
    @EJB
    TipoUsuarioDAOInterface tipoUsuarioDAO;
    @EJB
    LaboratorioDAOInterface laboratorioDAO;

    @Override
    public List<EncargadoLaboratorio> consultarEncargadoLaboratoriosPorParametro(Map<String, String> filtros) {
        try {
            List<EncargadoLaboratorio> lista = encargadoLaboratorioDAO.buscarEncargadosLaboratoriosPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO consultarEncargadoLaboratoriosPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public EncargadoLaboratorio obtenerEncargadoLaboratorioPorIDEncargadoLaboratorio(BigInteger idEncargadoLaboratorio) {
        try {
            EncargadoLaboratorio registro = encargadoLaboratorioDAO.buscarEncargadoLaboratorioPorID(idEncargadoLaboratorio);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO obtenerEncargadoLaboratorioPorIDEncargadoLaboratorio : " + e.toString());
            return null;
        }
    }

    //@Override
    public List<Facultad> obtenerListaFacultades() {
        try {
            List<Facultad> lista = facultadDAO.consultarFacultades();
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO obtenerListaFacultades : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad) {
        try {
            List<Departamento> lista = departamentoDAO.buscarDepartamentosPorIDFacultad(idFacultad);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO obtenerDepartamentosPorIDFacultad : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Laboratorio> obtenerLaboratoriosPorIDDepartamento(BigInteger idDepartamento) {
        try {
            List<Laboratorio> lista = laboratorioDAO.buscarLaboratorioPorIDDepartamento(idDepartamento);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO obtenerLaboratoriosPorIDDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public EncargadoLaboratorio obtenerEncargadoLaboratorioPorCorreoNumDocumento(String correo, String documento) {
        try {
            EncargadoLaboratorio registro = encargadoLaboratorioDAO.buscarEncargadoLaboratorioPorPorCorreoNumDocumento(correo, documento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO obtenerEncargadoLaboratorioPorCorreoNumDocumento : " + e.toString());
            return null;
        }
    }

    @Override
    public void actualizarInformacionEncargadoLaboratorio(EncargadoLaboratorio personalLab) {
        try {
            encargadoLaboratorioDAO.editarEncargadoLaboratorio(personalLab);
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO actualizarInformacionEncargadoLaboratorio : " + e.toString());
        }
    }

    @Override
    public void almacenarNuevoEncargadoLaboratorioEnSistema(Usuario usuarioNuevo, Persona personaNuevo, EncargadoLaboratorio personalNuevo) {
        try {
            int sec = 1;
            //usuarioNuevo.setIdusuario(idUsuario);
            TipoUsuario tipoUsuario = tipoUsuarioDAO.buscarTipoUsuarioPorNombre("ENCARGADOLAB");
            usuarioNuevo.setTipousuario(tipoUsuario);
            usuarioDAO.crearUsuario(usuarioNuevo);
            Usuario usuarioRegistrado = usuarioDAO.obtenerUltimoUsuarioRegistrado();
            //personaNuevo.setIdpersona(idPersona);
            personaNuevo.setUsuario(usuarioRegistrado);
            personaDAO.crearPersona(personaNuevo);
            Persona personaRegistrada = personaDAO.obtenerUltimaPersonaRegistrada();
            //estudianteNuevo.setIdestudiante(idEstudiante);
            personalNuevo.setPersona(personaRegistrada);
            encargadoLaboratorioDAO.editarEncargadoLaboratorio(personalNuevo);
        } catch (Exception e) {
            System.out.println("Error AdministrarEncargadosLaboratoriosBO almacenarNuevoEncargadoLaboratorioEnSistema : " + e.toString());
        }
    }
}
