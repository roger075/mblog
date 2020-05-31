package com.lin.dao;

import com.lin.po.Collect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CollectRepository extends JpaRepository<Collect, Long> {
    @Query(nativeQuery =true,value = "SELECT * FROM t_collect t  WHERE t.collected_bid=?1 AND t.collect_uid=?2")
    Collect findByCollectedBidAndCollectUid(Long collected_bid,Long collect_uid);
}
