import axiosInstance from "@/lib/axios";
import { LOCAL_STORAGE_TIMER_ID_KEY, LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY } from "@/lib/constants";
import { BaseRes, ErrorResponse, TimerCreateRequest, TimerCreateResponse } from "@/types/timer";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";

const createTimerFn = async (data: TimerCreateRequest): Promise<TimerCreateResponse> => {
  const response = await axiosInstance.post<BaseRes<TimerCreateResponse>>("/timers", data);
  if (!response.data.data) {
    throw new Error("No timer creation response data.");
  }
  return response.data.data;
};

export const useCreateTimer = () => {
  const router = useRouter();

  const {
    mutate: createTimer,
    isPending: isLoading,
    error,
  } = useMutation<TimerCreateResponse, AxiosError<ErrorResponse>, TimerCreateRequest>({
    mutationFn: createTimerFn,
    onSuccess: (data) => {
      const { timerId, ownerToken } = data;
      localStorage.setItem(LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY, ownerToken);
      localStorage.setItem(LOCAL_STORAGE_TIMER_ID_KEY, timerId);
      router.push(`/live?id=${timerId}`);
    },
    onError: (err) => {
      const message = err.response?.data?.message || "Unknown error occurred.";
      alert(`Failed to create timer: ${message}`);
      console.error("API call failed:", err);
    },
  });

  return { createTimer, isLoading, error };
};
