import axiosInstance from "@/lib/axios";
import { LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY, OWNER_TOKEN_HEADER_KEY } from "@/lib/constants";
import { getCurrentIsoDateTime } from "@/lib/utils";
import { ErrorResponse, TimerUpdateRequest } from "@/types/timer";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { toast } from "sonner";

const updateTimerFn = async (params: { timerId: string; newTargetTime: string }) => {
  const ownerToken = localStorage.getItem(LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY);
  if (!ownerToken) {
    toast.error("Cannot update without owner token.");
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
      if (!timerId) throw new Error("Timer ID is missing.");
      return updateTimerFn({ timerId, newTargetTime });
    },
    onSuccess,
    onError: (err: AxiosError<ErrorResponse>) => {
      const message = err.response?.data?.message || "Failed to update timer.";
      toast.error(message);
      console.error("Failed to update timer:", err);
    },
  });

  return { updateTimer: mutate, isUpdating };
};
