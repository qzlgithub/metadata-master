package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;

public interface RemoteClientService
{
    UserDTO userLogin(String username, String password);

    ResultDTO changeUserPassword(Long userId, String orgPassword, String newPassword);

    MessageListDTO getClientMessage(Long clientId, Page page);

    ResultDTO setSubUserDeleted(Long primaryUserId, Long subUserId);

    UserListDTO getSubUserList(Long clientId, Long primaryUserId);

    ResultDTO addAccount(Long primaryAccountId, String username, String password, String name, String phone);

    UserDTO changeStatus(Long primaryAccountId, Long clientUserId);

    UserDTO getAccountByUserId(Long clientUserId);

    UserDTO editChildAccount(Long primaryAccountId, Long clientUserId, String username, String password, String name,
            String phone, Integer enabled);

    CredentialDTO getUserCredential(Long userId, String password, Long productId);

    ResultDTO saveUserCredential(Long userId, Long productId, String appKey, String reqHost);
}
