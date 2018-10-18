package com.yy.aes

import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Created by binlly on 2018/10/16-18:32
 * @author binlly
 */

object AES {
    private const val UTF8 = "UTF-8"

    @Throws(InvalidKeyException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class, BadPaddingException::class, UnsupportedEncodingException::class)
    fun encryptString(content: String, key: String, mode: String, padding: String): String {
        val contentBytes = content.toByteArray(charset(UTF8))
        val keyBytes = key.toByteArray(charset(UTF8))
        val encryptedBytes = aesEncryptBytes(contentBytes, keyBytes, mode, padding)
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(encryptedBytes)
    }

    @Throws(InvalidKeyException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class, BadPaddingException::class, UnsupportedEncodingException::class)
    fun decryptString(content: String, key: String, mode: String, padding: String): String {
        val decoder = Base64.getDecoder()
        val encryptedBytes = decoder.decode(content)
        val keyBytes = key.toByteArray(charset(UTF8))
        val decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes, mode, padding)
        return String(decryptedBytes, charset(UTF8))
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class, BadPaddingException::class, UnsupportedEncodingException::class)
    private fun aesEncryptBytes(
        contentBytes: ByteArray, keyBytes: ByteArray, mode: String, padding: String
    ): ByteArray {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE, mode, padding)
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class, BadPaddingException::class, UnsupportedEncodingException::class)
    private fun aesDecryptBytes(
        contentBytes: ByteArray, keyBytes: ByteArray, mode: String, padding: String
    ): ByteArray {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE, mode, padding)
    }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class, InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class, BadPaddingException::class)
    private fun cipherOperation(
        contentBytes: ByteArray, keyBytes: ByteArray, enc: Int, mode: String, padding: String
    ): ByteArray {
        val secretKey = SecretKeySpec(keyBytes, "AES")
        val ivParameterSpec = IvParameterSpec(keyBytes)

        val cipher = Cipher.getInstance("AES/$mode/$padding")
        cipher.init(enc, secretKey, ivParameterSpec)

        return cipher.doFinal(contentBytes)
    }
}