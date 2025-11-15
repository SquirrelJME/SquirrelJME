# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Handling of building standalone natives for both emulator-base
# and NanoCoat, these are for later repackaging

# Build natives for every known version of GCC on the system
foreach(gccMap IN LISTS "${SQUIRRELJME_GCC_MAP}")
	# Obtain back the system and architecture
	squirreljme_unmap(systemNormal 0 "${gccMap}")
	squirreljme_unmap(archNormal 1 "${gccMap}")

	# Where is the GCC executable?
	set(gccExe "${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")


endforeach()
