# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Reconfigure the CMake project if detected to be out of date

# Globbing is _NOT_ recommended, however our inputs come from the Java side
# so if the input Jars or anything ever change everything needs to be
# reconfigured properly...
file(GLOB_RECURSE romFileSet
	LIST_DIRECTORIES false
	RELATIVE "${CMAKE_SOURCE_DIR}/rom/"
	CONFIGURE_DEPENDS
	"${CMAKE_SOURCE_DIR}/rom/*.jar"
	"${CMAKE_SOURCE_DIR}/rom/*.list"
	"${CMAKE_SOURCE_DIR}/rom/*.nano")
