/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "../include/3rdparty/jni/jni.h"
#include "../include/3rdparty/jni/jawt.h"

int main(int argc, char** argv)
{
	return JAWT_GetAWT(NULL, NULL);
}
