package com.jangburich.domain.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.jangburich.domain.store.StoreMenu;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jangburich.domain.owner.domain.entity.Owner;

import com.jangburich.presentation.store.dtos.request.StoreCreateRequest;
import com.jangburich.presentation.store.dtos.request.StoreUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Comment("가맹점")
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

    @Comment("회원가입 완료시 발급되는 ID (4자리)")
    @Column(name = "store_unique_code", nullable = false, columnDefinition = "varchar(4)")
    private String storeUniqueCode;

    @Comment("MID (내부 관리용) ")
    @Column(name="store_id", nullable = false, columnDefinition = "varchar(20)")
    private String storeId;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreMenu> storeMenus;

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

    @Builder
    public Store (Owner owner, String name, Category category, String representativeImage, Boolean reservationAvailable,
        Long maxReservation, Long minPrepayment, Long prepaymentDuration, String introduction, Double latitude,
        Double longitude, String address, String location, List<DayOfWeek> workDays, LocalTime openTime,
        LocalTime closeTime, String contactNumber, String storeUniqueCode, String storeId) {
        this.owner = owner;
        this.name = name;
        this.category = category;
        this.representativeImage = representativeImage;
        this.reservationAvailable = reservationAvailable;
        this.maxReservation = maxReservation;
        this.minPrepayment = minPrepayment;
        this.prepaymentDuration = prepaymentDuration;
        this.introduction = introduction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.location = location;
        this.workDays = workDays;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.contactNumber = contactNumber;
        this.storeUniqueCode = storeUniqueCode;
        this.storeId = storeId;
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
        if (dto.getCategory() != null)
            this.category = dto.getCategory();

        if (dto.getRepresentativeImage() != null)
            this.representativeImage = dto.getRepresentativeImage();

        if (dto.getIntroduction() != null)
            this.introduction = dto.getIntroduction();

        if (dto.getAddress() != null)
            this.address = dto.getAddress();

        if (dto.getLocation() != null)
            this.location = dto.getLocation();

        if(dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank())
            this.contactNumber = dto.getPhoneNumber();

        if(dto.getUniqueCode() != null && !dto.getUniqueCode().isBlank())
            this.storeUniqueCode = dto.getUniqueCode();

        if (dto.getDayOfWeek() != null)
            this.workDays = dto.getDayOfWeek();

        if (dto.getOpenTime() != null)
            this.openTime = dto.getOpenTime();

        if (dto.getCloseTime() != null)
            this.closeTime = dto.getCloseTime();

        // 이 부분은 체크가 업데이트가 어떤식으로 되는지 체크가 필요함
        if(dto.getStoreMenus() != null)
            this.storeMenus = dto.getStoreMenus();
    }

    public void createUniqueStoreCode(String storeUniqueCode) {
        this.storeUniqueCode = storeUniqueCode;
    }

    public void createStoreId(String storeUniqueId) {
        this.storeId = storeUniqueId;
    }

}
