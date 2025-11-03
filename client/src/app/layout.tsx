import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Providers from "@/components/providers";
import { Suspense } from "react";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Share Timer",
  description: "Share Timer",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <Providers>
          <Suspense fallback={<div className="flex flex-col items-center justify-center min-h-screen">로딩 중...</div>}>
            {children}
          </Suspense>
        </Providers>
      </body>
    </html>
  );
}
