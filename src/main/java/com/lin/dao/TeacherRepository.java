package com.lin.dao;

import com.lin.po.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    @Query("select t from Teacher t where t.idNumber = ?1")
    Teacher findByIdNumber(Long id_number);
}
