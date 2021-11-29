package com.prueba.empleados.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.empleados.entity.Empleados;

public interface EmpleadosDAO  extends JpaRepository<Empleados, Long>{

}
