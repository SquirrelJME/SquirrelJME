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
	def cast_to(self, other: PrettyType) -> List['PrettyValue']:
		"""
		Casts this value to another type.

		:param other: The other type.
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
	def register_string(self, debug_type: PrettyType,
			func: Callable[[PrettyValue], str]) -> None:
		"""
		Registers a type which returns a string to represent its value.

		:param debug_type: The type to register.
		:param func: The function to be called.
		"""
		pass

# ---------------------------------------------------------------------------

def pretty_sjme_char_seq(value: PrettyValue) -> str:
	# Everything is in the data member
	data_member: PrettyValue = value.member("data")
	type_member: PrettyValue = value.member("type")
	if data_member is None or type_member is None:
		return "<???>"

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
				return "<???>"
			return value.str_value()

		# Wide
		case 4:
			value = data_member.member("chars")
			if value is None:
				return "<???>"
			return value.str_value()

		# UTF
		case 5:
			value = data_member.member("bytes")
			if value is None:
				return "<???>"
			return value.str_value()

		# Static UTF
		case 6:
			value = data_member.member("staticUtf")
			if value is None:
				return "<???>"
			return value.str_value()

		# Static Wide
		case 7:
			value = data_member.member("staticWide")
			if value is None:
				return "<???>"
			return value.str_value()

	# Some other type?
	return "<???>"

# ---------------------------------------------------------------------------

def register_all_types(maker: PrettyMaker,
	register: PrettyRegister) -> None:
	"""
	Registers all types

	:param maker: The type maker.
	:param register: The debug registration system.
	"""
	# sjme_charSeq and variants
	register.register_string(maker.make_type("sjme_charSeq"),
		pretty_sjme_char_seq)
	register.register_string(maker.make_type("sjme_charSeqStatic"),
		pretty_sjme_char_seq)
