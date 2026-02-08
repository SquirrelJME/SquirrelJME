/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <sys/socket.h>
#include <netinet/in.h>

int main(int argc, char** argv)
{
	struct sockaddr_in posixAddress;
	posixAddress.sin_port = htons(1234);
	return 0;
}
