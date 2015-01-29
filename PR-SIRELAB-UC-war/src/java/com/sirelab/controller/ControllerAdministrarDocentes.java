package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarDocentesBOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Docente;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerAdministrarDocentes implements Serializable {

    @EJB
    AdministrarDocentesBOInterface administrarDocentesBO;

    private String parametroNombre, parametroApellido, parametroDocumento, parametroCorreo, parametroCargo;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private boolean parametroEstado;
    private boolean todosDocentes;
    private Map<String, String> filtros;
    //
    private boolean activoDepartamento;
    //
    private List<Docente> listaDocentes;
    private List<Docente> filtrarListaDocentes;
    //
    private Column numeroIDTabla, nombresTabla, apellidosTabla, correoTabla, facultadTabla, departamentoTabla, cargoTabla, estadoTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombresDocente, nuevoApellidoDocente, nuevoCorreoDocente, nuevoIdentificacionDocente, nuevoNumero1Docente, nuevoNumero2Docente, nuevoDireccionDocente, nuevoCargoDocente;
    private String nuevoUserDocente, nuevoPassDocente;
    private Facultad nuevoFacultadDocente;
    private Departamento nuevoDepartamentoDocente;
    private boolean activoNuevoDepartamento;

    public ControllerAdministrarDocentes() {
    }

    @PostConstruct
    public void init() {
        activoDepartamento = true;
        activoNuevoDepartamento = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroCargo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroEstado = true;
        todosDocentes = false;
        listaFacultades = administrarDocentesBO.obtenerListaFacultades();
        altoTabla = "150";
        inicializarFiltros();
        listaDocentes = null;
        filtrarListaDocentes = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroApellido", null);
        filtros.put("parametroDocumento", null);
        filtros.put("parametroCorreo", null);
        filtros.put("parametroCargo", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroFacultad", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroApellido) == true) && (!parametroApellido.isEmpty())) {
            filtros.put("parametroApellido", parametroApellido.toString());
        }
        if ((Utilidades.validarNulo(parametroDocumento) == true) && (!parametroDocumento.isEmpty())) {
            filtros.put("parametroDocumento", parametroDocumento.toString());
        }
        if ((Utilidades.validarNulo(parametroCorreo) == true) && (!parametroCorreo.isEmpty())) {
            filtros.put("parametroCorreo", parametroCorreo.toString());
        }
        if (todosDocentes == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
        if (parametroDepartamento.getIddepartamento() != null) {
            filtros.put("parametroDepartamento", parametroDepartamento.getIddepartamento().toString());
        }
        if (parametroFacultad.getIdfacultad() != null) {
            filtros.put("parametroFacultad", parametroFacultad.getIdfacultad().toString());
        }
        if ((Utilidades.validarNulo(parametroCargo)) && (!parametroCargo.isEmpty())) {
            filtros.put("parametroPlanEst", parametroCargo.toString());
        }
    }

    public void buscarDocentesPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaDocentes = administrarDocentesBO.consultarDocentesPorParametro(filtros);
            if (listaDocentes != null) {
                if (listaDocentes.size() > 0) {
                    activarFiltrosTabla();
                }
            }
            context.update("form:datosBusqueda");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarDocentes buscarDocentesPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        desactivarFiltrosTabla();
        activoDepartamento = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroCargo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroEstado = true;
        todosDocentes = false;
        inicializarFiltros();
        listaDocentes = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void modificacionTodosDocentes() {
        RequestContext.getCurrentInstance().update("formT:form:parametroEstado");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        numeroIDTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:numeroIDTabla");
        numeroIDTabla.setFilterStyle("width: 80px");
        nombresTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombresTabla");
        nombresTabla.setFilterStyle("width: 80px");
        apellidosTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:apellidosTabla");
        apellidosTabla.setFilterStyle("width: 80px");
        correoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:correoTabla");
        correoTabla.setFilterStyle("width: 80px");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("width: 80px");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("width: 80px");
        cargoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:cargoTabla");
        cargoTabla.setFilterStyle("width: 80px");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        numeroIDTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:numeroIDTabla");
        numeroIDTabla.setFilterStyle("display: none; visibility: hidden;");
        nombresTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombresTabla");
        nombresTabla.setFilterStyle("display: none; visibility: hidden;");
        apellidosTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:apellidosTabla");
        apellidosTabla.setFilterStyle("display: none; visibility: hidden;");
        correoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:correoTabla");
        correoTabla.setFilterStyle("display: none; visibility: hidden;");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("display: none; visibility: hidden;");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("display: none; visibility: hidden;");
        cargoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:cargoTabla");
        cargoTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaDocentes = null;
    }

    public void actualizarFacultades() {
        try {
            if (Utilidades.validarNulo(parametroFacultad)) {
                activoDepartamento = false;
                parametroDepartamento = new Departamento();
                listaDepartamentos = administrarDocentesBO.obtenerDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
            } else {
                activoDepartamento = true;
                listaDepartamentos = null;
                parametroDepartamento = new Departamento();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:parametroDepartamento");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarDocentes actualizarFacultades : " + e.toString());
        }
    }

    public void actualizarNuevoFacultades() {
        try {
            if (Utilidades.validarNulo(nuevoFacultadDocente)) {
                activoNuevoDepartamento = false;
                nuevoDepartamentoDocente = new Departamento();
                listaDepartamentos = administrarDocentesBO.obtenerDepartamentosPorIDFacultad(nuevoFacultadDocente.getIdfacultad());
            } else {
                listaDepartamentos = null;
                activoNuevoDepartamento = false;
                nuevoDepartamentoDocente = new Departamento();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:formularioDialogos:nuevoDepartamentoDocente");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarDocentes actualizarNuevoFacultades : " + e.toString());
        }
    }

    public String verDetallesDocente() {
        limpiarProcesoBusqueda();
        return "detallesdocente";
    }

    public void dispararDialogoNuevoDocente() {
        limpiarRegistroDocente();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroDocente");
        context.execute("NuevoRegistroDocente.show()");
    }

    public void limpiarRegistroDocente() {
        listaDepartamentos = null;
        activoNuevoDepartamento = false;
        nuevoDepartamentoDocente = new Departamento();
        nuevoApellidoDocente = null;
        nuevoCargoDocente = null;
        nuevoCorreoDocente = null;
        nuevoDireccionDocente = null;
        nuevoFacultadDocente = new Facultad();
        nuevoIdentificacionDocente = null;
        nuevoNombresDocente = null;
        nuevoNumero1Docente = null;
        nuevoNumero2Docente = null;
        nuevoPassDocente = null;
        nuevoUserDocente = null;
    }

    public boolean validarNombreApellidoDocente() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nuevoNombresDocente)) && (Utilidades.validarNulo(nuevoApellidoDocente))) {
            if (!Utilidades.validarCaracterString(nuevoNombresDocente)) {
                retorno = false;
            }
            if (!Utilidades.validarCaracterString(nuevoApellidoDocente)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarCorreoDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoCorreoDocente)) {
            if (Utilidades.validarCorreoElectronico(nuevoCorreoDocente)) {
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarIdentificacionDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoIdentificacionDocente)) {
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoNumero1Docente)) {
            if ((Utilidades.isNumber(nuevoNumero1Docente)) == false) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(nuevoNumero2Docente)) {
            if ((Utilidades.isNumber(nuevoNumero2Docente)) == false) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionDocente() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoDireccionDocente)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosUniversidadDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoCargoDocente) && Utilidades.validarNulo(nuevoFacultadDocente) && Utilidades.validarNulo(nuevoDepartamentoDocente)) {
            if (!Utilidades.validarCaracterString(nuevoCargoDocente.toString())) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validaDocenteYaRegistrado() {
        boolean retorno = true;
        Docente estudianteRegistrado = administrarDocentesBO.obtenerDocentePorCorreoNumDocumento(nuevoCorreoDocente, nuevoIdentificacionDocente);
        if (estudianteRegistrado != null) {
            retorno = false;
        }
        return retorno;
    }

    public void registrarNuevoDocente() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarNombreApellidoDocente() == true) {
            if (validarCorreoDocente() == true) {
                if (validarIdentificacionDocente() == true) {
                    if (validarDatosNumericosDocente() == true) {
                        if (validarDireccionDocente() == true) {
                            if (validarDatosUniversidadDocente() == true) {
                                if (validaDocenteYaRegistrado() == true) {
                                    almacenarNuevoDocenteEnSistema();
                                } else {
                                    context.execute("errorDocenteRegistrado.show()");
                                }
                            } else {
                                context.execute("errorUniversidadDocente.show()");
                            }
                        } else {
                            context.execute("errorDireccionDocente.show()");
                        }
                    } else {
                        context.execute("errorNumerosDocente.show()");
                    }
                } else {
                    context.execute("errorDocumentoDocente.show()");
                }
            } else {
                context.execute("errorEmailDocente.show()");
            }
        } else {
            context.execute("errorNombreApellidoDocente.show()");
        }
    }

    public void almacenarNuevoDocenteEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroDocente.hide()");
        try {
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setEstado(true);
            usuarioNuevo.setNombreusuario(nuevoUserDocente);
            usuarioNuevo.setPasswordusuario(nuevoPassDocente);
            Persona personaNueva = new Persona();
            personaNueva.setApellidospersona(nuevoApellidoDocente);
            personaNueva.setDireccionpersona(nuevoDireccionDocente);
            personaNueva.setEmailpersona(nuevoCorreoDocente);
            personaNueva.setIdentificacionpersona(nuevoIdentificacionDocente);
            personaNueva.setNombrespersona(nuevoNombresDocente);
            personaNueva.setTelefono1persona(nuevoNumero1Docente);
            personaNueva.setTelefono2persona(nuevoNumero2Docente);
            Docente docenteNuevo = new Docente();
            docenteNuevo.setDepartamento(nuevoDepartamentoDocente);
            docenteNuevo.setCargodocente(nuevoCargoDocente);
            administrarDocentesBO.almacenarNuevoDocenteEnSistema(usuarioNuevo, personaNueva, docenteNuevo);
            context.execute("registroExitosoDocente.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin almacenarNuevoDocenteEnSistema : " + e.toString());
            context.execute("registroFallidoDocente.show()");
        }
    }

    public void modificarIdentificacionDocente() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validar = validarIdentificacionDocente();
        if (validar == true) {
            nuevoUserDocente = nuevoIdentificacionDocente;
            nuevoPassDocente = nuevoIdentificacionDocente + "UC";
        } else {
            nuevoUserDocente = null;
            nuevoPassDocente = null;
        }
        context.update("formT:formularioDialogos:nuevoPasswordDocente");
        context.update("formT:formularioDialogos:nuevoUsuarioDocente");
    }

    // GET - SET
    public String getParametroNombre() {
        return parametroNombre;
    }

    public void setParametroNombre(String parametroNombre) {
        this.parametroNombre = parametroNombre;
    }

    public String getParametroApellido() {
        return parametroApellido;
    }

    public void setParametroApellido(String parametroApellido) {
        this.parametroApellido = parametroApellido;
    }

    public String getParametroDocumento() {
        return parametroDocumento;
    }

    public void setParametroDocumento(String parametroDocumento) {
        this.parametroDocumento = parametroDocumento;
    }

    public String getParametroCorreo() {
        return parametroCorreo;
    }

    public void setParametroCorreo(String parametroCorreo) {
        this.parametroCorreo = parametroCorreo;
    }

    public String getParametroCargo() {
        return parametroCargo;
    }

    public void setParametroCargo(String parametroCargo) {
        this.parametroCargo = parametroCargo;
    }

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public Departamento getParametroDepartamento() {
        return parametroDepartamento;
    }

    public void setParametroDepartamento(Departamento parametroDepartamento) {
        this.parametroDepartamento = parametroDepartamento;
    }

    public boolean isParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public boolean isTodosDocentes() {
        return todosDocentes;
    }

    public void setTodosDocentes(boolean todosDocentes) {
        this.todosDocentes = todosDocentes;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public List<Docente> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<Docente> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    public List<Docente> getFiltrarListaDocentes() {
        return filtrarListaDocentes;
    }

    public void setFiltrarListaDocentes(List<Docente> filtrarListaDocentes) {
        this.filtrarListaDocentes = filtrarListaDocentes;
    }

    public boolean isActivoDepartamento() {
        return activoDepartamento;
    }

    public void setActivoDepartamento(boolean activoDepartamento) {
        this.activoDepartamento = activoDepartamento;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public List<Facultad> getListaFacultades() {
        return listaFacultades;
    }

    public void setListaFacultades(List<Facultad> listaFacultades) {
        this.listaFacultades = listaFacultades;
    }

    public Facultad getParametroFacultad() {
        return parametroFacultad;
    }

    public void setParametroFacultad(Facultad parametroFacultad) {
        this.parametroFacultad = parametroFacultad;
    }

    public String getNuevoNombresDocente() {
        return nuevoNombresDocente;
    }

    public void setNuevoNombresDocente(String nuevoNombresDocente) {
        this.nuevoNombresDocente = nuevoNombresDocente;
    }

    public String getNuevoApellidoDocente() {
        return nuevoApellidoDocente;
    }

    public void setNuevoApellidoDocente(String nuevoApellidoDocente) {
        this.nuevoApellidoDocente = nuevoApellidoDocente;
    }

    public String getNuevoCorreoDocente() {
        return nuevoCorreoDocente;
    }

    public void setNuevoCorreoDocente(String nuevoCorreoDocente) {
        this.nuevoCorreoDocente = nuevoCorreoDocente;
    }

    public String getNuevoIdentificacionDocente() {
        return nuevoIdentificacionDocente;
    }

    public void setNuevoIdentificacionDocente(String nuevoIdentificacionDocente) {
        this.nuevoIdentificacionDocente = nuevoIdentificacionDocente;
    }

    public String getNuevoNumero1Docente() {
        return nuevoNumero1Docente;
    }

    public void setNuevoNumero1Docente(String nuevoNumero1Docente) {
        this.nuevoNumero1Docente = nuevoNumero1Docente;
    }

    public String getNuevoNumero2Docente() {
        return nuevoNumero2Docente;
    }

    public void setNuevoNumero2Docente(String nuevoNumero2Docente) {
        this.nuevoNumero2Docente = nuevoNumero2Docente;
    }

    public String getNuevoDireccionDocente() {
        return nuevoDireccionDocente;
    }

    public void setNuevoDireccionDocente(String nuevoDireccionDocente) {
        this.nuevoDireccionDocente = nuevoDireccionDocente;
    }

    public String getNuevoCargoDocente() {
        return nuevoCargoDocente;
    }

    public void setNuevoCargoDocente(String nuevoCargoDocente) {
        this.nuevoCargoDocente = nuevoCargoDocente;
    }

    public Facultad getNuevoFacultadDocente() {
        return nuevoFacultadDocente;
    }

    public void setNuevoFacultadDocente(Facultad nuevoFacultadDocente) {
        this.nuevoFacultadDocente = nuevoFacultadDocente;
    }

    public Departamento getNuevoDepartamentoDocente() {
        return nuevoDepartamentoDocente;
    }

    public void setNuevoDepartamentoDocente(Departamento nuevoDepartamentoDocente) {
        this.nuevoDepartamentoDocente = nuevoDepartamentoDocente;
    }

    public boolean isActivoNuevoDepartamento() {
        return activoNuevoDepartamento;
    }

    public void setActivoNuevoDepartamento(boolean activoNuevoDepartamento) {
        this.activoNuevoDepartamento = activoNuevoDepartamento;
    }

    public String getNuevoUserDocente() {
        return nuevoUserDocente;
    }

    public void setNuevoUserDocente(String nuevoUserDocente) {
        this.nuevoUserDocente = nuevoUserDocente;
    }

    public String getNuevoPassDocente() {
        return nuevoPassDocente;
    }

    public void setNuevoPassDocente(String nuevoPassDocente) {
        this.nuevoPassDocente = nuevoPassDocente;
    }

}
