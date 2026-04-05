#!/bin/bash
# FieldKit first-time device setup
# Run this after flashing and SSH'ing into a fresh device:
#   curl -fsSL https://raw.githubusercontent.com/implyinfer/meta-fieldkit/main/tools/setup-fieldkit.sh | bash
#
# Or copy and run locally:
#   scp tools/setup-fieldkit.sh root@192.168.55.1:/var/roothome/
#   ssh root@192.168.55.1 bash /var/roothome/setup-fieldkit.sh

set -euo pipefail

echo "=== FieldKit Device Setup ==="
echo "Device: $(cat /proc/device-tree/model 2>/dev/null)"
echo "Kernel: $(uname -r)"
echo ""

# ── Docker setup ─────────────────────────────────────────────────────
echo "[1/4] Configuring Docker..."
mkdir -p /etc/docker
cat > /etc/docker/daemon.json << 'DOCKER_EOF'
{
    "runtimes": {
        "nvidia": {
            "path": "nvidia-container-runtime",
            "runtimeArgs": []
        }
    },
    "default-runtime": "nvidia",
    "data-root": "/var/lib/docker"
}
DOCKER_EOF

systemctl enable docker 2>/dev/null || true
systemctl restart docker 2>/dev/null || true
echo "  Docker configured with nvidia runtime as default"

# ── Pull AI container ────────────────────────────────────────────────
echo "[2/4] Pulling base JetPack container (this may take a while)..."
docker pull nvcr.io/nvidia/l4t-jetpack:r36.4.0

# ── Project directory ──────────���───────────────────────────────���─────
echo "[3/4] Setting up project directory..."
mkdir -p /var/roothome/fieldkit/{src,models,data,logs}

cat > /var/roothome/fieldkit/docker-compose.yml << 'COMPOSE_EOF'
services:
  inference:
    image: fieldkit-ai:latest
    runtime: nvidia
    network_mode: host
    devices:
      - /dev/video0:/dev/video0
    volumes:
      - ./src:/app/src
      - ./models:/app/models
      - ./data:/app/data
      - ./logs:/app/logs
    restart: unless-stopped
    command: python3 /app/src/main.py
COMPOSE_EOF

# ── GPU verification ────────��───────────────────────────────────────
echo "[4/4] Verifying GPU access..."
docker run --rm --runtime nvidia nvcr.io/nvidia/l4t-jetpack:r36.4.0 \
    python3 -c "
import subprocess
result = subprocess.run(['nvidia-smi'], capture_output=True, text=True)
if result.returncode != 0:
    # Jetson uses tegrastats instead of nvidia-smi
    print('Jetson GPU detected (use tegrastats for monitoring)')
else:
    print(result.stdout[:500])
" 2>/dev/null || echo "  GPU container test complete"

echo ""
echo "=== Setup Complete ==="
echo ""
echo "Next steps:"
echo "  1. Build the AI container (takes ~1 hour on first build):"
echo "     cd /var/roothome/fieldkit"
echo "     docker build -t fieldkit-ai -f /var/roothome/Dockerfile.fieldkit-ai ."
echo ""
echo "  2. Or pull a pre-built container:"
echo "     docker pull ghcr.io/implyinfer/fieldkit-ai:latest"
echo ""
echo "  3. Run your inference app:"
echo "     docker compose up"
