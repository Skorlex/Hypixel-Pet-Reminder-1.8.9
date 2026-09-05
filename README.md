# Hypixel Pet Reminder (Forge 1.8.9)

Track your 1-hour Hypixel pet missions automatically on the Hypixel Network.

---

## Key Features

* **Automatic Mission Tracking:** Detects pet mission completions in chat using pattern recognition and immediately starts an accurate 1-hour countdown.
* **Smart Multi-Pet Handling:** Built-in debounce protection prevents duplicate alerts and overlapping timers when claiming multiple pets at once.
* **Persistent Timers:** Mission end times are saved locally to your config file, meaning your timer keeps ticking accurately even if you switch lobbies, disconnect, or restart Minecraft.
* **Clear Audio Notifications:** Plays a high-pitched harp chime upon mission dispatch and a loud orb chime when the cooldown expires, heard reliably even when moving at high speeds.
* **Interactive Chat Reminders:** Sends an alert every minute once a mission is ready, complete with an inline, clickable `[CANCEL]` button to silence alerts with a single click.

---

## Commands

Use `/pr` or `/petreminder` in chat:

* **`/pr timer`** — View the remaining minutes and seconds on your active mission.
* **`/pr cancel`** — Silence reminders and cancel the active mission timer.
* **`/pr toggle`** — Turn the entire mod on or off (automatically clears any running timer).
* **`/pr help`** — Open the in-game command reference list.
