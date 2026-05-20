package org.example.movie_rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "store")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_staff_id", nullable = false, unique = true)
    private Staff managerStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "store")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Customer> customers;

    @OneToMany(mappedBy = "store")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Inventory> inventories;

    @OneToMany(mappedBy = "store")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Staff> staffMembers;
}
