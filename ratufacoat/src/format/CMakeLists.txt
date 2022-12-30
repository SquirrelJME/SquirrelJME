# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Format support

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Define the library
add_library(SquirrelJMEFormat OBJECT
	detect.c
	format.c
	library.c
	memfile.c
	pack.c
	sqc.c
	sqclib.c
	sqcpack.c
	zip.c)

# Common C Directives
squirreljme_common_c(SquirrelJMEFormat)

# Make this position independent so it can be linked into shared libraries
squirreljme_enable_pic(SquirrelJMEFormat)
