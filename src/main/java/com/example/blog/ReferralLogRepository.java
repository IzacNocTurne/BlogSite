package com.example.blog;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReferralLogRepository extends JpaRepository<ReferralLog, Integer> {

    @Query("SELECT r.post FROM ReferralLog r GROUP BY r.post.id ORDER BY COUNT(r.post.id) DESC")
    List<Post> findTopPostsByReferralCount(Pageable pageable);

    @Query("SELECT new com.example.blog.ReferralStats(r.searchEngine, COUNT(r)) FROM ReferralLog r GROUP BY r.searchEngine")
    List<ReferralStats> findReferralStats();
}
