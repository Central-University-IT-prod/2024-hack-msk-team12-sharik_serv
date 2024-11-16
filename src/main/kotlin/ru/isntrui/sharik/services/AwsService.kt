package ru.isntrui.sharik.services

import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.time.Duration

@Service
class AwsService {
    private val accessKeyId: String? = System.getenv("AWS_ACCESS_KEY_ID")
    private val secretAccessKey: String? = System.getenv("AWS_SECRET_ACCESS_KEY")

    private val s3Client: S3Client = S3Client.builder()
        .region(Region.of(REGION))
        .endpointOverride(URI.create("https://storage.yandexcloud.net"))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
            )
        )
        .httpClient(
            ApacheHttpClient.builder()
                .socketTimeout(Duration.ofMinutes(5))
                .connectionTimeout(Duration.ofMinutes(2))
                .build()
        )
        .overrideConfiguration(
            ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(10))
                .apiCallAttemptTimeout(Duration.ofMinutes(5))
                .build()
        )
        .build()

    @Throws(IOException::class)
    fun uploadFile(inputStream: InputStream, fileName: String?, format: String?): String {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket("lbs3")
            .key(fileName + "." + format)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available().toLong()))
        return getFileUrl("lbs3", fileName + "." + format)
    }

    fun getFileUrl(bucketName: String?, fileName: String?): String {
        return String.format("http://igw.isntrui.ru:1401/res/%s", fileName)
    }

    companion object {
        private const val REGION = "ru-central1"
    }
}
