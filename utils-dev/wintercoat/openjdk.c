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
 * This supports OpenJDK.
 *
 * @since 2016/10/19
 */

#include "wintercoat.h"
#include "bootpath.h"

#if defined(WINTERCOAT_OPENJDK)

/****************************************************************************/

/** Has the boot classpath been initialized? */
static jboolean didinitializebcp = JNI_FALSE;

/** The OpenJDK root. */
static const char* openjdkrootdir = NULL;

/**
 * Initializes OpenJDK's bootstrap classes.
 *
 * @since 2017/02/05
 */
static void WC_OpenJDK_InitBootClasses(void)
{
	// For now, hardcode the path
	WC_VERBOSE(WC_VERBOSE_MODE_TODO, "Hardcoding the OpenJDK root", 0);
	openjdkrootdir = "/usr/lib/jvm/java-8-openjdk-powerpc";
	
	WC_TODO();
}

/**
 * Finds and loads a class from the bootstrap class loader.
 *
 * @param penv The owning environment.
 * @param pname The class name to locate.
 * @return The loaded class or NULL if not found.
 * @since 2016/10/19
 */
JNIEXPORT jclass JNICALL JVM_FindClassFromBootLoader(JNIEnv* penv,
	const char* pname)
{
	// {@squirreljme.error WC0i No environment specified.}
	WC_ASSERT("WC0i", penv == NULL);
	
	// {@squirreljme.error WC0j No class name specified.}
	WC_ASSERT("WC0j", pname == NULL);
	
	// Debug
	WC_VERBOSE(WC_VERBOSE_MODE_CLASS, "JVM_FindClassFromBootLoader(%s)",
		pname);
	
	// Need to initialize the classpath?
	if (didinitializebcp == JNI_FALSE)
		WC_OpenJDK_InitBootClasses();
	
	WC_TODO();
}

/****************************************************************************/

#endif


