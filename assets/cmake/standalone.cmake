# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Building of Standalone SquirrelJME

# Natives are needed for the Standalone Jar
squirreljme_include("standalone-natives.cmake")

# Now handle building the Standalone Jar
squirreljme_include("standalone-jar.cmake")
