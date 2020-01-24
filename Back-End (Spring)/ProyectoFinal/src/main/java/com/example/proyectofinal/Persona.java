package com.example.proyectofinal;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="Persona")
public class Persona implements Serializable {

	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY ) 
	private long id;
	@NotEmpty(message="no puede estar vacio")
	@Size(min=3, max=20,message="el tamaño tiene que estar entre 3 y 20 caracteres")
	@Column(nullable=false)
	private String nombre;
	@NotEmpty(message="no puede estar vacio")
	private String apellidos;
	@NotEmpty(message="no puede estar vacio")
	@Email(message="no es una direccion de correo valida")
	@Column(nullable=false, unique=true)
	private String email;
	@NotNull(message="no puede estar vacio")
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	private String foto;
	

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Persona(long id, String nombre,String apellidos, String email, Date createAt) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.createAt = createAt;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Persona() {
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}


	private static final long serialVersionUID = 1L;

}
