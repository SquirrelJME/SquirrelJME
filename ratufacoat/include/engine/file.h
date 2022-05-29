/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Handling implementation for files and otherwise, for direct file system
 * access.
 * 
 * @since 2022/05/22
 */

#ifndef SQUIRRELJME_FILE_H
#define SQUIRRELJME_FILE_H

#include "atomic.h"

/*--------------------------------------------------------------------------*/

/**
 * Functions which are used to access files.
 * 
 * @since 2022/05/22
 */
typedef struct sjme_fileFunctions
{
	int junk;
} sjme_fileFunctions;

/**
 * Represents a file that is potentially opened and may be accessed
 * accordingly.
 * 
 * @since 2022/05/22
 */
typedef struct sjme_file
{
	/** The functions used to access this file. */
	const sjme_fileFunctions* functions;
	
	/** Can read from this file? */
	sjme_jboolean isReadable;
	
	/** Can write to this file? */
	sjme_jboolean isWritable;
} sjme_file;

/**
 * Represents a special file type.
 * 
 * @since 2022/05/22
 */
typedef enum sjme_specialFileType
{
	/** Discard output. */
	SJME_SPECIAL_FILE_DISCARD_OUTPUT,
	
	/** Always EOF on read. */
	SJME_SPECIAL_FILE_ALWAYS_EOF_INPUT,
	
	/** The number of special file type. */
	SJME_NUM_SPECIAL_FILE_TYPES
} sjme_specialFileType;

/**
 * Opens a special file that performs in a specific way.
 * 
 * @param fileType The type of special file to open.
 * @param outFile The output file.
 * @param error Any resultant error state.
 * @return If opening the special file was a success.
 * @since 2022/05/22
 */
sjme_jboolean sjme_newSpecialFile(sjme_specialFileType fileType,
	sjme_file** outFile, sjme_error* error);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_FILE_H */
