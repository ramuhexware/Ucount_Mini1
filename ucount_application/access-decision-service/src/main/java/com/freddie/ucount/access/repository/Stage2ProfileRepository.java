package com.freddie.ucount.access.repository;

import com.freddie.ucount.access.entity.Stage2Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface Stage2ProfileRepository extends JpaRepository<Stage2Profile, Long> {

    Optional<Stage2Profile> findByProfileId(String profileId);

    Optional<Stage2Profile> findByUserId(String userId);

    List<Stage2Profile> findByUserType(String userType);

    // PostgreSQL Native Query Pattern for fetching profiles created before threshold for ACR batch job
    @Query(value = "SELECT * FROM ucount_stage2_profiles p WHERE p.created_at < :thresholdDate AND p.status = 'ACTIVE'", nativeQuery = true)
    List<Stage2Profile> findOldActiveProfilesNative(@Param("thresholdDate") LocalDateTime thresholdDate);

    @Query(value = "SELECT * FROM ucount_stage2_profiles p WHERE p.user_type = :userType AND p.compliance_passed = true", nativeQuery = true)
    List<Stage2Profile> findCompliantProfilesByUserTypeNative(@Param("userType") String userType);
}
