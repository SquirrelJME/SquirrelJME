/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Front end functions that may be implemented by front-ends as needed for any
 * potential functionality.
 * 
 * @since 2021/12/16
 */

#ifndef SQUIRRELJME_FRONTFUNC_H
#define SQUIRRELJME_FRONTFUNC_H

#include "frontend/frontdef.h"
#include "engine/file.h"
#include "engine/pipe.h"
#include "engine/scafdef.h"
#include "softmath.h"
#include "error.h"

/*--------------------------------------------------------------------------*/

/**
 * This is the interface that is used with SquirrelJME to interact with the
 * front-end and contains the accordingly native functions for anything
 * functionality which may be supported.
 * 
 * @since 2022/01/05
 */
struct sjme_frontBridge
{
	/**
	 * Returns the current time in milliseconds since UTC.
	 * 
	 * @param out The output value.
	 * @param error Any possible error state.
	 * @return Will return @c sjme_true if successful.
	 * @since 2022/01/05 
	 */
	sjme_jboolean (*currentTimeMillis)(sjme_jlong* out, sjme_error* error);
	
	/**
	 * Attempts to exit the given process and terminate it, this is system
	 * specific and will vary accordingly. The call to this is only made when
	 * there should be no more running virtual machine tasks and the entire
	 * machine is shut-down. This will not be called when a sub-task exits
	 * as it will return to the launcher or another task that is currently
	 * running.
	 * 
	 * @param exitCode The exit code to quit the process with.
	 * @param error Any possible error state.
	 * @return May return @c sjme_true if exit was a success and there is a
	 * delay for when termination actually happens, or @c sjme_false if exit
	 * has failed.
	 * @since 2022/01/05
	 */
	sjme_jboolean (*exit)(sjme_jint exitCode, sjme_error* error);
	
	/**
	 * Returns the number of monotonic nanoseconds that have elapsed.
	 * 
	 * @param out The output value.
	 * @param error Any possible error state.
	 * @return Will return @c sjme_true if successful.
	 * @since 2022/01/05 
	 */
	sjme_jboolean (*nanoTime)(sjme_jlong* out, sjme_error* error);
	
	/**
	 * Attempts to open the standard pipe as a file.
	 * 
	 * @param stdPipe The standard pipe to open.
	 * @param outFile The output file, for said open file.
	 * @param error On any error state.
	 * @return If the open was successful or not.
	 * @since 2022/05/22
	 */
	sjme_jboolean (*stdPipeFileOpen)(sjme_standardPipeType stdPipe,
		sjme_file** outFile, sjme_error* error);
};

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_FRONTFUNC_H */
