/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Docente;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AdministrarDocentesBOInterface {

    public List<Docente> consultarDocentesPorParametro(Map<String, String> filtros);

    public Docente obtenerDocentePorIDDocente(BigInteger idDocente);

    public List<Facultad> obtenerListaFacultades();

    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad);

    public Docente obtenerDocentePorCorreoNumDocumento(String correo, String documento);

    public void actualizarInformacionDocente(Docente docente);

    public void almacenarNuevoDocenteEnSistema(Usuario usuarioNuevo, Persona personaNuevo, Docente docenteNuevo);
}
