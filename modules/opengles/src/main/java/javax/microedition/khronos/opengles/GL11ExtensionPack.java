// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@SuppressWarnings({"FieldNamingConvention", "NewMethodNamingConvention"})
public interface GL11ExtensionPack
	extends GL
{
	int GL_BLEND_DST_ALPHA =
		32970;
	
	int GL_BLEND_DST_RGB =
		32968;
	
	int GL_BLEND_EQUATION =
		32777;
	
	int GL_BLEND_EQUATION_ALPHA =
		34877;
	
	int GL_BLEND_EQUATION_RGB =
		32777;
	
	int GL_BLEND_SRC_ALPHA =
		32971;
	
	int GL_BLEND_SRC_RGB =
		32969;
	
	int GL_COLOR_ATTACHMENT0_OES =
		36064;
	
	int GL_COLOR_ATTACHMENT10_OES =
		36074;
	
	int GL_COLOR_ATTACHMENT11_OES =
		36075;
	
	int GL_COLOR_ATTACHMENT12_OES =
		36076;
	
	int GL_COLOR_ATTACHMENT13_OES =
		36077;
	
	int GL_COLOR_ATTACHMENT14_OES =
		36078;
	
	int GL_COLOR_ATTACHMENT15_OES =
		36079;
	
	int GL_COLOR_ATTACHMENT1_OES =
		36065;
	
	int GL_COLOR_ATTACHMENT2_OES =
		36066;
	
	int GL_COLOR_ATTACHMENT3_OES =
		36067;
	
	int GL_COLOR_ATTACHMENT4_OES =
		36068;
	
	int GL_COLOR_ATTACHMENT5_OES =
		36069;
	
	int GL_COLOR_ATTACHMENT6_OES =
		36070;
	
	int GL_COLOR_ATTACHMENT7_OES =
		36071;
	
	int GL_COLOR_ATTACHMENT8_OES =
		36072;
	
	int GL_COLOR_ATTACHMENT9_OES =
		36073;
	
	int GL_DECR_WRAP =
		34056;
	
	int GL_DEPTH_ATTACHMENT_OES =
		36096;
	
	int GL_DEPTH_COMPONENT =
		6402;
	
	int GL_DEPTH_COMPONENT16 =
		33189;
	
	int GL_DEPTH_COMPONENT24 =
		33190;
	
	int GL_DEPTH_COMPONENT32 =
		33191;
	
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_OES =
		36049;
	
	int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES =
		36048;
	
	int
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_OES =
		36051;
	
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_OES =
		36050;
	
	int GL_FRAMEBUFFER_BINDING_OES =
		36006;
	
	int GL_FRAMEBUFFER_COMPLETE_OES =
		36053;
	
	int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES =
		36054;
	
	int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES =
		36057;
	
	int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_OES =
		36059;
	
	int GL_FRAMEBUFFER_INCOMPLETE_FORMATS_OES =
		36058;
	
	int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES
		=
		36055;
	
	int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_OES =
		36060;
	
	int GL_FRAMEBUFFER_OES =
		36160;
	
	int GL_FRAMEBUFFER_UNSUPPORTED_OES =
		36061;
	
	int GL_FUNC_ADD =
		32774;
	
	int GL_FUNC_REVERSE_SUBTRACT =
		32779;
	
	int GL_FUNC_SUBTRACT =
		32778;
	
	int GL_INCR_WRAP =
		34055;
	
	int GL_INVALID_FRAMEBUFFER_OPERATION_OES =
		1286;
	
	int GL_MAX_COLOR_ATTACHMENTS_OES =
		36063;
	
	int GL_MAX_CUBE_MAP_TEXTURE_SIZE =
		34076;
	
	int GL_MAX_RENDERBUFFER_SIZE_OES =
		34024;
	
	int GL_MIRRORED_REPEAT =
		33648;
	
	int GL_NORMAL_MAP =
		34065;
	
	int GL_REFLECTION_MAP =
		34066;
	
	int GL_RENDERBUFFER_ALPHA_SIZE_OES =
		36179;
	
	int GL_RENDERBUFFER_BINDING_OES =
		36007;
	
	int GL_RENDERBUFFER_BLUE_SIZE_OES =
		36178;
	
	int GL_RENDERBUFFER_DEPTH_SIZE_OES =
		36180;
	
	int GL_RENDERBUFFER_GREEN_SIZE_OES =
		36177;
	
	int GL_RENDERBUFFER_HEIGHT_OES =
		36163;
	
	int GL_RENDERBUFFER_INTERNAL_FORMAT_OES =
		36164;
	
	int GL_RENDERBUFFER_OES =
		36161;
	
	int GL_RENDERBUFFER_RED_SIZE_OES =
		36176;
	
	int GL_RENDERBUFFER_STENCIL_SIZE_OES =
		36181;
	
	int GL_RENDERBUFFER_WIDTH_OES =
		36162;
	
	int GL_RGB565_OES =
		36194;
	
	int GL_RGB5_A1 =
		32855;
	
	int GL_RGB8 =
		32849;
	
	int GL_RGBA4 =
		32854;
	
	int GL_RGBA8 =
		32856;
	
	int GL_STENCIL_ATTACHMENT_OES =
		36128;
	
	int GL_STENCIL_INDEX =
		6401;
	
	int GL_STENCIL_INDEX1_OES =
		36166;
	
	int GL_STENCIL_INDEX4_OES =
		36167;
	
	int GL_STENCIL_INDEX8_OES =
		36168;
	
	int GL_STR =
		-1;
	
	int GL_TEXTURE_BINDING_CUBE_MAP =
		34068;
	
	int GL_TEXTURE_CUBE_MAP =
		34067;
	
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_X =
		8318;
	
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y =
		8320;
	
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z =
		8322;
	
	int GL_TEXTURE_CUBE_MAP_POSITIVE_X =
		8317;
	
	int GL_TEXTURE_CUBE_MAP_POSITIVE_Y =
		8319;
	
	int GL_TEXTURE_CUBE_MAP_POSITIVE_Z =
		8321;
	
	int GL_TEXTURE_GEN_MODE =
		9472;
	
	int GL_TEXTURE_GEN_STR =
		36192;
	
	void glBindFramebufferOES(int __a, int __b);
	
	void glBindRenderbufferOES(int __a, int __b);
	
	void glBindTexture(int __a, int __b);
	
	void glBlendEquation(int __a);
	
	void glBlendEquationSeparate(int __a, int __b);
	
	void glBlendFuncSeparate(int __a, int __b, int __c, int __d);
	
	int glCheckFramebufferStatusOES(int __a);
	
	void glCompressedTexImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, Buffer __h);
	
	void glCopyTexImage2D(int __a, int __b, int __c, int __d, int __e,
	 int __f,
		int __g, int __h);
	
	void glDeleteFramebuffersOES(int __a, int[] __b, int __c);
	
	void glDeleteFramebuffersOES(int __a, IntBuffer __b);
	
	void glDeleteRenderbuffersOES(int __a, int[] __b, int __c);
	
	void glDeleteRenderbuffersOES(int __a, IntBuffer __b);
	
	void glEnable(int __a);
	
	void glFramebufferRenderbufferOES(int __a, int __b, int __c, int __d);
	
	void glFramebufferTexture2DOES(int __a, int __b, int __c, int __d,
	 int __e);
	
	void glGenFramebuffersOES(int __a, int[] __b, int __c);
	
	void glGenFramebuffersOES(int __a, IntBuffer __b);
	
	void glGenRenderbuffersOES(int __a, int[] __b, int __c);
	
	void glGenRenderbuffersOES(int __a, IntBuffer __b);
	
	void glGenerateMipmapOES(int __a);
	
	void glGetFramebufferAttachmentParameterivOES(int __a, int __b, int __c,
		int[] __d, int __e);
	
	void glGetFramebufferAttachmentParameterivOES(int __a, int __b, int __c,
		IntBuffer __d);
	
	void glGetIntegerv(int __a, int[] __b, int __c);
	
	void glGetIntegerv(int __a, IntBuffer __b);
	
	void glGetRenderbufferParameterivOES(int __a, int __b, int[] __c,
	 int __d);
	
	void glGetRenderbufferParameterivOES(int __a, int __b, IntBuffer __c);
	
	void glGetTexGenfv(int __a, int __b, float[] __c, int __d);
	
	void glGetTexGenfv(int __a, int __b, FloatBuffer __c);
	
	void glGetTexGeniv(int __a, int __b, int[] __c, int __d);
	
	void glGetTexGeniv(int __a, int __b, IntBuffer __c);
	
	void glGetTexGenxv(int __a, int __b, int[] __c, int __d);
	
	void glGetTexGenxv(int __a, int __b, IntBuffer __c);
	
	boolean glIsFramebufferOES(int __a);
	
	boolean glIsRenderbufferOES(int __a);
	
	void glRenderbufferStorageOES(int __a, int __b, int __c, int __d);
	
	void glStencilOp(int __a, int __b, int __c);
	
	void glTexEnvf(int __a, int __b, float __c);
	
	void glTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	void glTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	void glTexEnvx(int __a, int __b, int __c);
	
	void glTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	void glTexEnvxv(int __a, int __b, IntBuffer __c);
	
	void glTexGenf(int __a, int __b, float __c);
	
	void glTexGenfv(int __a, int __b, float[] __c, int __d);
	
	void glTexGenfv(int __a, int __b, FloatBuffer __c);
	
	void glTexGeni(int __a, int __b, int __c);
	
	void glTexGeniv(int __a, int __b, int[] __c, int __d);
	
	void glTexGeniv(int __a, int __b, IntBuffer __c);
	
	void glTexGenx(int __a, int __b, int __c);
	
	void glTexGenxv(int __a, int __b, int[] __c, int __d);
	
	void glTexGenxv(int __a, int __b, IntBuffer __c);
	
	void glTexParameterf(int __a, int __b, float __c);
}


