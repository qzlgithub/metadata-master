package com.mingdong.mis.domain;

import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.mis.domain.entity.ClientUser;

public class TransformDTO
{
    public static void userToDTO(ClientUser left, UserDTO right)
    {
        right.setUserId(left.getId());
        right.setClientId(left.getClientId());
        right.setName(left.getName());
        right.setPhone(left.getPhone());
        right.setUsername(left.getUsername());
        right.setEnabled(left.getEnabled());
    }
}
