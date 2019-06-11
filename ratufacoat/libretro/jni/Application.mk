# only compile the CPUs that Android actually used at some point
APP_ABI := armeabi armeabi-v7a arm64-v8a x86 x86_64
APP_PLATFORM := android-18
APP_STL := c++_static
APP_CPPFLAGS += -std=c++11
