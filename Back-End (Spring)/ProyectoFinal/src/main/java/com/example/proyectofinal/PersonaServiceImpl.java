package com.example.proyectofinal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonaServiceImpl implements PersonaService {

	@Autowired
	private PersonaRepository personaDao;
	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {		
		return (List<Persona>) personaDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Persona> findAll(Pageable pageable) {
		
		return personaDao.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Persona findById(Long id) {
		return personaDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Persona save(Persona persona) {
		return personaDao.save(persona);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		personaDao.deleteById(id);
	}
	

}
