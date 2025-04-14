/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <pthread.h>

int main(int argc, char** argv)
{
	pthread_t beep;
	
	pthread_self();
	pthread_create(&beep, NULL, NULL, NULL);
	
	return 0;
}
