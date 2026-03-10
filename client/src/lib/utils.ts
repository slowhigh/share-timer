/**
 * Milliseconds to HH:mm:ss
 * @param milliseconds milliseconds
 */
export const formatDuration = (milliseconds: number): string => {
  if (milliseconds < 0) milliseconds = 0;

  const totalSeconds = Math.floor(milliseconds / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  return [hours, minutes, seconds].map((unit) => String(unit).padStart(2, "0")).join(":");
};

/**
 * ISO to YYYY-MM-DDTHH:mm:ss in local time
 * @param isoString ISO 8601 string
 */
export const formatIsoDateTime = (isoString: string): string => {
  const date = new Date(isoString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
};

/**
 * Get timezone offset string (e.g., "UTC+9", "UTC-5", "UTC+5:30")
 */
export const getTimezoneOffsetString = (): string => {
  const offset = -new Date().getTimezoneOffset();
  const sign = offset >= 0 ? "+" : "-";
  const absOffset = Math.abs(offset);
  const hours = Math.floor(absOffset / 60);
  const minutes = absOffset % 60;
  return minutes === 0 ? `UTC${sign}${hours}` : `UTC${sign}${hours}:${String(minutes).padStart(2, "0")}`;
};

/**
 * Convert local datetime string (YYYY-MM-DDTHH:mm:ss) to UTC ISO string
 */
export const localDateTimeToUtcIso = (localDateTime: string): string => {
  return new Date(localDateTime).toISOString().split(".")[0] + "Z";
};

/**
 * Current UTC to YYYY-MM-DDTHH:mm:ssZ
 */
export const getCurrentIsoDateTime = (): string => {
  return new Date().toISOString().split(".")[0] + "Z";
};
