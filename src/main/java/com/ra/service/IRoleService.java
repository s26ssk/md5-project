package com.ra.service;

import com.ra.model.RoleName;
import com.ra.model.Roles;

import java.util.List;

public interface IRoleService {
	
	Roles findByRoleName(RoleName roleName);
	List<Roles> getAllRoles();

}
