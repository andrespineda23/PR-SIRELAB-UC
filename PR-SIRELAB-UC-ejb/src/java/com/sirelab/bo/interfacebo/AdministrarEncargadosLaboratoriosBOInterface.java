package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.EncargadoLaboratorio;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AdministrarEncargadosLaboratoriosBOInterface {

    public List<EncargadoLaboratorio> consultarEncargadoLaboratoriosPorParametro(Map<String, String> filtros);

    public EncargadoLaboratorio obtenerEncargadoLaboratorioPorIDEncargadoLaboratorio(BigInteger idEncargadoLaboratorio);

    public List<Facultad> obtenerListaFacultades();

    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad);

    public List<Laboratorio> obtenerLaboratoriosPorIDDepartamento(BigInteger idDepartamento);
    
    public EncargadoLaboratorio obtenerEncargadoLaboratorioPorCorreoNumDocumento(String correo, String documento);

    public void actualizarInformacionEncargadoLaboratorio(EncargadoLaboratorio personalLab);

    public void almacenarNuevoEncargadoLaboratorioEnSistema(Usuario usuarioNuevo, Persona personaNuevo, EncargadoLaboratorio personalNuevo);
    
    public void actualizarInformacionPersona(Persona persona);
    
    public void actualizarInformacionUsuario(Usuario usuario);

}
