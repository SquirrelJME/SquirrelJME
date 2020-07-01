/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"
#include "cc_squirreljme_jvm_mle_UIFormShelf.h"

struct formMethod
{
	jclass xclass;
	jmethodID xmeth;	
};

formMethod findFormMethod(JNIEnv* env,
	const char* const name, const char* const type)
{
	formMethod result;
	
	result.xclass = env->FindClass(
		"cc/squirreljme/emulator/uiform/SwingFormShelf");
	result.xmeth = env->GetStaticMethodID(result.xclass, name, type);
	
	return result;
}

JNIEXPORT jobjectArray JNICALL Impl_mle_FormShelf_displays(JNIEnv* env,
	jclass classy)
{
	formMethod call;
	
	call = findFormMethod(env, "displays",
		"()[Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;");
	return (jobjectArray)env->CallStaticObjectMethod(call.xclass, call.xmeth);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_displayShow(JNIEnv* env,
	jclass classy, jobject display, jobject form)
{
	formMethod call;
	
	call = findFormMethod(env, "displayShow",
		"(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V");
	env->CallStaticVoidMethod(call.xclass, call.xmeth, display, form);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_formDelete(JNIEnv* env,
	jclass classy, jobject form)
{
	formMethod call;
	
	call = findFormMethod(env, "formDelete",
		"(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V");
	env->CallStaticVoidMethod(call.xclass, call.xmeth, form);
} 

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formNew(JNIEnv* env,
	jclass classy)
{
	formMethod call;
	
	call = findFormMethod(env, "formNew",
		"()Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;");
	return env->CallStaticObjectMethod(call.xclass, call.xmeth);
}

JNIEXPORT jint JNICALL Impl_mle_FormShelf_metric(JNIEnv* env, jclass classy,
	jint metricId)
{
	formMethod call;
	
	call = findFormMethod(env, "metric", "(I)I");
	return env->CallStaticIntMethod(call.xclass, call.xmeth,
		metricId);
}

static const JNINativeMethod mleFormMethods[] =
{
	{"displays", "()[Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;", (void*)Impl_mle_FormShelf_displays},
	{"displayShow", "(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V", (void*)Impl_mle_FormShelf_displayShow},
	{"formDelete", "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V", (void*)Impl_mle_FormShelf_formDelete},
	{"formNew", "()Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;", (void*)Impl_mle_FormShelf_formNew},
	{"metric", "(I)I", (void*)Impl_mle_FormShelf_metric},
};

jint JNICALL mleFormInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/UIFormShelf"),
		mleFormMethods, sizeof(mleFormMethods) /
			sizeof(JNINativeMethod));
}
