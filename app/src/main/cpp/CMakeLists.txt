cmake_minimum_required(VERSION 3.22.1)

project("musicapp")

add_library(${CMAKE_PROJECT_NAME} SHARED
        musicapp-native-lib.cpp)

find_package(oboe REQUIRED CONFIG)

target_link_libraries(${CMAKE_PROJECT_NAME}
        android
        log
        oboe::oboe
)
