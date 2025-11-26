# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Pipeline export

# CMake-only Export
squirreljme_include("pipeline-export-cmake.cmake")

# OCI Container Export
if(OCI_EXECUTABLE)
	squirreljme_include("pipeline-export-oci.cmake")
endif()

# CircleCI Export
squirreljme_include("pipeline-export-circleci.cmake")
