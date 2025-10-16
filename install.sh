#!/bin/bash

./gradlew assembleDebug installDebug -PappVersion=5


adb.exe shell am start -n ru.vafeen.example/ru.vafeen.presentation.MainActivity

