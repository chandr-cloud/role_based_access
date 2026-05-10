package com.nt.service;

import com.nt.dto.RolesResponseDto;
import java.util.List;
import java.util.Map;

public interface AdminService {
    public Map<String, List<RolesResponseDto>> getAllUserRoles();
}
