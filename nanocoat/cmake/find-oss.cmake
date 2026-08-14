# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Find headers
include(CheckIncludeFile)

# Find soundcard header
CHECK_INCLUDE_FILE("soundcard.h" OSS_INCLUDE_BASE)
if(NOT OSS_INCLUDE_BASE)
	CHECK_INCLUDE_FILE("sys/soundcard.h" OSS_INCLUDE_SYS)
endif()

# If either are set we have OSS
if(OSS_INCLUDE_BASE)
	set(OSS_FOUND TRUE)
	set(OSS_INCLUDE_FILE "soundcard.h")
elseif(OSS_INCLUDE_SYS)
	set(OSS_FOUND TRUE)
	set(OSS_INCLUDE_FILE "sys/soundcard.h")

# Fallback to self provided OSS
elseif("${SQUIRRELJME_SYSTEM}" STREQUAL "bsd" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "linux" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "solaris")
	set(OSS_FOUND TRUE)
	set(OSS_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/include/3rdparty/oss")
	set(OSS_INCLUDE_FILE "soundcard.h")

# Not available
else()
	set(OSS_FOUND FALSE)
endif()

# Note OSS depends on ioctl to work, so if the system happens to lack ioctl
# support then we cannot have OSS audio
if(OSS_FOUND AND SJME_CONFIG_HAS_SYS_IOCTL_H)
	set(SQUIRRELJME_ENABLE_AUDIO_OSS_DEFAULT TRUE)
else()
	set(SQUIRRELJME_ENABLE_AUDIO_OSS_DEFAULT FALSE)
endif()

# Enable OSS ScritchAudio?
squirreljme_define_option(SQUIRRELJME_ENABLE_AUDIO_OSS
	"Enable ScritchAudio: Open Sound System")

