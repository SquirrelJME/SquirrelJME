# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME C Compiler

cmake_minimum_required(VERSION 3.7)

project(sjmecc
	VERSION 0.3.0
	DESCRIPTION "SquirrelJME C Compiler"
	HOMEPAGE_URL "https://squirreljme.cc"
	LANGUAGES C)

# Sources for the compiler
add_subdirectory(src)
