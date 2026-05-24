#!/bin/bash

BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"

ARCHIVO="$BASE_DIR/data/velocidades.txt"

while true
do

    > "$ARCHIVO"

    for i in {1..6}
    do
        echo $((RANDOM % 6 + 1)) >> "$ARCHIVO"
    done

    echo "Velocidades actualizadas."

    sleep 3

done
