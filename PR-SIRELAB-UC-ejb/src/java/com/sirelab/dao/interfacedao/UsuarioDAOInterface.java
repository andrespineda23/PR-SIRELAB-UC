/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface UsuarioDAOInterface {

    public void crearUsuario(Usuario usuario);

    public void editarUsuario(Usuario usuario);

    public void eliminarUsuario(Usuario usuario);

    public List<Usuario> consultarUsuarios();

    public Usuario buscarUsuarioPorID(BigInteger idRegistro);

    public Usuario obtenerUltimoUsuarioRegistrado();
}
