/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Represents class files.
 *
 * @since 2022/06/07
 */

#ifndef SQUIRRELJME_JCLASS_HXX
#define SQUIRRELJME_JCLASS_HXX

#include <memory>
#include "java/jobject.hxx"

/*--------------------------------------------------------------------------*/

/**
 * Represents a Java @c Class which is based on an object.
 *
 * @since 2022/06/07
 */
class sjme_jclass : public sjme_jobject
{
private:
	/** The super class of this class. */
	std::shared_ptr<sjme_jclass> javaSuperClass;

	/** The interfaces this class implements. */
	std::shared_ptr<sjme_jclass> javaInterfaces;
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_JCLASS_HXX */
