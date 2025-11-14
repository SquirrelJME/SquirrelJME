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

# Locate Install4J
find_program(Install4J_EXECUTABLE
	NAMES install4j)
find_program(Install4JC_EXECUTABLE
	NAMES install4jc)

# Print results of everything that was found
message(STATUS "OCI       : ${OCI_EXECUTABLE}")
message(STATUS "Fossil    : ${Fossil_EXECUTABLE}")
message(STATUS "Git       : ${Git_EXECUTABLE}")
message(STATUS "Flatpak   : ${Flatpak_EXECUTABLE}")
message(STATUS "install4j : ${Install4J_EXECUTABLE}")
message(STATUS "install4jc: ${Install4JC_EXECUTABLE}")

