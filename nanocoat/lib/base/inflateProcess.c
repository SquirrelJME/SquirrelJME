/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/inflate.h"
#include "sjme/debug.h"

sjme_errorCode sjme_inflate_processCodes(
	sjme_attrInNotNull sjme_inflate_state* state)
{
	sjme_errorCode error;
	sjme_inflate_buffer* inBuffer;
	sjme_inflate_buffer* outBuffer;
	sjme_inflate_window* window;
	sjme_juint code, windowReadLen, windowReadDist;
	
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This must be set!! */
	if (state->readCode == NULL ||
		state->readDist == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Read as much as possible until we hit saturation. */
	inBuffer = &state->input;
	outBuffer = &state->output;
	window = &state->window;
	while (outBuffer->ready < SJME_INFLATE_IO_BUFFER_SATURATED)
	{
		/* Read in code. */
		code = INT32_MAX;
		if (sjme_error_is(error = state->readCode(state, &code)) ||
			code == INT32_MAX)
			return sjme_error_default(error);
		
		/* Stop decoding! */
		if (code == 256)
		{
			/* Reset back to initial step. */
			state->step = SJME_INFLATE_STEP_CHECK_BTYPE;
			state->readCode = NULL;
			
			/* Success! */
			return SJME_ERROR_NONE;
		}
		
		/* Literal byte value. */
		else if (code >= 0 && code <= 255)
		{
			if (sjme_error_is(error = sjme_inflate_bitOut(
				outBuffer,
				SJME_INFLATE_LSB, window,
				8, code)))
				return sjme_error_default(error);
		}
		
		/* Window. */
		else if (code >= 257 && code <= 285)
		{
			/* Read in window length. */
			windowReadLen = INT32_MAX;
			if (sjme_error_is(error = sjme_inflate_processLength(
				state, inBuffer, code, &windowReadLen)) ||
				windowReadLen == INT32_MAX)
				return sjme_error_default(error);
			
			/* Read in distance. */
			windowReadDist = INT32_MAX;
			if (sjme_error_is(error = sjme_inflate_processDistance(
				state, inBuffer, code, &windowReadDist)) ||
				windowReadDist == INT32_MAX)
				return sjme_error_default(error);
			
			/* Copy from the input window. */
			if (sjme_error_is(error = sjme_inflate_processWindow(
				state, outBuffer, window,
				windowReadDist, windowReadLen)))
				return sjme_error_default(error);
		}
		
		/* Invalid. */
		else
			return SJME_ERROR_INFLATE_INVALID_CODE;
			
		/* Debug. */
		sjme_message("Code: %d 0x%x", code, code);
		sjme_message_hexDump(&window->window[0],
			window->length);
	}
	
	/* If we over-saturated, just stop and give all the data. */
	if (outBuffer->ready >= SJME_INFLATE_IO_BUFFER_SATURATED)
		return SJME_ERROR_BUFFER_SATURATED;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_processDistance(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
	sjme_attrInRange(257, 285) sjme_juint origCode,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	sjme_errorCode error;
	sjme_juint base, result, i, readIn;
	
	if (state == NULL || inBuffer == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in distance code. */
	base = INT32_MAX;
	if (sjme_error_is(error = state->readDist(
		state, &base)) || base == INT32_MAX)
		return sjme_error_default(error);
	
	/* Must be too high of a code! */
	if (base > 29)
		return SJME_ERROR_INFLATE_INVALID_CODE;
	
	/* Calculate the required distance to use */
	result = 1;
	for (i = 0; i < base; i++)
	{
		/* Similar to length but in groups of two. */
		if (i >= 2)
			result += 1;
		else
			result += (1 << ((((i / 2)) - 1)));
	}
	
	/* Also any extra bits needed as part of the distance. */
	if (base >= 4)
	{
		/* Similarly the same as length, just smaller parts. */
		i = ((base / 2)) - 1;
		
		/* Read in given bits. */
		readIn = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(
			inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			i, &readIn)) ||
			readIn == INT32_MAX)
			return sjme_error_default(error);
		
		/* Add in extra value. */
		result += readIn;
	}
	
	/* Give the result. */
	*outDist = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_processLength(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_buffer* inBuffer,
	sjme_attrInRange(257, 285) sjme_juint code,
	sjme_attrOutNotNull sjme_juint* outLength)
{
	sjme_errorCode error;
	sjme_juint base, result, i, readIn;
	
	if (state == NULL || inBuffer == NULL || outLength == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (code < 257 || code > 285)
		return SJME_ERROR_INFLATE_INVALID_CODE;
	
	/* Maximum distance possible? */
	if (code == 285)
	{
		*outLength = 258;
		return SJME_ERROR_NONE;
	}
	
	/* Get the base distance code. */
	base = code - 257;
	
	/* Calculate the required length to use */
	result = 3;
	for (i = 0; i < base; i++)
	{
		/* Determine how many groups of 4 the code is long. Since zero */
		/* appears as items then subtract 1 to make it longer. However */
		/* after the first 8 it goes up in a standard pattern. */
		if (i < 8)
			result += 1;
		else
			result += (1 << ((((i / 4)) - 1)));
	}
	
	/* Also any extra bits needed as part of the length. */
	if (base >= 8)
	{
		/* Calculate needed amount. Same as the length, it goes up in */
		/* a specific pattern as well except without single increments. */
		i = ((base / 4)) - 1;
		
		/* Read in given bits. */
		readIn = INT32_MAX;
		if (sjme_error_is(error = sjme_inflate_bitIn(
			inBuffer,
			SJME_INFLATE_LSB, SJME_INFLATE_POP,
			i, &readIn)) ||
			readIn == INT32_MAX)
			return sjme_error_default(error);
		
		/* Add in extra value. */
		result += readIn;
	}
	
	/* Give the result. */
	*outLength = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_processWindow(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrInNotNull sjme_inflate_buffer* outBuffer,
	sjme_attrInNotNull sjme_inflate_window* window,
	sjme_attrInPositive sjme_juint windowDist,
	sjme_attrInPositive sjme_juint windowLen)
{
	sjme_errorCode error;
	sjme_juint maxLen, i, w, readBase;
	sjme_jubyte* chunk;
	
	if (state == NULL || outBuffer == NULL || window == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The length chunk can never exceed the distance, however it does */
	/* wrap around accordingly. */
	maxLen = (windowLen > windowDist ? windowDist : windowLen);
	
	sjme_message("Dist %d > %d", windowDist, window->length);
	
	/* Cannot read more than what there is. */
	if (windowDist > window->length)
		return SJME_ERROR_INFLATE_DISTANCE_OUT_OF_RANGE;
	
	/* Setup buffer for the sliding window chunk. */
	chunk = sjme_alloca(sizeof(*chunk) * maxLen);
	if (chunk == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(chunk, 0, sizeof(*chunk) * maxLen);
	
	/* Can read in one full slice? */
	readBase = (window->end - windowDist) & SJME_INFLATE_WINDOW_MASK;
	if (readBase < window->end)
		memmove(chunk, &window->window[readBase], maxLen);
	
	/* Need to copy in two slices. */
	else
	{
		i = SJME_INFLATE_WINDOW_SIZE - readBase;
		memmove(&chunk[0], &window->window[readBase], i);
		memmove(&chunk[i], &window->window[0], maxLen - i);
	}
	
	/* Debug. */
	sjme_message("Dist chunk: %d", maxLen);
	sjme_message_hexDump(chunk, maxLen);
	
	/* Write output. */
	for (i = 0, w = 0; i < windowLen; i++)
	{
		/* Write value to the output. */
		if (sjme_error_is(error = sjme_inflate_bitOut(
			outBuffer, SJME_INFLATE_LSB, window,
			8, chunk[w] & 0xFF)))
			return sjme_error_default(error);
		
		/* Move window up, handle wrap around. */
		if ((++w) >= maxLen)
			w = 0;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}


sjme_errorCode sjme_inflate_readCodeDynamic(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outCode)
{
	if (state == NULL || outCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_inflate_readDistDynamic(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	if (state == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_inflate_readCodeFixed(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outCode)
{
	sjme_errorCode error;
	sjme_inflate_buffer* inBuffer;
	sjme_juint hiSeven, bitsNeeded, litBase, litSub, raw;
	
	if (state == NULL || outCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We at least need 7 bits for the minimum code length. */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_inflate_bitNeed(inBuffer,
		7)))
		return sjme_error_default(error);
	
	/* Read in upper 7 bits first, as a peek. */
	hiSeven = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_MSB, SJME_INFLATE_PEEK, 7,
		&hiSeven)) || hiSeven == INT32_MAX)
		return sjme_error_default(error);
	
	/* Determine the actual number of bits we need. */
	/* 0b0000000 - 0b0010111 */
	if (hiSeven >= 0 && hiSeven <= 23)
	{
		bitsNeeded = 7;
		litBase = 256;
		litSub = 0;
	}
		
	/* 0b0011000[0] - 0b1011111[1] */
	else if (hiSeven >= 24 && hiSeven <= 95)
	{
		bitsNeeded = 8;
		litBase = 0;
		litSub = 48;
	}
	
	/* 0b1100000[0] - 0b1100011[1] */
	else if (hiSeven >= 96 && hiSeven <= 99)
	{
		bitsNeeded = 8;
		litBase = 280;
		litSub = 192;
	}
	
	/* 0b1100100[00] - 0b1111111[11] */
	else
	{
		bitsNeeded = 9;
		litBase = 144;
		litSub = 400;
	}
	
	/* Now that we know what we need, make sure we have it. */
	if (sjme_error_is(error = sjme_inflate_bitNeed(inBuffer,
		bitsNeeded)))
		return sjme_error_default(error);
	
	/* Pop everything off now, so we can recover the code. */
	raw = INT32_MAX;
	if (sjme_error_is(error = sjme_inflate_bitIn(inBuffer,
		SJME_INFLATE_MSB, SJME_INFLATE_POP,
		bitsNeeded,
		&raw)) || raw == INT32_MAX)
		return sjme_error_default(error);
	
	/* Recover the code. */
	*outCode = litBase + (raw - litSub);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_readDistFixed(
	sjme_attrInNotNull sjme_inflate_state* state,
	sjme_attrOutNotNull sjme_juint* outDist)
{
	if (state == NULL || outDist == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just a basic bit read. */
	return sjme_inflate_bitIn(&state->input,
		SJME_INFLATE_MSB, SJME_INFLATE_POP,
		5, outDist);
}
