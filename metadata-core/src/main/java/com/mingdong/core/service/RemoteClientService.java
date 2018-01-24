package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ClientAccountDTO;
import com.mingdong.core.model.dto.ClientDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ClientListDTO;
import com.mingdong.core.model.dto.ClientOperateInfoListDTO;
import com.mingdong.core.model.dto.ClientOperateLogDTO;
import com.mingdong.core.model.dto.ClientProductDTO;
import com.mingdong.core.model.dto.ClientUserDTO;
import com.mingdong.core.model.dto.ClientUserListDTO;
import com.mingdong.core.model.dto.CredentialDTO;
import com.mingdong.core.model.dto.DictIndustryDTO;
import com.mingdong.core.model.dto.DictIndustryListDTO;
import com.mingdong.core.model.dto.MessageListDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.model.dto.UserDTO;
import com.mingdong.core.model.dto.UserListDTO;

import java.util.Date;
import java.util.List;

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

    UserDTO findByUsername(String username);

    ClientInfoListDTO getSimilarCorpByName(String corpName, Long clientId);

    DictIndustryListDTO getByParentAndStatus(Long parentIndustryId, Integer trueOrFalse);

    ClientInfoListDTO getClinetInfoListBy(Integer enabled, String account, String cropName, String shortName,
            List<Long> industryIdList, Page page);

    ResultDTO saveClientUser(ClientUserDTO clientUser);

    ResultDTO saveClient(ClientDTO client);

    ClientDTO getClientByClientId(Long clientId);

    ClientUserDTO getClientUserByUserId(Long userId);

    ResultDTO updateClientUserByUserId(ClientUserDTO clientUser);

    ClientAccountDTO getClientAccountByClientId(Long clientId);

    ResultDTO updateClientAccountById(ClientAccountDTO clientAccountDTO);

    ResultDTO saveClientAccount(ClientAccountDTO clientAccountDTO);

    ResultDTO updateClientById(ClientDTO clientDTO);

    DictIndustryDTO getDictIndustryById(Long industryId);

    ClientUserListDTO getListByClientAndStatus(Long clientId, Integer enabled, Integer deleted);

    ClientUserListDTO getClientUserListByClientIds(List<Long> clientIdList);

    ResultDTO saveClientOperateLogList(List<ClientOperateLogDTO> logList);

    ResultDTO updateClientUserStatusByIds(Integer enabled, Date date, List<Long> idList);

    ResultDTO setClientDeleted(List<Long> idList);

    ClientListDTO getClientListByIds(List<Long> idList);

    ResultDTO resetPasswordByIds(String pwd,List<Long> idList);

    ClientProductDTO getClientProductById(Long clientProductId);

    ResultDTO updateClientUserSkipNull(ClientUserDTO clientUserDTO);

    ClientProductDTO getClientProductByClientAndProduct(Long clientId, Long productId);

    ResultDTO saveClientProduct(ClientProductDTO clientProductDTO);

    ResultDTO updateClientProductSkipNull(ClientProductDTO cp);

    ClientOperateInfoListDTO getClientOperateInfoListByUserId(Long primaryUserId, Page page);
}
