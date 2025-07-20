package com.jangburich.domain.owner.domain.entity;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;

@Comment("가맹점 소유주")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Comment("휴대폰 번호")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Comment("사업자 이름")
    @Column(name = "business_name")
    private String businessName;

    @Comment("사업자 번호")
    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    @Comment("사장님 이름")
    @Column(name = "name")
    private String name;

    @Comment("가게 오픈 날짜")
    @Column(name = "opening_date")
    private LocalDate openingDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("사장님 UI 화면 모드 설정")
    @Enumerated(EnumType.STRING)
    @Column(name="home_mode")
    private HomeMode homeMode;

	public static Owner create(User user) {
		Owner newOwner = new Owner();
		newOwner.user = user;
		return newOwner;
	}

    public void register(String name, String registrationNumber, String businessName, LocalDate openingDate, String phoneNumber) {
        this.name = name;
        this.businessName = businessName;
        this.businessRegistrationNumber = registrationNumber;
        this.openingDate = openingDate;
        this.phoneNumber = phoneNumber; // TODO 암호화
    }

    public void update(HomeMode homeMode) {
        this.homeMode = homeMode;
    }
}
