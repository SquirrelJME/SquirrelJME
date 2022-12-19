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

#include "sjmerc.h"
#include "native.h"
#include "jnistub.h"

/**
 * Testing shim structure.
 * 
 * @since 2021/03/04
 */
typedef struct sjme_testShim
{
	/** Error state. */
	sjme_error error;
	
	/** Native function support. */
	sjme_nativefuncs* nativeFunctions;

	/** JNI system API. */
	sjme_vmSysApi* jniSysApi;
} sjme_testShim;

/**
 * Macro for prototyping tests.
 *
 * @param name The name of the test to wrap.
 */
#define SJME_TEST_PROTOTYPE(name) \
	SJME_EXTERN_C sjme_jint name(sjme_testShim* shim)

/** Pass test. */
#define PASS_TEST() SJME_JINT_C(0)

/** Fail test. */
#define FAIL_TEST(n) ((SJME_JINT_C(n)) != 0 ? (SJME_JINT_C(n)) : -1)

/** Fail test with sub value. */
#define FAIL_TEST_SUB(n, y) ((FAIL_TEST(n) << 16) | \
	((y) & SJME_JINT_C(0xFFFF)))

/** Fail test with three markers. */
#define FAIL_TEST_TRI(n, y, z) ((FAIL_TEST(n) << 24) | \
	(((y) & SJME_JINT_C(0xFF)) << 16) | ((z) & SJME_JINT_C(0xFF)))

/** Skip test. */
#define SKIP_TEST() SJME_JINT_C(-65535)

/* Available tests. */
#include "prototype.h"

#endif /* SQUIRRELJME_TESTS_H */
