# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchAudioWinMM SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchAudioCore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchAudioSoftMix_FPIC}>
	winmmCore.c
	winmmLoop.c
	winmmMidi.c
	winmmStream.c)

# Always make it PIC
squirreljme_always_fpic(ScritchAudioWinMM)

# The file where the include lives
target_compile_definitions(ScritchAudioWinMM PUBLIC
	SQUIRRELJME_OSS_INCLUDE_FILE="${OSS_INCLUDE_FILE}")

# Include main headers
target_include_directories(ScritchAudioWinMM PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Windows Multimedia Library is needed here
target_link_libraries(ScritchAudioWinMM PUBLIC
	"winmm${SQUIRRELJME_WIN_LIB_SUFFIX}")

# ScritchAudio requires a normalized library name
squirreljme_target_binary_name(ScritchAudioWinMM
	"squirreljme-scritchaudio-winmm")
squirreljme_target_binary_output(ScritchAudioWinMM
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchAudioWinMM)
