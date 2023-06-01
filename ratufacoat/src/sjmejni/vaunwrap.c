/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjmejni/vaunwrap.h"
#include "debug.h"

sjme_jboolean sjme_vmNumMethodArgs(sjme_jsize* outNum, sjme_vmThread* vmThread,
	sjme_jobject object, sjme_vmMethod method, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_vmUnwrapVaArgs(sjme_jvalue* outValues, sjme_jsize numValues,
	sjme_vmThread* vmThread, sjme_jobject object, sjme_vmMethod method,
	sjme_error* error, va_list args)
{
	sjme_todo("Implement this?");
	return sjme_false;
}


