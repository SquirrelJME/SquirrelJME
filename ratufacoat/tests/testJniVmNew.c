/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "jnistub.h"
#include "sjmejni/shadow.h"
#include "tests.h"

/**
 * Tests initialization of a new virtual machine along with its teardown.
 *
 * @since 2022/12/18
 */
SJME_TEST_PROTOTYPE(testJniVmNew)
{
	sjme_vmState* vm = NULL;
	sjme_vmThread* thread = NULL;
	sjme_vmStateShadow* vmShadow = NULL;
	sjme_vmThreadShadow* threadShadow = NULL;

	/* Start new virtual machine. */
	if (!sjme_vmNew(&vm, &thread, NULL,
		shim->jniSysApi, &shim->error))
		return FAIL_TEST(1);

	/* The shadow of a virtual machine should be itself. */
	vmShadow = sjme_vmGetStateShadow(vm);
	if ((void*)(*vm) != (void*)vmShadow ||
		vmShadow->shadowPtr != &vmShadow->functions ||
		(*vm) != &vmShadow->functions)
		return FAIL_TEST(2);

	/* Reserved1 should be the shadow. */
	if (vmShadow != (*vm)->reserved1)
		return FAIL_TEST(3);

	/* The shadow of a thread should be itself. */
	threadShadow = sjme_vmGetThreadShadow(thread);
	if ((void*)(*thread) != (void*)threadShadow ||
		threadShadow->shadowPtr != &threadShadow->functions ||
		(*thread) != &threadShadow->functions)
		return FAIL_TEST(4);

	/* Reserved1 should be the thread shadow. */
	if (threadShadow != (*thread)->reserved1)
		return FAIL_TEST(5);

	/* The shadowed thread should also have the VM as its parent. */
	if (threadShadow->parentVm != vmShadow)
		return FAIL_TEST(6);

	/* Immediately destroy the VM following. */
	if ((*vm)->DestroyJavaVM(vm) != SJME_ERROR_NONE)
		return FAIL_TEST(7);

	return PASS_TEST();
}
