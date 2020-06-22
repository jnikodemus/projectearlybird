package de.ntbit.projectearlybird.helper

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.parse.ParseFile
import java.io.ByteArrayOutputStream

class Converter {

    companion object {

        /**
         * Static method to convert a [Bitmap] to [ParseFile] expecting [contentResolver] and [uri].
         * Calls [convertBitmapToParseFile] for compression and at long last convert.
         * @return [ParseFile]
         */
        fun convertBitmapToParseFileByUri(contentResolver: ContentResolver, uri: Uri) : ParseFile {
            return convertBitmapToParseFileByUri(contentResolver, uri, 100)
        }

        fun convertBitmapToParseFileByUri(contentResolver: ContentResolver, uri: Uri, quality: Int) : ParseFile {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            return convertBitmapToParseFile(bitmap)
        }

        /**
         * Static method to convert a [Bitmap] to [ParseFile] expecting only the [Bitmap].
         * The provided Bitmap is compressed to [Bitmap.CompressFormat.PNG] in 100% quality.
         * @return [ParseFile]
         */
        fun convertBitmapToParseFile(bitmap: Bitmap) : ParseFile {
            return convertBitmapToParseFile(bitmap, 100)
        }

        fun convertBitmapToParseFile(bitmap: Bitmap, quality: Int) : ParseFile {
            var checkedQuality = quality
            if(checkedQuality > 100)
                checkedQuality = 100
            else if(checkedQuality < 1)
                checkedQuality = 1
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, checkedQuality, stream)
            val image: ByteArray = stream.toByteArray()
            return ParseFile(image)
        }
    }

}