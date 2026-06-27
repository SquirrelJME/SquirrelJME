# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
squirreljme_multilib_add_static_library(ScritchAudioSoftMix OBJECT
	softmixCore.c
	softmixLoop.c
	softmixMidi.c
	softmixMix.c
	softmixStream.c)

# Include main headers
squirreljme_multilib_static_include_directories(ScritchAudioSoftMix PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Export this target
squirreljme_multilib_export(ScritchAudioSoftMix)

