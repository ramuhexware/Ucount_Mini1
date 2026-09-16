package com.freddie.ucount.onboarding.repository;

import com.freddie.ucount.onboarding.entity.Stage1User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Stage1UserRepository extends JpaRepository<Stage1User, Long> {

    Optional<Stage1User> findByUserId(String userId);

    List<Stage1User> findByApprovalStatus(String approvalStatus);

    // PostgreSQL Native Query Pattern
    @Query(value = "SELECT * FROM ucount_stage1_users u WHERE u.network_domain = :domain AND u.approval_status = :status", nativeQuery = true)
    List<Stage1User> findUsersByDomainAndStatusNative(@Param("domain") String domain, @Param("status") String status);

    @Query(value = "SELECT COUNT(*) FROM ucount_stage1_users u WHERE u.organization_name = :orgName", nativeQuery = true)
    long countByOrganizationNameNative(@Param("orgName") String orgName);
}
