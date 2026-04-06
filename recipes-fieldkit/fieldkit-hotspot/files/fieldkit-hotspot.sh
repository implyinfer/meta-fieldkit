#!/bin/sh
# FieldKit WiFi Hotspot Setup
# Creates a WiFi AP on a USB WiFi adapter while keeping ethernet for internet.
# Uses NetworkManager for AP creation and iptables for NAT.

SSID="${FIELDKIT_HOTSPOT_SSID:-FieldKit}"
PASSWORD="${FIELDKIT_HOTSPOT_PASSWORD:-fieldkit123}"

# Find USB WiFi adapter (not the built-in one)
find_usb_wifi() {
    for iface in $(iw dev 2>/dev/null | grep "Interface" | awk '{print $2}'); do
        driver_path=$(readlink -f "/sys/class/net/$iface/device/driver" 2>/dev/null) || continue
        case "$driver_path" in
            */usb/*) echo "$iface"; return 0 ;;
        esac
    done
    return 1
}

AP_IF=$(find_usb_wifi) || { echo "No USB WiFi adapter found, skipping hotspot"; exit 0; }

echo "Starting hotspot on $AP_IF (SSID: $SSID)"

# Clean up existing hotspot
nmcli con down Hotspot 2>/dev/null || true
nmcli con delete Hotspot 2>/dev/null || true

# Create hotspot
nmcli device wifi hotspot ifname "$AP_IF" ssid "$SSID" password "$PASSWORD" || exit 1
nmcli con mod Hotspot connection.autoconnect yes
nmcli con mod Hotspot connection.interface-name "$AP_IF"

# Enable NAT if ethernet has internet
sleep 3
echo 1 > /proc/sys/net/ipv4/ip_forward
STA_IF=$(ip route | grep default | awk '{print $5}' | head -1)
if [ -n "$STA_IF" ] && [ "$STA_IF" != "$AP_IF" ]; then
    iptables -t nat -C POSTROUTING -s 10.42.0.0/24 -o "$STA_IF" -j MASQUERADE 2>/dev/null || \
        iptables -t nat -A POSTROUTING -s 10.42.0.0/24 -o "$STA_IF" -j MASQUERADE
    echo "NAT enabled: $AP_IF -> $STA_IF"
fi

echo "Hotspot active: $SSID @ 10.42.0.1"
