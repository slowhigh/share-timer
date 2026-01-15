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
 * ISO to YYYY-MM-DDTHH:mm:ssZ
 * @param isoString ISO 8601 string
 */
export const formatIsoDateTime = (isoString: string): string => {
  const date = new Date(isoString);
  const year = date.getUTCFullYear();
  const month = String(date.getUTCMonth() + 1).padStart(2, "0");
  const day = String(date.getUTCDate()).padStart(2, "0");
  const hours = String(date.getUTCHours()).padStart(2, "0");
  const minutes = String(date.getUTCMinutes()).padStart(2, "0");
  const seconds = String(date.getUTCSeconds()).padStart(2, "0");
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}Z`;
};

/**
 * Current UTC to YYYY-MM-DDTHH:mm:ssZ
 */
export const getCurrentIsoDateTime = (): string => {
  return new Date().toISOString().split(".")[0] + "Z";
};
