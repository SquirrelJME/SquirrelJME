/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchAudio types.
 *
 * @file
 * @since 2026/01/23
 */

#ifndef SJME_C_SQUIRRELJME_SCRITCHAUDIOTYPES_H
#define SJME_C_SQUIRRELJME_SCRITCHAUDIOTYPES_H

#include "sjme/stdTypes.h"
#include "lib/scritchaudio/scritchaudioConst.h"
#include "lib/scritchaudio/scritchaudioTypeDefs.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPES_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

struct sjme_scritchaudio_renderFormat
{
	/** The format. */
	sjme_scritchaudio_format format;

	/** The rate. */
	sjme_scritchaudio_rate rate;

	/** The channels used. */
	sjme_scritchaudio_channels channels;
};

struct sjme_scritchaudio_renderInfo
{
	/** The parent render info, if any. */
	sjme_scritchaudio_renderInfo* parent;

	/** The system clock. */
	sjme_jlong clock;

	/** The format. */
	sjme_scritchaudio_renderFormat format;

	/** The number of samples. */
	sjme_jint samples;

	/** The total samples, with channels. */
	sjme_jint totalSamples;

	/** The bytes per sample. */
	sjme_jint bytesPerSample;

	/** The buffer size. */
	sjme_jint bufSize;

	/** Increment from samples. */
	sjme_fixed fromIncr;

	/** Increment to samples. */
	sjme_fixed toIncr;
};

/**
 * Represents a single audio buffer.
 *
 * @since 2025/05/31
 */
typedef union sjme_scritchaudio_buffer
{
	/** Unsigned byte. */
	sjme_jubyte u[sjme_flexibleArrayCountUnion];

	/** Short. */
	sjme_jshort s[sjme_flexibleArrayCountUnion];

	/** Integer. */
	sjme_jint i[sjme_flexibleArrayCountUnion];

	/** Float. */
	sjme_jfloat f[sjme_flexibleArrayCountUnion];
} sjme_scritchaudio_buffer;

/**
 * ScritchAudio bugs.
 *
 * @since 2025/05/15
 */
typedef struct sjme_scritchaudio_bugs
{
	/** Audio is manually polled, there is no system managed loop. */
	sjme_jboolean manualPoll;

	/** Uses event based polling. */
	sjme_jboolean eventPoll;

	/** Writing to the output audio blocks until playback is finished. */
	sjme_jboolean outputBlocks;

	/** Triggering is not supported. */
	sjme_jboolean noTriggering;
} sjme_scritchaudio_bugs;

/**
 * ScritchAudio time.
 *
 * @since 2025/05/16
 */
typedef struct sjme_scritchaudio_time
{
	/** Milliseconds. */
	sjme_jint millis;

	/** Nanoseconds. */
	sjme_jint nanos;
} sjme_scritchaudio_time;

/**
 * Represents an audio clock.
 *
 * @since 2025/05/28
 */
typedef struct sjme_scritchaudio_clock
{
	/** The base clock. */
	sjme_jlong clockBase;

	/** The current clock time. */
	sjme_jlong clock;
} sjme_scritchaudio_clock;

struct sjme_scritchaudio_streamBuffer
{
	/** Any header that is needed (such as for winmm). */
	sjme_pointer header;

	/** The buffer data. */
	sjme_scritchaudio_buffer* buffer;
};

struct sjme_scritchaudio_latency
{
	/** The delay between manual polls. */
	sjme_jint pollDelayMillis;

	/** The delay between manual polls (Nanos). */
	sjme_jint pollDelayNanos;
};

struct sjme_scritchaudio_streamData
{
	/** The file descriptor, if applicable. */
	int fd;

	/** The handle to the device. */
	sjme_pointer handle;

	/** If headers are needed, are big are the headers? */
	sjme_jint headerSize;

	/** The render buffers slices, which are computed in smaller units. */
	sjme_scritchaudio_streamBuffer slice[SJME_SCRITCHAUDIO_RENDER_SLICES];

	/** The stream rendering information. */
	sjme_scritchaudio_renderInfo sliceInfo;

	/** The latency per individual slice. */
	sjme_scritchaudio_latency sliceLatencyPer;

	/** The playback buffer. */
	sjme_scritchaudio_streamBuffer play;

	/** The stream rendering information. */
	sjme_scritchaudio_renderInfo playInfo;

	/** The playback latency. */
	sjme_scritchaudio_latency playLatency;

	/** Was the audio thread bound? */
	sjme_atomic(sjme_jint) bound;

	/** The current event counter. */
	sjme_atomic(sjme_jint) eventCounter;
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPES_H
}
#undef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHAUDIOTYPES_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_SCRITCHAUDIOTYPES_H */