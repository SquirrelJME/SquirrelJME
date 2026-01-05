# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchAudioOSS SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchAudioCore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchAudioSoftMix_FPIC}>
	ossCore.c
	ossLoop.c
	ossMidi.c
	ossStream.c)

# Standard properties for dynamic libraries
squirreljme_dylib_standard_properties(ScritchAudioOSS)

# The file where the include lives
target_compile_definitions(ScritchAudioOSS PUBLIC
	SQUIRRELJME_OSS_INCLUDE_FILE="${OSS_INCLUDE_FILE}")

# Include main headers
if(DEFINED OSS_INCLUDE_DIR AND NOT OSS_INCLUDE_DIR STREQUAL "")
	target_include_directories(ScritchAudioOSS PUBLIC
		"${CMAKE_SOURCE_DIR}/include"
		"${OSS_INCLUDE_DIR}")
else()
	target_include_directories(ScritchAudioOSS PUBLIC
		"${CMAKE_SOURCE_DIR}/include")
endif()

# ScritchAudio requires a normalized library name
squirreljme_target_binary_name(ScritchAudioOSS
	"squirreljme-scritchaudio-oss")
squirreljme_target_binary_output(ScritchAudioOSS
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchAudioOSS)
