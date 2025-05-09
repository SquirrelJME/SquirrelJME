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

#ifndef SJME_C_SCRITCHAUDIO_H
#define SJME_C_SCRITCHAUDIO_H

#include "sjme/config.h"
#include "sjme/alloc.h"
#include "sjme/list.h"

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

/**
 * A single ScritchAudio stream.
 *
 * @since 2025/05/08
 */
typedef struct sjme_scritchaudio_streamBase sjme_scritchaudio_streamBase;

/**
 * A single ScritchAudio stream.
 *
 * @since 2025/05/08
 */
typedef sjme_scritchaudio_streamBase* sjme_scritchaudio_stream;

/**
 * A ScritchAudio render source.
 *
 * @since 2025/05/08
 */
typedef struct sjme_scritchaudio_sourceBase sjme_scritchaudio_sourceBase;

/**
 * A ScritchAudio render source.
 *
 * @since 2025/05/08
 */
typedef sjme_scritchaudio_sourceBase* sjme_scritchaudio_source;

/**
 * A single ScritchAudio MIDI port.
 *
 * @since 2025/05/08
 */
typedef struct sjme_scritchaudio_midiPortBase sjme_scritchaudio_midiPortBase;

/**
 * A single ScritchAudio MIDI port.
 *
 * @since 2025/05/08
 */
typedef sjme_scritchaudio_midiPortBase* sjme_scritchaudio_midiPort;

/** A list of ScritchAudio MIDI ports. */
SJME_LIST_DECLARE(sjme_scritchaudio_midiPort, 0);

/**
 * The audio format for audio encoding.
 *
 * @since 2025/05/08
 */
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

/**
 * The standard set of audio sampling rates.
 *
 * @since 2025/05/08
 */
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

/**
 * The output channels.
 *
 * @since 2025/05/08
 */
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

/**
 * Queries the MIDI ports and synths that are available to the system.
 *
 * @param inState The ScritchAudio state.
 * @param inOutPorts The ports which are available.
 * @param outNumPorts The number of ports that are available, this value may
 * be larger than the list if it is too small.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_queryMidiPortsFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInOutNotNull sjme_list_sjme_scritchaudio_midiPort* inOutPorts,
	sjme_attrOutNotNull sjme_jint* outNumPorts);

/**
 * Attaches or detaches a source renderer to or from a stream.
 *
 * @param inState The ScritchAudio state.
 * @param inStream The stream to attach to or detach from.
 * @param attach Is the source being attached to the stream?
 * @param source The source being attached or detached.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_sourceAttachFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrInNotNull sjme_scritchaudio_stream inStream,
	sjme_attrInValue sjme_jboolean attach,
	sjme_attrInNotNull sjme_scritchaudio_source source);

/**
 * Creates a new audio stream.
 *
 * @param inState The input state.
 * @param outStream The resultant audio stream.
 * @param inFormat The audio format to use, @c -1 means to use the system
 * preferred format.
 * @param inRate The rate to use, @c -1 means to use the system preferred
 * rate.
 * @param inChannels The number of channels to use, @c -1 means to use the
 * system preferred channels.
 * @return Any resultant error, if any.
 * @since 2025/05/08
 */
typedef sjme_errorCode (*sjme_scritchaudio_streamCreateFunc)(
	sjme_attrInNotNull sjme_scritchaudio inState,
	sjme_attrOutNotNull sjme_scritchaudio_stream* outStream,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_format inFormat,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_rate inRate,
	sjme_attrInNegativeOnePositive sjme_scritchaudio_channels inChannels);

	
/**
 * Functions for operating on ScritchAudio.
 *
 * @since 2025/05/07
 */
typedef struct sjme_scritchaudio_apiFunctions
{
	/** Queries the MIDI ports and synths available. */
	sjme_scritchaudio_queryMidiPortsFunc queryMidiPorts;
	
	/** Attaches or detaches a source. */
	sjme_scritchaudio_sourceAttachFunc sourceAttach;
	
	/** Create a new audio stream. */
	sjme_scritchaudio_streamCreateFunc streamCreate;
} sjme_scritchaudio_apiFunctions;

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
