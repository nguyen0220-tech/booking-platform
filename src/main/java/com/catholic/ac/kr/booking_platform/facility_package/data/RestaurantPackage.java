package com.catholic.ac.kr.booking_platform.facility_package.data;

import com.catholic.ac.kr.booking_platform.facility.data.resraurant.Restaurant;
import com.catholic.ac.kr.booking_platform.facility.data.resraurant.RestaurantMenu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(
        name = "restaurant_package",
        indexes = @Index(columnList = "id")
)
@DiscriminatorValue("RESTAURANT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantPackage extends FacilityPackage{
    @Column(nullable = false)
    private int maxCapacity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_package_menu", // table name
            joinColumns = @JoinColumn(name = "package_menu_id"),// FK
            inverseJoinColumns = @JoinColumn(name = "menu_id")// FK
    )
    private Set<RestaurantMenu> menus;
}
