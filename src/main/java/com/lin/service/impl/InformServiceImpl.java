package com.lin.service.impl;

import com.lin.dao.InformRepository;
import com.lin.po.Inform;
import com.lin.service.InformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InformServiceImpl implements InformService {

    @Autowired
    private InformRepository informRepository;

    @Override
    public Inform saveInform(Inform inform) {
        return informRepository.save(inform);
    }

    @Override
    public int getUnReadInform(Long id) {
        List<Inform> informList = informRepository.findByInformedIdAndReaded(id, (byte) 0);
        return informList.size();
    }

    @Override
    public void deleteInform(Inform inform) {
        informRepository.delete(inform);
    }

    @Override
    public Inform findById(Long id) {
        return informRepository.findById(id).get();
    }
}
