# Multiplatform Result

This library provides a way to model functions that can either Success or Fail, it introduces a new type Result<Value,Throwable>


## Motivation

The motivation behind this library is to bring a common pattern in FP to OOP in a more accessible way.

Result is designed to be used when you have to model functions that can fail, it encourages you to deal with your failure cases in a way that the user can understand it.

## Instalattion
```kotlin
  repositories {
        // ...
        jcenter()

  }
```
```kotlin
commonMain {
    dependencies {
        // ...
        implementation("cat.helm.result:multiplatform-result:1.0.0")
    }
}
``` 

## Usage
Consider the following case, when you have to make an api call.
``` Kotlin
fun getUser(): User? = 
    try{
        executeApiCall()
    }catch(e: Exception){
        null
    }
}
```
With this approach the user does not now why it has failed. Using result you can model your errors. 
``` Kotlin
fun getUser(): Result<User, MyModelErrors> = Result.of { 
     executeApiCall()
 }.mapError { exception ->  
     when(exception) {
        is IoException -> MyModelErrors.NoInternetException
        is ServerException -> MyModelErrors.UserNotFound
    }
}
```
In this case we can inform the user how to act in case that there is no internet, so he can recover from the failure.

``` Kotlin
result.success{ user ->
    showCallResult(user)
}      

result.failure { exception ->
    when(exception) {
            MyModelErrors.NoInternetException -> showConnectionIssues()
            MyModelErrors.UserNotFound -> showRetryLater()
        }
}
```

Result is designed to be highly chainable, so you can expres your happy path in a concise way.

``` Kotlin
val result = Result.of {
    apiCall()
}.recover {
    queryDb()
}.flatMap { fetchedResult ->
    saveInCache(fetchedResult)
}.map {
    it.toDomain()
}
```

## Monad Comprehension(ish)

In this type of framework is usual to find the following problem:

````kotlin
fun readFile(){
    val result = readFile(){ -> file
        file.readLine(){ line ->
            line.readWord(){ word ->
                "The word $word is in line ${line.position} in file ${file.name}"
            }
        }
    }
}
````
As you can see nesting problems can arise when you need all the previous parameters in the inner function. To solve that problem FP languages uses a technique called Monad comprehension in this library we have tried to reproduce this approach.

````kotlin
fun readFile() = Result.of {
   val file = readFile().value
   val line = file.readLine().value
   val word = line.readWord().value     
   "The word $word is in line ${line.position} in file ${file.name}"
}
````   

Or in a more concise way:

````kotlin
fun readFile() = Result.of {
   val (file) = readFile()
   val (line) = file.readLine()
   val (word) = line.readWord()     
   "The word $word is in line ${line.position} in file ${file.name}"
}
````   


## License
   Copyright 2020 Helm Mobile Development S.L.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.