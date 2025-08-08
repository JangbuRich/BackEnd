package com.jangburich.domain.store;

import com.jangburich.domain.common.BaseEntity;
import com.jangburich.domain.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_menu")
@Entity
public class StoreMenu extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", updatable = false)
    private Long id;

    @Comment("메뉴 이름")
    @Column(name = "name", columnDefinition = "varchar(100)")
    private String name;

    @Comment("메뉴 사진 url")
    @Column(name = "image_url", nullable = false, columnDefinition = "varchar(500)")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Store store;

    @Column(name= "deleted_at")
    @Comment("삭제 시간")
    private LocalDateTime deletedAt;

    @Builder
    public StoreMenu(String name, String imageUrl, Store store) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.store = store;
    }
}
