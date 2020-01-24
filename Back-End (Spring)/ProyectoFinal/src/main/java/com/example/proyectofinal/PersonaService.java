package com.example.proyectofinal;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PersonaService {
	
	public List<Persona> findAll();
	
	public Page<Persona> findAll(Pageable pageable);
	
	public Persona findById(Long id);
	
	public Persona save(Persona persona);
	
	public void delete(Long id);

}
