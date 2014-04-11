package com.dimitrisli.web.dailydeal

trait Publisher

object Manning extends Publisher { override def toString = "Manning" }

object APress extends Publisher { override def toString = "APress" }

object Springer extends Publisher  { override def toString = "Springer" }

object OReilly extends Publisher { override def toString = "OReilly" }

object Microsoft extends Publisher { override def toString = "Microsoft" }
