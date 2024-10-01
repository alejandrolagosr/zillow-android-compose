package com.flagos.zillow.data

class DogImagesRepository(private val dogImagesApiClient: DogImagesApiClient) {

    suspend fun getImages() = dogImagesApiClient.apiClient.getImages().message
}