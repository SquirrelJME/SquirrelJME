# only compile the CPUs that Android actually used at some point
APP_ABI := all
APP_PLATFORM := android-18
APP_STL := c++_static
APP_CPPFLAGS += -std=c++11
