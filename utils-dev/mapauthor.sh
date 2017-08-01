#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Maps author to a name.

# Force C locale
export LC_ALL=C

# Directory of this script
__exedir="$(dirname -- "$0")"

case "$1" in
	squirreljme)
		echo "SquirrelJME";
		;;
	
	steven.gawroriski)		echo "Stephanie Gawroriski" ;;
	steven-gawroriski)		echo "Stephanie Gawroriski" ;;
	stephanie.gawroriski)	echo "Stephanie Gawroriski" ;;
	stephanie-gawroriski)	echo "Stephanie Gawroriski" ;;
	xer)					echo "Stephanie Gawroriski" ;;
		
	*)
		echo "Unknown";
		;;
esac


