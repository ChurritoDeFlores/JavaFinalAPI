package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.entity.Cliente;
import com.example.demo.paginator.PageRender;
import com.example.demo.service.IClienteService;

import jakarta.validation.Valid;

@Controller
public class ClienteController {
	@Autowired
	private IClienteService clienteDAO; //inyeccion
	
	@GetMapping({"/listar","/"})
	public String Listar(@RequestParam(name="page",defaultValue="0")int page, Model model) {
		model.addAttribute("titulo","Listado de Clientes");
		Pageable page_request = PageRequest.of(page, 4);
		Page<Cliente> clientes = clienteDAO.FindAll(page_request);
		PageRender<Cliente> page_render = new PageRender<>("/listar",clientes);
		model.addAttribute("page",page_render);
		model.addAttribute("clientes", clientes);
		//model.addAttribute("clientes", clienteDAO.findAll());
		return "listar";
	}
	@GetMapping("/form")
	public String Crear(Map<String,Object>model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Inserte los datos");
		return "form";
	}
	@PostMapping("/form")
	public String Guardar(@Valid Cliente cliente, BindingResult resultado, Model model, @RequestParam("file") MultipartFile foto) {
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
		if(resultado.hasErrors()){
			model.addAttribute("Titulo","Formulario de cliente");
			return "form";
		}
		clienteDAO.save(cliente);
		return "redirect:/listar";
	}
	@GetMapping("/ver/{id}")
	public String Ver(@PathVariable(value="id")Long id, Map<String,Object> model) {
		Cliente cliente = clienteDAO.findById(id);
		if(cliente == null) {
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle Cliente");
		return "ver";
	}
	
	@GetMapping("/form/{id}")
	public String Editar(@PathVariable Long id, Map<String, Object> model) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteDAO.findById(id);
		}else {
			return "redirect:/listar";
		}
		model.put("cliente",cliente);
		model.put("titulo", "Editar CLiente");
		return "form";
	}
	@GetMapping("/eliminar/{id}")
	public String Eliminar(@PathVariable Long id) {
		if (id > 0) {
			clienteDAO.deleteById(id);
		}
		return "redirect:/listar";
	}
}
