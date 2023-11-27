package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.entity.Cliente;
import com.example.demo.service.IClienteService;


@RestController
@RequestMapping("/api/cliente")
public class APIClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/")
	public ResponseEntity<Object> Listar() {
		return new ResponseEntity<>(clienteService.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> Ver(@PathVariable(value="id")Long id) {
		Cliente cliente = clienteService.findById(id);
		if(cliente != null) {
			return new ResponseEntity<>(cliente,HttpStatus.OK);
		}
		return new ResponseEntity<>("Cliente no encontrado con ID: " + id,HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> Eliminar(@PathVariable(value="id") Long id) {
		Cliente cliente = clienteService.findById(id);
		if (cliente != null) {
			clienteService.deleteById(id);
			return new ResponseEntity<>("Cliente con ID: " + id + " ELIMINADO",HttpStatus.OK);
		}
		return new ResponseEntity<>("Cliente no encontrado con ID: " + id,HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/")
	public ResponseEntity<Object> Guardar(@RequestPart("nombre") String nombre, @RequestPart("apellido") String apellido, @RequestPart("email") String email, @RequestPart("foto") MultipartFile foto) {
		Cliente cliente = new Cliente();
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setEmail(email);
		if(!foto.isEmpty()) {
			if(cliente.getId()!=null && cliente.getId()>0 && cliente.getFoto()!=null && cliente.getFoto().length()>0) {
				Path rootPath = Paths.get("uploads").resolve(cliente.getFoto()).toAbsolutePath();
				File archivo = rootPath.toFile();
				if(archivo.exists()&&archivo.canRead()) {
					archivo.delete();
				}
			}
			String uniqueFileName = UUID.randomUUID().toString()+"-"+foto.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);
			Path rootAbsolute = rootPath.toAbsolutePath();
			try {
				Files.copy(foto.getInputStream(), rootAbsolute);
				cliente.setFoto(uniqueFileName);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		clienteService.save(cliente);
		return new ResponseEntity<>("Nuevo cliente creado",HttpStatus.CREATED);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Object> Editar(@PathVariable(value="id") Long id, @RequestPart("nombre") String nombre, @RequestPart("apellido") String apellido, @RequestPart("email") String email){
		Cliente cliente = new Cliente();
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setEmail(email);
		Cliente existingCliente = clienteService.findById(id);
	    if (existingCliente != null) {
	        clienteService.modifyById(id,cliente);
	        return new ResponseEntity<>("Cliente actualizado con ID: " + id, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Cliente no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
	    }
	}
}
