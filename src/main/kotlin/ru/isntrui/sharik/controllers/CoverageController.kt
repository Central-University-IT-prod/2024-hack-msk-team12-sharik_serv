package ru.isntrui.sharik.controllers

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.lang.Exception
import java.nio.file.Paths

@Hidden
@Controller
@RequestMapping("/api/coverage")
class CoverageController {
    @GetMapping("/report")
    fun serveCoverageReport(): ResponseEntity<Resource?> {
        return try {
            val filePath = Paths.get("build/reports/jacoco/test/html/index.html").toAbsolutePath()
            val resource: Resource = UrlResource(filePath.toUri())

            if (resource.exists() && resource.isReadable) {
                ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"coverage-report.html\"")
                    .body<Resource?>(resource)
            } else {
                ResponseEntity.status(404).build<Resource?>()
            }
        } catch (_: Exception) {
            ResponseEntity.status(500).build<Resource?>()
        }
    }

    @GetMapping("/jacoco-resources/{res}")
    fun getRes(@PathVariable res: String?): ResponseEntity<Resource?> {
        return getResource("build/reports/jacoco/test/html/jacoco-resources/$res")
    }

    @GetMapping("/{dir}/{res}")
    fun getRep(@PathVariable dir: String?, @PathVariable res: String?): ResponseEntity<Resource?> {
        return getResource("build/reports/jacoco/test/html/$dir/$res")
    }

    private fun getResource(resourcePath: String): ResponseEntity<Resource?> {
        try {
            val filePath = Paths.get(resourcePath).toAbsolutePath()
            val resource: Resource = UrlResource(filePath.toUri())

            return if (resource.exists() && resource.isReadable) {
                ResponseEntity.ok()
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + filePath.fileName.toString() + "\""
                    )
                    .body<Resource?>(resource)
            } else {
                ResponseEntity.status(404).body<Resource?>(null)
            }
        } catch (_: Exception) {
            return ResponseEntity.status(500).body<Resource?>(null)
        }
    }
}
