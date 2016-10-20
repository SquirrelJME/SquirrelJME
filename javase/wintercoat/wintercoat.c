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
 * This creates a new instance of the Java virtual machine.
 *
 * @param pvm The output virtual machine
 * @param penv The output environment.
 * @param pargs The input arguments.
 * @return JNI_OK on success, otherwise other errors.
 * @since 2016/10/19
 */
_JNI_IMPORT_OR_EXPORT_ jint JNICALL JNI_CreateJavaVM(JavaVM** pvm, void** penv,
	void* pargs)
{
	JavaVMInitArgs* initargs;
	jint i, n;
	JavaVMOption* op;
	char* opstr;
	char* eq;
	int len, klen;
	WC_JavaVM* jvm;
	WC_JNIEnv* env;
	WC_StaticString* sk;
	WC_StaticString* sv;
	WC_SystemPropertyLink* splink;
	
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
	
	// Allocate virtual machine
	jvm = (WC_JavaVM*)WC_ForcedMalloc(sizeof(*jvm));
	
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
			WC_VERBOSE(WC_VERBOSE_MODE_DEBUG, "Define property: %s", opstr);	
			
			// {@squirreljme.error WC07 System property definition requires an
			// equal sign. (The property definition)}
			eq = strchr(opstr, '=');
			if (eq == NULL)
			{
				WC_VERBOSE(WC_VERBOSE_MODE_ERROR, "WC07 %s", opstr);
				return JNI_EINVAL;
			}
			
			// Length of property name
			klen = eq - (opstr + 2);
			
			// Move past equal sign
			eq++;
			
			// Allocate static strings
			sk = WC_GetStaticString(opstr + 2, klen);
			sv = WC_GetStaticString(eq, (opstr + len) - eq);
			
			// Allocate new link
			splink = WC_ForcedMalloc(sizeof(*splink));
			
			// Set key and value
			splink->key = sk;
			splink->value = sv;
			
			// Link to the chain
			splink->next = jvm->furry.syspropchain;
			jvm->furry.syspropchain = splink;
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
			WC_VERBOSE(WC_VERBOSE_MODE_ERROR, "WC05 %s", opstr);
			return JNI_EINVAL;
		}
	}
	
	// Set target VM
	(*pvm) = (JavaVM*)jvm;
	
	// Setup function pointer tables
	WC_TODO();
	
	// Return environment
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
void* WC_ForcedMalloc(jint plen)
{
	void* rv;
	
	// {@squirreljme.error WC08 Forced allocation length is zero or
	// negative.}
	WC_ASSERT("WC08", plen <= 0);
	
	// Allocate
	rv = malloc(plen);
	
	// {@squirreljme.error WC09 Forced allocation failed.}
	WC_ASSERT("WC09", rv == NULL);
	
	// Clear it
	memset(rv, 0, plen);
	
	// Return it
	return rv;
}

/**
 * {@inheritDoc}
 * @since 2016/10/19
 */
WC_StaticString* WC_GetStaticString(const char* const pstr, jint plen)
{
	WC_StaticString* rv;
	char* buf;
	int i;
	
	// {@squirreljme.error WC0a Cannot allocate NULL static string.}
	WC_ASSERT("WC0a", pstr == NULL);
	
	// {@squirreljme.error WC0b Cannot allocate static string with negative
	// length.}
	WC_ASSERT("WC0b", plen < 0);
	
	// {@squirreljme.error WC0c Null byte in string would overflow size of
	// integer.}
	WC_ASSERT("WC0c", (plen + 1) < 0);
	
	// Allocate
	rv = (WC_StaticString*)WC_ForcedMalloc(sizeof(*rv));
	
	// Copy string to buffer
	buf = (char*)WC_ForcedMalloc(plen + 1);
	for (i = 0; i < plen; i++)
		buf[i] = pstr[i];
	
	// Store into the structure and set the lengt
	rv->utflen = plen;
	rv->utfchars = buf;
	
	// Debug
	WC_VERBOSE(WC_VERBOSE_MODE_DEBUG, "Static: %s %d", buf, plen);
	
	// Return it
	return rv;
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
#define BUFFER_SIZE 256
	va_list ap;
	char* buf;
	
	// Printing for this mode?
	if (1)
	{
		// Need arguments
		va_start(ap, pmesg);
		
		// Allocate buffer for string
		buf = malloc(sizeof(*buf) * BUFFER_SIZE);
		if (buf != NULL)
		{
			// Print to the buffer
			vsnprintf(buf, BUFFER_SIZE, (pmesg == NULL ? "NULL" : pmesg), ap); 
			buf[BUFFER_SIZE - 1] = '\0';
		
			// Print to output stream
			fprintf(stderr, "WinterCoat: VERBOSE %s:%d: %s [%d]: ",
				(pin == NULL ? "NULL" : pin), pline,
				(pfunc == NULL ? "NULL" : pfunc), pmode);
			fputs(buf, stderr);
			fputs("\n", stderr);
			
			// Free the buffer
			free(buf);
		}
		
		// Stop
		va_end(ap);
	}
#undef BUFFER_SIZE
}

