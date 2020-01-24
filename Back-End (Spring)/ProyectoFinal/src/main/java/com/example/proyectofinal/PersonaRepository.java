package com.example.proyectofinal;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.proyectofinal.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona,Long> {
}