# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# The SquirrelJME base library is added in the base root project and not
# by this one!

# Directory where ScritchUi/ScritchAudio lists should be placed
if(DEFINED ENV{SQUIRRELJME_BINARY_OUTPUT_DIR})
	set(SQUIRRELJME_SCRITCHLIST_DIR
		"$ENV{SQUIRRELJME_BINARY_OUTPUT_DIR}")
elseif(DEFINED SQUIRRELJME_BINARY_OUTPUT_DIR)
	set(SQUIRRELJME_SCRITCHLIST_DIR
		"${SQUIRRELJME_BINARY_OUTPUT_DIR}")
else()
	set(SQUIRRELJME_SCRITCHLIST_DIR
		"${CMAKE_BINARY_DIR}")
endif()

# Third party support libraries that come from outside SquirrelJME
add_subdirectory(3rdparty)

# Include common macros to make things easier for Scritch interfaces
squirreljme_include("scritchui.cmake")

# ScritchUi and related
add_subdirectory(scritchaudio)
add_subdirectory(scritchinput)
add_subdirectory(scritchui)

# JNI/SquirrelJME Bridge
add_subdirectory(jnisjme)
