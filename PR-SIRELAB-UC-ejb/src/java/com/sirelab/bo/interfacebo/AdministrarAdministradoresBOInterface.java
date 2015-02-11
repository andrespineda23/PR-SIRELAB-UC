/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AdministrarAdministradoresBOInterface {

    public List<Persona> consultarAdministradoresPorParametro(Map<String, String> filtros);

    public Persona obtenerAdministradorPorIDPersona(BigInteger idPersona);

    public Persona obtenerAdministradorPorCorreoNumDocumento(String correo, String documento);

    public void actualizarInformacionAdministrador(Persona administrador);

    public void almacenarNuevaPersonaEnSistema(Usuario usuarioNuevo, Persona personaNuevo);

    public void actualizarInformacionUsuario(Usuario usuario);

}
