/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Support for multiple libraries at once.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_PACK_H
#define SQUIRRELJME_PACK_H

#include "atomic.h"
#include "sjmerc.h"
#include "error.h"
#include "engine/scafdef.h"
#include "format/def.h"
#include "format/detect.h"
#include "format/format.h"
#include "format/library.h"
#include "memchunk.h"
#include "utf.h"

/*--------------------------------------------------------------------------*/

/** Is this a SpringCoat Pack? */
#define SJME_PACK_FLAG_IS_SPRINGCOAT INT32_C(0x1)

/**
 * This represents a library driver that is available for usage.
 * 
 * @since 2021/09/12
 */
typedef struct sjme_packDriver
{
	/** Is this for the given libraries driver? */
	sjme_formatDetectFunction detect;
	
	/** Initialization function. */
	sjme_formatInitFunction init;
	
	/** Destroy function. */
	sjme_formatDestroyFunction destroy;
	
	/**
	 * Queries the number of libraries in the pack file.
	 * 
	 * @param instance The instance to query.
	 * @param error The error state.
	 * @return The number of libraries, will be a negative value if it could
	 * not be assessed.
	 * @since 2021/11/07
	 */
	sjme_jint (*queryNumLibraries)(sjme_packInstance* instance,
		sjme_error* error);
	
	/**
	 * Queries the flags for the pack.
	 * 
	 * @param instance The instance to query.
	 * @param error The error state.
	 * @return The pack flags, if this could not be determined or is not valid
	 * then @c 0 must be returned.
	 * @since 2022/01/08
	 */
	sjme_jint (*queryPackFlags)(sjme_packInstance* instance,
		sjme_error* error);
	
	/**
	 * Attempts to locate a chunk for the given library, so that it may be
	 * read accordingly.
	 * 
	 * @param instance The pack instance we are loading for.
	 * @param outChunk The chunk to be read.
	 * @param index The index to locate.
	 * @param error The error state.
	 * @return If the chunk was located.
	 * @since 2021/11/07
	 */
	sjme_jboolean (*locateChunk)(sjme_packInstance* instance,
		sjme_memChunk* outChunk, sjme_jint index, sjme_error* error);
	
	/**
	 * Marks this library as being closed to perform any cleanup as needed.
	 * 
	 * @param instance The pack instance this belongs to.
	 * @param index The library index being closed.
	 * @param postComplete Is this being called post library removal?
	 * @param error The error state.
	 * @return If this was successful or not.
	 * @since 2021/11/11
	 */
	sjme_jboolean (*libraryMarkClosed)(sjme_packInstance* instance,
		sjme_jint index, sjme_jboolean postComplete, sjme_error* error);
	
	/**
	 * Queries the main arguments that are used to start the launcher.
	 * 
	 * @param instance The pack instance to query the parameter from.
	 * @param outArgs The output main arguments.
	 * @param error Any resultant error state.
	 * @return If the query was successful.
	 * @since 2022/03/01
	 */
	sjme_jboolean (*queryLauncherArgs)(sjme_packInstance* instance,
		sjme_mainArgs** outArgs, sjme_error* error);
	
	/**
	 * Queries the main class.
	 * 
	 * @param instance The pack instance to query the parameter from.
	 * @param outMainClass The output main class.
	 * @param error Any resultant error state.
	 * @return If the query was successful.
	 * @since 2022/03/01
	 */
	sjme_jboolean (*queryLauncherClass)(sjme_packInstance* instance,
		sjme_utfString** outMainClass, sjme_error* error);
		
	/**
	 * Queries the class path that is used for the launcher process.
	 * 
	 * @param instance The pack instance to query the parameter from.
	 * @param outClassPath The output class path.
	 * @param error Any resultant error state.
	 * @return If the query was successful.
	 * @since 2022/03/01
	 */
	sjme_jboolean (*queryLauncherClassPath)(sjme_packInstance* instance,
		sjme_classPath** outClassPath, sjme_error* error);
} sjme_packDriver;

