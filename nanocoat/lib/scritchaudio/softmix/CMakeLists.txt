# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare library
add_library(ScritchAudioSoftMix OBJECT
	softmixCore.c
	softmixLoop.c
	softmixMidi.c
	softmixMix.c
	softmixStream.c)

# Include main headers
target_include_directories(ScritchAudioSoftMix PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Export this target
squirreljme_export(ScritchAudioSoftMix)

