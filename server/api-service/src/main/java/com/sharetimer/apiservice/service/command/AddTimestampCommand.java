package com.sharetimer.apiservice.service.command;

import java.time.Instant;

public record AddTimestampCommand(String timerId, Instant capturedAt) {
}
