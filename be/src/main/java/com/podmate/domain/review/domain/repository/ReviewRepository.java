package com.podmate.domain.review.domain.repository;

import com.podmate.domain.review.domain.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRecipientId(Long recipientId);

}
