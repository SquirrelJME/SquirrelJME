/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: WinterCoat
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------
// This is a miniature Java VM virtual machine for simplistic sanity purposes.
// -------------------------------------------------------------------------*/

#include <stdlib.h>

#include "jni.h"
#include "jvm.h"

#include "wintercoat.h"

/**
 * {@inheritDoc}
 * @since 2016/10/19
 */
void WC_TODO_real(const char* const pin, int pline, const char* const pfunc)
{
	// Print it.
	fprintf(stderr, "WinterCoat: TODO %s:%d: %s\n", (pin == NULL ? "NULL" :
		pin), pline, (pfunc == NULL ? "NULL" : pfunc));
	
	// Never go back.
	abort();
}

/**
 * {@inheritDoc}
 * @since 2016/10/19
 */
void WC_ASSERT_real(const char* const pin, int pline, const char* const pfunc,
	const char* const pcode, int pcond)
{
	// Check condition
	if (pcond != 0)
	{
		// Print failue state
		fprintf(stderr, "WinterCoat: ASSERT %s:%d: %s, %s\n",
			(pin == NULL ? "NULL" : pin), pline,
			(pfunc == NULL ? "NULL" : pfunc),
			(pcode == NULL ? "NULL" : pcode));
		
		// Failed
		abort();
	}
}

/**
 *
 *
 * @param pargs VM arguments, output.
 * @return JNI_OK on success.
 * @since 2016/10/19
 */
_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_GetDefaultJavaVMInitArgs(void* pargs)
{
	// {@squirreljme.error AO01 VM initialization arguments is NULL.}
	WC_ASSERT("AO01", pargs == NULL);
	
	WC_TODO();
}

_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_CreateJavaVM(JavaVM** pvm, void** penv,
	void* args)
{
	WC_TODO();
}

_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_GetCreatedJavaVMs(JavaVM** pvm, jsize psz, jsize* pcount)
{
	WC_TODO();
}

