import cat.helm.result.*
import kotlin.test.*

class TestResult {
    @Test
    fun testFoldExecutesFailurePathOnResultFailure() {
        val failure = IllegalArgumentException().asFailure()

        val fold = failure.fold({ false }, { true })

        assertTrue(fold)
    }

    @Test
    fun testFoldExecutesSuccessPathOnResultSuccess() {
        val success = false.asSuccess()

        val fold = success.fold({ true }, { false })

        assertTrue(fold)
    }

    @Test
    fun successFunctionOnlyCalledIfResultIsSuccess() {
        var successExecuted = false
        var failureExecuted = false
        val success = asSuccess()

        success.success {
            successExecuted = true
        }
        success.failure {
            failureExecuted = true
        }

        assertTrue(successExecuted)
        assertFalse(failureExecuted)
    }

    @Test
    fun failureFunctionOnlyCalledIfResultIsFailure() {
        var successExecuted = false
        var failureExecuted = false
        val failure = IllegalArgumentException().asFailure()

        failure.success {
            successExecuted = true
        }
        failure.failure {
            failureExecuted = true
        }

        assertTrue(failureExecuted)
        assertFalse(successExecuted)
    }

    @Test
    fun isSuccessReturnsTrueOnlyOnResultSuccess() {
        val success = asSuccess()
        val failure = IllegalArgumentException().asFailure()

        assertTrue(success.isSuccess())
        assertFalse(failure.isSuccess())
    }

    @Test
    fun mapOnlyChangesIfResultIsSuccess() {
        val success = false.asSuccess()
        val failure = IllegalArgumentException().asFailure()

        val successMap = success.map { true }
        val failureMap = failure.map { true }

        assertTrue((successMap as Result.Success).value)
        assertEquals(failure, failureMap)
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun mapErrorOnlyChangesIfResultIsFailure() {
        val success = false.asSuccess()
        val failure = IllegalArgumentException().asFailure()

        val successMap = success.mapError { IndexOutOfBoundsException() }
        val failureMap = failure.mapError { IndexOutOfBoundsException() }

        assertTrue(failureMap is Result<Nothing, IndexOutOfBoundsException>)
        assertEquals(success, successMap)
    }

    @Test
    fun testGetValueOrNull() {
        val success = true.asSuccess()
        val failure = IllegalArgumentException().asFailure()

        assertTrue(success.getValueOrNull()!!)
        assertNull(failure.getValueOrNull())
    }

    @Test
    fun testResultOfCreatesASuccessResult() {
        val success = Result.of { true }

        assertTrue(success.getValueOrNull()!!)
    }

    @Test
    fun testResultOfCreatesAFailureResultIfNull() {
        val failure = Result of { null }

        assertTrue(failure is Result.Failure)
    }

    @Test
    fun testResultOfCreatesAFailureResultIfExceptionIsThrown() {
        val failure = Result of { throw IllegalArgumentException() }
        assertTrue(failure is Result.Failure)
    }

    @Test
    fun getOrDefaultReturnsValueIfResultIsSuccess() {
        val success = true.asSuccess()

        assertTrue(success.getOrDefault(false))
    }

    @Test
    fun getOrDefaultReturnsDefaultIfResultIsFailure() {
        val failure = IllegalArgumentException().asFailure()

        assertTrue(failure.getOrDefault(true))
    }

    @Test
    fun recoverOnlyReturnsOnFailure() {
        val failure = IllegalArgumentException().asFailure()
        val success = true.asSuccess()

        val recovered = failure.recover { true.asSuccess() }
        val successRecovered = success.recover { false.asSuccess() }

        assertTrue(recovered.getValueOrNull()!!)
        assertEquals(success, successRecovered)
    }

    @Test
    fun testFlatMapOnlyMapsOnSuccess() {
        val success = false.asSuccess()
        val failure = IllegalArgumentException().asFailure()

        val successResult = success.flatMap {
            true.asSuccess()
        }
        val failureResult = failure.flatMap {
            true.asSuccess()
        }

        assertTrue(successResult.getValueOrNull()!!)
        assertEquals(failureResult, failure)
    }

    @Test
    fun testCreatorFunctionsReturnsProperType() {
        assertTrue(true.asSuccess() is Result.Success)
        assertTrue(IllegalArgumentException().asFailure() is Result.Failure)
    }

    @Test()
    @Suppress("IMPLICIT_NOTHING_AS_TYPE_PARAMETER")
    fun testValueGetsTheValueOrFails(){
        val success = true.asSuccess()
        val failure = IllegalArgumentException().asFailure()

        assertTrue(success.value)
        assertFailsWith<IllegalArgumentException> {
            failure.value
        }
    }

    @Test()
    @Suppress("IMPLICIT_NOTHING_AS_TYPE_PARAMETER")
    fun testComponent1GetsTheValueOrFails(){
        val success = true.asSuccess()
        val failure = IllegalArgumentException().asFailure()

        val (value) = success
        assertTrue(value)
        assertFailsWith<IllegalArgumentException> {
            val (exception) = failure
        }
    }
}
