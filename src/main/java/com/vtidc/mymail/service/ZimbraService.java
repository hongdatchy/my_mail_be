package com.vtidc.mymail.service;

import com.vtidc.mymail.config.soap.SoapLoggerInterceptor;
import com.vtidc.mymail.config.soap.SoapSecurityInterceptorService;
import com.vtidc.mymail.dto.search.SearchAccountRequest;
import com.vtidc.mymail.dto.search.SearchDistributionListRequest;
import com.vtidc.mymail.dto.zimbra.*;
import com.vtidc.mymail.ultis.CommonUtils;
import com.vtidc.mymail.ultis.DatetimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Service
@AllArgsConstructor
public class ZimbraService {


    private WebServiceTemplate webServiceTemplate;

    private WebServiceTemplate webServiceTemplateAccount;

    private final SoapLoggerInterceptor soapLoggerInterceptor;

    private final SoapSecurityInterceptorService soapSecurityInterceptorServiceAccount;

    private static final String ZIMBRA_ADMIN_URL = "https://mail.example.com:7071/service/admin/soap";

    public SearchDirectoryResponse searchAccounts(SearchAccountRequest searchAccountRequest) {

        SearchDirectoryRequest req = new SearchDirectoryRequest();
        req.setOffset(searchAccountRequest.getPage() * searchAccountRequest.getSize());
        req.setLimit(searchAccountRequest.getSize());
        req.setApplyCos(false);
        req.setApplyConfig(false);
        req.setAttrs("displayName,zimbraId,cn,uid,zimbraCOSId,zimbraAccountStatus,zimbraLastLogonTimestamp,description,zimbraIsSystemAccount,zimbraIsDelegatedAdminAccount,zimbraIsAdminAccount,zimbraMailStatus,zimbraIsAdminGroup");
        req.setTypes("accounts");

        StringBuilder query = new StringBuilder();
        query.append("(description=type:")
                .append(searchAccountRequest.getType())
                .append(";*)(!(zimbraIsAdminAccount=TRUE))");

        if (searchAccountRequest.getTagId() != null) {
            query.append("(description=*tagId:")
                    .append(searchAccountRequest.getTagId())
                    .append(";)");
        }
        if (searchAccountRequest.getKeyword() != null && !searchAccountRequest.getKeyword().isBlank()) {
            query.append("(|(mail=*")
                    .append(searchAccountRequest.getKeyword())
                    .append("*)(displayName=*")
                    .append(searchAccountRequest.getKeyword())
                    .append("*))");
        }
        req.setQuery(query.toString());

        if (searchAccountRequest.getSortBy() != null && searchAccountRequest.getSortAscending() != null
        && !searchAccountRequest.getSortBy().isBlank() && !searchAccountRequest.getSortAscending().isBlank()) {
            req.setSortBy(searchAccountRequest.getSortBy());
            req.setSortAscending(searchAccountRequest.getSortAscending().equals(Sort.Direction.ASC.name())? "1": "0");
        }

        SearchDirectoryResponse response = (SearchDirectoryResponse) webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, req);
        if (response != null && response.getAccounts() != null) {
            for (Account account : response.getAccounts()) {
                if (account.getAttrs() != null) {
                    for (Attr attr : account.getAttrs()) {
                        if ("displayName".equals(attr.getName())) {
                            account.setDisplayName(attr.getValue());
                            account.setAttrs(null);
                            break;
                        }
                    }
                    account.setAttrs(null);
                }
            }
        }
        return response;
    }

    public SearchDirectoryResponse searchDistributionList(SearchDistributionListRequest searchAccountRequest) {

        SearchDirectoryRequest req = new SearchDirectoryRequest();
        req.setOffset(searchAccountRequest.getPage() * searchAccountRequest.getSize());
        req.setLimit(searchAccountRequest.getSize());
        req.setApplyCos(false);
        req.setApplyConfig(false);
        req.setAttrs("zimbraCreateTimestamp,displayName,zimbraId,uid,zimbraCOSId,zimbraAccountStatus,description");
        req.setTypes("distributionlists");

        StringBuilder query = new StringBuilder();

        if (searchAccountRequest.getTagId() != null) {
            query.append("(description=*tagId:")
                    .append(searchAccountRequest.getTagId())
                    .append(";)");
        }
        if (searchAccountRequest.getKeyword() != null && !searchAccountRequest.getKeyword().isBlank()) {
            query.append("(|(mail=*")
                    .append(searchAccountRequest.getKeyword())
                    .append("*)(displayName=*")
                    .append(searchAccountRequest.getKeyword())
                    .append("*))");
        }
        req.setQuery(query.toString());

        if (searchAccountRequest.getSortBy() != null && searchAccountRequest.getSortAscending() != null
                && !searchAccountRequest.getSortBy().isBlank() && !searchAccountRequest.getSortAscending().isBlank()) {
            req.setSortBy(searchAccountRequest.getSortBy());
            req.setSortAscending(searchAccountRequest.getSortAscending().equals(Sort.Direction.ASC.name())? "1": "0");
        }

        SearchDirectoryResponse response = (SearchDirectoryResponse) webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, req);
        if (response != null && response.getDistributionLists() != null) {
            for (DistributionList distributionList : response.getDistributionLists()) {
                if (distributionList.getAttrs() != null) {
                    for (Attr attr : distributionList.getAttrs()) {
                        if ("displayName".equals(attr.getName())) {
                            distributionList.setDisplayName(attr.getValue());
                        }
                        if ("zimbraCreateTimestamp".equals(attr.getName())) {
                            distributionList.setZimbraCreateTimestamp(DatetimeUtils.convertDateTime(attr.getValue()));
                        }
                        if ("description".equals(attr.getName())) {
                            distributionList.setTagId(CommonUtils.extractTagId(attr.getValue()));
                        }
                    }
                    distributionList.setAttrs(null);
                }
            }
        }
        return response;
    }

    public CreateAccountResponse createEmailAccount(CreateAccountRequest createEmailAccountRequest) throws RuntimeException {
        try {
            return (CreateAccountResponse)webServiceTemplate.
                    marshalSendAndReceive(ZIMBRA_ADMIN_URL, createEmailAccountRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyEmailAccount(ModifyAccountRequest modifyAccountRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, modifyAccountRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changePasswordEmailAccount(ChangePasswordRequest changePasswordRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, changePasswordRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void renameEmailAccount(RenameAccountRequest renameAccountRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, renameAccountRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEmailAccount(DeleteAccountRequest deleteAccountRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, deleteAccountRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SendMsgResponse sendMsg(SendMsgRequest sendMsgRequest) throws RuntimeException {
        try {
            soapSecurityInterceptorServiceAccount.setAccount(sendMsgRequest.getMessage().getEmailAddresses().get(0).getAddress());
            webServiceTemplateAccount.setInterceptors(new ClientInterceptor[]{
                    soapSecurityInterceptorServiceAccount,
                    soapLoggerInterceptor,
            });
            return (SendMsgResponse)webServiceTemplateAccount.
                    marshalSendAndReceive(ZIMBRA_ADMIN_URL, sendMsgRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CreateDistributionListResponse createDistributionList(CreateDistributionListRequest createDistributionListRequest) throws RuntimeException {
        try {
            return (CreateDistributionListResponse)webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, createDistributionListRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyDistributionList(ModifyDistributionListRequest modifyDistributionListRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, modifyDistributionListRequest);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addDistributionListMember(AddDistributionListMemberRequest addDistributionListMemberRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, addDistributionListMemberRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding member to distribution list", e);
        }
    }

    public void removeDistributionListMember(RemoveDistributionListMemberRequest removeDistributionListMemberRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, removeDistributionListMemberRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing member from distribution list", e);
        }
    }

    public void renameDistributionList(RenameDistributionListRequest renameDistributionListRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, renameDistributionListRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error while renaming distribution list", e);
        }
    }

    public void deleteDistributionList(DeleteDistributionListRequest deleteDistributionListRequest) throws RuntimeException {
        try {
            webServiceTemplate.marshalSendAndReceive(ZIMBRA_ADMIN_URL, deleteDistributionListRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error while delete distribution list", e);
        }
    }

}
