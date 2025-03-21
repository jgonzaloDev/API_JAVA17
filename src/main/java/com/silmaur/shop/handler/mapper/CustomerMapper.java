package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.CustomerDTO;
import com.silmaur.shop.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

  CustomerDTO toDto(Customer customer);

  Customer toEntity(CustomerDTO dto);
}