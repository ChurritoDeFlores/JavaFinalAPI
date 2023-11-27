package com.example.demo.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.models.entity.Cliente;

public interface IClienteService {
	public List<Cliente> findAll();
	public Cliente findById(Long id);
	public void save(Cliente cliente);
	public void deleteById(Long id);
	public void modifyById(Long id, Cliente cliente);
	public Page<Cliente> FindAll(Pageable pageable);
}
