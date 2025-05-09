# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Include common macros to make things easier
squirreljme_include("scritchui.cmake")

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

if(OSS_INCLUDE_FILE)
	add_subdirectory(oss)
endif()
