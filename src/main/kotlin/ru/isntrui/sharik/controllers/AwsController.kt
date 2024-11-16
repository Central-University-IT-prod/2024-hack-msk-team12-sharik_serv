package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.isntrui.sharik.services.AwsService
import kotlin.math.abs

@RestController
@RequestMapping("/api/aws")
@Tag(name = "AWS")
class AwsController @Autowired constructor(private val aws: AwsService) {

    @Operation(summary = "Upload file to AWS")
    @PostMapping("upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        var n: String
        var res: String
        if (file.isEmpty || file.originalFilename == null) {
            return ResponseEntity.badRequest().body("file or/and filename are empty")
        }
        try {
            n = "prod_avatar_" + abs(
                (file.originalFilename.hashCode())
            ) + "_" + ((Math.random() * Math.random() * 10000).toInt()
                .toString() + "" + ((System.currentTimeMillis() / (1000 * 1000 * 1000 * 1000)) * Math.random()).toInt())
            val `is` = file.inputStream
            res = aws.uploadFile(`is`, n, FilenameUtils.getExtension(file.originalFilename))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(e.toString())
        }
        return ResponseEntity.ok<String>(res)
    }
}

