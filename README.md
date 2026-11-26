<!-- ---------- Header ---------- -->
<div align="center">
  <img width="200" height="200"src="fastlane/metadata/android/en-US/images/icon.png">
  <h1>Translate You</h1>
<p>Privacy focused translator app built with MD3.</p>

<!-- ---------- Badges ---------- -->
  <div align="center">
    <img alt="License" src="https://img.shields.io/github/license/you-apps/TranslateYou?color=c3e7ff&style=flat-square">
    <img alt="Downloads" src="https://img.shields.io/github/downloads/you-apps/TranslateYou/total.svg?color=c3e7ff&style=flat-square">
    <img alt="Last commit" src="https://img.shields.io/github/last-commit/you-apps/TranslateYou?color=c3e7ff&style=flat-square">
    <img alt="Repo size" src="https://img.shields.io/github/repo-size/you-apps/TranslateYou?color=c3e7ff&style=flat-square">
    <img alt="Stars" src="https://img.shields.io/github/stars/you-apps/TranslateYou?color=c3e7ff&style=flat-square">
    <br>
</div>
</div>

<!-- ---------- Description ---------- -->
## Features

- [x] more than 150 supported languages
- [x] 17 different translation engines
- [x] Material Design 3 (You)
- [x] Dark and light theme
- [x] Support for Android sharing system
- [x] Multilingual interface
- [x] Translation from images
- [x] Translation history
- [x] Entirely Open Source
- [X] Written in Jetpack Compose 

<!-- ---------- Download ---------- -->
## Download

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png" alt="Get it on F-Droid" height="80">](https://f-droid.org/packages/com.bnyro.translate/)
[<img src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" alt="Get it on IzzyOnDroid" height="80">](https://apt.izzysoft.de/fdroid/index/apk/com.bnyro.translate)
[<img src="ghbadge.png" alt="Get it on GitHub" height="80">](https://github.com/you-apps/translateyou/releases)
[<img src="obtainiumbadge.png" alt="Get it on Obtainium" height="80">](https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/you-apps/TranslateYou/)

<!-- ---------- Supported translation engines ---------- -->
## Supported translation engines

### Open Source / Self-hostable
* <a href="https://github.com/thedaviddelta/lingva-translate">LingvaTranslate</a> _which uses Google Translate® to fetch translations._
* <a href="https://simple-web.org/projects/simplytranslate.html">SimplyTranslate</a>
* <a href="https://github.com/janvarev/OneRingTranslator">OneRingTranslator</a>
* AI Translator _using any OpenAI-compatible API, including self-hosted ones. Requires an API key._

### Proprietary
* Google Translate® (unofficial endpoint)
* Google Cloud Translation® _requires an API key from the user._
* <a href="https://www.bing.com/translator">Bing Translator®</a>
* <a href="https://www.deepl.com/translator">DeepL translator®</a> (browser, free API and paid API)
* <a href="https://translate.yandex.com/">Yandex®</a>
* <a href="https://www.iciba.com/">Iciba®</a>
* LingoCloud (Caiyun) _the default token can be replaced by the user._
* <a href="https://transmart.qq.com/">Transmart®</a>
* <a href="https://mymemory.translated.net/">MyMemory translator®</a>.
* <a href="https://en.pons.com/translate">PONS®</a>

<!-- ---------- Screenshots [Plus version] ---------- -->
## Screenshots

<div style="display: flex;">
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1-translate.png" width=30%>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2-translate.png" width=30%>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3-details.png" width=30%>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4-settings.png" width=30%>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/5-history.png" width=30%>
  <img src="fastlane/metadata/android/en-US/images/phoneScreenshots/6-about.png" width=30%>
</div>

<!-- ---------- Contribution ---------- -->
## Feedback and contributions
***All contributions are very welcome!***

* Feel free to join the [Matrix room](https://matrix.to/#/#you-apps:matrix.org) for discussions about the app.
* Bug reports and feature requests can be submitted [here](https://github.com/you-apps/TranslateYou/issues) (please make sure to fill out all the requested information properly!).
* If you are a developer and wish to contribute to the app, please **fork** the project and submit a [**pull request**](https://help.github.com/articles/about-pull-requests/).

## Translation
<a href="https://hosted.weblate.org/projects/you-apps/translate-you/">
<img src="https://hosted.weblate.org/widgets/you-apps/-/translate-you/287x66-grey.png" alt="Translation status" />
</a>

**Translations are greatly appreciated.** \
If **Translate You** is not in your language, feel free to participate by:
* Translating the application interface, via [**pull request**](https://help.github.com/articles/about-pull-requests/) or more simply by [**Weblate**](https://hosted.weblate.org/projects/you-apps/translate-you/).
* **Check** and **update** the [**pre-existing translations on Github**](https://github.com/you-apps/TranslateYou/tree/master/app/src/main/res) or more simply via [**Weblate**](https://hosted.weblate.org/projects/you-apps/translate-you/).

<!-- ---------- Privacy Policy and License ---------- -->
## Privacy Policy

**Translate You's privacy policy is available** [**here**](https://github.com/you-apps/TranslateYou/blob/master/PRIVACY%20POLICY.md)

## Usage as a Library
You can use the translation engines implemented in Translate You in your own Kotlin or Java projects.

See [here](https://repo1.maven.org/maven2/io/github/you-apps/translation-engines/) for the latest version number.
```kts
dependencies {
  implementation("io.github.you-apps:translation-engines:<version>")
}
```

```kotlin
class MySettingsProvider : EngineSettingsProvider {
    // can be used to set a custom URL
    fun getApiUrl(engine: TranslationEngine): String? = null
    fun getApiKey(engine: TranslationEngine): String? = null
    fun getSelectedModel(engine: TranslationEngine): String? = null
}
val engine = LVEngine(settingsProvider).createOrRecreate()

val supportedLanguages = engine.getLanguages()
...
val translationResult = engine.translate("hello world", "en", "de")
```

See [here](https://github.com/you-apps/TranslateYou/blob/master/translation-engines/src/main/java/net/youapps/translation_engines/TranslationEngine.kt) for more information on the settings configuration and supported features by the engines.

## Credits
* Icon design by [M00NJ](https://github.com/M00NJ)

## License

Translate You is licensed under the [**GNU General Public License**](https://www.gnu.org/licenses/gpl.html): You can use, study and share it as you want.
