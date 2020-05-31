package com.lin.service;

import com.lin.po.Inform;

public interface InformService {

    Inform saveInform(Inform inform);
    Inform findById(Long id);
    void deleteInform(Inform inform);
    int getUnReadInform(Long id);
}
