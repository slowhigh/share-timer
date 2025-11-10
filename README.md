# 🕒 ShareTimer

**ShareTimer** is a web service that lets you create custom timers and share them instantly with others.  
Whether you're coordinating an online study session, managing a team sprint, or setting a countdown for an event, ShareTimer makes time management collaborative and effortless.

---

## ✨ Features

- 🧭 **Create** — Set up timers in seconds with a clean, intuitive interface.  
- 🔗 **Share** — Generate a unique link to share your timer with anyone, anywhere.  
- ⏱️ **Sync (SSE)** — All timer events (creation, updates, timestamp additions, and expirations) are synchronized to connected clients using Server‑Sent Events (SSE).
- 💻 **Flexible** — Works on any device, no login required.

---

## 💡 Why ShareTimer?

Time isn’t just personal — it’s something we experience together.  
With **ShareTimer**, you can easily align your time with friends, teammates, or anyone you collaborate with.  
Stay in sync. Stay on time.

---

## ⚙️ Architecture Overview

The ShareTimer system is composed of the following main components:
- **Client (Owner / Guest)** — Browser web UI. Opens an SSE stream to receive real‑time events; sends REST requests to modify timers.
- **API Server** — HTTP REST endpoints for creating timers, adding timestamps, updating timers, and finding timers.
- **Realtime Server (SSE gateway)** — Exposes SSE endpoints for clients to subscribe to timer channels. Delivers ordered events, supports reconnection with `Heartbeat`, and acts as the pub/sub bridge.
- **PostgreSQL** — Durable storage for timer records and timestamp history.
- **Redis** — Handles TTL-based expiration, pub/sub for notifying the Realtime Server of events.

---

## 🔀 Sequence Diagrams

### Timer Creation

The user configures a timer and sends a creation request to the API Server.
The server validates the input, stores the timer in PostgreSQL, and returns a unique shareable Id.
![Create Timer Sequence](./docs/start-timer-sequnce-diagram.png)

### Joining a Shared Timer

Another user accesses the timer through its shared link.
The Client retrieves timer details via the API Server and displays the synchronized countdown UI.
![Join Timer Sequence](./docs/share-timer-sequnce-diagram.png)

### Real-Time Synchronization

Clients connect to the Realtime Server via SSE.
When the owner adds a timestamp or changes the timer, the API Server updates the database and uses Redis to alert the Realtime Server.
All linked clients are instantly informed of any changes to the timer's status, guaranteeing shared real-time consistency.
![Realtime Sync Sequence 1](./docs/add-timestamp-sequnce-diagram.png)
![Realtime Sync Sequence 2](./docs/update-timer-sequnce-diagram.png)


### Timer Expiration & Notification

When the countdown reaches zero, Redis triggers an expiration event.
Every client modifies their UI in response to a timer-expired event published by the Realtime Server.
![Expiration Sequence](./docs/expire-timer-sequnce-diagram.png)
