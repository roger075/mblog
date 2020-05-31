package com.lin.dao;

import com.lin.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findFirst10ByManuscriptAndModuleIdAndOpenOrderByUpdateTimeDesc(String manuscript,Long moduleId,String open, Pageable pageable);
    int countByManuscriptAndModuleIdAndOpen(String manuscript,Long moduleId,String open);
    List<Blog> findAllByUser_IdAndOpenAndManuscriptOrderByUpdateTimeDesc(Long userId,String open,String manuscript);
    List<Blog> findAllByUser_IdAndOpenNotAndManuscriptOrderByUpdateTimeDesc(Long userId,String open,String manuscript);

    @Query("select b from Blog b where (b.title like ?1 or b.content like ?1)AND b.manuscript='发布'AND b.open='open'")
    Page<Blog> findByQuery(String query,Pageable pageable);
}
