import axiosInstance from "@/lib/axios";
import { getCurrentIsoDateTime } from "@/lib/utils";
import { ErrorResponse, TimerAddTimestampRequest } from "@/types/timer";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";

const addTimestampFn = async (params: { timerId: string; requestBody: TimerAddTimestampRequest }) => {
  await axiosInstance.post(`/timers/${params.timerId}/timestamps`, params.requestBody);
};

export const useAddTimestamp = (timerId: string | null) => {
  const { mutate, isPending: isAdding } = useMutation({
    mutationFn: (requestBody: TimerAddTimestampRequest) => {
      if (!timerId) throw new Error("Timer ID가 없습니다.");
      return addTimestampFn({ timerId, requestBody });
    },
    onError: (err: AxiosError<ErrorResponse>) => {
      const message = err.response?.data?.message || "타임스탬프 추가에 실패했습니다.";
      alert(message);
      console.error("Failed to add timestamp:", err);
    },
  });

  const addTimestamp = () => {
    const requestBody: TimerAddTimestampRequest = {
      capturedAt: getCurrentIsoDateTime(),
    };
    mutate(requestBody);
  };

  return { addTimestamp, isAdding };
};
