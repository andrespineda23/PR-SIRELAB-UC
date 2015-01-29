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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "encargadolaboratorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncargadoLaboratorio.findAll", query = "SELECT e FROM EncargadoLaboratorio e"),
    @NamedQuery(name = "EncargadoLaboratorio.findByIdencargadolaboratorio", query = "SELECT e FROM EncargadoLaboratorio e WHERE e.idencargadolaboratorio = :idencargadolaboratorio")})
public class EncargadoLaboratorio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idencargadolaboratorio")
    private BigInteger idencargadolaboratorio;
    @JoinColumn(name = "persona", referencedColumnName = "idpersona")
    @ManyToOne(optional = false)
    private Persona persona;
    @JoinColumn(name = "laboratorio", referencedColumnName = "idlaboratorio")
    @ManyToOne(optional = false)
    private Laboratorio laboratorio;

    public EncargadoLaboratorio() {
    }

    public EncargadoLaboratorio(BigInteger idencargadolaboratorio) {
        this.idencargadolaboratorio = idencargadolaboratorio;
    }

    public BigInteger getIdencargadolaboratorio() {
        return idencargadolaboratorio;
    }

    public void setIdencargadolaboratorio(BigInteger idencargadolaboratorio) {
        this.idencargadolaboratorio = idencargadolaboratorio;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idencargadolaboratorio != null ? idencargadolaboratorio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncargadoLaboratorio)) {
            return false;
        }
        EncargadoLaboratorio other = (EncargadoLaboratorio) object;
        if ((this.idencargadolaboratorio == null && other.idencargadolaboratorio != null) || (this.idencargadolaboratorio != null && !this.idencargadolaboratorio.equals(other.idencargadolaboratorio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.EncargadoLaboratorio[ idencargadolaboratorio=" + idencargadolaboratorio + " ]";
    }
    
}
