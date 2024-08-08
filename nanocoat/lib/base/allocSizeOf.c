/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/allocSizeOf.h"
#include "sjme/rom.h"
#include "sjme/boot.h"

sjme_errorCode sjme_alloc_sizeOf(
	sjme_alloc_sizeOfId id, sjme_jint count,
	sjme_jint* outSize)
{
	if (outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (id <= SJME_ALLOC_SIZEOF_UNKNOWN || id >= SJME_NUM_ALLOC_TYPE_SIZEOF)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Which type? */
	switch (id)
	{
		case SJME_ALLOC_SIZEOF_ROM_SUITE_FUNCTIONS:
			*outSize = sizeof(sjme_rom_suiteFunctions);
			break;

		case SJME_ALLOC_SIZEOF_RESERVED_POOL:
			*outSize = 64 * 1024;
			break;

		case SJME_ALLOC_SIZEOF_NVM_BOOT_PARAM:
			*outSize = sizeof(sjme_nvm_bootParam);
			break;

		case SJME_ALLOC_SIZEOF_POINTER:
			*outSize = sizeof(sjme_pointer);
			break;

#if defined(SJME_CONFIG_HAS_LITTLE_ENDIAN)
		case SJME_ALLOC_SIZEOF_IS_LITTLE_ENDIAN:
			*outSize = 1;
			break;

		case SJME_ALLOC_SIZEOF_IS_BIG_ENDIAN:
			*outSize = 0;
			break;
#elif defined(SJME_CONFIG_HAS_BIG_ENDIAN)
		case SJME_ALLOC_SIZEOF_IS_LITTLE_ENDIAN:
			*outSize = 0;
			break;

		case SJME_ALLOC_SIZEOF_IS_BIG_ENDIAN:
			*outSize = 1;
			break;
#endif

		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
