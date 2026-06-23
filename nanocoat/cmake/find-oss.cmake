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
	set(OSS_FOUND YES)
	set(OSS_INCLUDE_FILE "soundcard.h")
elseif(OSS_INCLUDE_SYS)
	set(OSS_FOUND YES)
	set(OSS_INCLUDE_FILE "sys/soundcard.h")

# Fallback to self provided OSS
elseif("${SQUIRRELJME_SYSTEM}" STREQUAL "bsd" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "linux" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "solaris")
	set(OSS_FOUND YES)
	set(OSS_INCLUDE_DIR "${CMAKE_SOURCE_DIR}/include/3rdparty/oss")
	set(OSS_INCLUDE_FILE "soundcard.h")

# Not available
else()
	set(OSS_FOUND NO)
endif()
