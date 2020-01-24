package com.example.proyectofinal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Persona")
public class ControladorPersona {
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@RequestMapping("")
	public List<Persona> listPersona() {
		return (List<Persona>) personaRepository.findAll();
	}

	@RequestMapping(value="", method=RequestMethod.POST)
	public Persona crearPersona(@RequestBody Persona persona) {
		return personaRepository.save(persona);
	}
}
