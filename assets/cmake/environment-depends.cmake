# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Environment dependencies

# Locate an OCI program interface
find_program(OCI_EXECUTABLE
	NAMES podman docker)

# Locate Fossil SCM
find_program(Fossil_EXECUTABLE
	NAMES fossil)

# Locate Git SCM
find_program(Git_EXECUTABLE
	NAMES git)

# Locate Flatpak
find_program(Flatpak_EXECUTABLE
	NAMES flatpak)

# Print results of everything that was found
message(STATUS "OCI       : ${OCI_EXECUTABLE}")
message(STATUS "Fossil    : ${Fossil_EXECUTABLE}")
message(STATUS "Git       : ${Git_EXECUTABLE}")
message(STATUS "Flatpak   : ${Flatpak_EXECUTABLE}")

