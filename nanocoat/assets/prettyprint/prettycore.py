# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Core pretty printing support

import abc
from collections.abc import Callable
from typing import Any, List, final


class PrettyType(abc.ABC):
	def __init__(self, debug_type: Any, optimal_name: str | None):
		self.debug_type = debug_type
		self.optimal_name = optimal_name

	@abc.abstractmethod
	def is_pointer(self) -> bool:
		"""
		Is this a pointer?
		"""
		pass

	@abc.abstractmethod
	def names(self) -> list[str]:
		"""
		Returns all the known names for this type and any aliases such
		as typedefs.
		"""
		pass

	@final
	def name(self) -> str:
		"""
		Returns a name for the type.

		:return: The name for the type.
		"""
		# If an explicit name was given here, then use that
		if self.optimal_name is not None:
			return self.optimal_name

		# Otherwise, pick the first name
		names = self.names()
		if names is None or len(names) == 0:
			return "void"
		return names[0]

class PrettyValue(abc.ABC):
	def __init__(self, debug_value: Any):
		self.debug_value = debug_value

	@abc.abstractmethod
	def address(self) -> int:
		"""
		Returns the object address
		"""
		pass

	@abc.abstractmethod
	def as_dereference(self) -> List['PrettyValue']:
		"""
		Returns this value dereferenced
		"""
		pass

	@final
	def as_dereference_if_not(self) -> List['PrettyValue']:
		"""
		Returns self dereferenced if this is not a non-pointer already.
		"""
		if not self.pretty_type().is_pointer():
			return self
		return self.as_dereference()

	@final
	def as_dereference_if_not_all(self) -> List['PrettyValue']:
		"""
		Returns self dereferenced as much as possible
		"""
		maybe = self
		while maybe.pretty_type().is_pointer():
			# Dereference
			was = maybe
			maybe = maybe.as_dereference()

			# Unchanged? Do not want to loop forever here
			if was == maybe:
				return maybe
		return maybe

	@abc.abstractmethod
	def as_pointer(self) -> List['PrettyValue']:
		"""
		Returns this value as a pointer
		"""
		pass

	@final
	def as_pointer_if_not(self) -> List['PrettyValue']:
		"""
		Returns self as a pointer if this is not a pointer already.
		"""
		if self.pretty_type().is_pointer():
			return self
		return self.as_pointer()

	@abc.abstractmethod
	def cast_to(self, other: PrettyType) -> List['PrettyValue']:
		"""
		Casts this value to another type.

		:param other: The other type.
		"""
		pass

	@abc.abstractmethod
	def index(self, index: int) -> List['PrettyValue'] | None:
		"""
		Returns the index of a sub-member.

		:param index: The member index to get.
		"""
		pass

	@abc.abstractmethod
	def index_unsafe(self, index: int) -> List['PrettyValue'] | None:
		"""
		Returns the index of a sub-member, using an unsafe method.

		:param index: The member index to get.
		"""
		pass

	@abc.abstractmethod
	def int_value(self) -> int:
		"""
		Returns the best-guess integer value for this type.
		"""
		pass

	@abc.abstractmethod
	def member(self, member_name: str) -> List['PrettyValue'] | None:
		"""
		Returns the specified member of this value, or None.

		:param member_name: The name of the member to get.
		"""
		pass

	@abc.abstractmethod
	def pretty_type(self) -> PrettyType:
		"""
		Returns the pretty type for this value.
		"""
		pass

	@abc.abstractmethod
	def str_value(self) -> str:
		"""
		Returns the best-guess string value for this type.
		"""
		pass

	@abc.abstractmethod
	def str_wide_value(self) -> str:
		"""
		Returns the best-guess wide string value for this type.
		"""
		pass

class PrettyList(abc.ABC):
	def __init__(self, maker: List['PrettyMaker'], value: PrettyValue):
		self.maker = maker
		self.value = value

	@abc.abstractmethod
	def list_length(self) -> int:
		"""
		Returns the length of the list.
		"""
		pass

	@abc.abstractmethod
	def list_index(self, index: int) -> PrettyValue | None:
		"""
		Returns the value of a given index.

		:param index: The index to get the value of.
		"""
		pass

class PrettyMaker(abc.ABC):
	@abc.abstractmethod
	def make_type(self, type_name: str) -> PrettyType | None:
		"""
		Makes the given type and returns it.

		:param type_name: The type to make.
		"""
		pass

class PrettyRegister(abc.ABC):
	@abc.abstractmethod
	def register_list(self, debug_type: PrettyType,
		func: Callable[[PrettyMaker, PrettyValue], PrettyList]) -> None:
		"""
		Registers a type which returns a list to represent its value.

		:param debug_type: The type to register.
		:param func: The function to be called.
		"""
		pass

	@abc.abstractmethod
	def register_string(self, debug_type: PrettyType,
			func: Callable[[PrettyMaker, PrettyValue], str]) -> None:
		"""
		Registers a type which returns a string to represent its value.

		:param debug_type: The type to register.
		:param func: The function to be called.
		"""
		pass

# ---------------------------------------------------------------------------

def pretty_sjme_lpcwstr(maker: PrettyMaker, value: PrettyValue) -> str:
	# This is just a direct wide string value
	return value.str_wide_value()

