#!/bin/bash

# ====================================================
# TXT MIXER
# ====================================================
# Pipeline textual previo al runtime.
#
# Bash:
# excelente para automatización de archivos.
#
# Responsabilidad:
# - unir textos,
# - mezclarlos,
# - generar transmisión consolidada.
# ====================================================
# ====================================================
# ORIGEN DE NOTAS
# ====================================================
ORIGEN="data/text"

# =========================================
# ARCHIVO FINAL
# =========================================
SALIDA="data/marquesina_mix.txt"

# Limpia archivo anterior
> "$SALIDA"

# =========================================
# CREA ARCHIVO TEMPORAL
# =========================================
TEMP=$(mktemp)

# =========================================
# EXTRACCIÓN. RECORRE EL CONTENIDO DE LOS TXT
# =========================================
for archivo in "$ORIGEN"/*.txt
do
    cat "$archivo" >> "$TEMP"
    echo "" >> "$TEMP"
done

# =========================================
# MEZCLA LÍNEAS ALEATORIAMENTE
# =========================================
shuf "$TEMP" > "$SALIDA"

# =========================================
# LIMPIA TEMPORAL
# =========================================
rm "$TEMP"

echo "Archivo generado:"
echo "$SALIDA"
