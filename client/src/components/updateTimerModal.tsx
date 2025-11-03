"use client";

import { useUpdateTimer } from "@/hooks/useUpdateTimer";
import { DATETIME_LOCAL_REGEX, MSG_INVALID_DATE_FORMAT } from "@/lib/constants";
import { formatIsoDateTime } from "@/lib/utils";
import { useState } from "react";

interface UpdateTimerModalProps {
  timerId: string;
  currentTargetTime: string;
  onClose: () => void;
}

export const UpdateTimerModal = ({ timerId, currentTargetTime, onClose }: UpdateTimerModalProps) => {
  const [newTime, setNewTime] = useState(formatIsoDateTime(currentTargetTime).slice(0, -1));
  const { updateTimer, isUpdating } = useUpdateTimer(timerId, onClose);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!DATETIME_LOCAL_REGEX.test(newTime)) {
      alert(MSG_INVALID_DATE_FORMAT);
      return;
    }
    updateTimer(newTime + "Z");
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-xl w-full max-w-md">
        <h2 className="text-lg font-bold mb-4 text-gray-900 dark:text-white">기준 시각 수정</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            value={newTime}
            onChange={(e) => setNewTime(e.target.value)}
            className="p-2 border rounded-md w-full text-black"
            disabled={isUpdating}
          />
          <div className="mt-6 flex justify-end gap-4">
            <button
              type="button"
              onClick={onClose}
              disabled={isUpdating}
              className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md disabled:opacity-50"
            >
              취소
            </button>
            <button
              type="submit"
              disabled={isUpdating}
              className="px-4 py-2 bg-blue-500 text-white rounded-md disabled:bg-gray-400"
            >
              {isUpdating ? "수정 중..." : "수정"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
