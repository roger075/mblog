package com.lin.dao;

import com.lin.po.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    @Query(nativeQuery =true,value = "SELECT  * FROM t_blacklist t  WHERE t.blacklist_id=?1 AND t.blacklisted_id=?2")
    BlackList findByBlackList_IdAndBlackList_Id(Long blackList_id,Long blackListed_id);
}
