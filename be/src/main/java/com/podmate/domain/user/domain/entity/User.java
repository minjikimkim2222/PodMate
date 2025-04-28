package com.podmate.domain.user.domain.entity;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.model.entity.BaseEntity;
import com.podmate.domain.user.domain.enums.Role;
import com.podmate.domain.user.domain.enums.SocialType;
import com.podmate.domain.user.domain.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "user_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true) // account는 Null 가능 - 나중에 추가 등록 후 업데이트됨
    private String account;

    @Column(nullable = false, unique = true)
    private String email; // 카카오 이메일


    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "manner_score")
    private Double mannerScore;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public User(Long id, Address address, String nickname, String account, String email, String profileImage, Status status, SocialType socialType, Double mannerScore, Role role) {
        this.id = id;
        this.address = address;
        this.nickname = nickname;
        this.account = account;
        this.email = email;
        this.profileImage = profileImage;
        this.status = status;
        this.socialType = socialType;
        this.mannerScore = mannerScore;
        this.role = role;
    }

    // 최초 회원가입 시 -- 주소, 계좌번호 받지 않았을 때..
    public static User createFirstLoginUser(String nickname, String email, String profileImage, SocialType socialType, Role role){
        return User.builder()
                .nickname(nickname)
                .email(email)
                .profileImage(profileImage)
                .socialType(socialType) // 현재는 카카오만
                .status(Status.ACTIVE) // 디폴트값
                .mannerScore(5.0) // 디폴트값
                .role(role) // 디폴트값
                .build();
    }

    public static User createUserWithId(Long userId, String email, String nickname, String profileImage, SocialType socialType, Role role){
        return User.builder()
                .id(userId)
                .email(email)
                .nickname(nickname)
                .profileImage(profileImage)
                .socialType(socialType)
                .role(role)
                .build();
    }

    // User 도메인 관련 비즈니스 로직 (예: 닉네임 변경)
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
}

