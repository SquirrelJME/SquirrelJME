/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Java option argument parsing.
 * 
 * @file
 * @since 2026/03/02
 */

#ifndef SJME_C_SQUIRRELJME_JOPTARG_H
#define SJME_C_SQUIRRELJME_JOPTARG_H

#include "sjme/config.h"
#include "sjme/stdTypes.h"
#include "sjme/native.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JOPTARG_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
	
/**
 * Help parameter storage.
 * 
 * @since 2024/08/08
 */
typedef struct sjme_joptarg_helpParam
{
	/** The argument. */
	sjme_lpcstr arg;
	
	/** Description of the parameter. */
	sjme_lpcstr desc;
} sjme_joptarg_helpParam;

/**
 * Specifies the method to use when parsing arguments.
 * 
 * @since 2026/03/02
 */
typedef enum sjme_joptarg_method
{
	/**
	 * POSIX Parsing Method.
	 * 
	 * @code
	 * cmd -ao arg path path
	 * cmd -a -o arg path path
	 * cmd -o arg -a path path
	 * cmd -a -o arg -- path path
	 * cmd -a -oarg path path
	 * cmd -aoarg path path
	 * @endcode
	 */
	SJME_OPTARG_POSIX,
	
	/**
	 * Java Parsing Method.
	 * 
	 * @code
	 * -help
	 * -attach address
	 * -Dname=value
	 * @endcode
	 */
	SJME_OPTARG_JAVA,
	
	/** The number of argument parsing methods. */
	SJME_OPTARG_NUM_METHODS,
} sjme_joptarg_method;
	
/**
 * Flags that can modify how argument parsing is handled.
 * 
 * @since 2026/03/02
 */
typedef enum sjme_joptarg_flag
{
	/** Handle @code -- @endcode at the end as remaining arguments. */
	SJME_JOPTARG_FLAG_DASH_DASH = 1,
} sjme_joptarg_flag;
	
/**
 * Handler information for option arguments.
 * 
 * @since 2026/03/02
 */
typedef struct sjme_joptarg_handler sjme_joptarg_handler;
	
/**
 * Argument parsing state.
 * 
 * @since 2026/03/02
 */
typedef struct sjme_joptarg_state
{
	/** The original argument count. */
	sjme_jint argc;
	
	/** The original argument set. */
	const sjme_lpcstr* argv;
	
	/** The generic data to be passed to the handler. */
	sjme_pointer handlerData;
} sjme_joptarg_state;
	
typedef sjme_errorCode (*sjme_joptarg_handlerFunc)(
	sjme_attrInNotNull const sjme_joptarg_handler* handler,
	sjme_attrInNotNull sjme_joptarg_state* state);
	
struct sjme_joptarg_handler
{
	sjme_jint todo;
};
	
/**
 * Parses the specified set of arguments.
 * 
 * @param method The method used.
 * @param nal The NAL to use for output and response file reading.
 * @param handlers The handlers for valid options.
 * @param handlerData The data to be passed to handlers.
 * @param flags @link sjme_joptarg_flag @endlink flags.
 * @param argc The argument count.
 * @param argv The arguments.
 * @return Any resultant error, if any.
 * @since 2026/03/02
 */
sjme_errorCode sjme_joptarg_parse(
	sjme_attrInValue sjme_joptarg_method method,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull const sjme_joptarg_handler* handlers,
	sjme_attrInNotNull sjme_pointer handlerData,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInPositive sjme_jint argc,
	sjme_attrInNotNull const sjme_lpcstr* argv);
		
/**
 * This first tokenizes a line of arguments, then passes it 
 * to @link sjme_joptarg_parse @endlink which then parses the arguments.
 * 
 * This is generally for systems such as Windows.
 * 
 * @param method The method used.
 * @param nal The NAL to use for output and response file reading.
 * @param handlers The handlers for valid options.
 * @param handlerData The data to be passed to handlers.
 * @param flags @link sjme_joptarg_flag @endlink flags.
 * @param argLine The argument command line, to be tokenized.
 * @return Any resultant error, if any.
 * @since 2026/03/02
 */
sjme_errorCode sjme_joptarg_parseLong(
	sjme_attrInValue sjme_joptarg_method method,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull const sjme_joptarg_handler* handlers,
	sjme_attrInNotNull sjme_pointer handlerData,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInNotNull sjme_lpcstr argLine);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JOPTARG_H
}
#undef SJME_CXX_SQUIRRELJME_JOPTARG_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JOPTARG_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JOPTARG_H */
