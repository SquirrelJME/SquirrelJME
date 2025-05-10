# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchAudioOSS SHARED
	$<TARGET_OBJECTS:${SQUIRRELJME_BASE_LIB}>
	$<TARGET_OBJECTS:${SQUIRRELJME_SCRITCHAUDIO_CORE_LIB}>
	ossCore.c
	ossMidi.c
	ossStream.c)

# Always make it PIC
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchAudioOSS
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# The file where the include lives
target_compile_definitions(ScritchAudioOSS PUBLIC
	SQUIRRELJME_OSS_INCLUDE_FILE="${OSS_INCLUDE_FILE}")

# Include main headers
target_include_directories(ScritchAudioOSS PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchAudio requires a normalized library name
squirreljme_target_binary_name(ScritchAudioOSS
	"squirreljme-scritchaudio-oss")
squirreljme_target_binary_output(ScritchAudioOSS
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Generate exports
squirreljme_target_shared_library_exports(ScritchAudioOSS)
