# JNI wrapper for the on-device Wireshark decoder.
# Links the arm64 libwireshark/GLib set cross-compiled by
# wireshark-spike/build_wireshark_arm64.sh (staged in ../jniLibs/arm64-v8a),
# and compiles against the headers staged in wireshark-prebuilt/include.
cmake_minimum_required(VERSION 3.22.1)
project(ltws C CXX)

set(WS "${CMAKE_SOURCE_DIR}/wireshark-prebuilt/include")

add_library(ltws SHARED ws_jni.c ws_core.c)

target_include_directories(ltws PRIVATE
    "${WS}"              # epan/, wiretap/, wsutil/, config.h, ws_version.h, ws_symbol_export.h
    "${WS}/wsutil"       # some wsutil headers include siblings by bare name
    "${WS}/glib-2.0")    # glib.h + glibconfig.h

# Prebuilt shared objects (unversioned sonames, Android-friendly) by real name.
foreach(lib wireshark wiretap wsutil glib-2.0 gobject-2.0 gmodule-2.0 gio-2.0
            gcrypt gpg-error cares z ffi pcre2-8)
    add_library(ws_${lib} SHARED IMPORTED)
    set_target_properties(ws_${lib} PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/lib${lib}.so")
endforeach()

# 16 KB page alignment (2026-11-01). The app now builds with NDK r30, which links 16 KB-aligned by default;
# these flags stay as a safety net in case ndkVersion is ever lowered below r28.
# https://developer.android.com/guide/practices/page-sizes
target_link_options(ltws PRIVATE "-Wl,-z,max-page-size=16384" "-Wl,-z,common-page-size=16384")

find_library(log-lib log)
# Link only what ws_jni references directly; libwireshark's own NEEDED pulls the rest at runtime.
target_link_libraries(ltws ws_wireshark ws_wiretap ws_wsutil ws_glib-2.0 ws_gobject-2.0 ${log-lib})

# Product-owned JNI boundary around a pinned, embedded inference engine.
set(LT_LLAMA_SOURCE "${CMAKE_CURRENT_SOURCE_DIR}/../../../../../third_party/llama.cpp")
if(NOT EXISTS "${LT_LLAMA_SOURCE}/include/llama.h")
    message(FATAL_ERROR "Fetch the pinned runtime with tools/ai/fetch-runtime.ps1 before building.")
endif()
set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_COMMON OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_TESTS OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_TOOLS OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_SERVER OFF CACHE BOOL "" FORCE)
set(LLAMA_BUILD_APP OFF CACHE BOOL "" FORCE)
set(LLAMA_CURL OFF CACHE BOOL "" FORCE)
set(GGML_NATIVE OFF CACHE BOOL "" FORCE)
set(GGML_OPENMP OFF CACHE BOOL "" FORCE)
option(LT_AI_VULKAN "Enable the explicit on-device Vulkan inference experiment" OFF)
if(LT_AI_VULKAN)
    if(NOT DEFINED LT_VULKAN_BUILD_ROOT)
        message(FATAL_ERROR "The pinned Vulkan build dependencies are required")
    endif()
    set(Vulkan_INCLUDE_DIR "${LT_VULKAN_BUILD_ROOT}/vendor/Vulkan-Headers/include" CACHE PATH "" FORCE)
    set(Vulkan_LIBRARY "${CMAKE_SYSROOT}/usr/lib/aarch64-linux-android/28/libvulkan.so" CACHE FILEPATH "" FORCE)
    set(Vulkan_GLSLC_EXECUTABLE "${CMAKE_ANDROID_NDK}/shader-tools/windows-x86_64/glslc.exe" CACHE FILEPATH "" FORCE)
    set(SPIRV-Headers_DIR "${LT_VULKAN_BUILD_ROOT}/sdk/share/cmake/SPIRV-Headers" CACHE PATH "" FORCE)
    find_package(SPIRV-Headers CONFIG REQUIRED)
    set(GGML_VULKAN ON CACHE BOOL "" FORCE)
else()
    set(GGML_VULKAN OFF CACHE BOOL "" FORCE)
endif()
set(GGML_LLAMAFILE OFF CACHE BOOL "" FORCE)
add_subdirectory("${LT_LLAMA_SOURCE}" "${CMAKE_BINARY_DIR}/layertime-llama")
if(LT_AI_VULKAN)
    # This pinned backend finds SPIR-V headers but does not attach their include target.
    target_link_libraries(ggml-vulkan PRIVATE SPIRV-Headers::SPIRV-Headers)
    target_compile_options(ggml-vulkan PRIVATE -O3)
endif()
# Keep app diagnostics while optimizing the tensor kernels, even in debug APKs.
# Retain the portable arm64 baseline rather than requiring newer CPU extensions.
foreach(inference_target ggml-base ggml-cpu llama)
    target_compile_options(${inference_target} PRIVATE -O3)
endforeach()
add_library(ltai SHARED ai_jni.cpp)
target_compile_features(ltai PRIVATE cxx_std_17)
target_compile_options(ltai PRIVATE -O2)
target_link_libraries(ltai PRIVATE llama ${log-lib})
target_link_options(ltai PRIVATE "-Wl,-z,max-page-size=16384" "-Wl,-z,common-page-size=16384")

if(LT_AI_VULKAN)
    target_compile_definitions(ltai PRIVATE LT_AI_VULKAN=1)
endif()
