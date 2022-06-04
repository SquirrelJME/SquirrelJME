/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "builtin.h"
#include "jvm.h"
#include "engine/scaffold.h"
#include "enginestub.h"

/**
 * Tests initialization of the engine initialization.
 *
 * @since 2022/02/16
 */
SJME_TEST_PROTOTYPE(testEngineInit)
{
	sjme_engineConfig config;
	sjme_engineState* state;

	/* Needs built-in ROM to work properly. */
	if (sjme_builtInRomSize <= 0)
		return SKIP_TEST();

	/* Setup base config. */
	memset(&config, 0, sizeof(config));
	config.romPointer = sjme_builtInRomData;
	config.romSize = sjme_builtInRomSize;
	config.frontBridge = &sjme_testStubFrontBridge;

	/* Initialize engine. */
	if (!sjme_engineNew(&config, &state,
		&shim->error))
		return FAIL_TEST(1);

	/* Destroy it. */
	if (!sjme_engineDestroy(state, &shim->error))
		return FAIL_TEST(2);

	/* Is okay! */
	return PASS_TEST();
}
