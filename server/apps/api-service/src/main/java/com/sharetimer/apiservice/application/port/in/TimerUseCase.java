package com.sharetimer.apiservice.application.port.in;

import com.sharetimer.apiservice.adapter.in.web.dto.TimerCreateRes;
import com.sharetimer.apiservice.adapter.in.web.dto.TimerInfoRes;
import com.sharetimer.apiservice.application.port.in.command.AddTimestampCommand;
import com.sharetimer.apiservice.application.port.in.command.CreateTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.DeleteTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.GetTimerCommand;
import com.sharetimer.apiservice.application.port.in.command.UpdateTimerCommand;

public interface TimerUseCase {

  TimerCreateRes createTimer(CreateTimerCommand cmd);

  TimerInfoRes getTimerInfo(GetTimerCommand cmd);

  void deleteTimer(DeleteTimerCommand cmd);

  void updateTimer(UpdateTimerCommand cmd);

  void addTimestamp(AddTimestampCommand cmd);
}
