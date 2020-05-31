package com.lin.service;

import com.lin.po.Blog;
import com.lin.po.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {

    Tag findByName(String name);
    void tagAddList(List<Tag> list, String tag);
    List<String> getTags();
    Map<Long,String> getTopFiveTag();

}
