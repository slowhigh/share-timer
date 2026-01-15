"use client";

import { Button } from "@/components/button";
import { Card } from "@/components/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/dialog";
import { Input } from "@/components/input";
import { Label } from "@/components/label";
import { useAddTimestamp } from "@/hooks/useAddTimestamp";
import { useTimer } from "@/hooks/useTimer";
import { useUpdateTimer } from "@/hooks/useUpdateTimer";
import { DATETIME_LOCAL_REGEX, MSG_INVALID_DATE_FORMAT, MSG_SHARE_LINK_COPIED } from "@/lib/constants";
import { formatDuration, formatIsoDateTime } from "@/lib/utils";
import { Clock, Edit, Save, Share2 } from "lucide-react";
import { useSearchParams } from "next/navigation";
import { useState } from "react";

export default function LivePage() {
  const searchParams = useSearchParams();
  const timerId = searchParams.get("id");
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const { timerInfo, isLoading, error, remainingTime, now, isTimerEnded } = useTimer(timerId);
  const { addTimestamp, isAdding } = useAddTimestamp(timerId);

  const [newTime, setNewTime] = useState("");
  const { updateTimer, isUpdating } = useUpdateTimer(timerId, () => setIsDialogOpen(false));

  if (isLoading) return <div className="flex flex-col items-center justify-center min-h-screen">Loading...</div>;
  if (error)
    return (
      <div className="flex flex-col items-center justify-center min-h-screen text-red-500">Error: {error.message}</div>
    );
  if (!timerInfo)
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">Timer information not found.</div>
    );

  const handleShare = () => {
    navigator.clipboard.writeText(window.location.href);
    alert(MSG_SHARE_LINK_COPIED);
  };

  const handleUpdateTimer = () => {
    if (!DATETIME_LOCAL_REGEX.test(newTime)) {
      alert(MSG_INVALID_DATE_FORMAT);
      return;
    }
    updateTimer(newTime + "Z");
  };

  return (
    <div className="flex flex-col justify-center min-h-screen">
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-8">
        <div className="max-w-4xl mx-auto space-y-6">
          {/* Header */}
          <div className="text-center space-y-2">
            <div className="flex items-center justify-center gap-2">
              <Clock className="w-8 h-8 text-indigo-600" />
              <h1 className="text-indigo-900">Share your timer.</h1>
            </div>
          </div>

          {/* Main Timer Card */}
          <Card className="p-8 shadow-lg">
            <div className="space-y-6">
              {/* Target Time */}
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label className="text-gray-600">Target Time</Label>
                  <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                    <DialogTrigger asChild>
                      <Button
                        variant="outline"
                        size="sm"
                        className="cursor-pointer"
                        onClick={() => setNewTime(formatIsoDateTime(timerInfo.targetTime).slice(0, -1))}
                      >
                        <Edit className="w-4 h-4 mr-2" />
                        Edit Target Time
                      </Button>
                    </DialogTrigger>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle>Edit Target Time</DialogTitle>
                      </DialogHeader>
                      <div className="space-y-4 pt-4">
                        <div className="space-y-2">
                          <Label>Target Time (ISO 8601)</Label>
                          <Input
                            type="text"
                            value={newTime}
                            disabled={isUpdating}
                            onChange={(e) => setNewTime(e.target.value)}
                            placeholder="YYYY-MM-DDTHH:mm:ss"
                          />
                        </div>
                        <Button onClick={handleUpdateTimer} disabled={isUpdating} className="w-full">
                          {isUpdating ? "Saving..." : "Save"}
                        </Button>
                      </div>
                    </DialogContent>
                  </Dialog>
                </div>
                <div className="text-2xl text-indigo-600 font-mono">{formatIsoDateTime(timerInfo.targetTime)}</div>
              </div>

              {/* Current Time */}
              <div className="space-y-2">
                <Label className="text-gray-600">Current Time</Label>
                <div className="text-2xl text-gray-700 font-mono">{now} (UTC+0)</div>
              </div>

              {/* Remaining Time */}
              <div className="space-y-2">
                <Label className="text-gray-600">Remaining Time</Label>
                <div className="text-5xl font-mono text-green-600">{remainingTime}</div>
              </div>

              {/* Action Buttons */}
              <div className="flex gap-3 pt-4">
                <Button onClick={addTimestamp} disabled={isAdding || isTimerEnded} className="flex-1 cursor-pointer">
                  <Save className="w-4 h-4 mr-2" />
                  {isAdding ? "Saving..." : "Save Timestamp"}
                </Button>
                <Button
                  onClick={handleShare}
                  disabled={isTimerEnded}
                  variant="outline"
                  className="flex-1 cursor-pointer"
                >
                  <Share2 className="w-4 h-4 mr-2" />
                  Copy Share Link
                </Button>
              </div>
            </div>
          </Card>

          {/* Timestamp List */}
          {timerInfo.timestamps.length > 0 && (
            <Card className="p-6 shadow-lg">
              <h2 className="mb-4 text-gray-900">Saved Timestamps</h2>
              <div className="space-y-2">
                {timerInfo.timestamps.map((ts, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
                  >
                    <span className="font-mono text-gray-700">
                      {formatDuration(
                        Math.max(0, new Date(ts.targetTime).getTime() - new Date(ts.capturedAt).getTime())
                      )}{" "}
                      ({formatIsoDateTime(ts.capturedAt)})
                    </span>
                  </div>
                ))}
              </div>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
