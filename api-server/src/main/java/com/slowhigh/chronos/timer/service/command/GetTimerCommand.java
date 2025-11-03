package com.slowhigh.chronos.timer.service.command;

public record GetTimerCommand(String timerId, String ownerToken) {
}
