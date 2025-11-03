package com.slowhigh.chronos.timer.service;

import com.slowhigh.chronos.timer.dto.TimerCreateRes;
import com.slowhigh.chronos.timer.dto.TimerInfoRes;
import com.slowhigh.chronos.timer.service.command.AddTimestampCommand;
import com.slowhigh.chronos.timer.service.command.CreateTimerCommand;
import com.slowhigh.chronos.timer.service.command.DeleteTimerCommand;
import com.slowhigh.chronos.timer.service.command.GetTimerCommand;
import com.slowhigh.chronos.timer.service.command.UpdateTimerCommand;

public interface TimerService {

  TimerCreateRes createTimer(CreateTimerCommand command);

  TimerInfoRes getTimerInfo(GetTimerCommand command);

  void deleteTimer(DeleteTimerCommand command);

  void updateTimer(UpdateTimerCommand command);

  void addTimestamp(AddTimestampCommand command);
}
