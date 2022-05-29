/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Scaffold definitions.
 * 
 * @since 2022/01/05
 */

#ifndef SQUIRRELJME_SCAFDEF_H
#define SQUIRRELJME_SCAFDEF_H

#include "sjmerc.h"
#include "utf.h"

/*--------------------------------------------------------------------------*/

/**
 * The state of any given engine.
 * 
 * @since 2022/01/03
 */
typedef struct sjme_engineState sjme_engineState;

/**
 * The state of a single task within the engine.
 * 
 * @since 2022/01/09
 */
typedef struct sjme_engineTask sjme_engineTask;

/**
 * The state of a single thread within the engine.
 * 
 * @since 2022/01/03
 */
typedef struct sjme_engineThread sjme_engineThread;

/**
 * This contains the information for the class loader.
 * 
 * @since 2022/03/23
 */
typedef struct sjme_classLoader sjme_classLoader;

/**
 * Represents the main arguments to a program.
 * 
 * @since 2022/01/09
 */
typedef struct sjme_mainArgs
{
	/** The number of arguments that exist. */
	sjme_jint count;
	
	/** The main arguments. */
	sjme_utfString* args[0];
} sjme_mainArgs;

/**
 * Returns the size that would be used for @c sjme_mainArgs.
 * 
 * @param count The number of arguments to include.
 * @return The allocated memory size for the type.
 * @since 2022/03/09
 */
#define SJME_SIZEOF_MAIN_ARGS(count) (sizeof(sjme_mainArgs) + \
	((count) * sizeof(sjme_utfString*)))

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_SCAFDEF_H */
