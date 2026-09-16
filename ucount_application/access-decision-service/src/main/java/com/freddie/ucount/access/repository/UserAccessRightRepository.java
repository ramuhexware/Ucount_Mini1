package com.freddie.ucount.access.repository;

import com.freddie.ucount.access.entity.UserAccessRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccessRightRepository extends JpaRepository<UserAccessRight, Long> {

    List<UserAccessRight> findByUserId(String userId);

    List<UserAccessRight> findByProfileId(String profileId);

    void deleteByProfileId(String profileId);
}
