/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Represents a Java @c Object.
 *
 * @since 2022/06/07
 */

#ifndef SQUIRRELJME_JOBJECT_HXX
#define SQUIRRELJME_JOBJECT_HXX

#include <memory>
#include "sjmerc.h"

/*--------------------------------------------------------------------------*/

class sjme_jclass;

/**
 * Base class representation for Java @c Object.
 *
 * @since 2022/06/07
 */
class sjme_jobject
{
private:
	/** The identity hash code of this object, for save states. */
	sjme_jint identityHashCode;

	/** The Java class that this object is an instance of. */
	std::shared_ptr<sjme_jclass> javaClass;
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_JOBJECT_HXX */
