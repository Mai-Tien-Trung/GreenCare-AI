package com.example.GreenCareAI.repository;

import com.example.GreenCareAI.entity.Subscription;
import com.example.GreenCareAI.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.status IN :statuses")
    Optional<Subscription> findByUserIdAndStatusIn(@Param("userId") Long userId,
                                                   @Param("statuses") List<SubscriptionStatus> statuses);
}
