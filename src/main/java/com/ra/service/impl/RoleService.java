package com.ra.service.impl;

import com.ra.model.RoleName;
import com.ra.model.Roles;
import com.ra.repository.IRoleRepository;
import com.ra.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
	
	@Autowired
	private IRoleRepository roleRepository;
	
	@Override
	public Roles findByRoleName(RoleName roleName) {
		return roleRepository.findByRoleName(roleName).orElseThrow(() -> new RuntimeException("role not found"));
	}

	@Override
	public List<Roles> getAllRoles() {
		return roleRepository.findAll();
	}
}
