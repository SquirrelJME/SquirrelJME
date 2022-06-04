/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "enginestub.h"
#include "engine/file.h"

sjme_jboolean sjme_testStubStdFileOpen(sjme_standardPipeType stdPipe,
	sjme_file** outFile, sjme_error* error)
{
	if (outFile == NULL || error == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}

	/* Standard input is a bit different. */
	if (stdPipe == SJME_STANDARD_PIPE_STDIN)
		return sjme_newSpecialFile(SJME_SPECIAL_FILE_ALWAYS_EOF_INPUT,
			outFile, error);

	/* Otherwise, discard on writes. */
	return sjme_newSpecialFile(SJME_SPECIAL_FILE_DISCARD_OUTPUT,
		outFile, error);
}

const sjme_frontBridge sjme_testStubFrontBridge =
{
	.stdPipeFileOpen = sjme_testStubStdFileOpen,
};
