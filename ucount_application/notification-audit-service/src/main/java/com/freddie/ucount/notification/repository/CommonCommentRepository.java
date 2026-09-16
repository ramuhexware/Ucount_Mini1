package com.freddie.ucount.notification.repository;

import com.freddie.ucount.notification.entity.CommonComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonCommentRepository extends JpaRepository<CommonComment, Long> {

    List<CommonComment> findByEntityId(String entityId);

    List<CommonComment> findByServiceOrigin(String serviceOrigin);

    // PostgreSQL Native Query Pattern
    @Query(value = "SELECT * FROM ucount_common_comments c WHERE c.entity_id = :entityId ORDER BY c.created_at DESC", nativeQuery = true)
    List<CommonComment> findCommentsByEntityIdNative(@Param("entityId") String entityId);
}
