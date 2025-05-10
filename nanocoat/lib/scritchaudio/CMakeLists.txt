# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Include common macros to make things easier
squirreljme_include("scritchui.cmake")

# Reset any state
squirreljme_scritchany_enable(audio Audio)

# Core ScritchAudio Library
squirreljme_multilib_add_library(ScritchAudioCore
	audioCore.c
	audioStream.c)

# Include main headers
squirreljme_multilib_target_include_directories(ScritchAudioCore PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Software Mixer
add_subdirectory(softmix)

# Open Sound System
squirreljme_include("find-oss.cmake")

option(SQUIRRELJME_ENABLE_AUDIO_OSS "Enable OSS Audio" ON)
if(SQUIRRELJME_ENABLE_AUDIO_OSS)
	squirreljme_scritchaudio_build(OSS_FOUND oss OSS)
endif()
