package com.jangburich.presentation.team.dto.request;

public record RegisterTeamRequest(
    String teamType,
    String teamName,
    String description,
    String teamLeaderAccountNumber,
    String bankName
) {
}
