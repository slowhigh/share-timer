package com.sharetimer.apiservice.service.command;

public record GetTimerCommand(String timerId, String ownerToken) {
}