def pretty_sjme_char_seq(maker: PrettyMaker, value: PrettyValue) -> str:
	# Everything is in the data member
	data_member: PrettyValue = value.member("data")
	type_member: PrettyValue = value.member("type")
	if data_member is None or type_member is None:
		return "<A???>"

	# And how this is interpreted is based on the type
	match type_member.int_value():
		# Null, which is always a blank string
		case 0:
			return ""

		# Function
		case 1:
			return "<FUNCTION>"

		# Function Static
		case 2:
			return "<STATIC FUNCTION>"

		# Narrow
		case 3:
			value = data_member.member("bytes")
			if value is None:
				return "<3???>"
			return value.str_value()

		# Wide
		case 4:
			value = data_member.member("chars")
			if value is None:
				return "<4???>"
			return value.str_wide_value()

		# UTF
		case 5:
			value = data_member.member("bytes")
			if value is None:
				return "<5???>"
			return value.str_value()

		# Static UTF
		case 6:
			value = data_member.member("staticUtf")
			if value is None:
				return "<6???>"
			return value.str_value()

		# Static Wide
		case 7:
			value = data_member.member("staticWide")
			if value is None:
				return "<7???>"
			return value.str_wide_value()

	# Some other type?
	return "<Z???>"

def pretty_sjme_any_object(maker: PrettyMaker, value: PrettyValue) -> str:
	# If this object ends up being invalid, use an invalid string
	invalid: str = f"<OBJECT @0x{value.address():08x}?>"

	# There always is "object" if this has an object base
	# If it does not, we can just use the base object
	cast = value.member("object")
	if cast is None:
		cast = value
		invalid += "<->"
	else:
		invalid += "<+>"

	# Get the class this is, note that this is a non-cyclic
	is_class = cast.member("isClass")
	if is_class is None:
		return invalid + "<A>"
	is_class = is_class.member("value")
	if is_class is None:
		return invalid + "<B>"

	# Get the name of this class
	field_name = is_class.member("fieldName")
	if field_name is None:
		return invalid + "<C>"

	# Get the charSeq value from this
	char_seq = pretty_sjme_char_seq(maker,
		field_name.as_dereference_if_not_all())
	if char_seq is not None and char_seq != "<???>" and char_seq != "":
		return f"OBJECT {char_seq} @0x{value.address():08x}"

	# Unknown or invalid object?
	return invalid + "<Z>"

class AdapterList(PrettyList):
	def __init__(self, maker: List['PrettyMaker'], value: PrettyValue):
		super().__init__(maker, value)

	def list_length(self) -> int:
		len: PrettyValue = self.value.member("length")
		if len is None:
			return 0
		return len.int_value()

	def list_index(self, index: int) -> PrettyValue | None:
		elements: PrettyValue = self.value.member("elements")
		if elements is None:
			return None
		return elements.index(index)

def pretty_sjme_any_sjme_list(maker: PrettyMaker,
		value: PrettyValue) -> PrettyList:
	return AdapterList(maker, value)

# ---------------------------------------------------------------------------

def register_all_types(maker: PrettyMaker,
	register: PrettyRegister) -> None:
	"""
	Registers all types

	:param maker: The type maker.
	:param register: The debug registration system.
	"""
	# sjme_jchar* and sjme_lpcwstr
	for object_type in list[str](["sjme_jchar*",
		"sjme_jchar[]",
		"sjme_jchar[*]",
		"sjme_lpcwstr"]):
		register.register_string(maker.make_type(object_type),
			pretty_sjme_lpcwstr)

	# sjme_charSeq and variants
	for object_type in list[str](["sjme_charSeq",
		"sjme_charSeqStatic"]):
		register.register_string(maker.make_type(object_type),
			pretty_sjme_char_seq)

	# Objects and all of their variants
	for object_type in list[str](["sjme_jobject",
		"sjme_jclass",
		"sjme_jstring",
		"sjme_jthrowable",
		"sjme_jarray",
		"sjme_jweak",
		"sjme_nvm_thread"]):
		# The base type
		register.register_string(maker.make_type(object_type + "Base"),
			pretty_sjme_any_object)

		# The aliased pointer type
		register.register_string(maker.make_type(object_type),
			pretty_sjme_any_object)

	# Every list type, and what is currently known
	register.register_list(maker.make_type("sjme_list"),
		pretty_sjme_any_sjme_list)
	register.register_list(maker.make_type("sjme_list_*"),
		pretty_sjme_any_sjme_list)
	for object_type in list[str](["sjme_intPointer",
		"sjme_jbracketJarPackage",
		"sjme_jclass",
		"sjme_jfieldID",
		"sjme_jint",
		"sjme_jinterfaceID",
		"sjme_jintPP",
		"sjme_jmethodID",
		"sjme_jstring",
		"sjme_lpcstr",
		"sjme_lpstr",
		"sjme_nvm_class_annotation",
		"sjme_nvm_class_exceptionHandler",
		"sjme_nvm_class_fieldInfo",
		"sjme_nvm_class_info",
		"sjme_nvm_class_methodInfo",
		"sjme_nvm_class_poolEntry",
		"sjme_nvm_frame",
		"sjme_nvm_rom_library",
		"sjme_nvm_rom_swmEntry",
		"sjme_nvm_rom_swmLibrary",
		"sjme_nvm_stringPool_string",
		"sjme_nvm_task",
		"sjme_nvm_thread",
		"sjme_pointer",
		"sjme_scritchaudio_connection",
		"sjme_scritchaudio_midiPort",
		"sjme_scritchaudio_source",
		"sjme_scritchui_pencilFont",
		"sjme_scritchui_uiChoiceItem",
		"sjme_scritchui_uiComponent",
		"sjme_scritchui_uiMenuKind",
		"sjme_scritchui_uiScreen"]):
		register.register_list(
			maker.make_type("sjme_list_" + object_type),
			pretty_sjme_any_sjme_list)
		register.register_list(
			maker.make_type("sjme_list_" + object_type + "*"),
			pretty_sjme_any_sjme_list)
