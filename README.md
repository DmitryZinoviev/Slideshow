# Android Slideshow App


## Architecture & Tech Stack

The project follows **Clean Architecture** principles and is split into three modules:

- `app` — presentation layer  
- `domain` — business logic  
- `data` — data sources and implementations  

### Technologies used

- Koin — Dependency Injection  
- Retrofit — Networking  
- Room — Local database  
- File system storage — Media persistence  
- ExoPlayer — Video playback  

---

## API Handling & Data Validation

During API integration, several inconsistencies were identified:

- Some `playlistItems` contained invalid `playlistKey` values  
- Some items had different labels but referenced the same media file  

### Solutions

- Invalid items are filtered out before persisting  
- Media duplication is handled at the storage level  

---

## Data Fetching Flow

The following flow is implemented:

1. Fetch playlist data from the server  
2. Filter invalid entries  
3. Store valid data in the database  
4. For each required media file:
   - Create an entry in the `downloads` table  
   - Track download status via this table  

---

## Media Downloading

- Files are downloaded into temporary files  
- After successful download:
  - The file is renamed to its final name  


---

## Download State Management

- When all media files of a playlist are downloaded:
  - The playlist is marked as `isDownloaded = true`  

- When all playlists for a screen are ready:
  - The entire screen is considered ready for offline playback  

---

## App Startup Behavior

On application start:

- Temporary files are cleaned up  
- Incomplete downloads are analyzed and handled  

---

## Playlist Updates (Partially Implemented)

A diff mechanism between old and new data is implemented, allowing:

- Updating only changed records  
- Removing outdated entries  

### Not fully implemented

- Periodic playlist fetching  
- Automatic background updates  

### Planned approach

- Coroutine-based observer for periodic updates  
- For intervals longer than 15 minutes — `AlarmManager`  

---

## Media Playback

The most complex part was implementing smooth playback with transitions.

### Challenges

- ExoPlayer is used via `AndroidView`, which prevents using standard Compose `Crossfade`  

- The app must support videos and images media. But images are not supported by ExoPlayer

- To handle this app must swith from showing Image component and player 

---

## Solution

### Video transitions implemented by  switching `SurfaceView`  to `TextureView`  and applying alpha-based fade  

### Alternative tested  `VideoView` but applying alpha-based fade  causes excessive recompositions and lags of the media replay
