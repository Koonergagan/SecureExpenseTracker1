package week11.st566236.finalproject.data.util

// A generic sealed class to represent the state of a resource
sealed class Resource<out T> {

    // Represents a loading state, typically while waiting for data
    object Loading : Resource<Nothing>()

    // Represents a successful state with the loaded data
    data class Success<T>(val data: T) : Resource<T>()

    // Represents an error state with an error message
    data class Error(val message: String) : Resource<Nothing>()
}