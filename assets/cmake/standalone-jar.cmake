# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds the Standalone Jar

# The name of the target standalone Jar
set(SQUIRRELJME_JAR_BASENAME
	"squirreljme-standalone-${SQUIRRELJME_VERSION}.jar")

# Add rules and detection steps for the three
squirreljme_include("standalone-jar-compiled.cmake")
squirreljme_include("standalone-jar-download.cmake")
squirreljme_include("standalone-jar-cached.cmake")

# Merging of the Base Standalone with All Natives
