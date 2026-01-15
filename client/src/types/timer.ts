/**
 * Common Response Format
 */
export interface BaseRes<T> {
  statusCode: string;
  statusName: string;
  data: T | null;
}

/**
 * Error Response
 */
export interface ErrorResponse {
  statusCode: string;
  statusName: string;
  message: string;
}

/**
 * POST /timers
 * Request to create a timer
 */
export interface TimerCreateRequest {
  targetTime: string;
}

/**
 * POST /timers
 * Timer creation response data
 */
export interface TimerCreateResponse {
  timerId: string;
  ownerToken: string;
}

/**
 * GET /timers/{timerId}
 * Timestamp Information
 */
export interface TimestampInfoResponse {
  targetTime: string;
  capturedAt: string;
}

/**
 * GET /timers/{timerId}
 * Timer detail response data
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
 * Request to update timer
 */
export interface TimerUpdateRequest {
  requestTime: string;
  targetTime: string;
}

/**
 * POST /timers/{timerId}/timestamps
 * Request to add timestamp
 */
export interface TimerAddTimestampRequest {
  capturedAt: string;
}

/**
 * SSE event: targetTimeUpdate
 * Target time update event data
 */
export interface TargetTimeUpdateEventData {
  updatedAt: string;
  serverTime: string;
  newTargetTime: string;
}

/**
 * SSE event: timestampAdd
 * Timestamp added event data
 */
export interface TimestampAddedEventData {
  targetTime: string;
  capturedAt: string;
}