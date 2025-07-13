/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/cbor.h"

sjme_errorCode sjme_cbor_putArrayClose(
	sjme_attrInNotNull sjme_cbor cbor)
{
	sjme_errorCode error;
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_ARRAY_OPEN &&
		cbor->lastToken != SJME_CBOR_ARRAY_VALUE &&
		cbor->lastToken != SJME_CBOR_MAP_CLOSE)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;
	
	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"]", strlen("]"))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type. */
	cbor->lastToken = SJME_CBOR_ARRAY_CLOSE;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putArrayOpen(
	sjme_attrInNotNull sjme_cbor cbor)
{
	sjme_errorCode error;
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_FILE_START &&
		cbor->lastToken != SJME_CBOR_ARRAY_OPEN &&
		cbor->lastToken != SJME_CBOR_ARRAY_COMMA &&
		cbor->lastToken != SJME_CBOR_MAP_COLON &&
		cbor->lastToken != SJME_CBOR_UNKNOWN_COMMA)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;
	
	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"[", strlen("["))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type. */
	cbor->lastToken = SJME_CBOR_ARRAY_OPEN;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putColon(
	sjme_attrInNotNull sjme_cbor cbor)
{
	sjme_errorCode error;
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_MAP_KEY)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			":", strlen(":"))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Always ends up being map colon. */
	cbor->lastToken = SJME_CBOR_MAP_COLON;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putComma(
	sjme_attrInNotNull sjme_cbor cbor)
{
	sjme_errorCode error;
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_MAP_VALUE &&
		cbor->lastToken != SJME_CBOR_ARRAY_VALUE &&
		cbor->lastToken != SJME_CBOR_ARRAY_CLOSE &&
		cbor->lastToken != SJME_CBOR_MAP_CLOSE)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			",", strlen(","))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type, depends on array or not. */
	if (cbor->lastToken == SJME_CBOR_MAP_VALUE)
		cbor->lastToken = SJME_CBOR_MAP_COMMA;
	else if (cbor->lastToken == SJME_CBOR_ARRAY_VALUE)
		cbor->lastToken = SJME_CBOR_ARRAY_COMMA;
	else
		cbor->lastToken = SJME_CBOR_UNKNOWN_COMMA;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putKey(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Auto place comma? */
	if (cbor->lastToken == SJME_CBOR_ARRAY_VALUE ||
		cbor->lastToken == SJME_CBOR_MAP_VALUE)
		if (sjme_error_default(error = sjme_cbor_putComma(cbor)))
			return sjme_error_default(error);

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_MAP_OPEN &&
		cbor->lastToken != SJME_CBOR_MAP_COMMA &&
		cbor->lastToken != SJME_CBOR_UNKNOWN_COMMA)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"\"", strlen("\""))))
			return sjme_error_default(error);
		
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			(sjme_buffer)inKey, strlen(inKey))))
			return sjme_error_default(error);
		
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"\"", strlen("\""))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Always transitions to map keys. */
	cbor->lastToken = SJME_CBOR_MAP_KEY;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapClose(
	sjme_attrInNotNull sjme_cbor cbor)
{
	sjme_errorCode error;
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_MAP_OPEN &&
		cbor->lastToken != SJME_CBOR_MAP_VALUE &&
		cbor->lastToken != SJME_CBOR_ARRAY_CLOSE)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"}", strlen("}"))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type. */
	cbor->lastToken = SJME_CBOR_MAP_CLOSE;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapEntryA(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Entries are pseudo types. */
	if (sjme_error_is(error = sjme_cbor_putKey(cbor, inKey)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putColon(cbor)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putArrayOpen(cbor)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapEntryI(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInNotNull sjme_intMax inValue)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Entries are pseudo types. */
	if (sjme_error_is(error = sjme_cbor_putKey(cbor, inKey)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putColon(cbor)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putValueI(cbor, inValue)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapEntryJ(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inKey == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Entries are pseudo types. */
	if (sjme_error_is(error = sjme_cbor_putKey(cbor, inKey)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putColon(cbor)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putValueJ(cbor, inValue)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapEntryM(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inKey == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Entries are pseudo types. */
	if (sjme_error_is(error = sjme_cbor_putKey(cbor, inKey)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putColon(cbor)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putMapOpen(cbor)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapEntryS(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inKey,
	sjme_attrInNotNull sjme_lpcstr inValue)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inKey == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Entries are pseudo types. */
	if (sjme_error_is(error = sjme_cbor_putKey(cbor, inKey)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putColon(cbor)))
		return sjme_error_default(error);
	
	if (sjme_error_is(error = sjme_cbor_putValueS(cbor, inValue)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putMapOpen(
sjme_attrInNotNull sjme_cbor cbor)
{
	sjme_errorCode error;
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Implicit comma after array or map close? */
	if (cbor->lastToken == SJME_CBOR_ARRAY_CLOSE ||
		cbor->lastToken == SJME_CBOR_MAP_CLOSE)
		if (sjme_error_is(error = sjme_cbor_putComma(cbor)))
			return sjme_error_default(error);
	
	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_FILE_START &&
		cbor->lastToken != SJME_CBOR_ARRAY_OPEN &&
		cbor->lastToken != SJME_CBOR_ARRAY_COMMA &&
		cbor->lastToken != SJME_CBOR_MAP_COLON &&
		cbor->lastToken != SJME_CBOR_UNKNOWN_COMMA)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"{", strlen("{"))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type. */
	cbor->lastToken = SJME_CBOR_MAP_OPEN;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putValueJ(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Preface. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

	/* Write text. */
	if (cbor->isJson)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_cbor_putValueI(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_intMax inValue)
{
	/* -18446744073709551616 */
#define BUF_SIZE 24
	sjme_errorCode error;
	sjme_cchar buf[BUF_SIZE];
	
	if (cbor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_ARRAY_OPEN &&
		cbor->lastToken != SJME_CBOR_ARRAY_COMMA &&
		cbor->lastToken != SJME_CBOR_MAP_COLON &&
		cbor->lastToken != SJME_CBOR_UNKNOWN_COMMA)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Compose integer value. */
		memset(buf, 0, sizeof(buf));
		snprintf(buf, BUF_SIZE - 1, "%" PRId64,
			inValue);
		
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			buf, strlen(buf))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type, depends on if an array or not. */
	/* If a comma precedes this, then we know we are in an array. */
	if (cbor->lastToken == SJME_CBOR_MAP_COLON)
		cbor->lastToken = SJME_CBOR_MAP_VALUE;
	else
		cbor->lastToken = SJME_CBOR_ARRAY_VALUE;

	/* Success! */
	return SJME_ERROR_NONE;
#undef BUF_SIZE
}

sjme_errorCode sjme_cbor_putValueS(
	sjme_attrInNotNull sjme_cbor cbor,
	sjme_attrInNotNull sjme_lpcstr inValue)
{
	sjme_errorCode error;
	
	if (cbor == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Can only occur after these types. */
	if (cbor->lastToken != SJME_CBOR_ARRAY_OPEN &&
		cbor->lastToken != SJME_CBOR_ARRAY_COMMA &&
		cbor->lastToken != SJME_CBOR_MAP_COLON &&
		cbor->lastToken != SJME_CBOR_UNKNOWN_COMMA)
		return SJME_ERROR_CBOR_INVALID_PRECEDE;

	/* Write text. */
	if (cbor->isJson)
	{
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"\"", strlen("\""))))
			return sjme_error_default(error);
		
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			(sjme_buffer)inValue, strlen(inValue))))
			return sjme_error_default(error);
		
		/* Write token. */
		if (sjme_error_is(error = sjme_stream_outputWrite(cbor->out,
			"\"", strlen("\""))))
			return sjme_error_default(error);
	}

	/* Write binary. */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Set new type, depends on if an array or not. */
	/* If a comma precedes this, then we know we are in an array. */
	if (cbor->lastToken == SJME_CBOR_MAP_COLON)
		cbor->lastToken = SJME_CBOR_MAP_VALUE;
	else
		cbor->lastToken = SJME_CBOR_ARRAY_VALUE;

	/* Success! */
	return SJME_ERROR_NONE;
}
