package com.podmate.domain.review.domain.repository;

import com.podmate.domain.review.domain.entity.ReviewOption;
import com.podmate.domain.review.domain.enums.OptionText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewOptionRepository extends JpaRepository<ReviewOption, Long> {
    Optional<ReviewOption> findByOptionText(OptionText optionText);

}
