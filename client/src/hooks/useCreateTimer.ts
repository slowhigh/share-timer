import axiosInstance from "@/lib/axios";
import { LOCAL_STORAGE_TIMER_ID_KEY, LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY } from "@/lib/constants";
import { BaseRes, ErrorResponse, TimerCreateRequest, TimerCreateResponse } from "@/types/timer";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";

const createTimerFn = async (data: TimerCreateRequest): Promise<TimerCreateResponse> => {
  const response = await axiosInstance.post<BaseRes<TimerCreateResponse>>("/timers", data);
  if (!response.data.data) {
    throw new Error("타이머 생성 응답 데이터가 없습니다.");
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
      const message = err.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
      alert(`타이머 생성 실패: ${message}`);
      console.error("API call failed:", err);
    },
  });

  return { createTimer, isLoading, error };
};
