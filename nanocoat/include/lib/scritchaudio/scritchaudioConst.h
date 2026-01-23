/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchAudio Constants.
 *
 * @file
 * @since 2026/01/23
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHAUDIOCONST_H
#define SJME_C_SQUIRRELJME_SCRITCHAUDIOCONST_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHAUDIOCONST_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The minimum sleeping time, sleep does not occur below this (100ms). */
#define SJME_SCRITCHAUDIO_MIN_SLEEP_NANOS SJME_NANOS_MS(100)

/** The number of nanoseconds to give up if we are behind (750ms). */
#define SJME_SCRITCHAUDIO_GIVE_UP_NANOS SJME_NANOS_MS(750)

/** The number of nanoseconds to hold off when sleeping (10ms). */
#define SJME_SCRITCHAUDIO_HOLD_NANOS SJME_NANOS_MS(10)

/** The number of nanoseconds to pre-fill for triggering (50ms). */
#define SJME_SCRITCHAUDIO_TRIGGER_NANOS SJME_NANOS_MS(50)

/** The maximum amount of time the trigger cap can be (200ms). */
#define SJME_SCRITCHAUDIO_TRIGGER_CAP_NANOS SJME_NANOS_MS(200)

/** The poll delay time to use (200ms). */
#define SJME_SCRITCHAUDIO_POLL_DELAY_MILLIS 200

/** The subdivision slicing. */
#define SJME_SCRITCHAUDIO_RENDER_SLICES 8

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

	/** Signed 16-bit. */
	SJME_SCRITCHAUDIO_FORMAT_SHORT_S16 = 1,

	/** Signed 32-bit. */
	SJME_SCRITCHAUDIO_FORMAT_INT_S32 = 2,

	/** 32-bit floating point. */
	SJME_SCRITCHAUDIO_FORMAT_FLOAT_F32 = 3,

	/** The number of audio formats. */
	SJME_SCRITCHAUDIO_FORMAT_NUM_FORMATS = 4,
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
	/** Automatic. */
	SJME_SCRITCHAUDIO_CHANNELS_AUTOMATIC = -1,

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
 * Represents the type that a connection is.
 *
 * @since 2025/05/26
 */
typedef enum sjme_scritchaudio_connectionType
{
	/** Invalid connection type. */
	SJME_SCRITCHAUDIO_CONN_INVALID,

	/** A stream. */
	SJME_SCRITCHAUDIO_CONN_STREAM,

	/** A source which generates data for a stream. */
	SJME_SCRITCHAUDIO_CONN_SOURCE,

	/** A sink which reads data from a device, such as a microphone. */
	SJME_SCRITCHAUDIO_CONN_SINK,

	/** The number of connection types. */
	SJME_SCRITCHAUDIO_NUM_CONN_TYPES
} sjme_scritchaudio_connectionType;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOCONST_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOCONST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOCONST_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHAUDIOCONST_H */