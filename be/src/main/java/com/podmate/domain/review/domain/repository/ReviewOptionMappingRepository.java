package com.podmate.domain.review.domain.repository;

import com.podmate.domain.review.domain.entity.ReviewOption;
import com.podmate.domain.review.domain.entity.ReviewOptionMapping;
import java.util.List;
import java.util.Optional;

import com.podmate.domain.review.domain.enums.OptionText;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewOptionMappingRepository extends JpaRepository<ReviewOptionMapping, Long> {
    List<ReviewOptionMapping> findAllByReviewId(Long reviewId);
}
