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

/**
 * Macro for prototyping tests.
 *
 * @param name The name of the test to wrap.
 */
#define SJME_TEST_PROTOTYPE(name) int name(void)

/* Available tests. */
SJME_TEST_PROTOTYPE(testMemHandleCycle);
SJME_TEST_PROTOTYPE(testMemHandleInit);
SJME_TEST_PROTOTYPE(testMemHandleInvalid);
SJME_TEST_PROTOTYPE(testNothing);
SJME_TEST_PROTOTYPE(testOpCodes);

#endif /* SQUIRRELJME_TESTS_H */
