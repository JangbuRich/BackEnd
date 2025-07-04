package com.jangburich.domain.owner.domain.entity;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
}
