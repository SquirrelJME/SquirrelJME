/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/misc.h"
#include "sjme/util.h"

sjme_errorCode sjme_nvm_misc_deviceId(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrOutNotNull sjme_lpcstr* outUUID)
{
	/* f81d4fae-7dec-11d0-a765-00a0c91e6bf6 */
#define SJME_UUID_LEN 36
#define SJME_SEG 4
	static sjme_cchar uuid[SJME_UUID_LEN + 1];
	sjme_jint seg[SJME_SEG];
	sjme_jint i, dig, at;
	
	if (inState == NULL || outUUID == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The UUID has been calculated? */
	if (uuid[0] != '\0')
	{
		*outUUID = uuid;
		return SJME_ERROR_NONE;
	}
	
	/* What is the actual UUID of the system? */
	for (i = 0; i < SJME_SEG; i++)
		seg[i] = (((i & 1) != 0) ?
			INT32_C(0x12345678) : INT32_C(0x9ABCDEF0)) ^
			(((i & 2) != 0) ? INT32_C(0) : INT32_C(0xFFFFFFFF));
	
	/* Reverse hexalize the UUID. */
	for (i = SJME_UUID_LEN, at = 0; i >= 0; i--)
	{
		/* Dash is here. */
		if (i == 8 || i == 13 || i == 18 || i == 23)
		{
			uuid[i] = '-';
			continue;
		}
		
		/* Calculate the digit. */
		dig = sjme_util_intOverShift(uuid[at / 8], -(at % 8));
		at++;
		
		/* Place the digit */
		if (dig < 10)
			uuid[i] = '0' + dig;
		else
			uuid[i] = 'A' + (dig - 10);
	}
	
	/* Always end in NUL. */
	uuid[SJME_UUID_LEN] = '\0';
	
	/* Return the UUID. */
	*outUUID = uuid;
	return SJME_ERROR_NONE;
#undef SJME_SEG
#undef SJME_UUID_LEN
}
