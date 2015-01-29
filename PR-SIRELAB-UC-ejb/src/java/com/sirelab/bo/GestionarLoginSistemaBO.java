package com.sirelab.bo;

import com.sirelab.bo.interfacebo.GestionarLoginSistemaBOInterface;
import com.sirelab.dao.interfacedao.CarreraDAOInterface;
import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.dao.interfacedao.DocenteDAOInterface;
import com.sirelab.dao.interfacedao.EncargadoLaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.EntidadExternaDAOInterface;
import com.sirelab.dao.interfacedao.EstudianteDAOInterface;
import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.dao.interfacedao.PlanEstudiosDAOInterface;
import com.sirelab.dao.interfacedao.TipoUsuarioDAOInterface;
import com.sirelab.dao.interfacedao.UsuarioDAOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.PlanEstudios;
import com.sirelab.entidades.TipoUsuario;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class GestionarLoginSistemaBO implements GestionarLoginSistemaBOInterface {

    @EJB
    CarreraDAOInterface carreraDAO;
    @EJB
    PlanEstudiosDAOInterface planEstudiosDAO;
    @EJB
    DepartamentoDAOInterface departamentoDAO;
    @EJB
    PersonaDAOInterface personaDAO;
    @EJB
    UsuarioDAOInterface usuarioDAO;
    @EJB
    EstudianteDAOInterface estudianteDAO;
    @EJB
    TipoUsuarioDAOInterface tipoUsuarioDAO;
    @EJB
    DocenteDAOInterface docenteDAO;
    @EJB
    EntidadExternaDAOInterface entidadExternaDAO;
    @EJB
    EncargadoLaboratorioDAOInterface encargadoLaboratorioDAO;

    @Override
    public List<Departamento> obtenerListasDepartamentos() {
        try {
            List<Departamento> lista = departamentoDAO.consultarDepartamentos();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasDepartamentos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Carrera> obtenerListasCarrerasPorDepartamento(BigInteger idDepartamento) {
        try {
            List<Carrera> lista = carreraDAO.consultarCarrerasPorDepartamento(idDepartamento);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasCarrerasPorDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlanEstudios> obtenerListasPlanesEstudioPorCarrera(BigInteger idCarrera) {
        try {
            List<PlanEstudios> lista = planEstudiosDAO.consultarPlanesEstudiosPorCarrera(idCarrera);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasPlanesEstudioPorCarrera : " + e.toString());
            return null;
        }
    }

    @Override
    public Estudiante obtenerEstudiantePorCorreoNumDocumento(String correo, String documento) {
        try {
            Estudiante registro = estudianteDAO.buscarEstudiantePorDocumentoYCorreo(correo, documento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerEstudiantePorCorreoNumDocumento : " + e.toString());
            return null;
        }
    }

    @Override
    public void almacenarNuevoEstudianteEnSistema(Usuario usuarioNuevo, Persona personaNuevo, Estudiante estudianteNuevo) {
        try {
            int sec = 1;
            BigInteger idUsuario = new BigInteger(String.valueOf(sec++));
            //usuarioNuevo.setIdusuario(idUsuario);
            TipoUsuario tipoUsuario = tipoUsuarioDAO.buscarTipoUsuarioPorNombre("ESTUDIANTE");
            usuarioNuevo.setTipousuario(tipoUsuario);
            usuarioDAO.crearUsuario(usuarioNuevo);
            Usuario usuarioRegistrado = usuarioDAO.obtenerUltimoUsuarioRegistrado();
            BigInteger idPersona = new BigInteger(String.valueOf(sec++));
            //personaNuevo.setIdpersona(idPersona);
            personaNuevo.setUsuario(usuarioRegistrado);
            personaDAO.crearPersona(personaNuevo);
            Persona personaRegistrada = personaDAO.obtenerUltimaPersonaRegistrada();
            BigInteger idEstudiante = new BigInteger(String.valueOf(sec++));
            //estudianteNuevo.setIdestudiante(idEstudiante);
            estudianteNuevo.setPersona(personaRegistrada);
            estudianteDAO.crearEstudiante(estudianteNuevo);
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO almacenarNuevoEstudianteEnSistema : " + e.toString());
        }
    }

    @Override
    public Persona obtenerPersonaRecuperarContrasenia(String correo, String identificacion) {
        try {
            Persona registro = personaDAO.buscarPersonaPorCorreoYNumeroIdentificacion(correo, identificacion);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerPersonaRecuperarContrasenia : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona configurarContraseñaPersona(Persona persona) {
        try {
            boolean bandera = true;
            int nuevaPass = 0;
            while (bandera) {
                nuevaPass = (int) (Math.random() * 9999999) + 1;
                if (nuevaPass >= 100000) {
                    bandera = false;
                }
            }
            String newPass = String.valueOf(nuevaPass) + "SIRELAB";
            persona.getUsuario().setPasswordusuario(newPass);
            personaDAO.editarPersona(persona);
            Persona registro = personaDAO.buscarPersonaPorID(persona.getIdpersona());
            return registro;
        } catch (Exception e) {
            System.out.println("Error configurarContraseñaPersona GestionarLoginSistemaBO : " + e.toString());
            return null;
        }
    }

    @Override
    public Persona obtenerPersonaLogin(String usuario, String password) {
        try {
            Persona registro = personaDAO.obtenerPersonaLoginUserPassword(usuario, password);
            return registro;
        } catch (Exception e) {
            System.out.println("Error obtenerPersonaLogin GestionarLoginSistemaBO : " + e.toString());
            return null;
        }
    }

    @Override
    public Object obtenerUsuarioFinalLogin(String nombreTipoUsuario, BigInteger idPersona) {
        try {
            Object registro = null;
            if ("ESTUDIANTE".equals(nombreTipoUsuario)) {
                registro = estudianteDAO.buscarEstudiantePorIDPersona(idPersona);
            } else {
                if ("DOCENTE".equals(nombreTipoUsuario)) {
                    registro = docenteDAO.buscarDocentePorIDPersona(idPersona);
                } else {
                    if ("ENCARGADOLAB".equals(nombreTipoUsuario)) {
                        registro = encargadoLaboratorioDAO.buscarEncargadoLaboratorioPorIDPersona(idPersona);
                    } else {
                        registro = entidadExternaDAO.buscarEntidadExternaPorIDPersona(idPersona);
                    }
                }
            }
            return registro;
        } catch (Exception e) {
            System.out.println("Error obtenerUsuarioFinalLogin GestionarLoginSistemaBO : " + e.toString());
            return null;
        }
    }
}
