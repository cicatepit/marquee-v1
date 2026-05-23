#!/bin/bash

# =========================================
# UBICACIÓN REAL DEL SCRIPT
# =========================================
BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"

# =========================================
# RUTAS ABSOLUTAS
# =========================================
ORIGEN="$BASE_DIR/data/txt"
SALIDA="$BASE_DIR/data/marquesina_mix.txt"

# Limpia archivo anterior
> "$SALIDA"

# =========================================
# TEMPORAL
# =========================================
TEMP=$(mktemp)

# =========================================
# MEZCLA TXT
# =========================================
for archivo in "$ORIGEN"/*.txt; do

    [ -f "$archivo" ] || continue

    cat "$archivo" >> "$TEMP"
    echo "" >> "$TEMP"

done

# =========================================
# DEBUG VISUAL
# =========================================
echo "=== CONTENIDO TEMPORAL ==="
cat "$TEMP"

# =========================================
# SHUFFLE
# =========================================
shuf "$TEMP" > "$SALIDA"

# =========================================
# LIMPIEZA
# =========================================
rm "$TEMP"

echo "Archivo generado:"
echo "$SALIDA"
