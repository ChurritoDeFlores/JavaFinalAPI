package com.example.demo.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.dao.IClienteDAO_2;
import com.example.demo.models.entity.Cliente;


@Service
public class ClienteServiceImp implements IClienteService {
	
	@Autowired
	private IClienteDAO_2 clienteDao;
	
	@Transactional(readOnly=true)
	@Override
	public List<Cliente> findAll() {
		return (List<Cliente>)clienteDao.findAll();
	}

	@Transactional(readOnly=true)
	@Override
	public Cliente findById(Long id) {
		return (Cliente)clienteDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Transactional
	@Override
	public void deleteById(Long id) {
		clienteDao.deleteById(id);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Page<Cliente> FindAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}
	
	
	public void modifyById(Long id, Cliente cliente) {
		Cliente existingCliente = clienteDao.findById(id).orElse(null);
	    if (existingCliente != null) {
	        BeanUtils.copyProperties(cliente, existingCliente, getNullPropertyNames(cliente));
	        clienteDao.save(existingCliente);
	    }
	}
	
	private String[] getNullPropertyNames(Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
	    Set<String> emptyNames = new HashSet<>();
	    for (java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
}
