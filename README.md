

# Vending Hider Service

**Vending Hider Service** is a minimalist Android background service that continuously monitors and hides the Google Play Store app (`com.android.vending`) every 10 seconds using root access. Ideal for ROM developers, kiosk setups, or restricted environments.

---

## 🔧 Features

* Runs silently in the background as a foreground service
* Checks every 10 seconds if the Play Store is visible
* Automatically hides the app via `pm hide` if it's found
* Uses root access (via `su`)
* Logs every action for easy debugging

---

## ⚙️ Requirements

* Android device with **root access**
* The Play Store (`com.android.vending`) must be installed
* Installed as part of a system or privileged app (for persistent background execution)

---

## 🛠️ How It Works

1. Starts as a foreground service with a low-priority notification.
2. Every 10 seconds:

   * Checks if the Play Store is currently visible.
   * If not hidden, executes `su -c pm hide com.android.vending`.
3. Logs execution with a timestamp.

---

## 📂 File Structure

```
com.android.helper/
└── VendingHiderService.kt
```

---

## 📝 Example Log Output

```
Running VendingCheckTask #1 at 2025-06-09 20:45:12
Package not hidden, executing hide command
Hid package: com.android.vending
```

---

## ⚠️ Disclaimer

This app uses **root permissions** and modifies app visibility at the system level. Use responsibly.

---

