package com.lin.service;

import com.lin.po.Blog;
import com.lin.po.Report;
import com.lin.po.User;

import java.util.List;

public interface ReportService {
    Report saveReport(Report report);
    void deleteReport(Report report);
    Report findReport(Blog blog, User user);
    List<Report> findAll();
    Report findById(Long Id);
}
