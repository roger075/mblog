package com.lin.dao;

import com.lin.po.Concern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConcernRepository extends JpaRepository<Concern, Long> {
    @Query(nativeQuery =true,value = "SELECT  * FROM t_concern t  WHERE t.concern_id=?1 AND t.concerned_id=?2")
    Concern findByConcern_IdAndConcern_Id(Long concern_id,Long concerned_id);

}
