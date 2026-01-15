import axiosInstance from "@/lib/axios";
import { formatDuration, formatIsoDateTime } from "@/lib/utils";
import { SseService } from "@/services/sseService";
import { BaseRes, ErrorResponse, TimerInfoResponse } from "@/types/timer";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useRef, useState } from "react";
import { LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY, OWNER_TOKEN_HEADER_KEY } from "@/lib/constants";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";

const fetchTimerInfo = async (timerId: string): Promise<TimerInfoResponse> => {
  const ownerToken = localStorage.getItem(LOCAL_STORAGE_TIMER_OWNER_TOKEN_KEY);
  const headers: { [key: string]: string } = {};
  if (ownerToken) headers[OWNER_TOKEN_HEADER_KEY] = ownerToken;

  const { data } = await axiosInstance.get<BaseRes<TimerInfoResponse>>(`/timers/${timerId}`, { headers });
  if (!data.data) throw new Error("Timer info not found.");

  data.data.timestamps.sort((a, b) => new Date(a.capturedAt).getTime() - new Date(b.capturedAt).getTime());
  return data.data;
};

export const useTimer = (timerId: string | null) => {
  const router = useRouter();
  const [liveTimerInfo, setLiveTimerInfo] = useState<TimerInfoResponse | null>(null);
  const lastUpdatedAt = useRef<string | null>(null);
  const [isTimerEnded, setIsTimerEnded] = useState(false);

  const {
    data: initialData,
    isLoading,
    error,
  } = useQuery<TimerInfoResponse, AxiosError<ErrorResponse>>({
    queryKey: ["timer", timerId],
    queryFn: () => fetchTimerInfo(timerId!),
    enabled: !!timerId,
  });

  useEffect(() => {
    if (error) {
      if (error.response?.status === 404) {
        alert("Timer has ended or does not exist.");
        router.push("/");
      }
    }
  }, [error, router]);

  useEffect(() => {
    if (initialData) {
      setLiveTimerInfo(initialData);
      lastUpdatedAt.current = initialData.updatedAt;
    }
  }, [initialData]);

  useEffect(() => {
    if (!timerId || !liveTimerInfo) return;

    const sseService = new SseService(timerId, {
      onOpen: () => console.log("SSE connection established."),
      onError: () => console.error("SSE Error!"),
      onTargetTimeUpdate: (data) => {
        if (!lastUpdatedAt.current || new Date(data.updatedAt) > new Date(lastUpdatedAt.current)) {
          lastUpdatedAt.current = data.updatedAt;
          setLiveTimerInfo((prev) =>
            prev
              ? {
                  ...prev,
                  updatedAt: data.updatedAt,
                  serverTime: data.serverTime,
                  targetTime: data.newTargetTime,
                }
              : null
          );
        }
      },
      onTimestampAdd: (data) => {
        setLiveTimerInfo((prev) => {
          if (!prev) return null;
          const updatedTimestamps = [...prev.timestamps, data].sort(
            (a, b) => new Date(a.capturedAt).getTime() - new Date(b.capturedAt).getTime()
          );
          return { ...prev, timestamps: updatedTimestamps };
        });
      },
      onTimerEnd: () => {
        alert("Timer ended");
        setIsTimerEnded(true);
        sseService?.close();
      },
    });

    sseService.connect();

    return () => {
      sseService.close();
    };
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [timerId, liveTimerInfo?.updatedAt]);

  const [remainingTime, setRemainingTime] = useState("00:00:00");
  const [now, setNow] = useState("");

  useEffect(() => {
    if (!liveTimerInfo) return;

    if (isTimerEnded) {
      setRemainingTime("00:00:00");
      return;
    }

    const timeOffset = new Date(liveTimerInfo.serverTime).getTime() - new Date().getTime();
    const interval = setInterval(() => {
      const correctedNow = new Date(new Date().getTime() + timeOffset);
      const target = new Date(liveTimerInfo.targetTime);
      const diff = target.getTime() - correctedNow.getTime();

      setNow(formatIsoDateTime(correctedNow.toISOString()));

      if (diff > 0) {
        setRemainingTime(formatDuration(diff));
      } else {
        setRemainingTime("00:00:00");
        setIsTimerEnded(true);
      }
    }, 1000);

    return () => clearInterval(interval);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [liveTimerInfo?.serverTime, liveTimerInfo?.targetTime, isTimerEnded]);

  return {
    timerInfo: liveTimerInfo,
    isLoading,
    error,
    remainingTime,
    now,
    isTimerEnded,
  };
};
