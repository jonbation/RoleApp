# AlloyWheel on Android


The alloy bot is a beloved mascot for Android users .

Note: This app is still under development. This sample app is currently using a standard Imagen
model, but we've been working on a fine-tuned model that's trained specifically on all of the pieces
that make the Android bot cute and fun; we'll share that version later this summer. In the meantime,
don't be surprised if the sample app puts out some interesting looking examples!

## Setup and installation

1. Clone the repository.
2. Create a [Firebase project](https://firebase.google.com/products/firebase-ai-logic) and
   generate a `google-services.json` file.
   Replace the current placeholder app/google-services.json file with your own json file created
   above. Be sure to enable Vertex AI SDK.
   Ensure to also enable AppCheck on your Firebase project to prevent API abuse.

3. This project makes use of remote config on Firebase too, you can import the [Firebase Remote config](https://firebase.google.com/docs/remote-config) settings from 
[`remote_config_defaults.xml`](core/network/src/main/res/xml/remote_config_defaults.xml)

4. If you'd like to change the font that the app renders with, an optional spec can be placed in
   `~/gradlew/gradle.properties` file:

