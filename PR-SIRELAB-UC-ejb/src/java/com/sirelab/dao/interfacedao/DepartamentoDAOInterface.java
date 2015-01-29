/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Departamento;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface DepartamentoDAOInterface {

    public void crearDepartamento(Departamento departamento);

    public void editarDepartamento(Departamento departamento);

    public void eliminarDepartamento(Departamento departamento);

    public List<Departamento> consultarDepartamentos();

    public Departamento buscarDepartamentoPorID(BigInteger idRegistro);

    public List<Departamento> buscarDepartamentosPorIDFacultad(BigInteger idFacultad);

}
