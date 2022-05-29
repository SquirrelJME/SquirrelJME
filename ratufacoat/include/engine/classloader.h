/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Contians definitions for the class loader and otherwise.
 * 
 * @since 2022/03/19
 */

#ifndef SQUIRRELJME_CLASSLOADER_H
#define SQUIRRELJME_CLASSLOADER_H

#include "engine/scafdef.h"
#include "format/def.h"

/*--------------------------------------------------------------------------*/

struct sjme_classLoader
{
	/** The class path to source JARs from. */
	sjme_classPath* classPath;
};

/**
 * Initializes the class loader instance.
 * 
 * @param outLoader The output class loader.
 * @param classPath The class path to initialize with.
 * @param error Any resultant error state.
 * @return If creation was successful.
 * @since 2022/03/23
 */
sjme_jboolean sjme_classLoaderNew(sjme_classLoader** outLoader,
	sjme_classPath* classPath, sjme_error* error);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_CLASSLOADER_H */
