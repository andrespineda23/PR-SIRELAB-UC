/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Laboratorio;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface LaboratorioDAOInterface {

    public void crearLaboratorio(Laboratorio laboratorio);

    public void editarLaboratorio(Laboratorio laboratorio);

    public void eliminarLaboratorio(Laboratorio laboratorio);

    public List<Laboratorio> consultarLaboratorios();

    public Laboratorio buscarLaboratorioPorID(BigInteger idRegistro);

}