/**
 * Instance of a pack which is a singular ROM which contains multiple JARs or
 * sets of classes.
 * 
 * @since 2021/09/19
 */
struct sjme_packInstance
{
	/** The format instance. */
	sjme_formatInstance format;
	
	/** The driver used for this instance. */
	const sjme_packDriver* driver;
	
	/** Instance state for the current driver. */
	void* state;
	
	/** The number of available libraries. */
	sjme_jint numLibraries;
	
	/** The set of cached libraries in the pack. */
	sjme_atomicPointer* libraries;
	
	/** Counter for the pack instance. */
	sjme_counter counter;
	
	/** Flags for the pack. */
	sjme_jint flags;
};

/**
 * Resolves a class path from a set of C strings.
 * 
 * @param pack The pack to look inside of for libraries.
 * @param classPath The input class path to resolve.
 * @param result The resultant class path.
 * @param error Any potential error state.
 * @return Will return @c sjme_true on success.
 * @since 2022/01/09
 */
sjme_jboolean sjme_packClassPathFromCharStar(sjme_packInstance* pack,
	const char** classPath, sjme_classPath** result, sjme_error* error);

/**
 * Closes the given pack instance.
 * 
 * @param instance The instance to close.
 * @param error The error state.
 * @return If closing was a success or not.
 * @since 2021/10/23
 */
sjme_jboolean sjme_packClose(sjme_packInstance* instance,
	sjme_error* error);

/**
 * Obtains the details to launch the inbuilt launcher, this information is
 * contained within the ROM itself.
 * 
 * @param packInstance The pack to obtain the launcher details from.
 * @param outMainClass The main entry point for the launcher.
 * @param outArgs Output arguments for the launcher call.
 * @param outClassPath The output class path for the launcher.
 * @param error Any possible error state.
 * @return If this was able to be obtained.
 * @since 2022/01/09
 */
sjme_jboolean sjme_packGetLauncherDetail(sjme_packInstance* packInstance,
	sjme_utfString** outMainClass, sjme_mainArgs** outArgs,
	sjme_classPath** outClassPath, sjme_error* error);

/**
 * Opens the given pack and makes an instance of it.
 * 
 * @param outInstance The output instance for returning.
 * @param data The data block.
 * @param size The size of the data block.
 * @param error The error state on open.
 * @return If this was successful or not.
 * @since 2021/09/19
 */
sjme_jboolean sjme_packOpen(sjme_packInstance** outInstance, const void* data,
	sjme_jint size, sjme_error* error);

/**
 * Marks this library as being closed to perform any cleanup as needed.
 * 
 * @param packInstance The pack instance this belongs to.
 * @param libInstance The instance of the library being closed.
 * @param index The library index being closed.
 * @param postComplete Is this being called post library removal?
 * @param error The error state.
 * @return If this was successful or not.
 * @since 2021/11/09
 */
sjme_jboolean sjme_packLibraryMarkClosed(sjme_packInstance* packInstance,
	sjme_libraryInstance* libInstance, sjme_jint index,
	sjme_jboolean postComplete, sjme_error* error);

/**
 * Opens a library within a pack file.
 * 
 * @param packInstance The instance of the pack file.
 * @param outLibrary The output instance of the given library.
 * @param index The index of the library to open.
 * @param error The error state.
 * @return If opening the library was successful.
 * @since 2021/11/07
 */
sjme_jboolean sjme_packLibraryOpen(sjme_packInstance* packInstance,
	sjme_libraryInstance** outLibrary, sjme_jint index, sjme_error* error);

/**
 * Maps integer values to class path values.
 * 
 * @param packInstance The pack instance to read library values from.
 * @param outClassPath The class path to load libraries into.
 * @param index The index on the output classpath. 
 * @param targetLibIndex The target library index, which is the library to
 * use.
 * @param error On any resultant error state.
 * @return If the operation failed.
 * @since 2022/03/09
 */
sjme_jboolean sjme_packClassPathMapper(sjme_packInstance* packInstance,
	sjme_classPath* outClassPath, sjme_jint index, sjme_jint targetLibIndex,
	sjme_error* error);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_PACK_H */
