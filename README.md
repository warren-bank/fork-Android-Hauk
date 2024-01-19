### [Hauk](https://github.com/warren-bank/fork-Android-Hauk/tree/fork/v1.6.2/minsdk-19)

#### Fork Details

* fork: [bilde2910/Hauk](https://github.com/bilde2910/Hauk)
  - author: [Marius Lindvall](https://varden.info/)
  - tag: [v1.6.2](https://github.com/bilde2910/Hauk/releases/tag/v1.6.2)
  - commit: [26d8a2e13aa38891d4e94097465a25684c787045](https://github.com/bilde2910/Hauk/tree/26d8a2e13aa38891d4e94097465a25684c787045)
  - date: 2023-08-13
  - license: [Apache-2.0](./LICENSE)
  - more info: [original README](./README-original.md)

#### Goals

* lower the _minSdkVersion_ of the Android app
  - from: 24 (Android 7.0, Nougat)
  - to: 19 (Android 4.4, KitKat)

#### Notes

* the upstream repo contains the code for both:
  - the Android app
  - the PHP backend server
* this repo contains the code for only:
  - the Android app
* I have a separate [fork of the PHP backend server](https://github.com/warren-bank/render-web-services/tree/hauk)
  - its purpose is to update the Dockerfile to be rapidly deployed for free on the hosting provider: [render.com](https://render.com/docs/free)
