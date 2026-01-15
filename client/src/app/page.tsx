"use client";

import { Button } from "@/components/button";
import { Card } from "@/components/card";
import { useCreateTimer } from "@/hooks/useCreateTimer";
import { DATETIME_LOCAL_REGEX, MSG_INVALID_DATE_FORMAT } from "@/lib/constants";
import { TextField } from "@radix-ui/themes";
import { Clock } from "lucide-react";
import { useState } from "react";

export default function Home() {
  const getInitialTime = () => {
    const date = new Date();
    date.setUTCHours(date.getUTCHours() + 1);
    return date.toISOString().split(".")[0];
  };

  const [time, setTime] = useState(getInitialTime);
  const { createTimer, isLoading, error } = useCreateTimer();

  const handleCreate = () => {
    if (!DATETIME_LOCAL_REGEX.test(time)) {
      alert(MSG_INVALID_DATE_FORMAT);
      return;
    }
    createTimer({ targetTime: time + "Z" });

    console.log("Create Timer Error:", error);
  };

  return (
    <div className="flex flex-col justify-center min-h-screen">
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-8">
        <div className="max-w-4xl mx-auto space-y-6">
          {/* Header */}
          <div className="text-center space-y-2">
            <div className="flex items-center justify-center gap-2">
              <Clock className="w-8 h-8 text-indigo-600" />
              <h1 className="text-indigo-900">Please create a timer.</h1>
            </div>
          </div>

          {/* Main Timer Card */}
          <Card className="flex flex-col justify-center p-8 shadow-lg min-h-60">
            <div className="mx-auto">
              <TextField.Root
                className="text-2xl text-gray-700 font-mono"
                value={time}
                onChange={(e) => setTime(e.target.value)}
                autoFocus
              />

              <Button onClick={handleCreate} disabled={isLoading} className="w-full mt-6">
                {isLoading ? "Creating..." : "Create"}
              </Button>
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
}
