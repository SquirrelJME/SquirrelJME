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

// The class to forward to
#define SWINGUIFORM_CLASSNAME "cc/squirreljme/emulator/uiform/SwingFormShelf"

// Descriptors for calls
#define SWINGUIFORM_DISPLAYS_DESC "()[Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;"
#define SWINGUIFORM_DISPLAYSHOW_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V"
#define SWINGUIFORM_FORMDELETE_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V"
#define SWINGUIFORM_FORMITEMATPOSITION_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;I)Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;"
#define SWINGUIFORM_FORMITEMPOSITIONGET_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;)I"
#define SWINGUIFORM_FORMITEMPOSITIONSET_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;I)V"
#define SWINGUIFORM_FORMITEMREMOVE_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;I)Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;"
#define SWINGUIFORM_FORMNEW_DESC "()Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;"
#define SWINGUIFORM_ITEMDELETE_DESC "(Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;)V"
#define SWINGUIFORM_ITEMNEW_DESC "(I)Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;"
#define SWINGUIFORM_METRIC_DESC "(I)I"

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_displays(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"displays", SWINGUIFORM_DISPLAYS_DESC);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_displayShow(JNIEnv* env,
	jclass classy, jobject display, jobject form)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"displayShow", SWINGUIFORM_DISPLAYSHOW_DESC,
		display, form);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_formDelete(JNIEnv* env,
	jclass classy, jobject form)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"formDelete", SWINGUIFORM_FORMDELETE_DESC,
		form);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formItemAtPosition(JNIEnv* env,
	jclass classy, jobject form, jint position)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"formItemAtPosition", SWINGUIFORM_FORMITEMATPOSITION_DESC,
		form, position);
}

JNIEXPORT jint JNICALL Impl_mle_FormShelf_formItemPositionGet(JNIEnv* env,
	jclass classy, jobject form, jobject item)
{
	return forwardCallStaticInteger(env, SWINGUIFORM_CLASSNAME,
		"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONGET_DESC,
		form, item);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_formItemPositionSet(JNIEnv* env,
	jclass classy, jobject form, jobject item, jint position)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONSET_DESC,
		form, item, position);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formItemRemove(JNIEnv* env,
	jclass classy, jobject form, jint position)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"formItemRemove", SWINGUIFORM_FORMITEMREMOVE_DESC,
		form, position);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_formNew(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"formNew", SWINGUIFORM_FORMNEW_DESC);
}

JNIEXPORT void JNICALL Impl_mle_FormShelf_itemDelete(JNIEnv* env,
	jclass classy, jobject form)
{
	forwardCallStaticVoid(env, SWINGUIFORM_CLASSNAME,
		"itemDelete", SWINGUIFORM_ITEMDELETE_DESC,
		form);
}

JNIEXPORT jobject JNICALL Impl_mle_FormShelf_itemNew(JNIEnv* env,
	jclass classy, jint type)
{
	return forwardCallStaticObject(env, SWINGUIFORM_CLASSNAME,
		"itemNew", SWINGUIFORM_ITEMNEW_DESC,
		type);
}

JNIEXPORT jint JNICALL Impl_mle_FormShelf_metric(JNIEnv* env, jclass classy,
	jint metricId)
{
	return forwardCallStaticInteger(env, SWINGUIFORM_CLASSNAME,
		"metric", SWINGUIFORM_METRIC_DESC,
		metricId);
}

static const JNINativeMethod mleforwardMethods[] =
{
	{"displays", SWINGUIFORM_DISPLAYS_DESC, (void*)Impl_mle_FormShelf_displays},
	{"displayShow", SWINGUIFORM_DISPLAYSHOW_DESC, (void*)Impl_mle_FormShelf_displayShow},
	{"formDelete", SWINGUIFORM_FORMDELETE_DESC, (void*)Impl_mle_FormShelf_formDelete},
	{"formItemAtPosition", SWINGUIFORM_FORMITEMATPOSITION_DESC, (void*)Impl_mle_FormShelf_formItemAtPosition},
	{"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONGET_DESC, (void*)Impl_mle_FormShelf_formItemPositionGet},
	{"formItemPosition", SWINGUIFORM_FORMITEMPOSITIONSET_DESC, (void*)Impl_mle_FormShelf_formItemPositionSet},
	{"formItemRemove", SWINGUIFORM_FORMITEMREMOVE_DESC, (void*)Impl_mle_FormShelf_formItemRemove},
	{"formNew", SWINGUIFORM_FORMNEW_DESC, (void*)Impl_mle_FormShelf_formNew},
	{"itemDelete", SWINGUIFORM_ITEMDELETE_DESC, (void*)Impl_mle_FormShelf_itemDelete},
	{"itemNew", SWINGUIFORM_ITEMNEW_DESC, (void*)Impl_mle_FormShelf_itemNew},
	{"metric", SWINGUIFORM_METRIC_DESC, (void*)Impl_mle_FormShelf_metric},
};

jint JNICALL mleFormInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/UIFormShelf"),
		mleforwardMethods, sizeof(mleforwardMethods) /
			sizeof(JNINativeMethod));
}
