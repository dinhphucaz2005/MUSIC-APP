#!/bin/bash
set -e

FLUTTER_SDK_PATH="$HOME/dev/flutter/"
AIO_MODULE_DIR="third-party/aio_module"
export PATH="$FLUTTER_SDK_PATH/bin:$PATH"

echo "Running flutter pub get in $AIO_MODULE_DIR..."
flutter pub get --verbose --directory "$AIO_MODULE_DIR"

echo "Done!"
