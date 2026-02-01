# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Required minimum
cmake_minimum_required(VERSION 3.12)

# This is always needed
project(CheckFindJNI
	LANGUAGES)

# Try the normal package find here
find_package(JNI QUIET)
