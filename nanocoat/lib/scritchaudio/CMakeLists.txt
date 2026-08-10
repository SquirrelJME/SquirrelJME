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

if(SQUIRRELJME_ENABLE_AUDIO_OSS)
	squirreljme_scritchaudio_build(oss OSS)
endif()

# Windows Media System
if(SQUIRRELJME_ENABLE_AUDIO_WINMM)
	squirreljme_scritchaudio_build(winmm WinMM)
endif()
