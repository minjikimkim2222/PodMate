package com.podmate.domain.review.domain.repository;

import com.podmate.domain.review.domain.entity.Review;
import java.util.List;
import java.util.Optional;

import com.podmate.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRecipientId(Long recipientId);

    List<Review> findAllByWriter(User user);

}
