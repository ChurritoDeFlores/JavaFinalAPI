package com.example.demo.models.dao;

import java.util.List;

import com.example.demo.models.entity.Cliente;

public interface IClienteDAO {
	public List<Cliente> FindAll();
	public Cliente FindOne(Long id);
	public void Save(Cliente cliente);
	public void Delete(Long id);
}
