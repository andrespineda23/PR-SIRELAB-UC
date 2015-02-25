/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.controller.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaModulosBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.ModuloLaboratorio;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.Sede;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerDetallesPlantaModulo implements Serializable {

    @EJB
    GestionarPlantaModulosBOInterface gestionarPlantaModulosBO;

    private ModuloLaboratorio moduloLaboratorioDetalles;
    private BigInteger idModuloLaboratorio;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean disabledActivar, disabledInactivar;
    private boolean visibleGuardar;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio laboratorioModuloLaboratorio;
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private AreaProfundizacion areaModuloLaboratorio;
    private boolean activarAreaProfundizacion;
    private List<Sede> listaSedes;
    private Sede sedeModuloLaboratorio;
    private List<Edificio> listaEdificios;
    private Edificio edificioModuloLaboratorio;
    private boolean activarEdificio;
    private List<SalaLaboratorio> listaSalasLaboratorio;
    private SalaLaboratorio salaModuloLaboratorio;
    private boolean activarSala;
    private String detalleModuloLaboratorio, codigoModuloLaboratorio;
    private String costoModuloLaboratorio, capacidadModuloLaboratorio, inversionModuloLaboratorio;

    public ControllerDetallesPlantaModulo() {
    }

    @PostConstruct
    public void init() {
        activarAreaProfundizacion = true;
        activarEdificio = true;
        activarSala = true;
        //
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        FacesContext faceContext = FacesContext.getCurrentInstance();
        HttpServletRequest httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        UsuarioLogin usuarioLoginSistema = (UsuarioLogin) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if ("ADMINISTRADOR".equalsIgnoreCase(usuarioLoginSistema.getNombreTipoUsuario())) {
            disabledEditar = false;
        }
    }

    public void recibirIDModulosLaboratorioDetalles(BigInteger idModuloLaboratorio) {
        this.idModuloLaboratorio = idModuloLaboratorio;
        moduloLaboratorioDetalles = gestionarPlantaModulosBO.obtenerModuloLaboratorioPorIDModuloLaboratorio(idModuloLaboratorio);
        if (moduloLaboratorioDetalles.getEstadomodulo() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesModuloLaboratorio();
    }

    public void asignarValoresVariablesModuloLaboratorio() {
        detalleModuloLaboratorio = moduloLaboratorioDetalles.getDetallemodulo();
        codigoModuloLaboratorio = moduloLaboratorioDetalles.getCodigomodulo();
        costoModuloLaboratorio = moduloLaboratorioDetalles.getCostoalquiler().toString();
        capacidadModuloLaboratorio = moduloLaboratorioDetalles.getCapacidadmodulo().toString();
        inversionModuloLaboratorio = moduloLaboratorioDetalles.getCostomodulo().toString();

        areaModuloLaboratorio = moduloLaboratorioDetalles.getSalalaboratorio().getAreaprofundizacion();
        laboratorioModuloLaboratorio = moduloLaboratorioDetalles.getSalalaboratorio().getAreaprofundizacion().getLaboratorio();
        edificioModuloLaboratorio = moduloLaboratorioDetalles.getSalalaboratorio().getEdificio();
        sedeModuloLaboratorio = moduloLaboratorioDetalles.getSalalaboratorio().getEdificio().getSede();
        salaModuloLaboratorio = moduloLaboratorioDetalles.getSalalaboratorio();
    }

    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        listaSedes = gestionarPlantaModulosBO.consultarSedesRegistradas();
        listaLaboratorios = gestionarPlantaModulosBO.consultarLaboratoriosRegistrados();
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void restaurarInformacionModuloLaboratorio() {
        moduloLaboratorioDetalles = new ModuloLaboratorio();
        moduloLaboratorioDetalles = gestionarPlantaModulosBO.obtenerModuloLaboratorioPorIDModuloLaboratorio(idModuloLaboratorio);
        if (moduloLaboratorioDetalles.getEstadomodulo() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesModuloLaboratorio();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        activarAreaProfundizacion = true;
        activarEdificio = true;
        activarSala = true;
        listaAreasProfundizacion = null;
        listaSalasLaboratorio = null;
        listaEdificios = null;
        listaLaboratorios = null;
        listaSedes = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void actualizarLaboratorios() {
        try {
            if (Utilidades.validarNulo(laboratorioModuloLaboratorio)) {
                activarAreaProfundizacion = false;
                areaModuloLaboratorio = null;
                listaAreasProfundizacion = gestionarPlantaModulosBO.consultarAreasProfundizacionPorIDLaboratorio(laboratorioModuloLaboratorio.getIdlaboratorio());
                modificacionesRegistroModuloLaboratorio();
            } else {
                areaModuloLaboratorio = null;
                listaAreasProfundizacion = null;
                activarAreaProfundizacion = true;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:areaModuloLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaModulo actualizarLaboratorios : " + e.toString());
        }
    }

    public void actualizarSedes() {
        try {
            if (Utilidades.validarNulo(sedeModuloLaboratorio)) {
                activarEdificio = false;
                edificioModuloLaboratorio = null;
                listaEdificios = gestionarPlantaModulosBO.consultarEdificiosPorIDSede(sedeModuloLaboratorio.getIdsede());
                modificacionesRegistroModuloLaboratorio();
            } else {
                edificioModuloLaboratorio = null;
                listaEdificios = null;
                activarEdificio = true;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:edificioModuloLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaModulo actualizarSedes : " + e.toString());
        }
    }

    public void almacenarModificacionesModuloLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            if (validarInformacionModuloLaboratorio() == true) {
                if (validarDatosNumericosModuloLaboratorio() == true) {
                    if (validarEstructuraModuloLaboratorio() == true) {
                        modificarInformacionModuloLaboratorio();
                    } else {
                        context.execute("errorEstructuraModuloLaboratorio.show()");
                    }
                } else {
                    context.execute("errorNumerosModuloLaboratorio.show()");
                }
            } else {
                context.execute("errorInformacionModuloLaboratorio.show()");
            }
        } else {
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionModuloLaboratorio();
        }
    }

    public void modificarInformacionModuloLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            moduloLaboratorioDetalles.setCodigomodulo(codigoModuloLaboratorio);
            moduloLaboratorioDetalles.setCostomodulo(new BigInteger(inversionModuloLaboratorio));
            moduloLaboratorioDetalles.setDetallemodulo(detalleModuloLaboratorio);
            moduloLaboratorioDetalles.setCostoalquiler(new BigInteger(costoModuloLaboratorio));
            moduloLaboratorioDetalles.setCapacidadmodulo(Integer.valueOf(capacidadModuloLaboratorio).intValue());
            moduloLaboratorioDetalles.setSalalaboratorio(salaModuloLaboratorio);
            gestionarPlantaModulosBO.modificarInformacionModuloLaboratorio(moduloLaboratorioDetalles);
            context.execute("registroExitosoModuloLaboratorio.show()");
            restaurarInformacionModuloLaboratorio();
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaModulo almacenarNuevoModuloLaboratorioEnSistema : " + e.toString());
            context.execute("registroFallidoModuloLaboratorio.show()");
        }
    }

    public boolean validarInformacionModuloLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(detalleModuloLaboratorio)) && (Utilidades.validarNulo(codigoModuloLaboratorio))) {
            if (Utilidades.validarCaracterString(detalleModuloLaboratorio) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosModuloLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(costoModuloLaboratorio)) {
            if (!Utilidades.isNumber(costoModuloLaboratorio)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(capacidadModuloLaboratorio)) {
            if (!Utilidades.isNumber(capacidadModuloLaboratorio)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(inversionModuloLaboratorio)) {
            if (!Utilidades.isNumber(inversionModuloLaboratorio)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarEstructuraModuloLaboratorio() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(salaModuloLaboratorio)) {
            retorno = false;
        }
        return retorno;
    }

    public void modificacionesRegistroModuloLaboratorio() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }

    public void activarModuloLaboratorio() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(true);
                moduloLaboratorioDetalles.setEstadomodulo(bool);
                gestionarPlantaModulosBO.modificarInformacionModuloLaboratorio(moduloLaboratorioDetalles);
                restaurarInformacionModuloLaboratorio();
                context.execute("registroExitosoModuloLaboratorio.show()");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesModulosLaboratorio activarModuloLaboratorio : " + e.toString());
        }
    }

    public void inactivarModuloLaboratorio() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(false);
                moduloLaboratorioDetalles.setEstadomodulo(bool);
                gestionarPlantaModulosBO.modificarInformacionModuloLaboratorio(moduloLaboratorioDetalles);
                moduloLaboratorioDetalles = new ModuloLaboratorio();
                context.update("formT:form:panelMenu");
                restaurarInformacionModuloLaboratorio();
                context.execute("registroExitosoModuloLaboratorio.show();");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesModulosLaboratorio inactivarModuloLaboratorio : " + e.toString());
        }
    }

    public void buscarSalasPorParametros(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, String> filtrosSala = filtrosSala = new HashMap<String, String>();
        filtrosSala.put("parametroEdificio", null);
        filtrosSala.put("parametroSede", null);
        filtrosSala.put("parametroLaboratorio", null);
        filtrosSala.put("parametroAreaProfundizacion", null);
        listaSalasLaboratorio = null;
        if (Utilidades.validarNulo(laboratorioModuloLaboratorio)) {
            filtrosSala.put("parametroLaboratorio", laboratorioModuloLaboratorio.getIdlaboratorio().toString());
        }
        if (Utilidades.validarNulo(edificioModuloLaboratorio)) {
            filtrosSala.put("parametroEdificio", edificioModuloLaboratorio.getIdedificio().toString());
        }
        if (Utilidades.validarNulo(sedeModuloLaboratorio)) {
            filtrosSala.put("parametroSede", sedeModuloLaboratorio.getIdsede().toString());
        }
        if (Utilidades.validarNulo(areaModuloLaboratorio)) {
            filtrosSala.put("parametroAreaProfundizacion", areaModuloLaboratorio.getIdareaprofundizacion().toString());
        }
        salaModuloLaboratorio = null;
        listaSalasLaboratorio = gestionarPlantaModulosBO.consultarSalasLaboratorioPorParametroFiltrado(filtrosSala);
        if (null != listaSalasLaboratorio) {
            activarSala = false;
        } else {
            activarSala = true;
        }
        context.update("formT:form:salaModuloLaboratorio");
    }

    //GET - SET
    public ModuloLaboratorio getModuloLaboratorioDetalles() {
        return moduloLaboratorioDetalles;
    }

    public void setModuloLaboratorioDetalles(ModuloLaboratorio moduloLaboratorioDetalles) {
        this.moduloLaboratorioDetalles = moduloLaboratorioDetalles;
    }

    public BigInteger getIdModuloLaboratorio() {
        return idModuloLaboratorio;
    }

    public void setIdModuloLaboratorio(BigInteger idModuloLaboratorio) {
        this.idModuloLaboratorio = idModuloLaboratorio;
    }

    public boolean isActivarEditar() {
        return activarEditar;
    }

    public void setActivarEditar(boolean activarEditar) {
        this.activarEditar = activarEditar;
    }

    public boolean isDisabledEditar() {
        return disabledEditar;
    }

    public void setDisabledEditar(boolean disabledEditar) {
        this.disabledEditar = disabledEditar;
    }

    public boolean isModificacionRegistro() {
        return modificacionRegistro;
    }

    public void setModificacionRegistro(boolean modificacionRegistro) {
        this.modificacionRegistro = modificacionRegistro;
    }

    public boolean isDisabledActivar() {
        return disabledActivar;
    }

    public void setDisabledActivar(boolean disabledActivar) {
        this.disabledActivar = disabledActivar;
    }

    public boolean isDisabledInactivar() {
        return disabledInactivar;
    }

    public void setDisabledInactivar(boolean disabledInactivar) {
        this.disabledInactivar = disabledInactivar;
    }

    public boolean isVisibleGuardar() {
        return visibleGuardar;
    }

    public void setVisibleGuardar(boolean visibleGuardar) {
        this.visibleGuardar = visibleGuardar;
    }

    public List<Laboratorio> getListaLaboratorios() {
        return listaLaboratorios;
    }

    public void setListaLaboratorios(List<Laboratorio> listaLaboratorios) {
        this.listaLaboratorios = listaLaboratorios;
    }

    public Laboratorio getLaboratorioModuloLaboratorio() {
        return laboratorioModuloLaboratorio;
    }

    public void setLaboratorioModuloLaboratorio(Laboratorio laboratorioModuloLaboratorio) {
        this.laboratorioModuloLaboratorio = laboratorioModuloLaboratorio;
    }

    public List<AreaProfundizacion> getListaAreasProfundizacion() {
        return listaAreasProfundizacion;
    }

    public void setListaAreasProfundizacion(List<AreaProfundizacion> listaAreasProfundizacion) {
        this.listaAreasProfundizacion = listaAreasProfundizacion;
    }

    public AreaProfundizacion getAreaModuloLaboratorio() {
        return areaModuloLaboratorio;
    }

    public void setAreaModuloLaboratorio(AreaProfundizacion areaModuloLaboratorio) {
        this.areaModuloLaboratorio = areaModuloLaboratorio;
    }

    public boolean isActivarAreaProfundizacion() {
        return activarAreaProfundizacion;
    }

    public void setActivarAreaProfundizacion(boolean activarAreaProfundizacion) {
        this.activarAreaProfundizacion = activarAreaProfundizacion;
    }

    public List<Sede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<Sede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public Sede getSedeModuloLaboratorio() {
        return sedeModuloLaboratorio;
    }

    public void setSedeModuloLaboratorio(Sede sedeModuloLaboratorio) {
        this.sedeModuloLaboratorio = sedeModuloLaboratorio;
    }

    public List<Edificio> getListaEdificios() {
        return listaEdificios;
    }

    public void setListaEdificios(List<Edificio> listaEdificios) {
        this.listaEdificios = listaEdificios;
    }

    public Edificio getEdificioModuloLaboratorio() {
        return edificioModuloLaboratorio;
    }

    public void setEdificioModuloLaboratorio(Edificio edificioModuloLaboratorio) {
        this.edificioModuloLaboratorio = edificioModuloLaboratorio;
    }

    public boolean isActivarEdificio() {
        return activarEdificio;
    }

    public void setActivarEdificio(boolean activarEdificio) {
        this.activarEdificio = activarEdificio;
    }

    public List<SalaLaboratorio> getListaSalasLaboratorio() {
        return listaSalasLaboratorio;
    }

    public void setListaSalasLaboratorio(List<SalaLaboratorio> listaSalasLaboratorio) {
        this.listaSalasLaboratorio = listaSalasLaboratorio;
    }

    public SalaLaboratorio getSalaModuloLaboratorio() {
        return salaModuloLaboratorio;
    }

    public void setSalaModuloLaboratorio(SalaLaboratorio salaModuloLaboratorio) {
        this.salaModuloLaboratorio = salaModuloLaboratorio;
    }

    public boolean isActivarSala() {
        return activarSala;
    }

    public void setActivarSala(boolean activarSala) {
        this.activarSala = activarSala;
    }

    public String getDetalleModuloLaboratorio() {
        return detalleModuloLaboratorio;
    }

    public void setDetalleModuloLaboratorio(String detalleModuloLaboratorio) {
        this.detalleModuloLaboratorio = detalleModuloLaboratorio;
    }

    public String getCodigoModuloLaboratorio() {
        return codigoModuloLaboratorio;
    }

    public void setCodigoModuloLaboratorio(String codigoModuloLaboratorio) {
        this.codigoModuloLaboratorio = codigoModuloLaboratorio;
    }

    public String getCostoModuloLaboratorio() {
        return costoModuloLaboratorio;
    }

    public void setCostoModuloLaboratorio(String costoModuloLaboratorio) {
        this.costoModuloLaboratorio = costoModuloLaboratorio;
    }

    public String getCapacidadModuloLaboratorio() {
        return capacidadModuloLaboratorio;
    }

    public void setCapacidadModuloLaboratorio(String capacidadModuloLaboratorio) {
        this.capacidadModuloLaboratorio = capacidadModuloLaboratorio;
    }

    public String getInversionModuloLaboratorio() {
        return inversionModuloLaboratorio;
    }

    public void setInversionModuloLaboratorio(String inversionModuloLaboratorio) {
        this.inversionModuloLaboratorio = inversionModuloLaboratorio;
    }

}
