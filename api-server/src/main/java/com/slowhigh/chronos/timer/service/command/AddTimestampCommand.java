package com.slowhigh.chronos.timer.service.command;

import java.time.Instant;

public record AddTimestampCommand(String timerId, Instant capturedAt) {
}
