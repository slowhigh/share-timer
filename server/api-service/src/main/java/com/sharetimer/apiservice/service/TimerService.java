package com.sharetimer.apiservice.service;

import com.sharetimer.apiservice.dto.TimerCreateRes;
import com.sharetimer.apiservice.dto.TimerInfoRes;
import com.sharetimer.apiservice.service.command.AddTimestampCommand;
import com.sharetimer.apiservice.service.command.CreateTimerCommand;
import com.sharetimer.apiservice.service.command.DeleteTimerCommand;
import com.sharetimer.apiservice.service.command.GetTimerCommand;
import com.sharetimer.apiservice.service.command.UpdateTimerCommand;

public interface TimerService {

  TimerCreateRes createTimer(CreateTimerCommand cmd);

  TimerInfoRes getTimerInfo(GetTimerCommand cmd);

  void deleteTimer(DeleteTimerCommand cmd);

  void updateTimer(UpdateTimerCommand cmd);

  void addTimestamp(AddTimestampCommand cmd);
}
