package com.lin.service;

import com.lin.po.Module;

import java.util.List;

public interface ModuleService {
    Module findModuleByName(String name);
    List<Module> findName();
}
