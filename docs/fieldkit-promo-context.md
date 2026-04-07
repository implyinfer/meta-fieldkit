# FieldKit Promotional Context File

> Use this document as context when generating marketing copy, social media posts, website content, pitch decks, investor materials, product descriptions, and promotional videos for the Jetson Orin Nano Field Kit by imply+infer.

---

## Product Identity

**Product Name:** Jetson Orin Nano Field Kit
**Company:** imply+infer
**Website:** https://implyinfer.com
**Shop:** https://shop.implyinfer.com
**GitHub:** https://github.com/implyinfer
**Tagline options:**
- "Edge AI, out of the box."
- "From unboxing to inference in minutes."
- "The developer kit for physical AI."
- "Stop configuring. Start building."

---

## The Problem We Solve

Building on the Jetson Orin Nano is powerful but painful. Developers spend **weeks** before writing a single line of application code:

- Flashing OS images, debugging boot failures, getting NVMe to work
- Cross-compiling OpenCV, PyTorch, TensorRT, CTranslate2 with CUDA 12.6
- Configuring camera device trees, GStreamer pipelines, V4L2 drivers
- Setting up Docker, RTSP streaming, WiFi hotspot, web interfaces
- Troubleshooting CUDA version mismatches, library dependencies, kernel modules
- Making it all survive reboots and work reliably in the field

Every Jetson developer goes through this. Most give up or spend hundreds of hours. The Field Kit eliminates this entirely.

## What We Ship

### Hardware (Ruggedized Kit)

| Component | Spec | Why It Matters |
|-----------|------|----------------|
| **NVIDIA Jetson Orin Nano Super** | 8GB RAM, 67 TOPS AI, CUDA 12.6 | The most capable edge AI module under $500 |
| **256GB NVMe SSD** | Pre-flashed, bootable | 10x faster than microSD, pre-loaded with everything |
| **Dual Stereo IMX219 Cameras** | 160° FOV, CSI interface | Depth perception, SLAM, stereo vision — ready to go |
| **AC600 USB WiFi Adapter** | AP + Station simultaneous | Create a hotspot AND connect to internet at the same time |
| **9-DOF IMU Sensor** | ICM-20948 via I2C | Orientation tracking for robotics and SLAM |
| **Custom 3D-Printed Case** | Rigid camera mount, ventilated | Protects hardware, holds cameras in calibrated position |
| **PoE Support** (Gen 2) | Power over Ethernet | Single cable for power + network — deploy anywhere |

### Software (FieldKit OS)

**Two generations of software, both open source:**

#### Gen 1: JetPack-Based Image (Current Shipping)
Full Ubuntu-based image with everything pre-compiled and pre-installed:
- **10+ AI models** pre-loaded (YOLOv11, RF-DETR, Qwen, Ministral)
- **PyTorch, TensorRT, OpenCV** compiled with CUDA 12.6
- **CTranslate2, Llama.cpp, Whisper** for language and speech
- **Roboflow Inference Server** for GPU-accelerated vision
- **MediaMTX** for low-latency RTSP/WebRTC camera streaming
- **Open WebUI + Ollama** for local LLM chat
- **Livekit** for real-time voice/video
- **WiFi hotspot** with captive portal
- **Kiwix** for offline Wikipedia
- **Docker** with nvidia runtime

#### Gen 2: FieldKit OS / Avocado-Based (Next Generation)
Immutable, OTA-updatable edge OS built from source with Yocto:
- **Read-only rootfs** — the OS can't be corrupted by crashes or bad writes
- **A/B atomic OTA updates** — push updates remotely, automatic rollback if they fail
- **118GB writable /var** — auto-expands btrfs to fill any NVMe
- **Docker + nvidia runtime** — containerized AI workloads with full GPU access
- **Full driver support** — Ethernet, WiFi, Bluetooth, CAN, USB, NVMe, DisplayPort, HDMI audio
- **SSH out of the box** — password `fieldkit123`, mDNS discovery, static fallback IP
- **Production fleet ready** — same image on 1 device or 1000 devices
- **Open source** — https://github.com/implyinfer/meta-fieldkit

---

## Key Differentiators

### vs. Bare Jetson Orin Nano Developer Kit
| | Bare Dev Kit | Field Kit |
|---|---|---|
| Time to first inference | Days to weeks | Minutes |
| CUDA libraries compiled | No | Yes |
| Cameras included | No | Dual stereo 160° FOV |
| NVMe boot | Manual setup | Pre-configured |
| WiFi hotspot | Not included | AP + STA mode |
| Docker + models | Manual install | Pre-loaded |
| Case | None | Ruggedized with camera mount |
| OTA updates | None | A/B atomic rollback |

