package com.lin.dao;

import com.lin.po.Inform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InformRepository extends JpaRepository<Inform, Long> {
    @Query("select t from Inform t where t.usered.id = ?1 and t.readed = ?2")
    List<Inform> findByInformedIdAndReaded(Long informedId,byte readed);
}
