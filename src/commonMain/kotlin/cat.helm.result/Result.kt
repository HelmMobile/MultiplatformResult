package cat.helm.result

/**
 * Created by Borja on 4/1/17.
 */

sealed class Result<out Value, out Exception : Throwable> {

    abstract val value: Value

    class Success<out Value>(override val value: Value) : Result<Value, Nothing>()

    class Failure<out Exception : Throwable>(val exception: Exception) : Result<Nothing, Exception>() {
        override val value: Nothing
            get() = throw exception
    }

    operator fun component1(): Value = value

    inline fun <Return> fold(
        successPathFunction: (Value) -> Return,
        failurePathFunction: (Exception) -> Return
    ): Return = when (this) {
        is Success -> successPathFunction(value)
        is Failure -> failurePathFunction(exception)
    }

    inline fun success(successPathFunction: (Value) -> Unit) = fold(successPathFunction, {})

    inline fun failure(failurePathFunction: (Exception) -> Unit) = fold({}, failurePathFunction)

    fun isSuccess(): Boolean =
        when (this) {
            is Success -> true
            is Failure -> false
        }

    inline fun <NewValue> map(mapFunction: (Value) -> NewValue): Result<NewValue, Exception> =
        when (this) {
            is Success -> Success(mapFunction(value))
            is Failure -> this
        }

    inline fun <NewError : Throwable> mapError(mapFunction: (Throwable) -> NewError): Result<Value, NewError> =
        when (this) {
            is Success -> this
            is Failure -> {
                //  val logger = LoggerFactory.getLogger("Application")
                //  logger.error(exception.message, exception)
                Failure(mapFunction(exception))
            }
        }

    fun getValueOrNull(): Value? =
        when (this) {
            is Success -> value
            is Failure -> null
        }

    companion object {
        inline infix fun <Value> of(executableFunction: () -> Value?): Result<Value, Throwable> =
            try {
                when (val result = executableFunction()) {
                    null -> Failure(Throwable())
                    else -> Success(result)
                }
            } catch (e: Throwable) {
                e.asFailure()
            }
    }
}

fun <Value, Exception : Throwable> Result<Value, Exception>.getOrDefault(default: Value): Value =
    when (this) {
        is Result.Success -> value
        is Result.Failure -> default
    }

inline fun <Value, Exception : Throwable> Result<Value, Exception>.recover(recoverFunction: (throwable: Throwable) -> Result<Value, Exception>)
        : Result<Value, Exception> =
    when (this) {
        is Result.Success -> this
        is Result.Failure -> recoverFunction(this.exception)
    }

inline fun <Value, NewValue, Exception : Throwable>
        Result<Value, Exception>.flatMap(mapFunction: (Value) -> Result<NewValue, Exception>)
        : Result<NewValue, Exception> =
    when (this) {
        is Result.Success -> mapFunction(value)
        is Result.Failure -> this
    }

fun <Value> Value.asSuccess(): Result<Value, Nothing> = Result.Success(this)

fun <Error : Throwable> Error.asFailure(): Result<Nothing, Error> = Result.Failure(this)
