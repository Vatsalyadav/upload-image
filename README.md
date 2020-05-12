# Upload and View Image
Upload and View Image is an Android Application to upload capture photos using CameraX or choose image from gallery and upload to Firebase.
Phase 2: Include Navigation Component & Data Binding [In Progress]

<br>Upload and View Image App can:

1. Capture image using Camera with Flash, Zoom and Camera Switch functions
2. Pick images from gallery to upload
3. Crop image as per requirement before uploading on Firebase Storage and Realtime Database
4. Display uploaded images in a grid using RecyclerView
5. High Definition image can be zoomed in and out by pinch-in, pinch-out respectively
6. Image can also be zoomed in and out by double taps

## Screenshots

<p>
<img src="https://github.com/Vatsalyadav/upload-image/blob/master/UploadImage.gif" width="250" vspace="20" hspace="5" alt="Upload Image" />
<img src="https://github.com/Vatsalyadav/upload-image/blob/master/Viewimage.gif" width="250" vspace="20" hspace="5" alt="View Fullscreen Image" />
</p>

## Libraries Used

* AppCompat, CardView, RecyclerView, PhotoView as Design Library
* Glide
* CameraX
* Lifecycle
* Material 
* RxJava
* Firebase

## Setup

Navigate to `Tools -> Firebase` on Android Studio and add Realtime Database & Storage to your project. On click of connect, login into your Firebase account and a project will be created for the app with `google-services.json` file imported with configuration settings.
Add Storage and Realtime Database dependencies and select Test rules on Firebase Console to get started.

## Tools and Technologies are Used :
### Android Architecture Components, Android Jetpack and RxJava :

* ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment. It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).
* LiveData is a data holder class that can be observed within a given lifecycle. This means that an Observer can be added in a pair with a LifecycleOwner, and this observer will be notified about modifications of the wrapped data only if the paired LifecycleOwner is in active state. Rather than hiding the detail of SQLite, Room tries to embrace them by providing convenient APIs to query the database and also verify such queries at compile time. This allows you to access the full power of SQLite while having the type safety provided by Java SQL query builders.

* RxJava is Java implementation of Reactive Extension (from Netflix). Basically it’s a library that composes asynchronous events by following Observer Pattern. You can create asynchronous data stream on any thread, transform the data and consumed it by an Observer on any thread. The library offers wide range of amazing operators like map, combine, merge, filter and lot more that can be applied onto data stream.

## Architecture

### MVVM
MVVM stands for Model, View, ViewModel. MVVM facilitates a separation of development of the graphical user interface from development of the business logic or back-end logic (the data model). 

#### Model
Model holds the data of the application. Model represents domain specific data and business logic in MVC architecture. It maintains the data of the application. Model objects retrieve and store model state in the persistance store like a database. Model class holds data in public properties. It cannot directly talk to the View.
#### View
View represents the UI of the application devoid of any Application Logic. It observes the ViewModel.
#### ViewModel
ViewModel acts as a link between the Model and the View. It’s responsible for transforming the data from the Model. It provides data streams to the View. It also uses hooks or callbacks to update the View. It’ll ask for the data from the Model.
The following flow illustrates the core MVVM Pattern.
<img src="https://github.com/Vatsalyadav/NASA-pictures-app/blob/master/mvvm_architecture.png" width="750" vspace="20" hspace="5" alt="MVVM Architecture" />

## MVVM implementation in app
<b>Model</b> `ImageDetails` implements Serializable and holds image data like imageName and imageUrl that will be uploaded and retrieved

<b>Repository</b> `ImageRepository` has following purpose:
- `setupFirebase()` to setup Firebase components, StorageReference and DatabaseReference
- `getImagesList()` to get a LiveData of List of `ImageDetails` objects using LiveDataReactiveStreams that converts RxJava Flowable observable using fromPublisher. The image list is fetched from Firebase Realtime Database
- `insertImage()` to insert image in the Firebase Storage and its instance in Database using Single Observable to get `onSuccess()` and `onError` results

<b>ViewModel</b> 
- `ImageActivityViewModel` is a class designed to hold and manage UI-related data in a lifecycle conscious way
- `ImageActivityViewModel` will support `ImageActivity` and hold UI related data for it like `getImagesList()`
- ImageActivityViewModel is a lifecycle-aware and it allows data to survive configuration changes such as screen rotation.

<b>View</b> 
- `ImageGridActivity` interacts with ViewModel to get Image List and observe them for any changes
- ImageGridActivity uses `selectImage()` method to take an image from either Camera or Choose an image from Gallery
- Seleted Image can be cropped using CropActivity and then will be pushed in Storage using uploadImage() in view model
<p>
<img src="https://github.com/Vatsalyadav/upload-image/blob/master/Screenshot_20200505-081001.jpg" width="250" vspace="20" hspace="5" alt="Pick from Gallery" />
<img src="https://github.com/Vatsalyadav/upload-image/blob/master/Screenshot_20200505-081017.jpg" width="250" vspace="20" hspace="5" alt="Crop Image" />
<img src="https://github.com/Vatsalyadav/upload-image/blob/master/Screenshot_20200505-081054.jpg" width="250" vspace="20" hspace="5" alt="Image Fullscreen" />
</p>
- Android activity layout `activity_image.xml` has custom toolbar and FrameLaout for images grid and fullscreen image.
- `ImageGridActivity` uses `ImageListFragment` to show the list of images using RecyclerView and adapter in a Grid
- `ImageGridActivity` uses `ImageDetailFragment` to show fullscreen selected image with double tap and pinch to zoom

<b>View</b> `CameraActivity` allows user to Capture an using withh Zoom, Flash and Switch Camera functions

### Adapters
RecyclerViewAdapter `ImageGridRecyclerAdapter` provides a binding for uploaded images, set to views that are displayed within the recyclerView for the ImageGridActivity, i.e, Image grid screen. Images will be load using Glide will CircularProgressDrawable as placeholder and sad icon for image loading error. Glide is used for a fast image loading and caching.
RecyclerView is an efficient version of ListView which acts as a container for rendering data set of views that can be recycled and scrolled efficiently.

## CameraX
CameraX is an Android Jetpack use case based API, it has abstracted 3 main handles which you can use to interact with the camera: Preview, Image analysis and Image capture. Choosing which use case(s) to use depends on what you intend to use the camera for
* Preview : Provides a way to get the camera preview stream.
* Image analysis : Provides a way to process camera data (frames).
* Image capture : Provides a way to capture and save a photo.

We have used `Preview` and `Image capture` use cases in `CameraActivity`. They have been implemented by : 
* Preview Use Caes : Preview.Builder provides options to set either the target aspect ratio or resolution to be used. The rotation can also be configured, which should typically match the device’s orientation. 
The Preview use case needs a Surface to display the incoming preview frames it receives from the camera. You can provide a Surface by calling `Preview.setSurfaceProvider(SurfaceProvider)`, the SurfaceProvider passes the preview Surface to be used by the camera.
* Image Capture Use Case : ImageCapture.takePicture(executor, OnImageCapturedCallback), if the image is successfully captured, onCaptureSuccess() is called with an ImageProxy that wraps the capture image. If the image capture fails, onError() is invoked with the type of the error. Both callbacks are run in the passed in Executor, if they need to run on the main thread, pass in the main thread Executor. ImageCapture.Builder allows to set the flash mode to be used when taking a photo, it can be one of the following: FLASH_MODE_ON, FLASH_MODE_OFF or FLASH_MODE_AUTO.

Developed By
------------

* Vatsal Yadav  - www.linkedin.com/in/vatsalyadav 
