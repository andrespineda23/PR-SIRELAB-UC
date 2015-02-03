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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "edificio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Edificio.findAll", query = "SELECT e FROM Edificio e"),
    @NamedQuery(name = "Edificio.findByIdedificio", query = "SELECT e FROM Edificio e WHERE e.idedificio = :idedificio"),
    @NamedQuery(name = "Edificio.findByDireccion", query = "SELECT e FROM Edificio e WHERE e.direccion = :direccion"),
    @NamedQuery(name = "Edificio.findByDescripcionedificio", query = "SELECT e FROM Edificio e WHERE e.descripcionedificio = :descripcionedificio")})
public class Edificio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idedificio")
    private BigInteger idedificio;
    @Size(max = 45)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 45)
    @Column(name = "descripcionedificio")
    private String descripcionedificio;
    @JoinColumn(name = "sede", referencedColumnName = "idsede")
    @ManyToOne(optional = false)
    private Sede sede;

    public Edificio() {
    }

    public Edificio(BigInteger idedificio) {
        this.idedificio = idedificio;
    }

    public BigInteger getIdedificio() {
        return idedificio;
    }

    public void setIdedificio(BigInteger idedificio) {
        this.idedificio = idedificio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcionedificio() {
        return descripcionedificio;
    }

    public void setDescripcionedificio(String descripcionedificio) {
        this.descripcionedificio = descripcionedificio;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idedificio != null ? idedificio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Edificio)) {
            return false;
        }
        Edificio other = (Edificio) object;
        if ((this.idedificio == null && other.idedificio != null) || (this.idedificio != null && !this.idedificio.equals(other.idedificio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Edificio[ idedificio=" + idedificio + " ]";
    }
    
}
