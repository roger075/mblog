package com.lin.dao;

import com.lin.po.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(nativeQuery =true,value = "SELECT * FROM t_report t  WHERE t.reported_id=?1 AND t.report_id=?2")
    Report findByReportedIdAndReportId(Long reportedid,Long reportid);
}
