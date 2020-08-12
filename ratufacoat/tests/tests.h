/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#ifndef SQUIRRELJME_TESTS_H
#define SQUIRRELJME_TESTS_H

#include <stdlib.h>

/** Macro for prototyping tests. */
#define SJME_TEST_PROTOTYPE(name) int name(void)

/** Available tests. */
SJME_TEST_PROTOTYPE(testOpCodes);
SJME_TEST_PROTOTYPE(testNothing);

#endif /* SQUIRRELJME_TESTS_H */
