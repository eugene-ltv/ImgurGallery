# ImgurGallery

Demo application for browsing the Imgur gallery using [Imgur API](https://apidocs.imgur.com/?version=latest).


## Features

 * Gallery sections: Hot, Top, User submitted
 * Switching between Grid / List / Staggered view
 * Including / excluding viral images from 'User submitted' section
 * Caching images in memory / on disk
 * Handling screen orientation changes
 * Wear OS app that displays images list from 'Hot' section
 
 
## Prerequisites

To build and run this project you must obtain personal ``client_id``  for accessing Imgur API. Follow this [link](https://apidocs.imgur.com/?version=latest#intro) to get instructions how to do it.

Define your Client ID in ``app/build.gradle``
```
def imgurApiKey = '"<YOUR_CLIENT_ID>"'
```

## Built With

* Kotlin, Coroutines
* Retrofit
* Navigation Component (Jetpack)
* Dagger2
* Glide
* ViewModel (Jetpack)


## Screenshots
![Screenshot1](./readme/screenshot1.png "Screenshot1")

![Screenshot2](./readme/screenshot2.png "Screenshot2")

![Screenshot3](./readme/screenshot3.png "Screenshot3")
