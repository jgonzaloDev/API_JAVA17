package com.silmaur.shop.service;

import com.silmaur.shop.dto.UserDTO;
import io.reactivex.rxjava3.core.Single;

public interface UserService {
  Single<UserDTO> createUser(UserDTO userDTO);

}
