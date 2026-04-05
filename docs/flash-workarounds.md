# Flash Workarounds

Known issues when flashing Avocado OS to Jetson Orin Nano with JetPack 6.2.2 / L4T R36.5.0.

## 1. UEFI binary name mismatch in partition layout

**Symptom:** `tegrahost_v2` fails with `Stat for uefi_t23x_general_with_dtb.bin failed` during signing.

**Cause:** The flash partition layout XMLs reference `uefi_t23x_general_with_dtb.bin` (the upstream NVIDIA name) but the Avocado/meta-tegra build produces `uefi_jetson.bin`. The `--cpubl uefi_jetson.bin` argument causes tegraflash.py to create `uefi_jetson_with_dtb.bin`, which doesn't match.

**Fix:** In your tegraflash working directory, replace the name in all XML files:

```bash
sed -i 's/uefi_t23x_general_with_dtb\.bin/uefi_jetson_with_dtb.bin/g' \
    flash.xml flash.xml.in internal-flash.xml bupgen-internal-flash.xml
```

**Affected files:** `flash.xml`, `flash.xml.in`, `internal-flash.xml`, `bupgen-internal-flash.xml`

## 2. bpmp_dtb UnboundLocalError in tegraflash_impl_t234.py

**Symptom:** Python `UnboundLocalError: local variable 'bpmp_dtb' referenced before assignment` around line 1565.

**Cause:** Bug in L4T R36.5.0 tegraflash tooling where `bpmp_dtb` variable is used before being assigned in certain code paths.

**Fix:** In `tegraflash_impl_t234.py`, add `bpmp_dtb = None` before the line that references `bpmp_dtb_in_layout`:

```python
# Add this line:
bpmp_dtb = None
bpmp_dtb_in_layout = get_partition_filename('bpmp_fw_dtb', 'type')
```
