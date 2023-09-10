package com.test.imageapplication.repositiry;

import com.test.imageapplication.entity.DBImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
public interface ImageRepository extends JpaRepository<DBImage, Long> {
    @Query(value = "SELECT * FROM dbimage img where ?1=ANY(img.content)", nativeQuery = true)
    List<DBImage> findAllByKey(String key);
}
