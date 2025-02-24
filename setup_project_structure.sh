#!/bin/bash

# Define the base directory
BASE_DIR="app/src/main/java/com/example/fairshare"

# List of directories to create
directories=(
  "$BASE_DIR"
  "$BASE_DIR/data"
  "$BASE_DIR/data/dao"
  "$BASE_DIR/di"
  "$BASE_DIR/ui"
  "$BASE_DIR/ui/screens"
)

# List of files to create
files=(
  "$BASE_DIR/FairShareApplication.kt"
  "$BASE_DIR/data/Bucket.kt"
  "$BASE_DIR/data/Task.kt"
  "$BASE_DIR/data/FairShareDatabase.kt"
  "$BASE_DIR/data/dao/BucketDao.kt"
  "$BASE_DIR/data/dao/TaskDao.kt"
  "$BASE_DIR/di/DatabaseModule.kt"
  "$BASE_DIR/ui/MainViewModel.kt"
  "$BASE_DIR/ui/screens/MainScreen.kt"
)

# Create directories if they don't exist
for dir in "${directories[@]}"; do
  if [ ! -d "$dir" ]; then
    mkdir -p "$dir"
    echo "Created directory: $dir"
  else
    echo "Directory already exists: $dir"
  fi
done

# Create files if they don't exist
for file in "${files[@]}"; do
  if [ ! -f "$file" ]; then
    touch "$file"
    echo "Created file: $file"
  else
    echo "File already exists: $file"
  fi
done
