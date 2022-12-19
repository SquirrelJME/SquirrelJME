/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "tests.h"
#include "jnistub.h"

/**
 * Tests initialization of a new virtual machine along with its teardown.
 *
 * @since 2022/12/18
 */
SJME_TEST_PROTOTYPE(testJniVmNew)
{
	sjme_vmState* vm = NULL;
	sjme_vmThread* initThread = NULL;

	/* Start new virtual machine. */
	if (SJME_INTERFACE_ERROR_NONE != sjme_vmNew(&vm,
		&initThread, NULL, shim->jniSysApi))
		return FAIL_TEST(1);

	sjme_todo("Implement this?");
	return FAIL_TEST(1);
}
