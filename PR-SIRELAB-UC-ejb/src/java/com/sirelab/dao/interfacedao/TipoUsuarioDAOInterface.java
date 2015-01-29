/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.TipoUsuario;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface TipoUsuarioDAOInterface {

    public void crearTipoUsuario(TipoUsuario tipoUsuario);

    public void editarTipoUsuario(TipoUsuario tipoUsuario);

    public void eliminarTipoUsuario(TipoUsuario tipoUsuario);

    public List<TipoUsuario> consultarTiposUsuarios();

    public TipoUsuario buscarTipoUsuarioPorID(BigInteger idRegistro);

    public TipoUsuario buscarTipoUsuarioPorNombre(String nombreTipoUsuario);

}
