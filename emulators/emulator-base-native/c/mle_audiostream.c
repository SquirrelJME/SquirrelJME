/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"
#include "lib/scritchaudio/scritchaudio.h"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/AudioStreamShelf"
#define FORWARD_NATIVE_CLASS "cc/squirreljme/emulator/EmulatedAudioStreamShelf"

#define FORWARD_DESC_create \
	DESC_METHOD(DESC_AUDIOSTREAM, DESC_STRING DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC_decoder \
	DESC_METHOD(DESC_AUDIOPLAYER, DESC_STRING DESC_STRING DESC_INT \
	DESC_INT DESC_INT DESC_ARRAY(DESC_BYTE) DESC_INT DESC_INT)
#define FORWARD_DESC_decoderSupports \
	DESC_METHOD(DESC_BOOLEAN, DESC_STRING)
#define FORWARD_DESC_destroy \
	DESC_METHOD(DESC_VOID, DESC_AUDIOSTREAM)
#define FORWARD_DESC_midiPort \
	DESC_METHOD(DESC_MIDIPORT, DESC_STRING DESC_INT DESC_INT DESC_INT)
#define FORWARD_DESC_midiRenderer \
	DESC_METHOD(DESC_AUDIORENDERER, DESC_MIDIPORT)
#define FORWARD_DESC_register \
	DESC_METHOD(DESC_VOID, DESC_AUDIOSTREAM DESC_AUDIORENDERER)
#define FORWARD_DESC_unregister \
	DESC_METHOD(DESC_VOID, DESC_AUDIOSTREAM DESC_AUDIORENDERER)

FORWARD_IMPL(AudioStream, create,
	jobject, Object,
	FORWARD_IMPL_args(jstring name, jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(name, format, rate, channels))
FORWARD_IMPL(AudioStream, decoder,
	jobject, Object,
	FORWARD_IMPL_args(jstring urlFile, jstring mime, jint format,
		jint rate, jint channels, jarray buf, jint off, jint len),
	FORWARD_IMPL_pass(urlFile, mime, format, rate, channels, buf, off, len))
FORWARD_IMPL(AudioStream, decoderSupports,
	jboolean, Boolean,
	FORWARD_IMPL_args(jstring mime),
	FORWARD_IMPL_pass(mime))
FORWARD_IMPL_VOID(AudioStream, destroy,
	FORWARD_IMPL_args(jobject stream),
	FORWARD_IMPL_pass(stream))
FORWARD_IMPL(AudioStream, midiPort,
	jobject, Object,
	FORWARD_IMPL_args(jstring mime, jint format, jint rate, jint channels),
	FORWARD_IMPL_pass(mime, format, rate, channels))
FORWARD_IMPL(AudioStream, midiRenderer,
	jobject, Object,
	FORWARD_IMPL_args(jobject port),
	FORWARD_IMPL_pass(port))
FORWARD_IMPL_VOID(AudioStream, register,
	FORWARD_IMPL_args(jobject stream, jobject renderer),
	FORWARD_IMPL_pass(stream, renderer))
FORWARD_IMPL_VOID(AudioStream, unregister,
	FORWARD_IMPL_args(jobject stream, jobject renderer),
	FORWARD_IMPL_pass(stream, renderer))

static const JNINativeMethod mleAudioStreamMethods[] =
{
	FORWARD_list(AudioStream, create),
	FORWARD_list(AudioStream, decoder),
	FORWARD_list(AudioStream, decoderSupports),
	FORWARD_list(AudioStream, destroy),
	FORWARD_list(AudioStream, midiPort),
	FORWARD_list(AudioStream, midiRenderer),
	FORWARD_list(AudioStream, register),
	FORWARD_list(AudioStream, unregister),
};

jint JNICALL mleAudioStreamInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, FORWARD_CLASS),
		mleAudioStreamMethods, sizeof(mleAudioStreamMethods) /
			sizeof(JNINativeMethod));
}
