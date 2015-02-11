package com.sirelab.bo;

import com.sirelab.bo.interfacebo.AdministrarEstudiantesBOInterface;
import com.sirelab.dao.interfacedao.CarreraDAOInterface;
import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.dao.interfacedao.EstudianteDAOInterface;
import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.dao.interfacedao.PlanEstudiosDAOInterface;
import com.sirelab.dao.interfacedao.UsuarioDAOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.PlanEstudios;
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
public class AdministrarEstudiantesBO implements AdministrarEstudiantesBOInterface {

    @EJB
    UsuarioDAOInterface usuarioDAO;
    @EJB
    FacultadDAOInterface facultadDAO;
    @EJB
    CarreraDAOInterface carreraDAO;
    @EJB
    PlanEstudiosDAOInterface planEstudiosDAO;
    @EJB
    DepartamentoDAOInterface departamentoDAO;
    @EJB
    PersonaDAOInterface personaDAO;
    @EJB
    EstudianteDAOInterface estudianteDAO;

    //@Override
    public List<Departamento> obtenerListasDepartamentos() {
        try {
            List<Departamento> lista = departamentoDAO.consultarDepartamentos();
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerListasDepartamentos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Facultad> obtenerListaFacultades() {
        try {
            List<Facultad> lista = facultadDAO.consultarFacultades();
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerListaFacultades : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad) {
        try {
            List<Departamento> lista = departamentoDAO.buscarDepartamentosPorIDFacultad(idFacultad);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerListasCarrerasPorDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Carrera> obtenerListasCarrerasPorDepartamento(BigInteger idDepartamento) {
        try {
            List<Carrera> lista = carreraDAO.consultarCarrerasPorDepartamento(idDepartamento);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerListasCarrerasPorDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlanEstudios> obtenerListasPlanesEstudioPorCarrera(BigInteger idCarrera) {
        try {
            List<PlanEstudios> lista = planEstudiosDAO.consultarPlanesEstudiosPorCarrera(idCarrera);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerListasPlanesEstudioPorCarrera : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estudiante> consultarEstudiantesPorParametro(Map<String, String> filtros) {
        try {
            List<Estudiante> lista = estudianteDAO.buscarEstudiantesPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO consultarEstudiantesPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public Estudiante obtenerEstudiantePorIDEstudiante(BigInteger idEstudiante) {
        try {
            Estudiante registro = estudianteDAO.buscarEstudiantePorID(idEstudiante);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerEstudiantePorIDEstudiante : " + e.toString());
            return null;
        }
    }

    @Override
    public Estudiante obtenerEstudiantePorCorreoNumDocumento(String correo, String documento) {
        try {
            Estudiante registro = estudianteDAO.buscarEstudiantePorDocumentoYCorreo(correo, documento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO obtenerEstudiantePorCorreoNumDocumento : " + e.toString());
            return null;
        }
    }

    @Override
    public void actualizarInformacionEstudiante(Estudiante estudiante) {
        try {
            estudianteDAO.editarEstudiante(estudiante);
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO actualizarInformacionEstudiante : " + e.toString());
        }
    }

    //@Override
    public void actualizarInformacionPersona(Persona persona) {
        try {
            personaDAO.editarPersona(persona);
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO actualizarInformacionPersona : " + e.toString());
        }
    }

    //@Override
    public void actualizarInformacionUsuario(Usuario usuario) {
        try {
            usuarioDAO.editarUsuario(usuario);
        } catch (Exception e) {
            System.out.println("Error AdministrarEstudiantesBO actualizarInformacionUsuario : " + e.toString());
        }
    }
}
