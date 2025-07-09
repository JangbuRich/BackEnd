package com.jangburich.domain.store.domain;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreTeam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "point")
    private Integer point;

    @Column(name = "personal_allocated_point")
    private Integer personalAllocatedPoint;

    @Column(name = "remain_point")
    private Integer remainPoint;

    @Column(name = "prepaid_expiration_date")
    private LocalDate prepaidExpirationDate;

    @Column(name = "prepay_count")
    private Integer prepayCount;

    public void updatePersonalAllocatedPoint(Integer point) {
        this.personalAllocatedPoint = point;
    }

    public void useRemainPoint(Integer point) {
        validateRemainPoint(point);
        this.remainPoint -= point;
    }

    private void validateRemainPoint(Integer point) {
        if (remainPoint < point) {
            throw new IllegalArgumentException("남은 포인트보다 큰 급액은 결제할 수 없습니다. 남은 포인트: " + remainPoint);
        }
    }

    public void addPoint(Integer point) {
        this.point += point;
    }

    public void addRemainPoint(Integer point) {
        this.remainPoint += point;
    }

    public void subRemainPoint(Integer point) {
        this.remainPoint -= point;
    }


    @Builder
    public StoreTeam(Store store, Team team, Integer point, Integer personalAllocatedPoint, Integer remainPoint,
                     LocalDate prepaidExpirationDate) {
        this.store = store;
        this.team = team;
        this.point = point;
        this.personalAllocatedPoint = personalAllocatedPoint;
        this.remainPoint = remainPoint;
        this.prepaidExpirationDate = prepaidExpirationDate;
    }

    public static StoreTeam create(Team team, Store store, Integer point) {
        StoreTeam storeTeam = new StoreTeam();
        storeTeam.team = team;
        storeTeam.store = store;
        storeTeam.point = point;
        storeTeam.remainPoint = point;
        storeTeam.personalAllocatedPoint = 0;
        return storeTeam;
    }

    public void recharge(int prepayAmount) {
        this.point += prepayAmount;
        this.remainPoint += prepayAmount;
    }

	public void usePoint(Integer point) {
		this.remainPoint -= point; // TODO 동시성 문제 검토
	}
}