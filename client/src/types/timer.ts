/**
 * 공통 응답 포맷
 */
export interface BaseRes<T> {
  statusCode: string;
  statusName: string;
  data: T | null;
}

/**
 * 에러 응답
 */
export interface ErrorResponse {
  statusCode: string;
  statusName: string;
  message: string;
}

/**
 * POST /timers
 * 타이머 생성 요청
 */
export interface TimerCreateRequest {
  targetTime: string;
}

/**
 * POST /timers
 * 타이머 생성 응답 데이터
 */
export interface TimerCreateResponse {
  timerId: string;
  ownerToken: string;
}

/**
 * GET /timers/{timerId}
 * 타임스탬프 정보
 */
export interface TimestampInfoResponse {
  targetTime: string;
  capturedAt: string;
}

/**
 * GET /timers/{timerId}
 * 타이머 상세 정보 응답 데이터
 */
export interface TimerInfoResponse {
  updatedAt: string;
  targetTime: string;
  serverTime: string;
  timestamps: TimestampInfoResponse[];
  isOwner: boolean;
}

/**
 * PUT /timers/{timerId}
 * 타이머 업데이트 요청
 */
export interface TimerUpdateRequest {
  requestTime: string;
  targetTime: string;
}

/**
 * POST /timers/{timerId}/timestamps
 * 타임스탬프 추가 요청
 */
export interface TimerAddTimestampRequest {
  capturedAt: string;
}

/**
 * SSE event: targetTimeUpdate
 * 기준 시각 변경 이벤트 데이터
 */
export interface TargetTimeUpdateEventData {
  updatedAt: string;
  serverTime: string;
  newTargetTime: string;
}

/**
 * SSE event: timestampAdd
 * 타임스탬프 추가 이벤트 데이터
 */
export interface TimestampAddedEventData {
  targetTime: string;
  capturedAt: string;
}