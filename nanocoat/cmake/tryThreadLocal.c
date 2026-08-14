/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#define SJME__CONFIG_CHECK__SJME_THREAD_LOCAL
#include "sjme/config.h"

int main(int argc, char** argv)
{
	sjme_threadLocal(int, a);
	
	a = 1234;
	return 0;
}
