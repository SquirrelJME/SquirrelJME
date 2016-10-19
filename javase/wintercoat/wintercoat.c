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
#include <string.h>

#include "jni.h"
#include "jvm.h"

#include "wintercoat.h"

/**
 *
 *
 * @param pvm The output virtual machine
 * @param penv The output environment.
 * @param pargs The input arguments.
 * @return JNI_OK on success, otherwise other errors.
 */
_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_CreateJavaVM(JavaVM** pvm, void** penv,
	void* pargs)
{
	JavaVMInitArgs* initargs;
	jint i, n;
	JavaVMOption* op;
	char* opstr;
	int len;
	
	// {@squirreljme.error WC02 No output JavaVM pointer specified.}
	WC_ASSERT("WC02", pvm == NULL);
	
	// {@squirreljme.error WC03 No output environment pointer specified.}
	WC_ASSERT("WC03", penv == NULL);
	
	// {@squirreljme.error WC04 No input argument pointer specified.}
	WC_ASSERT("WC04", pargs == NULL);
	
	// Cast
	initargs = (JavaVMInitArgs*)pargs;
	
	// Too new a version?
	if (initargs->version > JNI_VERSION_1_8)
		return JNI_EVERSION;
	
	// Handle all options
	n = initargs->nOptions;
	for (i = 0; i < n; i++)
	{
		// Get option
		op = &initargs->options[i];
		opstr = op->optionString;
		
		// {@squirreljme.error WC06 A passed option string was NULL.}
		WC_ASSERT("WC06", opstr == NULL);
		
		// Get length
		len = strlen(opstr);
		
		// Replace print to file?
		if (0 == strcmp(opstr, "vfprintf"))
			WC_TODO();
		
		// Replace exit?
		else if (0 == strcmp(opstr, "exit"))
			WC_TODO();
		
		// Replace abort?
		else if (0 == strcmp(opstr, "abort"))
			WC_TODO();
		
		// Define system property?
		else if (len >= 2 && 0 == strncmp(opstr, "-D", 2))
		{
			// Debug
			WC_VERBOSE(WC_VERBOSE_MODE_DEBUG, "Define property: %s\n", opstr);	
			
			WC_TODO();
		}
		
		// verbose all?
		else if (0 == strcmp(opstr, "-verbose"))
			WC_TODO();
		
		// verbose class loader?
		else if (0 == strcmp(opstr, "-verbose:class"))
			WC_TODO();
		
		// verbose garbage collection?
		else if (0 == strcmp(opstr, "-verbose:gc"))
			WC_TODO();
		
		// verbose JNI?
		else if (0 == strcmp(opstr, "-verbose:jni"))
			WC_TODO();
		
		// {@squirreljme.error WC05 Invaid JVM argument. (The argument)}
		else
		{
			fprintf(stderr, "WinterCoat: WC05 %s\n", opstr);
			return JNI_EINVAL;
		}
	}
	
	WC_TODO();
	
	// Ok
	return JNI_OK;
}

_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_GetCreatedJavaVMs(JavaVM** pvm, jsize psz, jsize* pcount)
{
	WC_TODO();
}

/**
 * Checks if the requested virtual machine version is supported by this JVM.
 *
 * @param pargs Contains the VM arguments used to check the version. The
 * version field is checked and on success is changed to the actual supported
 * version.
 * @return JNI_OK on success, JNI_EVERSION if the version is invalid.
 * @since 2016/10/19
 */
_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_GetDefaultJavaVMInitArgs(void* pargs)
{
	JavaVMInitArgs* initargs;
	
	// {@squirreljme.error WC01 VM initialization arguments is NULL.}
	WC_ASSERT("WC01", pargs == NULL);
	
	// Cast
	initargs = (JavaVMInitArgs*)pargs;
	
	// Has to at most be Java 8
	if (initargs->version <= JNI_VERSION_1_8)
	{
		initargs->version = JNI_VERSION_1_8;
		return JNI_OK;
	}
	
	// Otherwise fail
	return JNI_EVERSION;
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
void WC_VERBOSE_real(const char* const pin, int pline,
	const char* const pfunc, int pmode,
	const char* const pmesg, ...)
{
	WC_TODO();
}

