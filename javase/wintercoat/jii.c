/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Implements the Java invoke interface calls.
 *
 * @since 2016/10/19
 */

#include "jii.h"

/**
 * {@inheritDoc}
 * @since 2016/10/19
 */
jint JNICALL WC_JII_AttachCurrentThread(JavaVM* pvm, void** penv, void* pargs)
{
	// {@squirreljme.error WC0d The owning virtual machine is NULL.}
	WC_ASSERT("WC0d", pvm == NULL);
	
	// {@squirreljme.error WC0e No output environment was specified.}
	WC_ASSERT("WC0e", penv == NULL);
	
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_AttachCurrentThreadAsDaemon(JavaVM* pvm, void** penv,
	void* pargs)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_DestroyJavaVM(JavaVM* pvm)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_DetachCurrentThread(JavaVM* pvm)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_GetEnv(JavaVM* pvm, void** penv, jint pversion)
{
	WC_TODO();
	return 1;
}

