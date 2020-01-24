package com.example.proyectofinal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PersonaRestController {
	
	@Autowired
	private PersonaService personaService;
	
	private final Logger log = LoggerFactory.getLogger(PersonaRestController.class);
	
	@GetMapping("/persona")
	public List<Persona> index(){
		return personaService.findAll();
	}
	
	@GetMapping("/persona/page/{page}")
	public Page<Persona> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return personaService.findAll(pageable);
	}

	@GetMapping("/persona/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Persona persona = null;
		Map<String, Object> response = new HashMap<>();
		try {
			persona = personaService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Fallo al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(persona == null) {
			response.put("mensaje", "La persona con el ID:".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Persona>(persona, HttpStatus.OK) ;
	}
	
	@PostMapping("/persona")
	public ResponseEntity<?> create(@Valid @RequestBody Persona persona, BindingResult result) {
		
		Persona personaNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			personaNew = personaService.save(persona);
		} catch(DataAccessException e) {
			response.put("mensaje", "Fallo al realizar la inserción en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","La persona se ha insertado con exito");
		response.put("persona", personaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/persona/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Persona persona,BindingResult result, @PathVariable Long id) {
		Persona personaActual = personaService.findById(id);
		Persona personaUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(personaActual == null) {
			response.put("mensaje", "Error al editar: La persona con el ID:".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
		personaActual.setApellidos(persona.getApellidos());
		personaActual.setNombre(persona.getNombre());
		personaActual.setEmail(persona.getEmail());
		personaActual.setCreateAt(persona.getCreateAt());
		
		personaUpdated = personaService.save(personaActual);
		} catch(DataAccessException e) {
			response.put("mensaje", "Fallo al actualizar la persona en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","La persona se ha actualizado con exito");
		response.put("persona", personaUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED) ;
	}
	
	@DeleteMapping("/persona/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Persona persona = personaService.findById(id);
			String nombreFotoAnterior = persona.getFoto();
			if(nombreFotoAnterior !=null && nombreFotoAnterior.length() >0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
		personaService.delete(id);
	}catch(DataAccessException e) {
		response.put("mensaje", "Error al eliminar la persona de la base de datos");
		response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		response.put("mensaje","La persona se ha eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
}
	@PostMapping("/persona/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		
		Persona persona = personaService.findById(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ","");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			log.info(rutaArchivo.toString());
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen: " + nombreArchivo);
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			String nombreFotoAnterior = persona.getFoto();
			if(nombreFotoAnterior !=null && nombreFotoAnterior.length() >0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			persona.setFoto(nombreArchivo);
			personaService.save(persona);
			response.put("persona", persona);
			response.put("mensaje", "Imagen subida correctamente: " + nombreArchivo);
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED) ;
	}
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		log.info(rutaArchivo.toString());
		Resource recurso=null;
		try {
			recurso= new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
			try {
				recurso= new UrlResource(rutaArchivo.toUri());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			log.error("Error no se pudo cargar la imagen: " + nombreFoto);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
		
	}
}
