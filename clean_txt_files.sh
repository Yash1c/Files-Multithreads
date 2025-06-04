#!/bin/bash

# Main directories
DIRS=("All" "Sample")

echo "Starting cleanup..."

for DIR in "${DIRS[@]}"; do
    if [ -d "$DIR" ]; then
        echo "Cleaning in $DIR..."

        # Remove .txt files directly in the directory
        find "$DIR" -maxdepth 1 -type f -name "*.txt" -print0 | xargs -0 -r rm -f

        # Remove __MACOSX folder if it exists
        if [ -d "$DIR/__MACOSX" ]; then
            echo "Removing __MACOSX folder from $DIR..."
            rm -rf "$DIR/__MACOSX"
        fi

        # Remove files inside the amostra folder, if it exists
        if [ -d "$DIR/amostra" ]; then
            echo "Removing contents of amostra folder from $DIR..."
            find "$DIR/amostra" -type f -print0 | xargs -0 -r rm -f
            echo "Removing amostra folder from $DIR..."
            rmdir "$DIR/amostra" 2>/dev/null || rm -rf "$DIR/amostra"
        fi

        # Synchronize the filesystem
        sync
    else
        echo "Directory $DIR does not exist!"
    fi
done

echo "Cleanup complete!"