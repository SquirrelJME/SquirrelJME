# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Common find support
squirreljme_include("find.cmake")

# Find headers
include(CheckIncludeFile)

# Find soundcard header
CHECK_INCLUDE_FILE("soundcard.h" OSS_INCLUDE_BASE)
if(NOT OSS_INCLUDE_BASE)
	CHECK_INCLUDE_FILE("sys/soundcard.h" OSS_INCLUDE_SYS)
endif()

# If either are set we have OSS
if(OSS_INCLUDE_BASE)
	set(OSS_INCLUDE_FILE "soundcard.h")
elseif(OSS_INCLUDE_SYS)
	set(OSS_INCLUDE_FILE "sys/soundcard.h")
endif()
