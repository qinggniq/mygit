cmake_minimum_required(VERSION 3.5)
project(GetMaxAndSub)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp ScoreManager.hpp ScoreManager.cpp Heap.h Heap.cpp )
add_executable(GetMaxAndSub ${SOURCE_FILES})