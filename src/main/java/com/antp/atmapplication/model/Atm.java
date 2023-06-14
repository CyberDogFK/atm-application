package com.antp.atmapplication.model;

import jakarta.persistence.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "atm_balances",
            joinColumns = @JoinColumn(name = "balance_id"),
            inverseJoinColumns = @JoinColumn(name = "atm_id"))
    private List<AtmBalance> balanceList;

    public Atm(String address, List<AtmBalance> balanceList) {
        this.address = address;
        this.balanceList = balanceList;
    }
}
