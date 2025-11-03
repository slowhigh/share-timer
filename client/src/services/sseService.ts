import { TargetTimeUpdateEventData, TimestampAddedEventData } from "@/types/timer";

interface SseEventHandlers {
  onTargetTimeUpdate: (data: TargetTimeUpdateEventData) => void;
  onTimestampAdd: (data: TimestampAddedEventData) => void;
  onTimerEnd: () => void;
  onOpen: () => void;
  onError: () => void;
}

export class SseService {
  private eventSource: EventSource | null = null;
  private readonly timerId: string;
  private readonly handlers: SseEventHandlers;

  constructor(timerId: string, handlers: SseEventHandlers) {
    if (!timerId) {
      throw new Error("Timer ID is required to initialize SseService.");
    }
    this.timerId = timerId;
    this.handlers = handlers;
  }

  public connect() {
    if (this.eventSource) return;

    const url = `${process.env.NEXT_PUBLIC_SSE_BASE_URL}/timers/${this.timerId}`;
    this.eventSource = new EventSource(url);

    this.eventSource.onopen = this.handlers.onOpen;
    this.eventSource.onerror = () => {
      this.handlers.onError();
      this.close();
    };

    this.eventSource.addEventListener("targetTimeUpdate", (event) => {
      const data: TargetTimeUpdateEventData = JSON.parse(event.data);
      this.handlers.onTargetTimeUpdate(data);
    });

    this.eventSource.addEventListener("timestampAdd", (event) => {
      const data: TimestampAddedEventData = JSON.parse(event.data);
      this.handlers.onTimestampAdd(data);
    });

    this.eventSource.addEventListener("timerEnd", () => {
      this.handlers.onTimerEnd();
    });
  }

  public close() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      console.log("SSE connection closed.");
    }
  }
}
