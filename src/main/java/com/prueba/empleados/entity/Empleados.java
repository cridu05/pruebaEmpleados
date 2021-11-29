package com.prueba.empleados.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class Empleados implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombres;
	private String apellidos;
	private String tipoDocumento;
	private String numeroDocumento;
	private Date fechaNacimiento;
	private Date fechaVinculacionCompania;
	private String cargo;
	private Double salario;

	@JsonInclude()
	@Transient
	private String tiempoVinculacionCompania;

	@JsonInclude()
	@Transient
	private String edadActualEmpleado;

	public Empleados() {
		// TODO Auto-generated constructor stub
	}

	public Empleados(String nombres, String apellidos, String tipoDocumento, String numeroDocumento,
			Date fechaNacimiento, Date fechaVinculacionCompania, String cargo, Double salario) {
		super();
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaVinculacionCompania = fechaVinculacionCompania;
		this.cargo = cargo;
		this.salario = salario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Date getFechaVinculacionCompania() {
		return fechaVinculacionCompania;
	}

	public void setFechaVinculacionCompania(Date fechaVinculacionCompania) {
		this.fechaVinculacionCompania = fechaVinculacionCompania;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public String getTiempoVinculacionCompania() {
		return tiempoVinculacionCompania;
	}

	public void setTiempoVinculacionCompania(String tiempoVinculacionCompania) {
		this.tiempoVinculacionCompania = tiempoVinculacionCompania;
	}

	public String getEdadActualEmpleado() {
		return edadActualEmpleado;
	}

	public void setEdadActualEmpleado(String edadActualEmpleado) {
		this.edadActualEmpleado = edadActualEmpleado;
	}

}
