package br.com.fabex.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Payment {
    private String id;
    private double amount;
}
