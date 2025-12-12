package com.sharetimer.apiservice.application.port.in.command;

import java.time.Instant;

public record AddTimestampCommand(String timerId, Instant capturedAt) {
}
