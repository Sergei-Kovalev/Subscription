package jdev.kovalev.repository;

import jdev.kovalev.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    @Query(value = """
            SELECT s.* FROM subscriptions s
            LEFT JOIN user_subscription us ON s.subscription_id = us.subscription_id
            GROUP BY s.subscription_id
            ORDER BY COUNT(us.user_id) DESC
            LIMIT 3
            """, nativeQuery = true)
    List<Subscription> findTop();
}
