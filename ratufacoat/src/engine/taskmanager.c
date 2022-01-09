/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "engine/taskmanager.h"

sjme_jboolean sjme_engineTaskNew(sjme_engineState* engineState,
	sjme_classPath* classPath,
	const char* mainClass, sjme_mainArgs* mainArgs,
	sjme_engineSystemPropertySet* sysProps,
	sjme_taskPipeRedirectType stdOutMode,
	sjme_taskPipeRedirectType stdErrMode, sjme_jboolean forkThread,
	sjme_jboolean rootVm, sjme_engineTask** outTask,
	sjme_engineThread** outMainThread, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}
