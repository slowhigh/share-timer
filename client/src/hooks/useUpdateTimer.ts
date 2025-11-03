import axiosInstance from "@/lib/axios";
import { LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY, OWNER_TOKEN_HEADER_KEY } from "@/lib/constants";
import { getCurrentIsoDateTime } from "@/lib/utils";
import { ErrorResponse, TimerUpdateRequest } from "@/types/timer";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";

const updateTimerFn = async (params: { timerId: string; newTargetTime: string }) => {
  const ownerToken = localStorage.getItem(LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY);
  if (!ownerToken) {
    alert("소유자 토큰이 없어 업데이트할 수 없습니다.");
    return;
  }
  const requestBody: TimerUpdateRequest = {
    targetTime: params.newTargetTime,
    requestTime: getCurrentIsoDateTime(),
  };
  await axiosInstance.put(`/timers/${params.timerId}`, requestBody, {
    headers: { [OWNER_TOKEN_HEADER_KEY]: ownerToken },
  });
};

export const useUpdateTimer = (timerId: string | null, onSuccess: () => void) => {
  const { mutate, isPending: isUpdating } = useMutation({
    mutationFn: (newTargetTime: string) => {
      if (!timerId) throw new Error("Timer ID가 없습니다.");
      return updateTimerFn({ timerId, newTargetTime });
    },
    onSuccess,
    onError: (err: AxiosError<ErrorResponse>) => {
      const message = err.response?.data?.message || "타이머 업데이트에 실패했습니다.";
      alert(message);
      console.error("Failed to update timer:", err);
    },
  });

  return { updateTimer: mutate, isUpdating };
};