### vs. Other Edge AI Platforms
- **Lower cost** than industrial platforms ($500-800 total vs. $2000+ for comparable)
- **Open source** — full stack, no vendor lock-in, customize everything
- **NVIDIA ecosystem** — CUDA, TensorRT, DeepStream, Isaac ROS compatibility
- **Community partner** — official Roboflow community hardware partner
- **Hackable** — it's a developer kit, not a sealed appliance

### vs. Cloud AI
- **No internet required** — runs completely offline
- **No latency** — inference happens on-device in milliseconds
- **No recurring costs** — one-time hardware purchase, no API fees
- **Privacy** — data never leaves the device
- **Deployed anywhere** — warehouses, farms, factories, vehicles, remote sites

---

## Use Cases

### Computer Vision
- Real-time object detection and tracking
- Quality inspection on manufacturing lines
- Inventory monitoring in warehouses
- Wildlife monitoring in remote locations
- Security and surveillance
- Sports analytics

### Robotics
- Robot perception and navigation
- Stereo depth estimation for obstacle avoidance
- SLAM (Simultaneous Localization and Mapping)
- Human-robot interaction (Reachy Mini integration)
- Autonomous vehicle perception

### Edge AI Inference
- On-device LLM inference (Qwen, Ministral, Llama)
- Speech-to-text (Whisper) and text-to-speech
- Voice assistants with tool calling
- Document processing and OCR
- Anomaly detection

### Education & Research
- AI/ML coursework with real hardware
- Robotics research platform
- Computer vision teaching tool
- Hackathon starter kit

### Field Deployment
- Agricultural monitoring
- Environmental sensing
- Infrastructure inspection
- Remote facility monitoring
- Portable AI demonstration units

---

## Technical Specifications

### Compute
- **CPU:** 6-core Arm Cortex-A78AE @ 1.5GHz
- **GPU:** 1024 CUDA cores, 32 Tensor cores, 67 TOPS
- **RAM:** 8GB LPDDR5
- **Storage:** 256GB NVMe SSD (expandable)

### AI Performance
- **CUDA:** 12.6
- **TensorRT:** Hardware-accelerated inference
- **PyTorch:** Pre-compiled with CUDA support
- **OpenCV:** Compiled with CUDA, GStreamer, V4L2
- **Inference:** 30+ FPS object detection (YOLOv11n @ 640)

### Connectivity
- **Ethernet:** Gigabit (Realtek r8169)
- **WiFi:** AC600 dual-band (AP + Station simultaneous)
- **Bluetooth:** 5.0 (RTL8822CE)
- **USB:** USB 3.2 Gen 2 + USB 2.0
- **CAN Bus:** Available on carrier board
- **I2C/SPI/GPIO:** Full 40-pin header access
- **Display:** DisplayPort 1.4

### Power
- **Input:** USB-C PD or DC barrel jack
- **PoE:** Supported via PoE HAT (Gen 2 kits)
- **Consumption:** 7-25W (configurable power modes)

### Physical
- **Dimensions:** ~130 x 100 x 65mm (with case)
- **Weight:** ~300g (with case and cameras)
- **Operating temp:** 0°C to 50°C
- **Case:** 3D-printed, ventilated, rigid camera mount
- **STL files:** Open source on Printables

---

## Software Architecture (Gen 2 — FieldKit OS)

```
┌──────────────────────────────────────────────┐
│  Your Application (Docker container)         │
│  PyTorch, OpenCV+CUDA, ROS2, custom code     │
├──────────────────────────────────────────────┤
│  FieldKit OS (meta-fieldkit layer)           │
│  SSH, nginx, Docker, NetworkManager,         │
│  drivers, kiosk display, WiFi hotspot        │
├──────────────────────────────────────────────┤
│  Avocado OS (immutable rootfs)               │
│  systemd, avocadoctl, A/B OTA, sysext       │
├──────────────────────────────────────────────┤
│  NVIDIA L4T + JetPack 6.2.2                 │
│  Kernel 6.6, CUDA 12.6, cuDNN 9.3           │
├──────────────────────────────────────────────┤
│  Jetson Orin Nano Super (hardware)           │
└──────────────────────────────────────────────┘
```

### Why Immutable OS?
- **Can't brick it** — read-only root means no corruption from crashes or bad writes
- **Atomic updates** — A/B partition swap, automatic rollback on failure
- **Reproducible** — same image = same behavior on every device in your fleet
- **Secure** — attack surface is minimal, OS can't be modified at runtime
- **Auditable** — every deployed image is bit-identical and traceable

---

## Pricing & Packaging

### Field Kit (Complete)
Everything needed to start building edge AI:
- Jetson Orin Nano Super Developer Kit
- 256GB NVMe SSD (pre-flashed)
- Dual IMX219 stereo cameras
- AC600 USB WiFi adapter
- ICM-20948 IMU sensor
- 3D-printed ruggedized case
- Pre-loaded AI models and tools
- **Shop:** https://shop.implyinfer.com

