/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

// The class to forward to
#define MIDI_CLASSNAME "cc/squirreljme/emulator/EmulatedMidiShelf"

#define MIDI_DATARECEIVE_DESC "(Lcc/squirreljme/jvm/mle/brackets/MidiPortBracket;[BII)I"
#define MIDI_DATATRANSMIT_DESC "(Lcc/squirreljme/jvm/mle/brackets/MidiPortBracket;[BII)V"
#define MIDI_DEVICENAME_DESC "(Lcc/squirreljme/jvm/mle/brackets/MidiDeviceBracket;)Ljava/lang/String;"
#define MIDI_DEVICES_DESC "()[Lcc/squirreljme/jvm/mle/brackets/MidiDeviceBracket;"
#define MIDI_PORTS_DESC "(Lcc/squirreljme/jvm/mle/brackets/MidiDeviceBracket;Z)[Lcc/squirreljme/jvm/mle/brackets/MidiPortBracket;"

JNIEXPORT jint JNICALL Impl_mle_MidiShelf_dataReceive(JNIEnv* env,
	jclass classy, jobject port, jbyteArray buf, jint off, jint len)
{
	return forwardCallStaticInteger(env, MIDI_CLASSNAME,
		"dataReceive", MIDI_DATARECEIVE_DESC,
		port, buf, off, len);
}

JNIEXPORT void JNICALL Impl_mle_MidiShelf_dataTransmit(JNIEnv* env,
	jclass classy, jobject port, jbyteArray buf, jint off, jint len)
{
	forwardCallStaticVoid(env, MIDI_CLASSNAME,
		"dataTransmit", MIDI_DATATRANSMIT_DESC,
		port, buf, off, len);
}

JNIEXPORT jstring JNICALL Impl_mle_MidiShelf_deviceName(JNIEnv* env,
	jclass classy, jobject device)
{
	return (jstring)forwardCallStaticObject(env, MIDI_CLASSNAME,
		"deviceName", MIDI_DEVICENAME_DESC,
		device);
}

JNIEXPORT jobjectArray JNICALL Impl_mle_MidiShelf_devices(JNIEnv* env,
	jclass classy)
{
	return (jobjectArray)forwardCallStaticObject(env, MIDI_CLASSNAME,
		"devices", MIDI_DEVICES_DESC);
}

JNIEXPORT jobjectArray JNICALL Impl_mle_MidiShelf_ports(JNIEnv* env,
	jclass classy, jobject device, jboolean transmit)
{
	return (jobjectArray)forwardCallStaticObject(env, MIDI_CLASSNAME,
		"ports", MIDI_PORTS_DESC,
		device, transmit);
}

static const JNINativeMethod mleMidiMethods[] =
{
	{"dataReceive", MIDI_DATARECEIVE_DESC, (void*)Impl_mle_MidiShelf_dataReceive},
	{"dataTransmit", MIDI_DATATRANSMIT_DESC, (void*)Impl_mle_MidiShelf_dataTransmit},
	{"deviceName", MIDI_DEVICENAME_DESC, (void*)Impl_mle_MidiShelf_deviceName},
	{"devices", MIDI_DEVICES_DESC, (void*)Impl_mle_MidiShelf_devices},
	{"ports", MIDI_PORTS_DESC, (void*)Impl_mle_MidiShelf_ports},
};

jint JNICALL mleMidiInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/MidiShelf"),
		mleMidiMethods, sizeof(mleMidiMethods) /
			sizeof(JNINativeMethod));
}
