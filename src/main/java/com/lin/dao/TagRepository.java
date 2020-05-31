package com.lin.dao;

import com.lin.po.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String Name);
    @Query(value = "select t.name from Tag t where t.usable = ?1")
    List findNameByUsable(Byte i);
    @Query(nativeQuery =true,value = "SELECT t.tag_list_id  FROM t_blog_tag_list t GROUP BY t.tag_list_id ORDER BY COUNT(t.blog_list_id) DESC LIMIT 5")
    List findTopFiveTag();
    Tag getById(Long id);
}
