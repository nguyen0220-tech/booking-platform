package com.catholic.ac.kr.booking_platform.facility.data.resraurant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "restaurant_menu",
        indexes = @Index(columnList = "restaurant_id, deleted", name = "index_restaurant_id")
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private boolean soldOut;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void create() {
        createdAt = LocalDateTime.now();
        this.deleted = false;
    }

    public void updateMenu(String name, String description, BigDecimal price) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (price != null) this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        // 1. Nếu cùng trỏ đến 1 ô nhớ -> Chắc chắn bằng nhau
        if (this == o) return true;

        // 2. Nếu object kia null hoặc không cùng class (hoặc proxy) -> Không bằng nhau
        if (!(o instanceof RestaurantMenu that)) return false;

        // Nếu id của cả 2 đều khác null thì so sánh id.
        // Nếu 1 trong 2 id bằng null (object mới chưa lưu DB) -> luôn false (vì chưa thể xác định danh tính)
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        // Điều này đảm bảo hashCode không bị thay đổi trước và sau khi object được lưu vào DB (khi id chuyển từ null thành có giá trị).
        return getClass().hashCode();
    }
}
