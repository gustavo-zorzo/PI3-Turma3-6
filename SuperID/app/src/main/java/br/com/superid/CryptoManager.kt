package br.com.superid

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoManager {

    private const val AES_KEY_BASE64 = "lZr8hvBcmv9VgZ5fJmUvMyJTbHg1+nqA8r5QF5DCAXk="

    private val secretKey: SecretKey
        get() {
            val decodedKey = Base64.decode(AES_KEY_BASE64, Base64.NO_WRAP)
            return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        }

    fun encryptAES(data: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(data.toByteArray())
        val combined = iv + encrypted
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decryptAES(data: String): String {
        val decoded = Base64.decode(data, Base64.NO_WRAP)
        val iv = decoded.copyOfRange(0, 16)
        val encrypted = decoded.copyOfRange(16, decoded.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val original = cipher.doFinal(encrypted)
        return String(original)
    }

}