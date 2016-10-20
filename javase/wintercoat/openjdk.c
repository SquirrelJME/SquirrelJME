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

#if defined(WINTERCOAT_OPENJDK)

/****************************************************************************/

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
	
	WC_TODO();
}

/****************************************************************************/

#endif


