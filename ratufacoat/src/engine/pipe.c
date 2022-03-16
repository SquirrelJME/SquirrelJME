/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "engine/pipe.h"

const sjme_pipeFunction sjme_pipeFunctions
	[NUM_SJME_TASK_PIPE_REDIRECTS] =
{
	/** @c SJME_TASK_PIPE_REDIRECT_DISCARD . */
	{
		.close = NULL,
		.flush = NULL,
		.newInstance = NULL,
		.read = NULL,
		.write = NULL,
	},
	
	/** @c SJME_TASK_PIPE_REDIRECT_BUFFER . */
	{
		.close = NULL,
		.flush = NULL,
		.newInstance = NULL,
		.read = NULL,
		.write = NULL,
	},
	
	/** @c SJME_TASK_PIPE_REDIRECT_TERMINAL . */
	{
		.close = NULL,
		.flush = NULL,
		.newInstance = NULL,
		.read = NULL,
		.write = NULL,
	},
};
