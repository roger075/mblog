package com.lin.service.impl;

import com.lin.dao.TagRepository;
import com.lin.po.Blog;
import com.lin.po.Tag;
import com.lin.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void tagAddList(List<Tag> list,String tag) {
        if(!tag.isEmpty()){
            Tag temp = findByName(tag);
            if((temp != null)&&(temp.getUsable() == 1)){
                list.add(temp);
            }
        }
    }

    @Override
    @Transactional
    public Tag findByName(String name) {
        if(!name.isEmpty()) {
            Tag tag = tagRepository.findByName(name);
            if (tag != null) {
                if (tag.getUsable() == 1) {
                    return tag;
                } else return null;
            } else {
                Tag temp = new Tag();
                temp.setName(name);
                temp.setUpdateTime(new Date());
                temp.setUsable((byte) 1);
                tagRepository.save(temp);
                return temp;
            }
        } else return null;
    }

    @Override
    public List<String> getTags() {
        return tagRepository.findNameByUsable((byte)1);
    }

    @Override
    public Map<Long,String> getTopFiveTag() {
        List<BigInteger> topTenTag = tagRepository.findTopFiveTag();
        Map<Long,String> map = new HashMap<>();
        for(BigInteger i : topTenTag){
            Tag tag = tagRepository.getById(i.longValue());
            map.put(i.longValue(),tag.getName());
        }
        return map;
    }
}
