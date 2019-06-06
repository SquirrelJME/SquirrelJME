/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Source.
 *
 * @since 2019/06/02
 */

#include "sjmerc.h"

void* sjme_malloc(sjme_jint size)
{
	if (size <= 0)
		return NULL;
}

/** Executes code running within the JVM. */
int sjme_jvmexec(sjme_jvm* jvm)
{
	if (jvm == NULL)
		return 0;
	
	return 0;
}

/** Creates a new instance of the JVM. */
sjme_jvm* sjme_jvmnew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs)
{
	sjme_jvmoptions nulloptions;
	
	/* We need native functions. */
	if (nativefuncs == NULL)
		return NULL;
	
	/* If there were no options specified, just use a null set. */
	if (options == NULL)
	{
		memset(&nulloptions, 0, sizeof(nulloptions));
		options = &nulloptions;
	}
}
