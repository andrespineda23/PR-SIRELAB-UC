/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findByIdpersona", query = "SELECT p FROM Persona p WHERE p.idpersona = :idpersona"),
    @NamedQuery(name = "Persona.findByIdentificacionpersona", query = "SELECT p FROM Persona p WHERE p.identificacionpersona = :identificacionpersona"),
    @NamedQuery(name = "Persona.findByNombrespersona", query = "SELECT p FROM Persona p WHERE p.nombrespersona = :nombrespersona"),
    @NamedQuery(name = "Persona.findByApellidospersona", query = "SELECT p FROM Persona p WHERE p.apellidospersona = :apellidospersona"),
    @NamedQuery(name = "Persona.findByEmailpersona", query = "SELECT p FROM Persona p WHERE p.emailpersona = :emailpersona"),
    @NamedQuery(name = "Persona.findByTelefono1persona", query = "SELECT p FROM Persona p WHERE p.telefono1persona = :telefono1persona"),
    @NamedQuery(name = "Persona.findByTelefono2persona", query = "SELECT p FROM Persona p WHERE p.telefono2persona = :telefono2persona"),
    @NamedQuery(name = "Persona.findByDireccionpersona", query = "SELECT p FROM Persona p WHERE p.direccionpersona = :direccionpersona")})
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpersona")
    private BigInteger idpersona;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "identificacionpersona")
    private String identificacionpersona;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombrespersona")
    private String nombrespersona;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "apellidospersona")
    private String apellidospersona;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "emailpersona")
    private String emailpersona;
    @Size(max = 20)
    @Column(name = "telefono1persona")
    private String telefono1persona;
    @Size(max = 20)
    @Column(name = "telefono2persona")
    private String telefono2persona;
    @Size(max = 45)
    @Column(name = "direccionpersona")
    private String direccionpersona;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona")
    private Collection<EntidadExterna> entidadExternaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona")
    private Collection<Estudiante> estudianteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona")
    private Collection<EncargadoLaboratorio> encargadoLaboratorioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona")
    private Collection<Docente> docenteCollection;
    @JoinColumn(name = "usuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Persona() {
    }

    public Persona(BigInteger idpersona) {
        this.idpersona = idpersona;
    }

    public Persona(BigInteger idpersona, String identificacionpersona, String nombrespersona, String apellidospersona, String emailpersona) {
        this.idpersona = idpersona;
        this.identificacionpersona = identificacionpersona;
        this.nombrespersona = nombrespersona;
        this.apellidospersona = apellidospersona;
        this.emailpersona = emailpersona;
    }

    public BigInteger getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(BigInteger idpersona) {
        this.idpersona = idpersona;
    }

    public String getIdentificacionpersona() {
        return identificacionpersona;
    }

    public void setIdentificacionpersona(String identificacionpersona) {
        this.identificacionpersona = identificacionpersona;
    }

    public String getNombrespersona() {
        return nombrespersona;
    }

    public void setNombrespersona(String nombrespersona) {
        this.nombrespersona = nombrespersona;
    }

    public String getApellidospersona() {
        return apellidospersona;
    }

    public void setApellidospersona(String apellidospersona) {
        this.apellidospersona = apellidospersona;
    }

    public String getEmailpersona() {
        return emailpersona;
    }

    public void setEmailpersona(String emailpersona) {
        this.emailpersona = emailpersona;
    }

    public String getTelefono1persona() {
        return telefono1persona;
    }

    public void setTelefono1persona(String telefono1persona) {
        this.telefono1persona = telefono1persona;
    }

    public String getTelefono2persona() {
        return telefono2persona;
    }

    public void setTelefono2persona(String telefono2persona) {
        this.telefono2persona = telefono2persona;
    }

    public String getDireccionpersona() {
        return direccionpersona;
    }

    public void setDireccionpersona(String direccionpersona) {
        this.direccionpersona = direccionpersona;
    }

    @XmlTransient
    public Collection<EntidadExterna> getEntidadExternaCollection() {
        return entidadExternaCollection;
    }

    public void setEntidadExternaCollection(Collection<EntidadExterna> entidadExternaCollection) {
        this.entidadExternaCollection = entidadExternaCollection;
    }

    @XmlTransient
    public Collection<Estudiante> getEstudianteCollection() {
        return estudianteCollection;
    }

    public void setEstudianteCollection(Collection<Estudiante> estudianteCollection) {
        this.estudianteCollection = estudianteCollection;
    }

    @XmlTransient
    public Collection<EncargadoLaboratorio> getEncargadoLaboratorioCollection() {
        return encargadoLaboratorioCollection;
    }

    public void setEncargadoLaboratorioCollection(Collection<EncargadoLaboratorio> encargadoLaboratorioCollection) {
        this.encargadoLaboratorioCollection = encargadoLaboratorioCollection;
    }

    @XmlTransient
    public Collection<Docente> getDocenteCollection() {
        return docenteCollection;
    }

    public void setDocenteCollection(Collection<Docente> docenteCollection) {
        this.docenteCollection = docenteCollection;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpersona != null ? idpersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.idpersona == null && other.idpersona != null) || (this.idpersona != null && !this.idpersona.equals(other.idpersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Persona[ idpersona=" + idpersona + " ]";
    }

}
