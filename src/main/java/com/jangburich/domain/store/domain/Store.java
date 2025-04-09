package com.jangburich.domain.store.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jangburich.domain.owner.domain.Owner;

import com.jangburich.domain.store.dto.request.StoreCreateRequest;
import com.jangburich.domain.store.dto.request.StoreUpdateRequest;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "representative_image")
    private String representativeImage;

    @Column(name = "reservation_available")
    private Boolean reservationAvailable;

    @Column(name = "max_reservation")
    private Long maxReservation;

    @Column(name = "min_prepayment")
    private Long minPrepayment;

    @Column(name = "prepayment_duration")
    private Long prepaymentDuration;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "location")
    private String location;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "work_days", joinColumns = @JoinColumn(name = "work_schedule_id"))
    @Column(name = "day_of_week")
    private List<DayOfWeek> workDays;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "open_time")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "contact_number")
    private String contactNumber;

    public static Store create(Owner owner) {
        Store newOwner = new Store();
        newOwner.owner = owner;
        return newOwner;
    }

    public void additionalInfo(
        Boolean reservationAvailable,
        Long maxReservation,
        Long minPrepayment,
        Long prepaymentDuration
    ) {
        this.reservationAvailable = reservationAvailable;
        this.maxReservation = maxReservation;
        this.minPrepayment = minPrepayment;
        this.prepaymentDuration = prepaymentDuration;
    }

    public static Store of(Owner owner, StoreCreateRequest storeCreateRequest, List<DayOfWeek> dayOfWeeks, String imageUrl) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Store newStore = new Store();
        newStore.owner = owner;
        newStore.name = storeCreateRequest.getStoreName();
        newStore.category = storeCreateRequest.getCategory();
        newStore.introduction = storeCreateRequest.getIntroduction();
        newStore.latitude = storeCreateRequest.getLatitude();
        newStore.longitude = storeCreateRequest.getLongitude();
        newStore.address = storeCreateRequest.getAddress();
        newStore.location = storeCreateRequest.getLocation();
        newStore.workDays = dayOfWeeks;
        newStore.openTime = storeCreateRequest.getOpenTime() != null
            ? LocalTime.parse(storeCreateRequest.getOpenTime(), timeFormatter)
            : null
        ;
        newStore.closeTime = storeCreateRequest.getCloseTime() != null
            ? LocalTime.parse(storeCreateRequest.getCloseTime(), timeFormatter)
            : null;

        newStore.contactNumber = storeCreateRequest.getPhoneNumber();
        newStore.reservationAvailable = storeCreateRequest.getReservationAvailable();
        newStore.maxReservation = storeCreateRequest.getMaxReservation();
        newStore.minPrepayment = storeCreateRequest.getMinPrepayment();
        newStore.prepaymentDuration = storeCreateRequest.getPrepaymentDuration();
        newStore.representativeImage = imageUrl;
        return newStore;
    }

    public void update(StoreUpdateRequest dto) {
        if (dto.getCategory() != null) {
            this.category = dto.getCategory();
        }
        if (dto.getReservationAvailable() != null) {
            this.reservationAvailable = dto.getReservationAvailable();
        }
        if (dto.getRepresentativeImage() != null) {
            this.representativeImage = dto.getRepresentativeImage();
        }
        if (dto.getMaxReservation() != null) {
            this.maxReservation = dto.getMaxReservation();
        }
        if (dto.getMinPrepayment() != null) {
            this.minPrepayment = dto.getMinPrepayment();
        }
        if (dto.getPrepaymentDuration() != null) {
            this.prepaymentDuration = dto.getPrepaymentDuration();
        }
        if (dto.getIntroduction() != null) {
            this.introduction = dto.getIntroduction();
        }
        if (dto.getLatitude() != null) {
            this.latitude = dto.getLatitude();
        }
        if (dto.getLongitude() != null) {
            this.longitude = dto.getLongitude();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        if (dto.getLocation() != null) {
            this.location = dto.getLocation();
        }
        if (dto.getDayOfWeek() != null) {
            this.workDays = dto.getDayOfWeek();
        }
        if (dto.getOpenTime() != null) {
            this.openTime = dto.getOpenTime();
        }
        if (dto.getCloseTime() != null) {
            this.closeTime = dto.getCloseTime();
        }
    }
}
