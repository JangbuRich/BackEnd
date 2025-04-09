package com.jangburich.domain.team.dto.request;

public record RegisterTeamRequest(
    String teamType,
    String teamName,
    String description,
    String teamLeaderAccountNumber,
    String bankName
) {
}
