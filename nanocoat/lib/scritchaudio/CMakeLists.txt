# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Software Mixer
add_subdirectory(softmix)

# Core ScritchAudio Library
squirreljme_multilib_add_library(ScritchAudioCore
	audioConn.c
	audioCore.c
	audioLoop.c
	audioMidi.c
	audioStream.c)

# Include main headers
squirreljme_multilib_include_directories(ScritchAudioCore PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Add in the software mixer always
squirreljme_multilib_link_libraries_required(ScritchAudioCore PUBLIC
	BaseStatic
	ScritchAudioSoftMix)

# Export the core library
squirreljme_multilib_export(ScritchAudioCore)

# Open Sound System
squirreljme_include("find-oss.cmake")

# Note OSS depends on ioctl to work, so if the system happens to lack ioctl
# support then we cannot have OSS audio
if(OSS_FOUND AND SJME_CONFIG_HAS_SYS_IOCTL_H)
	set(SQUIRRELJME_ENABLE_AUDIO_OSS_DEFAULT YES)
else()
	set(SQUIRRELJME_ENABLE_AUDIO_OSS_DEFAULT NO)
endif()

option(SQUIRRELJME_ENABLE_AUDIO_OSS "Enable OSS Audio"
	${SQUIRRELJME_ENABLE_AUDIO_OSS_DEFAULT})
if(SQUIRRELJME_ENABLE_AUDIO_OSS)
	squirreljme_scritchaudio_build(oss OSS)
endif()

# Windows Media System
option(SQUIRRELJME_ENABLE_AUDIO_WINMM "Enable WinMM Audio"
	${SQUIRRELJME_ON_IF_WIN32})
if(SQUIRRELJME_ENABLE_AUDIO_WINMM)
	squirreljme_scritchaudio_build(winmm WinMM)
endif()
