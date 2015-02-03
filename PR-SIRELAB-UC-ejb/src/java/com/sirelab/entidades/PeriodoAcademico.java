/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "periodoacademico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoAcademico.findAll", query = "SELECT p FROM PeriodoAcademico p"),
    @NamedQuery(name = "PeriodoAcademico.findByIdperiodoacademico", query = "SELECT p FROM PeriodoAcademico p WHERE p.idperiodoacademico = :idperiodoacademico"),
    @NamedQuery(name = "PeriodoAcademico.findByDetalleperiodo", query = "SELECT p FROM PeriodoAcademico p WHERE p.detalleperiodo = :detalleperiodo"),
    @NamedQuery(name = "PeriodoAcademico.findByFechainicial", query = "SELECT p FROM PeriodoAcademico p WHERE p.fechainicial = :fechainicial"),
    @NamedQuery(name = "PeriodoAcademico.findByFechafinal", query = "SELECT p FROM PeriodoAcademico p WHERE p.fechafinal = :fechafinal")})
public class PeriodoAcademico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idperiodoacademico")
    private BigInteger idperiodoacademico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "detalleperiodo")
    private String detalleperiodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechainicial")
    @Temporal(TemporalType.DATE)
    private Date fechainicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechafinal")
    @Temporal(TemporalType.DATE)
    private Date fechafinal;

    public PeriodoAcademico() {
    }

    public PeriodoAcademico(BigInteger idperiodoacademico) {
        this.idperiodoacademico = idperiodoacademico;
    }

    public PeriodoAcademico(BigInteger idperiodoacademico, String detalleperiodo, Date fechainicial, Date fechafinal) {
        this.idperiodoacademico = idperiodoacademico;
        this.detalleperiodo = detalleperiodo;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
    }

    public BigInteger getIdperiodoacademico() {
        return idperiodoacademico;
    }

    public void setIdperiodoacademico(BigInteger idperiodoacademico) {
        this.idperiodoacademico = idperiodoacademico;
    }

    public String getDetalleperiodo() {
        return detalleperiodo;
    }

    public void setDetalleperiodo(String detalleperiodo) {
        this.detalleperiodo = detalleperiodo;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idperiodoacademico != null ? idperiodoacademico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodoAcademico)) {
            return false;
        }
        PeriodoAcademico other = (PeriodoAcademico) object;
        if ((this.idperiodoacademico == null && other.idperiodoacademico != null) || (this.idperiodoacademico != null && !this.idperiodoacademico.equals(other.idperiodoacademico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.PeriodoAcademico[ idperiodoacademico=" + idperiodoacademico + " ]";
    }
    
}