### Software Only (Free, Open Source)
- FieldKit OS image: https://github.com/implyinfer/jetson-orin-nano-field-kit/releases
- meta-fieldkit Yocto layer: https://github.com/implyinfer/meta-fieldkit
- DIY build guide in README
- 3D print files on Printables

---

## Community & Ecosystem

- **Official Roboflow Community Hardware Partner**
- **Open source** — Apache 2.0 license
- **Active GitHub** with documentation, guides, and releases
- **YouTube** — Zero to Hero video guides
- **Reachy Mini integration** — Pollen Robotics robot support
- **Upstream contributions** — PRs to Avocado OS project

---

## Proof Points & Traction

- Complete open source stack downloaded and used by Jetson developers
- Official Roboflow community hardware partnership
- Upstream contributions accepted to Avocado OS (avocado-linux/meta-avocado)
- Pre-compiled CUDA libraries save developers 100+ hours per setup
- 3D print files available on Printables for case customization
- Video content with walkthrough guides on YouTube

---

## Brand Voice Guidelines

- **Technical but accessible** — we respect that our users are developers, but we don't gatekeep
- **Practical over theoretical** — show real results, real code, real hardware
- **Honest about tradeoffs** — edge AI has constraints (power, memory, thermal), we work with them
- **Open source first** — everything is available, nothing is hidden behind a paywall
- **Builder culture** — we're makers building tools for makers

---

## Key Messages by Audience

### For Developers
"Stop spending weeks configuring your Jetson. The Field Kit ships with everything pre-compiled and pre-configured — PyTorch, OpenCV, TensorRT, Docker, cameras, WiFi — so you can start writing inference code on day one."

### For Robotics Teams
"Dual stereo cameras, IMU, CAN bus, PoE, and a ruggedized case with rigid camera mount. The Field Kit is a complete perception platform for mobile robots, drones, and autonomous systems."

### For Product Teams / Startups
"Prototype your edge AI product in days, not months. When you're ready for production, the same FieldKit OS with atomic OTA updates scales from your desk to a fleet of devices in the field."

### For Educators
"Give students real hardware with real AI capabilities. The Field Kit runs local LLMs, computer vision, and voice assistants — no cloud API keys, no recurring costs, no internet required."

### For Enterprise / Fleet Deployment
"Immutable OS, A/B atomic updates, automatic rollback, reproducible builds, remote management. FieldKit OS is built for deploying and maintaining hundreds of edge AI devices reliably."

---

## Social Media Snippets

**Twitter/X:**
- "Went from unboxing to running RF-DETR object detection in under 5 minutes. No CUDA compilation. No driver debugging. Just plug in and infer. 🔥"
- "The Jetson Orin Nano Field Kit now ships with an immutable OS, A/B OTA updates, and automatic rollback. Your edge devices just got production-ready."
- "Why we open sourced everything: the STL files, the OS image, the Yocto layer, and the build system. Edge AI should be accessible."

**LinkedIn:**
- "We've shipped a production-grade edge AI platform for under $500. Immutable OS, atomic OTA, Docker with GPU support, dual stereo cameras, and PoE. Here's how we built it."
- "The biggest pain point in edge AI isn't the model — it's the deployment. That's why we built FieldKit OS: an immutable Linux distribution purpose-built for NVIDIA Jetson that you can update remotely without ever touching the device."

---

## FAQ for Marketing Copy

**Q: What's the difference between the Field Kit and a bare Jetson developer kit?**
A: A bare dev kit is a board. The Field Kit is a complete platform — cameras, WiFi, case, NVMe, and a pre-configured software stack with 100+ hours of compilation and configuration already done.

**Q: Do I need internet for it to work?**
A: No. Everything runs locally. Models, inference, LLMs, voice — all on-device. The WiFi hotspot creates its own network for client devices to connect.

**Q: Can I use my own models?**
A: Yes. The Roboflow inference server accepts any ONNX, TensorRT, or Roboflow-hosted model. PyTorch and OpenCV are pre-compiled with CUDA for custom inference pipelines.

**Q: How do I update devices in the field?**
A: FieldKit OS (Gen 2) supports A/B atomic OTA updates. Push a new OS image remotely — it installs to the inactive partition, switches on reboot, and automatically rolls back if it fails.

**Q: Is this suitable for production deployment?**
A: Yes. The immutable rootfs, reproducible builds, atomic updates, and fleet management capabilities are designed for production edge AI deployments at scale.

**Q: What cameras does it support?**
A: Ships with dual IMX219 CSI cameras. Also supports USB cameras (RealSense D435 tested), any V4L2/UVC camera, and IP cameras via RTSP.
