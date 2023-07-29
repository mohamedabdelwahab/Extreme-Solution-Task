package com.mohamed.sampleapp.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Patterns
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.isEmail() : Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPasswordMatch(password: String): Boolean {
    return this == password
}


@SuppressLint("SimpleDateFormat")
fun String.getFormatDate() : String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getFormatDateTimeType() : String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("MM/dd HH:mm a")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getUpcomingEventDateFormat(): String {

    val dateFormat = SimpleDateFormat("MM/dd/yyyy - HH:mm")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("MMMM/yyyy")
    return dfFormat.format(date)

}

@SuppressLint("SimpleDateFormat")
fun String.getPastEventDateFormat(): String {

    val dateFormat = SimpleDateFormat("MM/dd/yyyy - HH:mm")

    val date: Date = dateFormat.parse(this)

    val dfFormat: DateFormat =
        SimpleDateFormat("yyyy")
    return dfFormat.format(date)

}

 fun String.prepareFilePart(partName: String): MultipartBody.Part {
    val file = File(this)
    // create RequestBody instance from file
    val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)

}

fun Uri.requestBody(context: Context, partName: String): MultipartBody.Part {
    val mediaType = extension(context)?.toMediaTypeOrNull()
    val bytes = context.contentResolver.openInputStream(this)?.buffered()?.use { it.readBytes() }

    return bytes?.let {
        MultipartBody.Part.createFormData(
            partName, fileNme(context),
            bytes.toRequestBody(mediaType, 0, bytes.size)
        )
    } ?: MultipartBody.Part.createFormData(partName, "", "".requestBodyFromString())
}

fun Uri.extension(context: Context) = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType(context))

fun Uri.mimeType(context: Context) = context.contentResolver.getType(this)

fun Uri.fileNme(context: Context) =
    context.contentResolver.query(this, null, null, null, null)?.let {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        return it.getString(nameIndex)
    } ?: ""


 fun String.createPartFromString(): RequestBody {
    return RequestBody.create(MultipartBody.FORM, this)
}

fun String.requestBodyFromString(): RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}

