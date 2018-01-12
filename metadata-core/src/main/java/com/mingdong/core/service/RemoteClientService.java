package com.mingdong.core.service;

import com.mingdong.core.model.dto.BaseDTO;
import com.mingdong.core.model.dto.HomeDTO;
import com.mingdong.core.model.dto.UserDTO;

public interface RemoteClientService
{
    UserDTO userLogin(String username, String password);

    BaseDTO changeUserPassword(Long userId, String orgPassword, String newPassword);

    HomeDTO getUserHomeData(Long clientId, Long clientUserId);
}
