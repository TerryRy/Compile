cmake_minimum_required(VERSION 3.10)
project(Compiler VERSION 1.0)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

include_directories(include)

file(GLOB_RECURSE SOURCES "src/*.cpp")

add_executable(Compiler ${SOURCES})
