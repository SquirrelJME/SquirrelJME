LOCAL_PATH := $(call my-dir)

CORE_DIR := $(LOCAL_PATH)/../..

GIT_VERSION := " $(shell git rev-parse --short HEAD || echo unknown)"

platform = android_jni

# Palm OS 5 support
ifeq ($(TARGET_ARCH_ABI), x86)
   EMU_ARCH := x86_32
else ifeq ($(TARGET_ARCH_ABI), x86_64)
   EMU_ARCH := x86_64
else ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
	EMU_ARCH := armv7
else ifeq ($(TARGET_ARCH_ABI), arm64-v8a)
   EMU_ARCH := armv8
else
   EMU_ARCH := unknown
endif

include $(CORE_DIR)/src/frontend/libretro/legacy/Makefile.common

COREFLAGS := -ffast-math -funroll-loops -D__LIBRETRO__ -DINLINE=inline $(INCFLAGS) $(COREDEFINES)

ifneq ($(GIT_VERSION), " unknown")
	COREFLAGS += -DGIT_VERSION=\"$(GIT_VERSION)\"
endif

include $(CLEAR_VARS)
LOCAL_MODULE    := retro
LOCAL_SRC_FILES := $(SOURCES_C) $(SOURCES_CXX) $(SOURCES_ASM)
LOCAL_CFLAGS    := $(COREFLAGS)
LOCAL_LDFLAGS   := -Wl,-version-script=$(CORE_DIR)/libretro/link.T

ifeq ($(TARGET_ARCH_ABI), armeabi-v7a)
	LOCAL_ARM_NEON := true
endif

include $(BUILD_SHARED_LIBRARY)
