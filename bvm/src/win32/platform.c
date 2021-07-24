/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>
#include "entry.h"

/**
 * Main Windows entry point.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return The exit status of the process.
 * @since 2021/07/24
 */
int main(int argc, char** argv)
{
	if (bvm_main(argc, argv) == sjme_true)
		return EXIT_SUCCESS;
	return EXIT_FAILURE;
}

