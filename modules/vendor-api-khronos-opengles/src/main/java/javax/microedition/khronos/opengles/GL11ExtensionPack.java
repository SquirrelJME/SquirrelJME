// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Api
@SuppressWarnings({"FieldNamingConvention", "NewMethodNamingConvention"})
public interface GL11ExtensionPack
	extends GL
{
	@Api
	int GL_BLEND_DST_ALPHA =
		32970;
	
	@Api
	int GL_BLEND_DST_RGB =
		32968;
	
	@Api
	int GL_BLEND_EQUATION =
		32777;
	
	@Api
	int GL_BLEND_EQUATION_ALPHA =
		34877;
	
	@Api
	int GL_BLEND_EQUATION_RGB =
		32777;
	
	@Api
	int GL_BLEND_SRC_ALPHA =
		32971;
	
	@Api
	int GL_BLEND_SRC_RGB =
		32969;
	
	@Api
	int GL_COLOR_ATTACHMENT0_OES =
		36064;
	
	@Api
	int GL_COLOR_ATTACHMENT10_OES =
		36074;
	
	@Api
	int GL_COLOR_ATTACHMENT11_OES =
		36075;
	
	@Api
	int GL_COLOR_ATTACHMENT12_OES =
		36076;
	
	@Api
	int GL_COLOR_ATTACHMENT13_OES =
		36077;
	
	@Api
	int GL_COLOR_ATTACHMENT14_OES =
		36078;
	
	@Api
	int GL_COLOR_ATTACHMENT15_OES =
		36079;
	
	@Api
	int GL_COLOR_ATTACHMENT1_OES =
		36065;
	
	@Api
	int GL_COLOR_ATTACHMENT2_OES =
		36066;
	
	@Api
	int GL_COLOR_ATTACHMENT3_OES =
		36067;
	
	@Api
	int GL_COLOR_ATTACHMENT4_OES =
		36068;
	
	@Api
	int GL_COLOR_ATTACHMENT5_OES =
		36069;
	
	@Api
	int GL_COLOR_ATTACHMENT6_OES =
		36070;
	
	@Api
	int GL_COLOR_ATTACHMENT7_OES =
		36071;
	
	@Api
	int GL_COLOR_ATTACHMENT8_OES =
		36072;
	
	@Api
	int GL_COLOR_ATTACHMENT9_OES =
		36073;
	
	@Api
	int GL_DECR_WRAP =
		34056;
	
	@Api
	int GL_DEPTH_ATTACHMENT_OES =
		36096;
	
	@Api
	int GL_DEPTH_COMPONENT =
		6402;
	
	@Api
	int GL_DEPTH_COMPONENT16 =
		33189;
	
	@Api
	int GL_DEPTH_COMPONENT24 =
		33190;
	
	@Api
	int GL_DEPTH_COMPONENT32 =
		33191;
	
	@Api
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_OES =
		36049;
	
	@Api
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES =
		36048;
	
	@Api
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_OES =
		36051;
	
	@Api
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_OES =
		36050;
	
	@Api
	int GL_FRAMEBUFFER_BINDING_OES =
		36006;
	
	@Api
	int GL_FRAMEBUFFER_COMPLETE_OES =
		36053;
	
	@Api
	int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES =
		36054;
	
	@Api
	int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES =
		36057;
	
	@Api
	int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_OES =
		36059;
	
	@Api
	int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_OES =
		36058;
	
	@Api
	int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES =
		36055;
	
	@Api
	int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_OES =
		36060;
	
	@Api
	int GL_FRAMEBUFFER_OES =
		36160;
	
	@Api
	int GL_FRAMEBUFFER_UNSUPPORTED_OES =
		36061;
	
	@Api
	int GL_FUNC_ADD =
		32774;
	
	@Api
	int GL_FUNC_REVERSE_SUBTRACT =
		32779;
	
	@Api
	int GL_FUNC_SUBTRACT =
		32778;
	
	@Api
	int GL_INCR_WRAP =
		34055;
	
	@Api
	int GL_INVALID_FRAMEBUFFER_OPERATION_OES =
		1286;
	
	@Api
	int GL_MAX_COLOR_ATTACHMENTS_OES =
		36063;
	
	@Api
	int GL_MAX_CUBE_MAP_TEXTURE_SIZE =
		34076;
	
	@Api
	int GL_MAX_RENDERBUFFER_SIZE_OES =
		34024;
	
	@Api
	int GL_MIRRORED_REPEAT =
		33648;
	
	@Api
	int GL_NORMAL_MAP =
		34065;
	
	@Api
	int GL_REFLECTION_MAP =
		34066;
	
	@Api
	int GL_RENDERBUFFER_ALPHA_SIZE_OES =
		36179;
	
	@Api
	int GL_RENDERBUFFER_BINDING_OES =
		36007;
	
	@Api
	int GL_RENDERBUFFER_BLUE_SIZE_OES =
		36178;
	
	@Api
	int GL_RENDERBUFFER_DEPTH_SIZE_OES =
		36180;
	
	@Api
	int GL_RENDERBUFFER_GREEN_SIZE_OES =
		36177;
	
	@Api
	int GL_RENDERBUFFER_HEIGHT_OES =
		36163;
	
	@Api
	int GL_RENDERBUFFER_INTERNAL_FORMAT_OES =
		36164;
	
	@Api
	int GL_RENDERBUFFER_OES =
		36161;
	
	@Api
	int GL_RENDERBUFFER_RED_SIZE_OES =
		36176;
	
	@Api
	int GL_RENDERBUFFER_STENCIL_SIZE_OES =
		36181;
	
	@Api
	int GL_RENDERBUFFER_WIDTH_OES =
		36162;
	
	@Api
	int GL_RGB565_OES =
		36194;
	
	@Api
	int GL_RGB5_A1 =
		32855;
	
	@Api
	int GL_RGB8 =
		32849;
	
	@Api
	int GL_RGBA4 =
		32854;
	
	@Api
	int GL_RGBA8 =
		32856;
	
	@Api
	int GL_STENCIL_ATTACHMENT_OES =
		36128;
	
	@Api
	int GL_STENCIL_INDEX =
		6401;
	
	@Api
	int GL_STENCIL_INDEX1_OES =
		36166;
	
	@Api
	int GL_STENCIL_INDEX4_OES =
		36167;
	
	@Api
	int GL_STENCIL_INDEX8_OES =
		36168;
	
	@Api
	int GL_STR =
		-1;
	
	@Api
	int GL_TEXTURE_BINDING_CUBE_MAP =
		34068;
	
	@Api
	int GL_TEXTURE_CUBE_MAP =
		34067;
	
	@Api
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_X =
		8318;
	
	@Api
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y =
		8320;
	
	@Api
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z =
		8322;
	
	@Api
	int GL_TEXTURE_CUBE_MAP_POSITIVE_X =
		8317;
	
	@Api
	int GL_TEXTURE_CUBE_MAP_POSITIVE_Y =
		8319;
	
	@Api
	int GL_TEXTURE_CUBE_MAP_POSITIVE_Z =
		8321;
	
	@Api
	int GL_TEXTURE_GEN_MODE =
		9472;
	
	@Api
	int GL_TEXTURE_GEN_STR =
		36192;
	
	@Api
	void glBindFramebufferOES(int __a, int __b);
	
	@Api
	void glBindRenderbufferOES(int __a, int __b);
	
	@Api
	void glBindTexture(int __a, int __b);
	
	@Api
	void glBlendEquation(int __a);
	
	@Api
	void glBlendEquationSeparate(int __a, int __b);
	
	@Api
	void glBlendFuncSeparate(int __a, int __b, int __c, int __d);
	
	@Api
	int glCheckFramebufferStatusOES(int __a);
	
	@Api
	void glCompressedTexImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, Buffer __h);
	
	@Api
	void glCopyTexImage2D(int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h);
	
	@Api
	void glDeleteFramebuffersOES(int __a, int[] __b, int __c);
	
	@Api
	void glDeleteFramebuffersOES(int __a, IntBuffer __b);
	
	@Api
	void glDeleteRenderbuffersOES(int __a, int[] __b, int __c);
	
	@Api
	void glDeleteRenderbuffersOES(int __a, IntBuffer __b);
	
	@Api
	void glEnable(int __a);
	
	@Api
	void glFramebufferRenderbufferOES(int __a, int __b, int __c, int __d);
	
	@Api
	void glFramebufferTexture2DOES(int __a, int __b, int __c, int __d, int __e);
	
	@Api
	void glGenFramebuffersOES(int __a, int[] __b, int __c);
	
	@Api
	void glGenFramebuffersOES(int __a, IntBuffer __b);
	
	@Api
	void glGenRenderbuffersOES(int __a, int[] __b, int __c);
	
	@Api
	void glGenRenderbuffersOES(int __a, IntBuffer __b);
	
	@Api
	void glGenerateMipmapOES(int __a);
	
	@Api
	void glGetFramebufferAttachmentParameterivOES(int __a, int __b, int __c,
		int[] __d, int __e);
	
	@Api
	void glGetFramebufferAttachmentParameterivOES(int __a, int __b, int __c,
		IntBuffer __d);
	
	@Api
	void glGetIntegerv(int __a, int[] __b, int __c);
	
	@Api
	void glGetIntegerv(int __a, IntBuffer __b);
	
	@Api
	void glGetRenderbufferParameterivOES(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetRenderbufferParameterivOES(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetTexGenfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glGetTexGenfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glGetTexGeniv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetTexGeniv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetTexGenxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetTexGenxv(int __a, int __b, IntBuffer __c);
	
	@Api
	boolean glIsFramebufferOES(int __a);
	
	@Api
	boolean glIsRenderbufferOES(int __a);
	
	@Api
	void glRenderbufferStorageOES(int __a, int __b, int __c, int __d);
	
	@Api
	void glStencilOp(int __a, int __b, int __c);
	
	@Api
	void glTexEnvf(int __a, int __b, float __c);
	
	@Api
	void glTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glTexEnvx(int __a, int __b, int __c);
	
	@Api
	void glTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexEnvxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glTexGenf(int __a, int __b, float __c);
	
	@Api
	void glTexGenfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glTexGenfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glTexGeni(int __a, int __b, int __c);
	
	@Api
	void glTexGeniv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexGeniv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glTexGenx(int __a, int __b, int __c);
	
	@Api
	void glTexGenxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexGenxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glTexParameterf(int __a, int __b, float __c);
}


