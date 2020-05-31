package com.lin.service.impl;

import com.lin.dao.ModuleRepository;
import com.lin.po.Module;
import com.lin.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {


    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public Module findModuleByName(String name) {
        return moduleRepository.findByContent(name);
    }


    @Override
    public List<Module> findName() {
        return moduleRepository.findAll();
    }
}
