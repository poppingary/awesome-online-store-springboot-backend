package com.gyl.awesome_inc.domain.dto;

import com.gyl.awesome_inc.domain.model.ShipAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateAddressResponse {
    private List<ShipAddress> shipAddressSet;
}