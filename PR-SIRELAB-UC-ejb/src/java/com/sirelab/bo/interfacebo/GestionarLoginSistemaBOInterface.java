/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.PlanEstudios;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarLoginSistemaBOInterface {

    public List<Departamento> obtenerListasDepartamentos();

    public List<Carrera> obtenerListasCarrerasPorDepartamento(BigInteger idDepartamento);

    public List<PlanEstudios> obtenerListasPlanesEstudioPorCarrera(BigInteger idCarrera);

    public Estudiante obtenerEstudiantePorCorreoNumDocumento(String correo, String documento);

    public void almacenarNuevoEstudianteEnSistema(Usuario usuario, Persona persona, Estudiante estudiante);

    public Persona obtenerPersonaRecuperarContrasenia(String correo, String identificacion);

    public Persona configurarContraseñaPersona(Persona persona);

    public Persona obtenerPersonaLogin(String usuario, String password);

    public Object obtenerUsuarioFinalLogin(String nombreTipoUsuario, BigInteger idPersona);

}
