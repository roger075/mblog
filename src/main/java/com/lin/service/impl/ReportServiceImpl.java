package com.lin.service.impl;

import com.lin.dao.ReportRepository;
import com.lin.po.Blog;
import com.lin.po.Report;
import com.lin.po.User;
import com.lin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public void deleteReport(Report report) {
        reportRepository.delete(report);
    }

    @Override
    public Report findReport(Blog blog, User user) {
        return reportRepository.findByReportedIdAndReportId(blog.getId(),user.getId());
    }

    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public Report findById(Long Id) {
        return reportRepository.findById(Id).get();
    }
}
