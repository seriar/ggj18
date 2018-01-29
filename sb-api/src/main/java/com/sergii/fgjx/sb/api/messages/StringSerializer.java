package com.sergii.fgjx.sb.api.messages;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.sergii.fgjx.sb.api.messages.requests.*;
import com.sergii.fgjx.sb.api.messages.responses.*;
import com.sergii.fgjx.sb.api.Messages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StringSerializer implements MessageSerializer {

    public Request deserializeRequest(byte[] payload) {
        try {
            Messages.Request req = Messages.Request.parseFrom(payload);
            switch (req.getOptCase()){
                case LISTSESSION:
                    return new SessionListRequest(
                            req.getRequseterId(),
                            req.getId(),
                            req.getListSession().getSize());
                case JOINSESSION:
                    return new SessionJoinRequest(
                            req.getRequseterId(),
                            req.getId(),
                            req.getJoinSession().getSessionId());
                case CREATESESSION:
                    return new SessionCreationRequest(
                            req.getRequseterId(),
                            req.getId());
                case LISTPLAYERS:
                    return new PlayerListRequest(
                            req.getRequseterId(),
                            req.getId(),
                            req.getListPlayers().getSessionId());
                case ROLE:
                    return new RoleRequest(
                            req.getRequseterId(),
                            req.getId());
                case WEAPONREQUEST:
                    return new SelectWeaponRequest(
                        req.getRequseterId(),
                        req.getId(),
                        req.getWeaponRequest().getWeapon());
                case TRANSMISSION:
                    return new CodeTransmissionRequest(
                            req.getRequseterId(),
                            req.getId());
                case ACTIVATION:
                    return new ActivationRequest(
                            req.getRequseterId(),
                            req.getId(),
                            req.getActivation().getCode());
                case REPORT:
                    return new ReportRequest(
                            req.getRequseterId(),
                            req.getId());
                case OPT_NOT_SET:
                    break;
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();

        }
        return null;

    }

    public byte[] serializeRequest(Request request) {
        final Messages.Request.Builder requestBuilder = Messages.Request.newBuilder()
                .setId(request.getId())
                .setRequseterId(request.getRequester());

//        if (request instanceof SessionCreationRequest) {
//
//        } else
        if (request instanceof SessionListRequest) {
            final SessionListRequest req = (SessionListRequest) request;
            requestBuilder.setListSession(
                    Messages.SessionListRequest.newBuilder()
                            .setSize(req.getSize()));

        } else if (request instanceof SessionJoinRequest) {
            final SessionJoinRequest req = (SessionJoinRequest) request;
            requestBuilder.setJoinSession(Messages.SessionJoinRequest.newBuilder().setSessionId(req.getSession()));
        } else if (request instanceof PlayerListRequest) {
            final PlayerListRequest req = (PlayerListRequest) request;
            requestBuilder.setListPlayers(Messages.PlayerListRequest.newBuilder().setSessionId(req.getSessionId()));
        } else if (request instanceof RoleRequest) {
            requestBuilder.setRole(Messages.RoleRequest.newBuilder());
        } else if (request instanceof SelectWeaponRequest) {
            final SelectWeaponRequest req = (SelectWeaponRequest)request;
            requestBuilder.setWeaponRequest(Messages.SelectWeaponRequest.newBuilder()
                    .setWeapon(req.getWeapon()));
        } else if (request instanceof ActivationRequest) {
            final ActivationRequest req = (ActivationRequest) request;
            requestBuilder.setActivation(Messages.ActivationRequest.newBuilder().setCode(req.getCode()));
        } else if (request instanceof CodeTransmissionRequest) {
            requestBuilder.setTransmission(Messages.CodeTransmissionRequest.newBuilder());
        } else if (request instanceof ReportRequest) {
            requestBuilder.setReport(Messages.ReportRequest.newBuilder());

        } else {
            return null;
        }
        return requestBuilder.build().toByteArray();
    }

    public byte[] serializeResponse(Response response) {
        final Messages.Response.Builder responseBuilder = Messages.Response.newBuilder()
                .setId(response.getId());
        if (response instanceof SessionCreationResponse) {
            final SessionCreationResponse res = (SessionCreationResponse) response;
            responseBuilder.setCreateSession(Messages.SessionCreateResponse.newBuilder()
                    .setSessionId(res.getId())
                    .setSuccess(res.isSuccess()));
        } else if (response instanceof SessionListResponse) {
            final SessionListResponse res = (SessionListResponse) response;
            responseBuilder.setListSession(Messages.SessionListResponse.newBuilder()
                    .addAllSesionId(res.getSessionIds()));
        } else if (response instanceof SessionJoinResponse) {
            final SessionJoinResponse res = (SessionJoinResponse) response;
            responseBuilder.setJoinSession(Messages.SessionJoinResponse.newBuilder()
                    .setSuccess(res.isSuccess())
                    .setSessionId(res.getSessionTopic()));
        } else if (response instanceof PlayerListResponse) {
                final PlayerListResponse res = (PlayerListResponse) response;
                responseBuilder.setListPlayers(Messages.PlayerListResponse.newBuilder()
                        .addAllPlayer(res.getPlayers()));
        } else if (response instanceof RoleResponse) {
                final RoleResponse res = (RoleResponse) response;
                responseBuilder.setRole(Messages.RoleResponse.newBuilder()
                        .setMaster(res.isMaster()));
        } else if (response instanceof SelectWeaponResponse) {
            responseBuilder.setWeaponResponse(Messages.WeaponSelectionResponse.newBuilder());
        } else if (response instanceof ActivationResponse) {
            final ActivationResponse res = (ActivationResponse) response;
            responseBuilder.setActivation(Messages.ActivationResponse.newBuilder()
                    .setQuality(res.getQuality())
                    .setDelay(res.getTime()));
        } else if (response instanceof CodeTransmissionResponse) {
            final CodeTransmissionResponse res = (CodeTransmissionResponse) response;
            responseBuilder.setTransmission(Messages.CodeTransmissionResponse.newBuilder()
                    .setCode(res.getCode()));
        } else if (response instanceof ReportResponse) {
            final ReportResponse res = (ReportResponse) response;
            responseBuilder.setReport(Messages.ReportResponse.newBuilder()
                    .setVictory(res.isVictory())
                    .setHealth(res.getHealth())
                    .setTeamHealth(res.getTeamHealth())
                    .setEnemyHealth(res.getEnsdfemyHealth())
                    .setAttackPerformance(res.getAttackPerformance())
                    .setTeamAttackPerformance(res.getTeamAttackPerformance())
                    .setEnemyAttackPerformance(res.getEnemyAttackPerformance())
                    .setDefencePerformance(res.getDefencePerformance())
                    .setTeamDefencePerformance(res.getTeamDefencePerformance())
                    .setEnemyDefencePerformance(res.getEnemyDefencePerformance()));
        } else {
            return null;
        }
        return responseBuilder.build().toByteArray();
    }

    public Response deserializeResponse(byte[] payload) {

        try {
            Messages.Response res = Messages.Response.parseFrom(payload);
            switch (res.getOptCase()) {
                case LISTSESSION:
                    final Set<String> sessionIds = res.getListSession().getSesionIdList().asByteStringList().stream()
                            .map(ByteString::toStringUtf8)
                            .collect(Collectors.toSet());
                    return new SessionListResponse(res.getId(), sessionIds, 0);
                case JOINSESSION:
                    final Messages.SessionJoinResponse joinSession = res.getJoinSession();
                    return new SessionJoinResponse(res.getId(), joinSession.getSessionId(), joinSession.getSuccess());
                case CREATESESSION:
                    final Messages.SessionCreateResponse create = res.getCreateSession();
                    return new SessionCreationResponse(res.getId(), create.getSessionId(), create.getSuccess());
                case LISTPLAYERS:
                    final List<String> playerList =
                            res.getListPlayers().getPlayerList().asByteStringList().stream()
                            .map(ByteString::toStringUtf8)
                            .collect(Collectors.toList());
                    return new PlayerListResponse(res.getId(), playerList);
                case ROLE:
                    final Messages.RoleResponse role = res.getRole();
                    return new RoleResponse(res.getId(), role.getMaster());
                case WEAPONRESPONSE:
                    return new SelectWeaponResponse(res.getId());
                case TRANSMISSION:
                    return new CodeTransmissionResponse(res.getId(), res.getTransmission().getCode());
                case ACTIVATION:
                    final Messages.ActivationResponse activation = res.getActivation();
                    return new ActivationResponse(res.getId(),
                            activation.getQuality(),
                            activation.getDelay());
                case REPORT:
                    break;
                case OPT_NOT_SET:
                default:
                    break;
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
