/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Defines for packs and libraries.
 * 
 * @since 2022/01/09
 */

#ifndef SQUIRRELJME_FORMAT_DEF_H
#define SQUIRRELJME_FORMAT_DEF_H

#include <list>
#include <memory>
#include "sjmerc.h"

/*--------------------------------------------------------------------------*/

/**
 * Instance of a pack which is a singular ROM which contains multiple JARs or
 * sets of classes.
 * 
 * @since 2021/09/19
 */
typedef struct sjme_packInstance sjme_packInstance;

/**
 * Instance of a library which represents a single JAR or set of classes.
 * 
 * @since 2021/09/19
 */ 
typedef struct sjme_libraryInstance sjme_libraryInstance;

/**
 * Represents a class path.
 * 
 * @since 2022/01/09
 */
class sjme_classPath : std::list<std::shared_ptr<sjme_libraryInstance>>
{
public:
	
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_FORMAT_DEF_H */
