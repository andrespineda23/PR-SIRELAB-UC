/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Laboratorio;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AdministrarParametroAreaProfundizacionBOInterface {

    public List<Facultad> consultarFacultadesRegistradas();

    public List<Departamento> consultarDepartamentosPorIDFacultad(BigInteger facultad);

    public List<Laboratorio> consultarLaboratoriosPorIDDepartamento(BigInteger departamento);

    public List<AreaProfundizacion> consultarAreasProfundizacionPorParametro(Map<String, String> filtros);

    public void crearNuevaAreaProfundizacion(AreaProfundizacion areaProfundizacion);

    public void modificarInformacionAreaProfundizacion(AreaProfundizacion areaProfundizacion);

    public AreaProfundizacion obtenerAreaProfundizacionPorIDAreaProfundizacion(BigInteger idAreaProfundizacion);
}
