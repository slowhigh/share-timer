package com.sharetimer.apiservice.application.port.in.command;

public record GetTimerCommand(String timerId, String ownerToken) {
}
