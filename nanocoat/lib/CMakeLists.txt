# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# The SquirrelJME base library is added in the base root project and not
# by this one

if(SQUIRRELJME_ENABLE_FPIC)
	set(SQUIRRELJME_BASE_LIB BasePIC)
	set(SQUIRRELJME_SCRITCHAUDIO_CORE_LIB ScritchAudioCorePIC)
	set(SQUIRRELJME_SCRITCHUI_CORE_LIB ScritchUICorePIC)
	set(SQUIRRELJME_SCRITCHUI_FB_LIB ScritchUIFBPIC)
else()
	set(SQUIRRELJME_BASE_LIB Base)
	set(SQUIRRELJME_SCRITCHAUDIO_CORE_LIB ScritchAudioCore)
	set(SQUIRRELJME_SCRITCHUI_CORE_LIB ScritchUICore)
	set(SQUIRRELJME_SCRITCHUI_FB_LIB ScritchUIFB)
endif()

# Third party support libraries that come from outside SquirrelJME
add_subdirectory(3rdparty)

# ScritchUI and related
add_subdirectory(scritchaudio)
add_subdirectory(scritchinput)
add_subdirectory(scritchui)
