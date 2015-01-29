/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "entidadexterna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntidadExterna.findAll", query = "SELECT e FROM EntidadExterna e"),
    @NamedQuery(name = "EntidadExterna.findByIdentidadexterna", query = "SELECT e FROM EntidadExterna e WHERE e.identidadexterna = :identidadexterna"),
    @NamedQuery(name = "EntidadExterna.findByIdentificacionentidad", query = "SELECT e FROM EntidadExterna e WHERE e.identificacionentidad = :identificacionentidad"),
    @NamedQuery(name = "EntidadExterna.findByNombreentidad", query = "SELECT e FROM EntidadExterna e WHERE e.nombreentidad = :nombreentidad"),
    @NamedQuery(name = "EntidadExterna.findByEmailentidad", query = "SELECT e FROM EntidadExterna e WHERE e.emailentidad = :emailentidad")})
public class EntidadExterna implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "identidadexterna")
    private BigInteger identidadexterna;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "identificacionentidad")
    private String identificacionentidad;
    @Size(max = 45)
    @Column(name = "nombreentidad")
    private String nombreentidad;
    @Size(max = 45)
    @Column(name = "emailentidad")
    private String emailentidad;
    @JoinColumn(name = "persona", referencedColumnName = "idpersona")
    @ManyToOne(optional = false)
    private Persona persona;

    public EntidadExterna() {
    }

    public EntidadExterna(BigInteger identidadexterna) {
        this.identidadexterna = identidadexterna;
    }

    public EntidadExterna(BigInteger identidadexterna, String identificacionentidad) {
        this.identidadexterna = identidadexterna;
        this.identificacionentidad = identificacionentidad;
    }

    public BigInteger getIdentidadexterna() {
        return identidadexterna;
    }

    public void setIdentidadexterna(BigInteger identidadexterna) {
        this.identidadexterna = identidadexterna;
    }

    public String getIdentificacionentidad() {
        return identificacionentidad;
    }

    public void setIdentificacionentidad(String identificacionentidad) {
        this.identificacionentidad = identificacionentidad;
    }

    public String getNombreentidad() {
        return nombreentidad;
    }

    public void setNombreentidad(String nombreentidad) {
        this.nombreentidad = nombreentidad;
    }

    public String getEmailentidad() {
        return emailentidad;
    }

    public void setEmailentidad(String emailentidad) {
        this.emailentidad = emailentidad;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identidadexterna != null ? identidadexterna.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntidadExterna)) {
            return false;
        }
        EntidadExterna other = (EntidadExterna) object;
        if ((this.identidadexterna == null && other.identidadexterna != null) || (this.identidadexterna != null && !this.identidadexterna.equals(other.identidadexterna))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.EntidadExterna[ identidadexterna=" + identidadexterna + " ]";
    }

}
