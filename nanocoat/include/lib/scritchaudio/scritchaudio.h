/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Main ScritchAudio header.
 * 
 * @since 2025/05/07
 */

#ifndef SCRITCHAUDIO_H
#define SCRITCHAUDIO_H

#include "sjme/config.h"
#include "sjme/alloc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SCRITCHAUDIO_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * ScritchAudio state structure.
 *
 * @since 2025/05/07
 */
typedef struct sjme_scritchaudioBase sjme_scritchaudioBase;

/**
 * ScritchAudio state structure.
 *
 * @since 2025/05/07
 */
typedef sjme_scritchaudioBase* sjme_scritchaudio;

typedef enum sjme_scritchaudio_format
{
	/** Automatic. */
	SJME_SCRITCHAUDIO_FORMAT_AUTOMATIC = -1,
	
	/** Unsigned 8-bit PCM. */
	SJME_SCRITCHAUDIO_FORMAT_BYTE_U8 = 0,
	
	/** 8-bit a-law. */
	SJME_SCRITCHAUDIO_FORMAT_BYTE_ALAW = 1,
	
	/** 8-bit mu-law. */
	SJME_SCRITCHAUDIO_FORMAT_BYTE_ULAW = 2,
	
	/** Signed 16-bit. */
	SJME_SCRITCHAUDIO_FORMAT_SHORT_S16 = 3,
	
	/** Signed 24-bit, as integer type. */
	SJME_SCRITCHAUDIO_FORMAT_INT_S24 = 4,
	
	/** Signed 32-bit. */
	SJME_SCRITCHAUDIO_FORMAT_INT_S32 = 5,
	
	/** 32-bit floating point. */
	SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32 = 6,
	
	/** The number of audio formats. */
	SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS = 7,
} sjme_scritchaudio_format;

typedef enum sjme_scritchaudio_rate
{
	/** Automatic. */
	SJME_SCRITCHAUDIO_RATE_AUTOMATIC = -1,
	
	/** 8000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_8000 = 8000,
	
	/** 11025 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_11025 = 11025,
	
	/** 16000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_16000 = 16000,
	
	/** 22050 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_22050 = 22050,
	
	/** 24000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_24000 = 24000,
	
	/** 44100 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_44100 = 44100,
	
	/** 48000 Hz. */
	SJME_SCRITCHAUDIO_RATE_HZ_48000 = 48000,
	
	/** Maximum supported sample rate. */
	SJME_SCRITCHAUDIO_RATE_MAX_SAMPLE_RATE = 384000,
} sjme_scritchaudio_rate;

typedef enum sjme_scritchaudio_channels
{
	/** Mono audio. */
	SJME_SCRITCHAUDIO_CHANNELS_MONO = 1,
	
	/** Stereo. */
	SJME_SCRITCHAUDIO_CHANNELS_STEREO = 2,
	
	/** Basic surround sound. */
	SJME_SCRITCHAUDIO_CHANNELS_BASIC_SURROUND = 4,
	
	/** Full surround sound. */
	SJME_SCRITCHAUDIO_CHANNELS_FULL_SURROUND = 8,
} sjme_scritchaudio_channels;

typedef sjme_errorCode (*sjme_scritchaudio_createFunc)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrOutNotNull sjme_scritchaudio* outAudio,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels numChannels);
	
/**
 * Functions for operating on ScritchAudio.
 *
 * @since 2025/05/07
 */
typedef struct sjme_scritchaudio_functions
{
	/** Create a new audio stream. */
	sjme_scritchaudio_createFunc create;

	/** Create a new native decoder for a file format. */ 
	void* decoder;

	/** Does the decoder support the given file format? */
	void* decoderSupports;

	/** Destroys an audio stream. */
	void* destroy;

	/** Performs manual iteration and pumping of audio data. */
	void* loopIterate;

	/** Opens a native MIDI interface to the host system. */
	void* midiPort;

	/** Opens a synthesizer based MIDI interface using a renderer. */
	void* midiSynth;

	/** Attaches a renderer to a stream. */
	void* rendererAttach;

	/** Detaches a renderer from a stream. */
	void* rendererDetach;
} sjme_scritchaudio_functions;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SCRITCHAUDIO_H
}
#undef SJME_CXX_SCRITCHAUDIO_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SCRITCHAUDIO_H */
#endif /* #ifdef __cplusplus */

#endif /* SCRITCHAUDIO_H */
