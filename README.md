### Design/Tech Choices :

This is a quick summary of the design/tech choices I made. This only specifies the “what” I did. This can serve as a guide to discuss further if we want to dive deep into the “why”

- My strength is producing high quality, modular and extensible code. I generally rely on Product Managers and UX designers to provide me with mock ups of how the screen should look and behave as UX is not my core strength.  
- A video is provided under videoDemo/ folder, just in case trying the app wasn't possible

**Technologies Used :**
* Kotlin
* Hilt (Dagger) to inject the dependencies
* Retrofit for network calls
* RxJava to make the app reactive to the fetched data
* Kotlin Coroutines are also used for fetching data
* DataBinding is used extensively

**HILT (vs Dagger) :** I chose to use Hilt dependency injection library instead of `Dagger` just to show that I keep myself familiar with latest updates in Android and also that it reduces the amount of boilerplate code that’s necessary

**Using UI states to manage UI behaviour :** `ForecastDataUiState` is used to manage the UI states.

There are typically 2 approaches

* Write `setText/setVisibility` methods listening to RxObservables or LiveData exposed by a ViewModel
* With advent of DataBinding, some use it to determine UI behavior including logic to toggle visibilities

I always found both the approaches problematic, for reasons we can discuss later. I took a middle path that allows me to move the UI behavior to a data class

**RxJava support to fetch data :**

* RxJava is used to fetch the data & also used to deal with threads

**Used Coroutines also to fetch :**

* I also used Coroutines despite RxJava being widely used in Android App/SDK development to demonstrate how network operations can be performed using simple and readable Coroutine code
* I have also written test cases for the method in the ViewModel that uses Coroutines

### Possible Improvements / Feature additions:

* The code currently has `ApiResponse.Success` and `ApiResponse.Error`. I would like to add `ApiResponse.Failure` as well.
 - `ApiResponse.Failure` is meant to handle different client http error codes returned by an api - `4xx`.
 - `ApiResponse.Error` is only meant to handle - 5xx. The reason for this classification can be discussed later

* A utility to check whether the internet connection is available
