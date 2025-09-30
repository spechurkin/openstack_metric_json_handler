package me.nn2.libs.services.compute

import me.nn2.libs.data.compute.ImageData
import me.nn2.libs.services.AbstractMetricService
import org.openstack4j.api.Builders.imageV2
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.common.Payloads
import org.openstack4j.model.image.v2.ContainerFormat
import org.openstack4j.model.image.v2.DiskFormat
import org.openstack4j.model.image.v2.Image
import java.net.URL

class ImageService(client: OSClientV3) : AbstractMetricService(client) {
    fun getImages(): List<ImageData> {
        return convertToDto()
    }

    fun getImage(imageName: String): Image? {
        return client.imagesV2().list().firstOrNull { it.name == imageName }
    }

    fun createImage(
        imageName: String,
        minRamGb: Long,
        minDiskGb: Long,
        visibility: String,
        urlToImg: URL
    ) {
        if (getImage(imageName) == null) {
            val visibilityFmt = when (visibility) {
                "Частный" -> Image.ImageVisibility.PRIVATE
                "Общий" -> Image.ImageVisibility.PUBLIC
                else -> Image.ImageVisibility.PUBLIC
            }

            val imageCreate = imageV2()
                .name(imageName)
                .containerFormat(ContainerFormat.BARE)
                .diskFormat(DiskFormat.QCOW2)
                .minRam(minRamGb)
                .minDisk(minDiskGb)
                .visibility(visibilityFmt)
                .build()
            val payload = Payloads.create(urlToImg)

            val image = client.imagesV2().create(imageCreate)

            client.imagesV2().upload(image?.id, payload, image)
        } else {
            createImage(
                addSymbolsToCopy(imageName),
                minRamGb,
                minDiskGb,
                visibility,
                urlToImg
            )
        }
    }

    fun updateImage(
        imageName: String,
        minRamGb: Long,
        minDiskGb: Long,
        visibility: String
    ): ImageData {
        val visibilityFmt = when (visibility) {
            "Частный" -> Image.ImageVisibility.PRIVATE
            "Общий" -> Image.ImageVisibility.PUBLIC
            else -> Image.ImageVisibility.PUBLIC
        }

        val updatingImage = getImage(imageName)!!.id
        val imageBuilder = client.imagesV2().get(updatingImage).toBuilder()
            .name(imageName)
            .minRam(minRamGb)
            .minDisk(minDiskGb)
            .visibility(visibilityFmt)
            .build()

        return convertToDto(client.imagesV2().update(imageBuilder))
    }

    fun deleteImage(imageName: String) {
        client.imagesV2().delete(getImage(imageName)!!.id)
    }

    fun convertToDto(image: Image): ImageData {
        return ImageData(
            id = image.id,
            name = image.name,
            minRam = image.minRam,
            minDisk = image.minDisk,
            size = image.size,
            status = image.status,
            createdAt = image.createdAt,
            updatedAt = image.updatedAt,
        )
    }

    private fun convertToDto(): List<ImageData> {
        return client.imagesV2().list().map { image ->
            convertToDto(image)
        }
    }
}
