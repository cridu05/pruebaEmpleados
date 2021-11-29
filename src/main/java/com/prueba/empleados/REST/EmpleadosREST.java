package com.prueba.empleados.REST;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.empleados.DAO.EmpleadosDAO;
import com.prueba.empleados.entity.Empleados;

@RestController
@RequestMapping("/empleados")
public class EmpleadosREST {

	@Autowired
	EmpleadosDAO empleadosDAO;

	@GetMapping(value = "/agregar", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> insertarEmpleado(@RequestParam(value = "nombres", defaultValue = "") String nombres,
			@RequestParam(value = "apellidos", defaultValue = "") String apellidos,
			@RequestParam(value = "tipoDocumento", defaultValue = "") String tipoDocumento,
			@RequestParam(value = "numeroDocumento", defaultValue = "") String numeroDocumento,
			@RequestParam(value = "fechaNacimiento", defaultValue = "") String fechaNacimiento,
			@RequestParam(value = "fechaVinculacionCompania", defaultValue = "") String fechaVinculacionCompania,
			@RequestParam(value = "cargo", defaultValue = "") String cargo,
			@RequestParam(value = "salario", defaultValue = "") String salario) {
		Map<String, String> respuesta = new HashMap();
		if (nombres != null && !nombres.equals("") && apellidos != null && !apellidos.equals("")
				&& tipoDocumento != null && !tipoDocumento.equals("") && numeroDocumento != null
				&& !numeroDocumento.equals("") && fechaNacimiento != null && !fechaNacimiento.equals("")
				&& fechaVinculacionCompania != null && !fechaVinculacionCompania.equals("") && cargo != null
				&& !cargo.equals("") && salario != null && !salario.equals("")) {

			Date fechaNacimientoDate, fechaVinculacionCompaniaDate;
			LocalDate fechaActual = LocalDate.now();
			Period periodoEdadEmpleado;
			try {
				fechaVinculacionCompaniaDate = new SimpleDateFormat("dd-MM-yyyy").parse(fechaVinculacionCompania);
			} catch (Exception e) {
				respuesta.put("mensaje", "El formato de la fecha de vinculación es incorrecta.");
				return ResponseEntity.badRequest().body(respuesta);
			}

			try {
				fechaNacimientoDate = new SimpleDateFormat("dd-MM-yyyy").parse(fechaNacimiento);
				Calendar cal = Calendar.getInstance();
				cal.setTime(fechaNacimientoDate);
				LocalDate fechaNacimientoL = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH));

				periodoEdadEmpleado = Period.between(fechaNacimientoL, fechaActual);
				if (periodoEdadEmpleado.getYears() < 18) {
					respuesta.put("mensaje", "El empleado debe ser mayor de edad.");
					return ResponseEntity.badRequest().body(respuesta);
				}
			} catch (Exception e) {
				respuesta.put("mensaje", "El formato de la fecha de nacimiento es incorrecta.");
				return ResponseEntity.badRequest().body(respuesta);
			}

			Double salarioL = 0.0;
			try {
				salarioL = Double.parseDouble(salario);
			} catch (NumberFormatException e) {
				respuesta.put("mensaje", "El salario debe ser un valor numerico.");
				return ResponseEntity.badRequest().body(respuesta);
			}

			Empleados empleado = new Empleados(nombres, apellidos, tipoDocumento, numeroDocumento, fechaNacimientoDate,
					fechaVinculacionCompaniaDate, cargo, salarioL);

			empleado = empleadosDAO.save(empleado);

			if (empleado.getId() != null) {
				calcularTiempo(empleado);
				return ResponseEntity.status(HttpStatus.OK).body(empleado);
			} else {
				respuesta.put("mensaje", "Se presento un problema al insertar la información del empleado.");
				return ResponseEntity.badRequest().body(respuesta);
			}
		} else {
			respuesta.put("mensaje", "Ninguno de los campos debe estar vacio.");
			return ResponseEntity.badRequest().body(respuesta);
		}
	}

	@GetMapping(value = "/consultarEmpleado", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> consultarEmpleado(@RequestParam(value = "id", defaultValue = "0") Long id) {
		Optional<Empleados> empleado = empleadosDAO.findById(id);
		if (empleado.isPresent()) {
			Empleados empleadoResponse = empleado.get();
			calcularTiempo(empleadoResponse);
			return ResponseEntity.status(HttpStatus.OK).body(empleadoResponse);
		} else {
			Map<String, String> respuesta = new HashMap();
			respuesta.put("mensaje", "No se encontro información del empleado consultado");
			return ResponseEntity.badRequest().body(respuesta);
		}
	}

	@GetMapping(value = "/listarEmpleados", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> listarEmpleados() {
		List<Empleados> listaEmpleados = empleadosDAO.findAll();
		if (listaEmpleados != null && listaEmpleados.size() > 0) {
			for (Empleados empleado : listaEmpleados) {
				calcularTiempo(empleado);
			}
			return ResponseEntity.status(HttpStatus.OK).body(listaEmpleados);
		} else {
			Map<String, String> respuesta = new HashMap();
			respuesta.put("mensaje", "No se encontro información de empleados");
			return ResponseEntity.badRequest().body(respuesta);
		}
	}

	public void calcularTiempo(Empleados empleado) {
		LocalDate fechaActual = LocalDate.now();
		Period periodo;
		Calendar cal = Calendar.getInstance();
		cal.setTime(empleado.getFechaNacimiento());
		LocalDate fechaLocalDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		periodo = Period.between(fechaLocalDate, fechaActual);

		String mensaje = "";
		mensaje += mensajeTiempo("año", periodo.getYears(), mensaje);
		mensaje += mensajeTiempo("mes", periodo.getMonths(), mensaje);
		mensaje += mensajeTiempo("dia", periodo.getDays(), mensaje);

		empleado.setEdadActualEmpleado(mensaje);

		cal = Calendar.getInstance();
		cal.setTime(empleado.getFechaVinculacionCompania());
		fechaLocalDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));

		periodo = Period.between(fechaLocalDate, fechaActual);

		mensaje = "";
		mensaje += mensajeTiempo("año", periodo.getYears(), mensaje);
		mensaje += mensajeTiempo("mes", periodo.getMonths(), mensaje);
		mensaje += mensajeTiempo("dia", periodo.getDays(), mensaje);

		empleado.setTiempoVinculacionCompania(mensaje);
	}

	public String mensajeTiempo(String tipo, int numero, String mensajeActual) {
		String mensaje = "";
		switch (tipo) {
		case "año": {
			if (numero > 0 && numero == 1) {
				mensaje = numero + " año";
			} else if (numero > 1) {
				mensaje = numero + " años";
			}
			break;
		}
		case "mes": {
			if (numero > 0 && numero == 1) {
				mensaje = (!mensajeActual.equals("") ? ", " : "") + numero + " mes";
			} else if (numero > 1) {
				mensaje = (!mensajeActual.equals("") ? ", " : "") + numero + " meses";
			}
			break;
		}
		case "dia": {
			if (numero > 0 && numero == 1) {
				mensaje = (!mensajeActual.equals("") ? " y " : "") + numero + " día.";
			} else if (numero > 1) {
				mensaje = (!mensajeActual.equals("") ? " y " : "") + numero + " días.";
			}
			break;
		}
		}
		return mensaje;
	}

}
