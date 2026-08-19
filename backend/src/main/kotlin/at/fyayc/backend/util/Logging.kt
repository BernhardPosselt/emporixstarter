package at.fyayc.backend.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

// taken from https://youtu.be/TxmBk_VhuqY?si=7urOkjEtensY8-35&t=1481
// can be removed once Spring ships these natively

inline fun logger(): Logger =
    LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

inline fun <reified T> LoggerFactory.loggerForType() =
    LoggerFactory.getLogger(T::class.java)
