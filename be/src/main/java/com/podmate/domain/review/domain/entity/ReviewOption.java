package com.podmate.domain.review.domain.entity;

import com.podmate.domain.review.domain.enums.OptionText;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.valueextraction.UnwrapByDefault;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_option")
@Getter
public class ReviewOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_option_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_text", nullable = false)
    private OptionText optionText;

}
