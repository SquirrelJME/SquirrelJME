/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "engine/pipe.h"
#include "engine/pipeintern.h"
#include "memory.h"

struct sjme_pipeInstance
{
	/** The functions used to handle this pipe. */
	sjme_pipeFunction functions;
	
	/** Is the pipe closed? */
	sjme_jboolean isClosed;
};

sjme_jboolean sjme_pipeDelete(sjme_pipeInstance* inPipe,
	sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_pipeNewFromBuffer(sjme_buffer* buffer,
	sjme_jboolean isReadable, sjme_jboolean isWritable,
	sjme_pipeInstance** outPipe, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_pipeNewFromFile(sjme_file* file,
	sjme_pipeInstance** outPipe, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_pipeNewNull(sjme_jboolean isReadable,
	sjme_jboolean isWritable, sjme_pipeInstance** outPipe, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}
