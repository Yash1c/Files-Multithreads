#!/bin/bash

# Main directories
DIRS=("All" "Sample")

echo "Starting automatic unzip..."

for DIR in "${DIRS[@]}"; do
    if [ -d "$DIR" ]; then
        echo "Entering $DIR..."
        cd "$DIR" || exit 1

        for ZIP_FILE in *.zip; do
            if [ -f "$ZIP_FILE" ]; then
                echo "Unzipping $ZIP_FILE in $DIR..."
                unzip -o "$ZIP_FILE"
            else
                echo "No zip files found in $DIR."
            fi
        done

        cd ..
    else
        echo "Directory $DIR does not exist!"
    fi
done

echo "All unzips complete!"