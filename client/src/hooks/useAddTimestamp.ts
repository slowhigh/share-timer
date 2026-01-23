import axiosInstance from "@/lib/axios";
import { getCurrentIsoDateTime } from "@/lib/utils";
import { ErrorResponse, TimerAddTimestampRequest } from "@/types/timer";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { toast } from "sonner";

const addTimestampFn = async (params: { timerId: string; requestBody: TimerAddTimestampRequest }) => {
  await axiosInstance.post(`/timers/${params.timerId}/timestamps`, params.requestBody);
};

export const useAddTimestamp = (timerId: string | null) => {
  const { mutate, isPending: isAdding } = useMutation({
    mutationFn: (requestBody: TimerAddTimestampRequest) => {
      if (!timerId) throw new Error("Timer ID is missing.");
      return addTimestampFn({ timerId, requestBody });
    },
    onError: (err: AxiosError<ErrorResponse>) => {
      const message = err.response?.data?.message || "Failed to add timestamp.";
      toast.error(message);
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
