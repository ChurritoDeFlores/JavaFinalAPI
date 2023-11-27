package com.example.demo.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.models.entity.Cliente;

public interface IClienteDAO_2 extends CrudRepository<Cliente,Long>, PagingAndSortingRepository<Cliente, Long>{
	
}
